#include <cmath>
using namespace std;

inline double intd(double t) {
	return exp(-t*t/2);
}	// integrand

double q(double x, double stepsize){
	double sum = 0;
	double b = x<0? -x:x;
	for(double i=0; i<b; i+=stepsize)
		sum+= (intd(i)+intd(i+stepsize))*stepsize/2;
	return (x>=0)? 0.5-sum/sqrt(2*M_PI) : 0.5+sum/sqrt(2*M_PI);
}	// q