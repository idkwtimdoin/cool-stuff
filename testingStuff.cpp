#include <iostream>
#include <thread>
#include <chrono>
#include <string>
#include <cstdlib>
#include <cmath>
#include <fstream>
using namespace std;

void myclock() {
	int go; cout << "go?: "; cin >> go; 
	if (go)
		for (int i = 0; 1; i++) {
			cout << "\t" << (i / 3600) % 24 << ":" << (i / 60) % 60 << ":" << i % 60 << endl;
			this_thread::sleep_for(chrono::seconds(1));
		}
}
void geoS() {
	double r;
	do {
		cout << "r: ]0,1[ ";	cin >> r;
	} while (r <= 0 || r >= 1);
	double diff = 1; double sum = 0; double temp; int iter;
	for (int n = 0; diff >= 0.01; ++n) {
		temp = sum;
		sum += (double)pow(r, n);
		diff = sum - temp;
		iter = n;
		cout << n << endl << sum << endl << "\v";
	}
	//	cout << "sum approx: " << sum << "\titer: " << iter << endl;
}
void cases() {
	char user_cmd;
	int some_num;
	some_num = 6;
	cout << "Type in either a, b, c or d:" << endl;
	cin >> user_cmd;
	switch (user_cmd)
	{
	case 'a': cout << "You typed in an a" << endl;
		cout << "That is nice." << endl;
		break;
	case'b': cout << "You typed in a b" << endl;
		cout << "That is also nice " << endl;
		break;
	case 'c': cout << "You typed in a c" << endl;
		cout << "The number is : " << some_num << endl;
		break;
	case 'd': cout << "you typed in a d" << endl;
		cout << "I hope that you are not typing in your grade!" << endl;
		break;
	default: cout << "Really you should follow instructions!" << endl;
	}
}
void christree() {
	int dia, I;
	do {
		cout << "r: "; cin >> dia;
		int index = 2 * dia + 1;
		for (int i = 1; i <= index; ++i) {
			for (int j = 1; j <= index; ++j) {
				I = i > dia + 1 ? (dia + 1) - (i - (dia + 1)) : i;
				cout << (((j >= ((dia + 1) - (I - 1))) && (j <= (dia + 1) + (I - 1))) ? "*" : " ");
			}
			cout << "\n";
		}
	} while (dia != 0);
}
double long F(double long I, double long prod) {
	if (I<2)	return 1;
	else {
		prod *= I;	I--;
		if (I >= 2)	return F(I, prod);
		else	return prod;
	}
}
void fabonacci() {
	int max; cout << "#terms: "; cin >> max;
	int temp = 0, sum = 0, t1 = 0, t2 = 1;
	cout << "0 " << "1 1 ";
	for (int i = 1; i <= max; ++i) {
		temp = t2;    sum = t1 + t2;
		if (!(sum == 1 || sum == 0)) cout << sum << " ";
		t2 = sum;    t1 = temp;
	}
} // fabonacci
void rando() {
	int max; cout << "max: "; cin >> max;
	for (int i = 1; i <= max; ++i) {
		float h = rand() % 365;
		cout << h << "\n";
	}
}
string ConvertString(string input_string, int mod_int) {
	int act_mod = mod_int < 0 ? (26 + mod_int) % 26 : mod_int % 26;        /* if -ve mod_int, return the +ve equivalent number */
	for (int i = 0; i < input_string.length(); i++)
		if (input_string[i] >= 65 && input_string[i] <= 90)            // ascii for A-Z
			input_string[i] = (input_string[i] + act_mod)>90 ? 64 + ((input_string[i] + act_mod) % 90) : input_string[i] + act_mod;
		else    /* the ternary operator isures that ascii's don't go out of bounds, if it does, moludlus it, then add it to the respective 'base counter' (64, 96) */
			if (input_string[i] >= 97 && input_string[i] <= 122)    // ascii for a-z
				input_string[i] = (input_string[i] + act_mod)>122 ? 96 + ((input_string[i] + act_mod) % 122) : input_string[i] + act_mod;
	return input_string;
} // ConvertString
void assign1() {
	int mod_int, recover_int;
	string input_string, mod_string, recovered_string;
	while (1) {
		cout << "Please enter modification key (or -1 to exit)" << endl;
		cin >> mod_int;
		if (mod_int == -1) break;
		cout << "Please enter text to be modified" << endl;
		cin >> input_string;
		mod_string = ConvertString(input_string, mod_int);
		cout << endl << "The modified text is " << endl << mod_string << endl;
		recover_int = 26 - (mod_int % 26);
		recovered_string = ConvertString(mod_string, recover_int);
		cout << endl << "The recovered text is " << endl << recovered_string << endl << endl;
	}
} // assign
double fact() {
	unsigned long long I; cout << "I: "; cin >> I;
	unsigned long long prod = 1;
	return F(I, prod);
}
double bin(long N, double p, int p1, int p2) {
	double long sum = 0;
	ofstream ray;
	ray.open("data");
	for (int x = p1; x <= p2; x++) {
		sum += ((pow(p, x)*pow(1 - p, N - x)) / (F(x, 1)*F(N - x, 1)));
		ray << x << "\t" << F(N, 1)*((pow(p, x)*pow(1 - p, N - x)) / (F(x, 1)*1.0*F(N - x, 1))) << "\n";
	}
	return F(N, 1)*sum;
} 
void assign2() {
	int flag = 1;
	long N; double p; int p1, p2;
	while (flag) {
		do { 
			cout << "for bin(N, p, [p1-p2]), \ninput: N, p, p1, p2 | all are +ve, N> p2, p<=1:\n";	
			cin >> N >> p >> p1 >> p2; 
		} while (N > p2 && p > 0 && p1 > 0 && p2 > 0 && p <= 1);
		cout << "bin(" << N << ", " << p << ", [" << p1 << "-" << p2 << "]" << "): " << bin(N, p, p1, p2)
		<< "\n\nagain? 0 for no: "; cin >> flag;
	}
} 
int febonaci(int f) {
	if (f <=1 )
		return f;
	else
		return febonaci(f - 1) + febonaci(f - 2);
}
float check(string in, int order) {
	int lol = 1;	float rez=0;
	if (order != 3) {
		for (int i = 0; i < in.length(); ++i)	if (!(in[i] <= 57 && in[i] >= 48))	lol = -1;
		if (lol!=-1) {
			for (int i = 0; i < in.length(); ++i) 
				rez += (in[i]-48)*pow(10, in.length()-i-1);
			return  rez;
		}
		else return -1;
	}
	else {
		for (int i = 0; i < in.length(); ++i)
		{
			if (i == 1) continue;
			if (!(in[0] <= 57 && in[0] >= 48))	lol = -1;
		}
		if (lol!=-1 && in[1] == 46 && in[0] == 48) {
			for (int i = 2; i < in.length(); ++i)
				rez += (in[i]-48)*pow(10,-i+1);
			return rez;
		}
		else return -1;
	}
}
float* george() {
	string a, input;	 bool bigger = 1;	float* A = new float[4];
	cout << "input N, x2, x1, p where all are +ve, N>=x2>=x1, 0<=p<=1:\n";
	for (int i = 0; i < 4; ++i) {
		switch (i) {
		case 0: input = "N";	break;
		case 1: input = "x2";	break;
		case 2: input = "x1";	break;
		case 3: input = "p";
		}
		do {
			cout << input << ": ";	cin >> a;
			if(i!=4)	A[i] = check(a, i);
			else 	A[i] = check(a, i);	
			bigger = i >= 1 ? A[i]>A[i-1] : 0;
		} while (A[i] == -1 || bigger);
		cout << "\nyour " << input << " is " << A[i] << "\n";
	}
	return A;
}
bool prime(long long int n) {
	bool check=0;
	for (int i = 2; i < n; ++i)
	{
		cout << "\n" << i;
		if (n%i == 0)	{	check++; break;	}
	}
	return check;
}
void isitprime() {
	long long int numb;
	do {
		cout << "your numb: "; cin >> numb;
		cout << "\n0/1 prime/not:\t" << prime(numb) << "\n";
	} while (1);
}
void denis() {
	int sum = 0;
	int arr[100];
	for (int i = 0; i < 100; i++) {
		arr[i] = i + 1;
		sum += arr[i];
		cout << sum << "\n" << "\t";
	}
}

int main() {
//	cout << fact() << "\n";
//	cases();
//	geoS();	
//	myclock();
//	christree();
//	fabonacci();
//	rando();
//	assign1();
//	assign2();
//	cout << febonaci(10);
//	isitprime();
//	denis();
	system("PAUSE");
} 