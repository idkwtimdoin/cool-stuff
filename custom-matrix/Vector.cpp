#include "Vector.h"
#include <iostream>

Vector::Vector() {
	v = new double[3];
	for (int i = 0; i < 3; ++i)	v[i] = 0;
	l = 3;
}
Vector::Vector(unsigned int a, double b) {
	a = a? a : 1;			//<<-------- can't have a vector with 0 elements
	v = new double[a];
	for (int i = 0; i < a; ++i)		v[i] = b;
	l = a;
}
Vector::Vector(double a, double b, double c) {
	v = new double[3];
	v[0] = a;
	v[1] = b;
	v[2] = c;
	l = 3;
}

Vector::Vector(const Vector& O1) {
	v = new double[O1.l];
	l = O1.l;
	for (int i = 0; i < l; ++i)	v[i] = O1(i);
}

Vector::~Vector() {	
	(*this).reset();		// reset elements just to make sure
	delete[] v;				// delete the dynamically allocated memory
}

double& Vector::operator() (const unsigned int i) const {
	if (i + 1 > l)	throw "vector [j]subscript out of range\n";					//<----- avoid access violation runtime errors
	else	return v[i];
}

Vector Vector::operator+(const Vector& O1) {
	if (O1.leng() != l)	throw "(+) numb elements does not match\n";				//<----- check that number elements matches before adding all
	else {
		Vector O3(O1.leng(), 0);
		for (int i = 0; i < O3.leng(); ++i) O3(i) = v[i] + O1(i);
		return O3;
	}
}

Vector Vector::operator-(const Vector& O1) {
	if (O1.leng() != l)	throw "(-) numb elements does not match\n";				//<----- check that number elements matches before sub all
	else {
		Vector O3(O1.leng(), 0);
		for (int i = 0; i < O3.leng(); ++i) O3(i) = v[i] - O1(i);
		return O3;
	}
}

Vector Vector::operator*(const Vector& O1) {
	if (O1.leng() != 3 || l!=3) throw "number of elements needs to be 3 for cross product\n";	
	else																		//<----- for cross product, it should be a 3 x 3 vector, else we need a tensor
		return Vector(O1(2)*v[1] - O1(1)*v[2], O1(0)*v[2] - O1(2)*v[0], O1(1)*v[0] - O1(0)*v[1]);
}

double Vector::operator^(const Vector& O1) {
	if (O1.leng() != l)	throw "(^) numb elements does not match\n";				//<----- check for size before dotting
	else {
		double sum = 0;
		for (int i = 0; i < O1.leng(); ++i) sum += (O1(i) * v[i]);
		return sum;
	}
}

//	the =operator is used when we assign a vector to another even if (*this).l =/= assignedObject.l
void Vector::operator=(const Vector& O1) {
	v = new double[O1.l];
	l = O1.l;
	for (int i = 0; i < l; ++i) 
		v[i] = O1(i);
}

void Vector::reset() {
	for (int i = 0; i < l; ++i) v[i] = 0;
}

void Vector::rescale(double m) {
	for (int i = 0; i < l; ++i) v[i] *= m;
}

int Vector::leng() const {
	return l;
}
void Vector::display() {
	for (int j = 0; j < l; ++j)
		std::cout << v[j] << " ";
	std::cout << "\n\n";
}