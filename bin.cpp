#include <iostream>
#include <cmath>
using namespace std;

double long f(int x) {
	if(x<2) return 1;
	double long sum =1;
	for(int i=x; i>1; --i)	sum*=(i);
	return sum;
}	// f

double bin(long N, double p, int x1, int x2) {
	if(N<0 || p<0 || p>1 || x1>N || x1<0 || x2>N || x2<0)
		throw invalid_argument("make sure N>0, 1>p>0, N>min#successes>0, N>max#successes>0");
	if(x1>x2){
		int temp = x1;
		x1=x2;
		x2=temp;
	}
	double long sum = 0;
	if(x2+1-x1 < N/2)
		for (int x=x1; x<=x2; ++x)
			sum+= pow(p, x)*pow(1-p, N-x) / (f(x)*f(N-x));
	else{
		for(int x=0; x<x1; ++x)
			sum+= pow(p, x)*pow(1-p, N-x) / (f(x)*f(N-x));
		for(int x=x2+1; x<=N; ++x)
			sum+= pow(p, x)*pow(1-p, N-x) / (f(x)*f(N-x));
		return 1-f(N)*sum;
	}
	return f(N)*sum;
}	// bin

int main(int argc, char *argv[]) {
	if(argc!=4+1 && argc!=3+1)
		cerr<< "\t*args: bin N p min#successes max#successes\n\t*or bin N p #success\n";
	else
		try{
			int N = stoi(argv[1]);
			double p = stod(argv[2]);
			int l = stoi(argv[3]);		
			if(argc==4+1){
				int u = stoi(argv[4]);
				printf("\tp(%d <=x<= %d)= %.8e%%\n", l, u, bin(N, p, l, u)*100);
			}else
				printf("\tp(x = %d)= %.8e%%\n", l, bin(N, p, l, l)*100);
			if(stod(argv[1])-stoi(argv[1])!=0 || stod(argv[3])-stoi(argv[3])!=0 || ( argv[4]!=NULL && stod(argv[4])-stoi(argv[4])!=0))
				cout <<"\t*expected integer numbers are truncated to integers! eg. 2.6 -> 2\n";
		} catch(invalid_argument& err){ cerr<<"\t*"<<err.what()<<"\n"; }
}	// main