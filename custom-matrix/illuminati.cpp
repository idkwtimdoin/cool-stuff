#include <iostream>
#include "Vector.h"
#include "Matrix.h"
#include <exception>

int main() {
//	create vectors
	std::cout << "testing vectors:\n\n";
	Vector V1(2,-3,-5);
	Vector V2(3, 0, 4);
	Vector V3(5, 5);
	Vector V4;
	Vector V5(V4);
	V4 = V1 - V2;

//	testing for errors
	try {
		V1.display();
		V2.display();
		std::cout << "V2 cross V1:\t";	(V2*V1).display();
		std::cout << "V1 cross V2:\t";	(V1*V2).display();
		std::cout << "V1 dot V2:\t" << (V1^V2) << std::endl << std::endl;
		std::cout << "V1+V2:\t";	(V1 + V2).display();
		std::cout << "V1-V2:\t";	(V4).display();
//	testing adding 2 vectors which have different number of elements
		std::cout << "V1]3elements + V3]5elements:\t";	 (V1 + V3).display();
	}	catch (char* error) { std::cerr << "error:\t" << error <<"\n"; }
	
//	create matrices
	std::cout << "\ntesting matrices:\n\n";
	Matrix M1(2, 4, 2.5);
	Matrix M2(4, 2, 160);
	Matrix M3(2, 4, 40);
	Matrix M4(M3, 1);
	Matrix M5;
	M5 = M1 + M3;

//	testing matricies
	try {
		M5.display();
		M1.display();
		M3.display();
		M2.display();
		M4.display();
		(M3*M4).display();
		(M4*M3).display();
//	testing M1-M2 and M1,M2 are [2,4],[4,2] thus; rols x cols don't match
		(M1-M2).display();
	}	catch (char* error) { std::cerr << "error:\t" << error << "\n"; }
//	testing fetching an element that is out of [j]range
	try {
		std::cout << M5[0](6);
	}	catch (char* error) { std::cerr << "error:\t" << error << "\n"; }
//	testing fetching an element that is out of [i]range
	try {
		std::cout << M5(6, 6);
	}	catch (char* error) { std::cerr << "error:\t" << error << "\n"; }

	system("PAUSE");
}