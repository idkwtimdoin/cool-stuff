/*
 * my first program that had a purpose so there's a lot of redundancies
 * 3d modeling of parallel plate and cylinder capacitors
*/

import java.io.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
class FinalCapacitor
{
	static double delta = 5.0e-3;						// grid length 														 _______________________________ _jmax1
	static double hL = 1.0;								// half length of horizontal field								  |	|			V+	|	   			|_
	static double hW = 1.0;								// half width of vertical field									  |	|			[	|	]			| j2
	static double hLP = 0.22;							// half length of plate											 hW |			[	|	]			|
	static double hD = 0.10;							// distance between one plate and vertical equator				  |	|___________[___|	]			|_jmax2
	static boolean accel = true;	 					// accelerated convergence : normal finite difference			 	|			[	|	]  	|		|
	static boolean quarter= true;						// quarter : HALF MATRIX									    	|			[___D___]  hLP		|
	static boolean cylinCap= false;						// cylinder capacitor : PPC										  ^ |			[	|	]	|		|_j1
	static boolean charge = false;						// add charge density variation (better make quarter false)		  j |___________i1__|_______________|
	static double tol = (delta<=1.0e-3)? 1e-15:1e-10;	// tolerated error varies with delta								i >				^-------hL------^
	static double radius1 = 0.32;						// radius of +ve chraged cylinder												  imax2 		  imax1
	static double radius2 = 0.21;						// radius of -ve charged cylinder
	static double Vmin = 100.;							// -ve&+ve electric potential of conductors
	static double Vmax = 150.;							// in case of charge density, this is the highest potential point on conductor
	static int maxIter= 314159;							// maximum amount of iterations permitted
	static double omega = 1.9900;						// (SOR >1/SUR <1) (if omega>=2 you're nuts)

	public static void main (String []bananas)
		throws IOException, FileNotFoundException{
// files for storing data
		PrintWriter elec = new PrintWriter (new FileOutputStream ("potentials"),true);		// for gnu 3d surface curve
		PrintWriter sim = new PrintWriter (new FileOutputStream ("MATRIX"),true);			// for matrix entries visiualization
// dependent constants
		int jmax2 = (int)(hW/delta+1);					// max row index for /4 MATRIX
		int jmax1 = (int)(2*jmax2-1);					// max row index for FULL & HALF MATRIX
		int imax2 = (int)(hL/delta+1);					// max column index for /4 & HALF MATRIX
		int imax1 = (int)(2*imax2-1);					// max column index for FULL MATRIX
		int rDel1 = (int)(radius1/delta);				// radius in grids number
		int rDel2 = (int)(radius2/delta);
// get started: initialize conductor coordinates where i: columns, j: rows, and other variables
		int i1, j1, j2;
		i1 = j1 = j2 =0;								// promise java that they have values before conditional initialization (they will change)
		int jdecrement, idecrement;						// what is decremented from V[0].length & V.length on sweeping
		double[][] V;
		int[][] coF;
// create sample matrices
		if(quarter){
			V = new double[imax2][jmax2];
			coF = new int[imax2][jmax2];
			jdecrement = 0;								// if quarter, sweept horizontal unique bound, jmax2-1
		}else{
			V = new double[imax2][jmax1];
			coF = new int[imax2][jmax1];
			jdecrement = 1;								// if !quarter, sweep till the end of jmax1-1-1 since there is an extreme boundary line
		} // if quarter
// initialize conductors for real
		if(cylinCap){
			V = putCylin(V, Vmin, rDel1, jmax2);		// initialize cylinders
			V = putCylin(V, -Vmin, rDel2, jmax2);
			for(int j=0; j<V[0].length; j++){			// choose good row for 'check' for cylinCap sample matrix
				if( V[V.length-1][j]==Vmin || V[V.length-1][j]==-Vmin ){
					j1 = j;								// [j1-1] takes the first row that has at least one grid with |Vmin| beside it for 'check'; j2 & i1 are not used
					break;
				} // if
			} // for j
			idecrement = 0;								// sweep till vertical bound and take the symmetric cells to avoid index error in case of cylinCap
		}else{
			j1 = (int)(hW/delta-hLP/delta);				// first row that has |Vmin| in plate & j1 is used for 'check'
			i1 = (int)(hL/delta-hD/delta);				// first column of plate
			j2 = quarter? jmax2 : jmax1-j1;				// row that bounds the plate; j2 extends if HALF MATRIX (2*j1 -1)
			for(int j=j1; j<j2; j++)					// initialize plate
				V[i1][j]= Vmin;
			idecrement = 1;								// don't sweep the imposed vertical boundary line (imax2-1) if PPC
		} // if cylinCap
// solve the full simulation field matrix
		coF = update(V, coF, jmax2);
		if(charge)		V = addChargeDensity(V, coF, j1, j2, i1, jmax2);
		V = sweep(V, coF, j1, idecrement, jdecrement);
		if(quarter)	 V = reflect(V);
		V = reload(V);
// store V 3d points in potentials by gnuplot format
		for (int i=0; i<imax1; i+=5){														/*<-------INCREMENT HERE*/
			for(int j=0; j<jmax1; j+=5)													/*make increment=1 if plot with pm3d; make it=10 if plot with lines*/
				if(j!=(jmax1-1)) elec.println(i+"\t"+j+"\t"+V[i][j]);
				else			 elec.println(i+"\t"+j+"\t"+V[i][j]+"\n");
		} // for i
// store V in MATRIX
		sim.print("i^/j>\t");
		for(int j=0; j<jmax1; j++)
			if(j!=jmax1-1)	sim.print(" "+j+"\t");
			else			sim.print(j+"\t\n");
		for (int i=0; i<imax1; i++){
			sim.print(i+"\t");
			for(int j=0; j<jmax1; j++)
				if(j!=(jmax1-1))	sim.print((Math.round(V[i][j]*100.0)/100.0)+"\t");
				else				sim.println((Math.round(V[i][j]*100.0)/100.0)+"\t");
		} // for i
// graph: thanks to Sameer the teacher
		BufferedImage image = new BufferedImage (V.length, V[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for (int i=0; i<V.length; i++){
			for(int j=0; j<V[0].length; j++)
				image.setRGB(i,j,(int)(Math.abs(650*(V[i][j]))));					/*<-------------CONTRAST HERE*/
		} // for i
		JFrame sameer = new JFrame("electric equipotential lines");
		sameer.setSize(100,100);
		sameer.getContentPane().add(new JLabel(new ImageIcon(image)));
		sameer.setVisible(true);
	} // main

// solve matrix (this do-while loop satisfies all 16 possible variations of the 4 booleans)
	public static double[][] sweep (double [][]V, int [][]coF, int j1, int idecrement, int jdecrement)
	{
		double check = Vmin;		// the sum of all V[i][j1-1], where i varies (initial value of check does not matter as long as =/=0)
		double checkOld;			// same sum from last iteration
		int ci, cj;					// for handling unique boundary lines (Dr.M.Dikeakos)
		long tI, tF;				// starting time, finishing time
		int iter =0;				// current iteration
		double diff;				// |check-checkOld|
		tI = System.currentTimeMillis();							// let go time
		do{															// start sweeping
			checkOld = check;										// save the value of 'check' of the (n)th iteration since it changes on the (n+1)th iteration
			check =0.;												// assign 0 to check to start again without accumulation
			for (int i=1; i<V.length-idecrement; i++){
				ci= (cylinCap && i==V.length-1)? -1 : 1;
				for(int j=1; j<V[0].length-jdecrement; j++){
					cj = (j==V[0].length-1)? -1 : 1;
					if(coF[i][j]==0)
						if(accel)	V[i][j]+= omega*(V[i][j-1]+V[i][j+cj]+V[i-1][j]+V[i+ci][j]- 4.*V[i][j])/4.;		// Successive Over/Under Relaxation (23.31)
						else		V[i][j] = (V[i-1][j]+V[i+ci][j]+V[i][j-1]+V[i][j+cj])/4.;						// Finite Difference algorithm	(23.28)
				} // for j
				 check += V[i][j1-1];
			} // for i
			diff = Math.abs(check-checkOld);
			iter++;
//////////////////////////////////////////TEST FOR CONVERGENCE//////////////////////////////////////////
			System.out.println("iter: "+iter+"\tdiff:\t"+diff+"\tV[1,1]: "+V[1][1]);
		} while (diff>tol && iter<maxIter);							// stop sweeping
		tF = System.currentTimeMillis();							// catch time
//////////////////////////////////////////NEW INDEXES & TIME//////////////////////////////////////////
		int jmax = quarter? V[0].length*2 -1 : V[0].length;
		int imax = V.length*2 -1;
		System.out.println("\njmax2:\t"+V[0].length+"\timax2:\t"+V.length+"\nj elements: "+jmax+"\ti elements: "+imax);
		System.out.println("time taken: "+(tF-tI)/1000.+" seconds\t"+imax*jmax+" elements");
		System.out.println("\nomega: "+omega+"\naccel: "+accel+"\nquarter: "+quarter+"\ncylinCap: "+cylinCap+"\ncharge: "+charge);
		System.out.println("\n\n\t\5\t\3\t\4\t\6\t\30\t\6\t\4\t\3\t\5\n\t\33\t\33\t\33\t\33  more omega?\t\32\t\32\t\32\t\32\n\t\5\t\3\t\4\t\6\t\31\t\6\t\4\t\3\t\5\n\n");
		return V;
	} // solve

// insert a cylinder in sample matrix
	public static double[][] putCylin (double [][]V, double voltage, int rDel, int jmax2)
	{
		int coordinate;														// radius from (imax2-1, jmax2-1) to (i,j)
		for (int i=0; i<V.length; i++){
			for(int j=0; j< V[0].length; j++){
				coordinate = (int)(Math.sqrt(Math.pow(V.length-1-i,2)+Math.pow(jmax2-1-j,2)));
				if (coordinate==rDel)	V[i][j] = voltage;					// whenever (int)(sqrt(x^2 + y^2)) satisfies r, make it a conductor boundary line
			} // for j
		} // for i
		return V;
	} // putCylin

// update cofactor matrix as a sweeping condition (the reason the condition is not if(V[i][j]=Vmin) is due to different simulation fields and charge density variation)
	public static int[][] update (double [][]V, int [][]coF, int jmax2)
	{
		for (int i=0; i< V.length; i++){
			for(int j=0; j< V[0].length; j++){
				if(cylinCap){								// coF[i][j] !=0 means that this point is a boundary line
					if (V[i][j]== Vmin)	coF[i][j] = 2;		// just don't make coF[][] for the conductor coordinates = 0
					if (V[i][j]==-Vmin)	coF[i][j] = 3;
					if ( (int)(Math.sqrt(Math.pow(V.length-1-i,2)+Math.pow(jmax2-1-j,2))) > jmax2-1-1)
						coF[i][j] = 1;						// jmax2-1 : middle row; and to account for the extreme boundary lines, extra -1
				}else
					if (V[i][j]==Vmin)	coF[i][j] = 2;
			} // for j
		}  // for i
		return coF;
	} // update

// reflect values over horizontal equator to get HALF MATRIX in case of simulating /4 matrix
	public static double[][] reflect (double [][]quart)
	{
		double []half[] = new double [quart.length][2*(quart[0].length)-1];
		for(int i=0; i<quart.length; i++){
			for(int j=0; j<quart[0].length; j++)
				half[i][j] = quart[i][j];				// deep copy
		} // for i
		for(int i=0; i<quart.length; i++){				// start reflecting
			for(int j=0; j<quart[0].length; j++)
				half[i][half[0].length-1-j] = half[i][j];
		} // for i
		return half;
	} // reflect

// reflect values over vertical equator to get FULL MATRIX after reflecting /4 matrix or directly after solving /2 matrix
	public static double[][] reload (double [][]half)
	{
		int sign = cylinCap? 1 : -1;					// if cylinCap, reflect same values : opposite values
		double []entire[] = new double [2*(half.length)-1][half[0].length];
		for(int i=0; i<half.length; i++){
			for(int j=0; j< half[0].length; j++)
				entire[i][j]= half[i][j];				// deep copy
		} // for i
		for(int i=0; i<half.length; i++){				// start reloading
			for(int j=0; j< half[0].length; j++)
				entire[entire.length-1-i][j] = sign*entire[i][j];
		} // for i
		return entire;
	} // reload

// add linear charge density variation to conductors
	public static double[][] addChargeDensity (double [][]V, int [][]coF, int j1, int j2, int i1, int jmax2)
	{
		if(!cylinCap){
			double dVdJ = (Vmax-Vmin)/(j2-j1-1);		// plate interval [j1, j2-1]
			for(int j=j1; j<j2; j++)
				V[i1][j]+= (j-j1)*dVdJ;					// linear kenimatics
		}else{
			double theta;								// angle between i=imax2-1 and <imax2-1-i, jmax2-1-j>
			for (int i=0; i<V.length; i++){
				for(int j=0; j<V[0].length; j++){
					if(coF[i][j]!=0 && coF[i][j]!=1){	// if it's a conductor, find theta, multiply it by (Vmax-Vmin)/pi
						if(j>jmax2-1)	theta = Math.abs(Math.atan((V.length-1-i)/(jmax2-1-j*1.)));
						else if(j==jmax2-1)	theta = Math.PI/2.;
							 else theta = Math.PI/2. + Math.abs(Math.atan((jmax2-1-j)/(V.length-1-i*1.)));
						if (coF[i][j]==2)				// if +ve cylinder (coF[i][j]==2), then increment electric potential
							V[i][j]+= (Vmax-Vmin)*theta/Math.PI;
						else							// if -ve cylincer (coF[i][j]==3), then decrement electric potential
							V[i][j]-= (Vmax-Vmin)*theta/Math.PI;
					} // if out
				} // for j
			} // for i
		} // if cylinCap
		return V;
	} // addChargeDensity
}