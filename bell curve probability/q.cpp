#include <iostream>
#include "p.h"
using namespace std;

int main(int argc, char *argv[]) {
	if(argc!=1+1 && argc!=1+2)	cerr<< "\t*args: q x [integral stepsize]\n\tdefault stepsize=1e-5\n";
	else
		try{
			double x= stod(argv[1]);
			double stepsize= argc==1+2? stod(argv[2]) : 1e-5;
			double ans = q(x, stepsize);
			printf("\tQ(X>%.3f)= %.8e%\n", x, ans);
			printf("\tQ(X<%.3f)= %.8e%\n", x, 1-ans);
		} catch(invalid_argument& err){ cerr<<"\t*"<<err.what()<<"\n"; }
}	// main