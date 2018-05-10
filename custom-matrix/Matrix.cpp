#include "Matrix.h"
#include <iostream>

Matrix::Matrix() {
	M = new Vector[3];
	for (int i = 0; i < 3; ++i)
		M[i] = Vector(i==0? 1:0, i==1? 1:0, i==2? 1:0);				// just the 3x3 default identity matrix
	w = 3;
}

Matrix::Matrix(unsigned int imax, unsigned int jmax, double a) {
	imax = imax ? imax : 1;											//<<-------- can't have a vector with 0 elements
	M = new Vector[imax];
	for (int i = 0; i < imax; ++i)
		M[i] = Vector(jmax, a);
	w = imax;
}

Matrix::Matrix(const Matrix& A, int t) {
//	normal deep copy if t=/=1
	if (t != 1) {
		w = A.w;
		M = new Vector[A.w];
		for (int i = 0; i < A.w; ++i)	M[i] = Vector(A[0].leng(), 0);

		for (int i = 0; i < A.w; ++i)
			M[i] = A[i];
	} else {
//	transped version of the matrix
		w = A[0].leng();
		M = new Vector[A[0].leng()];
		for (int i = 0; i < A[0].leng(); ++i)	M[i] = Vector(A.w, 0);

		for (int i = 0; i < A.w; ++i)
			for (int j = 0; j < A[0].leng(); ++j)	M[j](i) = A(i, j);		//<<----- (*this)(j,i) = assignedObject(i,j) INVERSED ORDER
	}
}

Matrix::~Matrix(){
//	note the delete[] operator is not used for Vector* M because the Vector Class destructor is classed first; thus deleting all elements of M
}

Vector& Matrix::operator[] (unsigned int i) const {
	if (i + 1 > w)	throw "matrix [i]subscript out of range\n";				//<<----- avoid access violation errors
	else	return M[i];
}

double& Matrix::operator() (unsigned int i, unsigned int j) const {
	if (i + 1 > w)	throw "matrix [i]subscript out of range\n";				//<<----- avoid access violation errors
	else	return M[i](j);
}

Matrix Matrix::operator+(const Matrix& O1) {
	if (w != O1.w)	throw "+rows x cols don't match\n";		//<<---- check that size is the same
	else {
		Matrix New(O1.w, O1[0].leng(), 0);
		for (int i = 0; i < O1.w; ++i)
			New[i] = (*this)[i] + O1[i];
	return New;
	}
}

Matrix Matrix::operator-(const Matrix& O1) {
	if (w != O1.w)	throw "-rows x cols don't match\n";		//<<---- check that size is the same
	else {
		Matrix New(O1.w, O1[0].leng(), 0);
		for (int i = 0; i < O1.w; ++i)
			New[i] = (*this)[i] - O1[i];
		return New;
	}
}

Matrix Matrix::operator*(const Matrix& O1) {
	if ((*this)[0].leng() != O1.w)	throw "*multiplication of M1]cols =/= M2]rows\n";		//<<---- M1*M2 then M1 cols must= M2 rows
	else {
// and product matrix must be M[M1 rows][M2 cols]
		Matrix New(w, O1[0].leng(), 0);
// make a temp transposed version of assignedObject: note the transpoed constructor because (~,int ***1***)
		Matrix T(O1, 1);
		for (int i = 0; i < New.w; ++i)
			for (int j = 0; j < New[0].leng(); ++j)
// each element in the product matrix is equal to the sum of products of (*this)at(vector[i]) and transposed assignedObject(vector[i]), which is the same as dot product
					New(i, j) = (*this)[i] ^ T[j];
		return New;
	}
}
void Matrix::rescale(double m) {
	for (int i = 0; i < w; ++i)
		(*this)[i].rescale(m);
}

void Matrix::reset() {
	for (int i = 0; i < w; ++i)
		(*this)[i].reset();
}

void Matrix::display() {
	for (int i = 0; i < w; ++i)
		(*this)[i].display();
	std::cout << std::endl;
}