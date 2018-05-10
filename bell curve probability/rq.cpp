#include <iostream>
#include "p.h"
using namespace std;

void bisection(double a, double b, double tol, double P){
	if(P==0.5){
		printf("\tQ(%.3f)= %.4e%\n", 0, P);
		return;
	}
	int i= 0;
	int sign = P<0.5? -1 : 1;
	double stepsize, fxs, xs, p = P>0.5? 1-P : P;
	do{
		xs = (a+b)/2;
		if(i<5)	stepsize = 1/pow(10, ++i*0.5);
		fxs = q(xs, stepsize);
		if(q(a, stepsize)>p && fxs<p)	b = xs;
		else 	a = xs;
	} while(abs(fxs-p) > tol);
	printf("\tQ(X<%.3f)= %.4e%\n", sign*xs, P>0.5? 1-fxs : fxs);
	printf("\tQ(X>%.3f)= %.4e%\n", -sign*xs, P>0.5? 1-fxs : fxs);
}	// bisection

int main(int argc, char *argv[]) {
	if(argc!=1+1 && argc!=1+2)	cerr<< "\t*args: rq p [accuracy]\n\tif 1<p<100 is taken as %\n\tdefault accuracy=0.001\n";
	else
		try{
			double p= stod(argv[1]);
			if(p>1)	p/=100;
			if(p>1 || p<0)	
				cerr << "\tp range is [0:1]\n";
			else
				bisection(0, 5, ( (argc==1+2)? stod(argv[2]) : 0.001 ), p);
		} catch(invalid_argument& err){ cerr<<"\t*"<<err.what()<<"\n"; }
}	// main