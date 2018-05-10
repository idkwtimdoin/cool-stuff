/*
1-	2016 fall
2-	written using Visual Studio 2015 | windows8
3-	the program uses GA for finding the global minimum for Langermann's function and to find a 3rd degree polynomial function that best fits a set of 20 points
4-	both of the data mentioned in (3) are customized manually
*/
#include "structs.h"

void store(int l, float delta, Point&, Ind&);

int main() {
	srand(time(NULL));
	Pool<Ind> Lan(nInd);
	Pool<Point> Ply(nPts);

	/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	+-+-+-+	+-+-+-+-			SOLVE THE LANGERMANN USING THE GIVEN 5 VARIABLES			+-+-+-+-+-+-+-+-+-
	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
	std::cout << "solve langermann using the given params in 'structs.h' @L15\n";
	Lan.solve(nGen / 3, nInd / 5, nGen / 20.);

	/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	+-+-+-+		ESTIMATE A POLYNOMIAL FUNCTION FOR THE GIVEN POINTS	THE FOLLOWING ARGUMENTS 		+-+-+-+-+-
	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
	std::cout << "\nestimate polynomial function for points in 'structs.h' @L16\n";
	Ply.solve(nGen, nPts / 4, nGen / 2.5);

	/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	+-+-+-+					STORE DATA FOR PLOTTING USING GNU V5.0			+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
	+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
	store(LengthSimField, DELTA, *Ply, *Lan);

	system("PAUSE");
	return 0;
}

void store(int l, float delta, Point& aksis, Ind& grnted) {
	int max = l*1./delta;
	ofstream Langermann;
	Langermann.open("Langermann.txt");
	for (int i = 0; i < max; ++i)
		for (int j = 0; j < max; ++j) {
			grnted.c[0] = i*delta;
			grnted.c[1] = j*delta;
			Langermann << grnted.c[0] << "\t" << grnted.c[1] << "\t" << grnted.eval(1) << "\n" << ((j + 1 == max) ? "\n" : "");
		}
	Langermann.close();
		
	ofstream Polynomial;
	Polynomial.open("Polynomial.txt");
	for (int i = -max + 1; i < max; ++i)
		Polynomial << i*delta << "\t" << aksis.Polyf(i*delta) << endl;
	Polynomial.close();
		
	ofstream Points;
	Points.open("Points.txt");
	for (int i = 0; i < (sizeof(xyPoints) / sizeof(xyPoints[0])); ++i)
		Points << xyPoints[i][0] << "\t" << xyPoints[i][1] << endl;
	Points.close();
	
	std::cout << "\nlangermann x,y,z points stored in 'Langermann.tet'opfile\n"
		<< "chosen x,y points stored in 'Points.txt' opfile\n"
		<< "best-fit polynomial x,y points stored in 'Polynomial.txt'\n"
		<< "these files are stored in gnuplot v5.0 format and were used for evaluation\n";
}