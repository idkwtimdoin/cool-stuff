#include <iostream>
#include <cmath>
using namespace std;

inline double f(double x) {
	return x-2*exp(-x);
}	// f

void bisection(double a, double b, double t, int maxIterations){
	if(f(a)==0)	cout<< "xs: "<<a <<"\n";
	if(f(b)==0)	cout<< "xs: "<<b <<"\n";
	if(f(a)*f(b)>0)	cout<< "bad bounds\n";
	else{
		int i=0;
		double xs;
		do{
		printf("\t%-2d -- a: %.5e, b: %.5e, E: %.5e\n", i, a, b, abs(f(xs)));
			xs = (a+b)/2;
			if(f(a)*f(xs)<0)	b = xs;
			else 	a = xs;
		} while(abs(f(xs)) > t && ++i<maxIterations);
	printf("\n\t%-2d -- xs: %.7e, E: %e\n", ++i, xs, abs(f(xs)));
	cout<< (i-1==maxIterations? "\t**max iter reached\n" : "");
	}
}	// bisection

int main(int argc, char *argv[]) {
	try{
		if(argc == 3+1 || argc == 4+1)
			// lower-, upperbound, tolerance, max iterations by default=100
			bisection(stod(argv[1]), stod(argv[2]), stod(argv[3]), argc==4+1? stoi(argv[4]) : 100);	
		else
			cerr<< "args: bis <upper> <lower> <tol> <OPTIONAL max iterations>\n";
	} catch(invalid_argument& err){ cerr<<"\t*"<<err.what()<<"\n"; }
}	// main