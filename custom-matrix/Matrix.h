#pragma once
#include "Vector.h"

class Matrix : public Vector
{
	unsigned int w;		//	imax
	Vector* M;			//	array pointer to vectors
public:
	Matrix();										// creates a default 3x3 identity matrix
	Matrix(unsigned int, unsigned int, double);		// create matrix[unsigned int][unsigned int] each with entry 'double'
	Matrix(const Matrix&, int);						// copy constructor unless int==1? ->transpose constructor
	~Matrix();

	void rescale(double) override;
	void reset() override;
	void display() override;

	double& operator() (unsigned int i, unsigned int j) const;		//	return matrix at(i,j)
	Vector& operator[] (unsigned int i) const;						//	returns the (i)th row (vector)
	Matrix operator+ (const Matrix&);								//	M1+M2
	Matrix operator- (const Matrix&);								//	M1-M2
	Matrix operator* (const Matrix&);								//	M1*M2 -> returns M3[M1.w, M2.l]
};