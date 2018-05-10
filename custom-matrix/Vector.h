#pragma once
class Vector
{
	unsigned int l;		//	jmax
	double* v;			//	array pointer to doubles
public:
	Vector();							// creates a default (x,y,z) = (0,0,0)
	Vector(unsigned int, double);		// creates a vector with #(unsigned int) elements, each of entry (double)
	Vector(double, double, double);		// creates a vector (x,y,z) = (double, double, double)
	Vector(const Vector&);				// copies a vector
	~Vector();

	virtual void reset();				// set all elements =0
	virtual void rescale(double);		// mult all elements by (double)
	virtual void display();				// cout vector components
	int leng() const;					// returns the number of elements in vector

	double& operator() (const unsigned int) const;		// returns v[(const unsigned int)]
	Vector operator+ (const Vector&);					// V1.+(V2)
	Vector operator- (const Vector&);					// V1.-(V2)
	Vector operator* (const Vector&);					// (*this) cross (V2)	where (*this) is the object that is calling the function using the dot operator '.'
	double operator^ (const Vector&);					// V1 dot V2
	void operator= (const Vector&);						// equate a vector to (*this)
};