#include <iostream>
#include <math.h>
#include <fstream>
#include <ctime>
#include <vector>
#include <thread>
#include <chrono> 

using namespace std;

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+		SET THE PROBLEM PARAMETERS AND THE ABSOLUTE VALUE OF THE SIMULATION DOMAIN	BOUNDARY	+-+-+-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
const float params[3][5] = { { 3,5,2,1,7 },{ 5,2,1,4,9 },{ 1,2,5,2,3 } };
const float xyPoints[20][2] = { { -1.5,-6 },{ -1.7,-10 },{ -1,0 },{ 0,4 },{ 1,1 },{ 2,2 },{ 2,5 },{ 3,4 },{ 2.5,7 },{ 3,10 },{ -1.8,-22 },
								{ -2,-30 },{ -2.5,-50 },{ 2.8,12 },{ 3.9,47 },{ 3.4,25 },{ 0.6,2.5 },{ 1.5,0.33 },{ 2.5,7 },{ -3,-66 } };
#define LengthSimField (10)

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+			SET APPROPRIATE PARAMS FOR PLOTTING AND FOR THE ALGORITHMS			+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
#define DELTA (0.05)
#define nPts (100)
#define nInd (100)
#define nGen (100)
#define PI (3.14159265358979)

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+		Cop IS BASE CLASS FOR BOTH KINDS OF INIVIDUALS: (x,y),(a,b,c,d)			+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
struct Cop {
	float* c;
	int s;
	Cop() {
		s = 2;
		c=new float[2];
		c[0] = rand()*LengthSimField*1. / RAND_MAX;
		c[1] = rand()*LengthSimField*1. / RAND_MAX;
	}
	~Cop() { delete c; }
	Cop(float x, float y) {
		s = 2;
		c = new float[2];
		c[0] = x;
		c[1] = y;
	}
	virtual float eval(float) = 0;
	virtual void mutate() {
		int r = rand() % s;
		c[r] = LengthSimField - c[r];
	}
	void disp() {
		for (int i = 0; i<s; ++i)
			cout << c[i] << ((i + 1 == s) ? "\t" : ",");
		cout << this->eval(1) << endl;
;	}
	void setCoor(Cop& sel) {
		for (int i = 0; i < s; ++i)	c[i] = sel.c[i];
	}
};

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+		Ind (x,y) INHERITS FROM Cop AND STORES ITS (x,y) DYNAMICALLY IN c*			+-+-+-+-+-+-+-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
struct Ind : public Cop {
	Ind() : Cop() {}
	Ind(float x, float y): Cop(x,y) {}
	float Langer(float x, float y, float sum) {
		for (int i = 0; i < sizeof(params[0]) / sizeof(params[0][0]); ++i)
			sum -= exp(-(pow(x - params[0][i], 2) + pow(y - params[1][i], 2)) / PI)*cos((pow(x - params[0][i], 2) + pow(y - params[1][i], 2)) * PI) * params[2][i];
		return sum;
	}
	float eval(float sign) {
		if (sign)	return Langer(c[0], c[1], 0);
		else	return abs(Langer(c[0], c[1], 0));
	}
};

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+-+-+		Point (a,b,c,d) INHERITS x,y AND ADDS c,d TO THE COLLECTION OF VARIABLES 			+-+-+-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
struct Point : public Cop {
	Point() :Cop() {
		s = 4;
		c[2]= rand()*LengthSimField*1. / RAND_MAX;
		c[3]= rand()*LengthSimField*1. / RAND_MAX;
		for (int i = 0; i < s; ++i)	c[i] *= ((1.*rand() / RAND_MAX)>0.5 ? 1 : -1);
	}
	Point(float x, float y, float z, float w) :Cop(x, y) {
		s = 4;
		c[2] = z;
		c[3] = w;
	}
	float Polyf(float x) {
		return ( c[0]*pow(x, 3) + c[1] *x*x + c[2] *x + c[3]);
	}
	float eval(float error) override {
		error = 0;
		for (int i = 0; i < sizeof(xyPoints) / sizeof(xyPoints[0]); ++i) {
			error += pow(Polyf(xyPoints[i][0]) - xyPoints[i][1], 2);
		}
		return error/(sizeof(xyPoints) / sizeof(xyPoints[0]));
	}
	void mutate() override{
		for (int i = 0; i < s; ++i) {
			c[i] += c[i]*(1.*rand() / RAND_MAX);
			if ((1.*rand() / RAND_MAX)>0.8)	c[i] = 0;
		}

	}
};

/*+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
+-+-  POOL IS THE CONTAINER CLASS THAT PROVIDES THE CORRESPONDING INVIRONMENT FOR THE INDIVIDUALS   +-+-+-
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-*/
template <typename T>
struct Pool {
	vector<T*>pop;
	int index;
	int maxGen;
	int squad;
	float mut_const;
	int numMutated;
	Pool(int numbInd) {
		index = numbInd;
		for (int i = 0; i < index; ++i)
			pop.push_back(new T);
		numMutated = 0;
	}
	void findB() {
		T* temp = new T;
		for (int i = 0; i < index - 1; ++i) {
			for (int j = i + 1; j < index; ++j)
				if ((*pop[i]).eval(1) <(*pop[j]).eval(1)) {
					*temp = *pop[i];
					*pop[i] = *pop[j];
					*pop[j] = *temp;
				}
		}
	}
	T* operator[](int i) {
		return pop[i];
	}
	float ave(float average) {
		for (int i = 0; i < index; ++i) average += (*pop[i]).eval(1);
		return average / index;
	}
	void disp() {
		for (int i = 0; i < index; ++i)
			(*pop[i]).disp();
		cout << endl;
	}
	void crux(int x, int y, int z) {
		float r;
		for (int i = 0; i < (*pop[x]).s; ++i) {
			r = 1.*rand() / RAND_MAX;
			(*(*this)[x]).c[i] = r*(*(*this)[y]).c[i] + (1 - r)*(*(*this)[z]).c[i];
		}
	}
	void solve(int numGen, int numElites, float mut) {
		clock_t t1, t2;
		mut_const = mut;
		maxGen = numGen;
		squad = numElites;

		//	a clone of the same size of the population that is to be solved
		Pool<T>Copy(index);

		t1 = clock();
		for (int i = 0; i < maxGen; ++i)
		{
			//	sort the population | arrange in descending order
			this->findB();

			// the clone[j] takes either 1] , 2] , 3]
			for (int j = 0; j < index; ++j)
				//	1]	some of the elite of past generation to crossover with the next
				if (j >= index - squad)
					Copy[j]->setCoor(*(*this)[j]);
				else {
					//	2]	some mutated individuals
					if ((1.*rand() / RAND_MAX) < exp(-i / mut_const)) {
						Copy[j]->mutate();
						numMutated++;
					} else
						//	3] a crossedover gene of 2 individuals that are slightly more probable to be elite
						Copy.crux(j, rand() % index, rand() % index);
				}
				for (int j = 0; j < index; ++j)
					//	after being improved, the clone is assigned to the original population using deep assignment, at end of each generation
					(*this)[j]->setCoor(*Copy[j]);
				cout << "gnr# " << i  << (i<=99 ? "\t" : "") << "\t#mutated= "<< numMutated << "\tave(fitness)=  " << this->ave(0) <<endl;
				numMutated = 0;
		}
		t2 = clock();

		//	display some of the fit solutions harvested for whatever pool we're dealing with
		cout << "\n\nrun time: " << (t2 - t1) / 1000. << "s\nELITES:\n";
		for (int i = 0; i < squad; i += 2)
			(*(*this)[index - 1- i]).disp();
		cout << endl;
	}
};