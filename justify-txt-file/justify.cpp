#include "justify.h"

unsigned int justify(unsigned int width, string in, vector<string> & out) {
	//	not leagal width? return definition that corresponds to 2
	if (in.length() == 0)
		return ERROR_NO_CHARS;
	else
		//	empty string? return definition that corresponds to 1
		if (!(width >= MIN_COL_WIDTH && width <= MAX_COL_WIDTH))
			return ERROR_ILLEGAL_WIDTH;
	//	string good? justify it and retrun 0 that corresponds to 0
		else {
			string a = "";
			for (unsigned int i = 0; i < in.length(); ++i) {
				//	'a' takes all chars until including width-2, at which it examins the next 3 chars; 'a' is then stacked over out[final] dynamically after doing the following modifications:
				a += in[i];
				if (a.length() == width - 1) {
					//	we at a space? then stack 'a' on out[final]; if instead the we at alpha and next char is space? then we skip next iter to not start out[line+1] with space
					if (isspace(in[i]) || isspace(in[i + 1])) {
						out.push_back(a);
						i += (isspace(in[i + 1]) ? 1 : 0);
					}
					//	2 chars coming up in a row?	don't take either in 'a' and instead add hyphen at end; don't skip next iters because next 2 chars will start out[line+1]
					else if (isalpha(in[i + 1]) && isalpha(in[i + 2])) {
						out.push_back(a + '-');
					}
					//	next is punctuation mark or char followed by space? then add it to 'a' without the space; skip next 2 iter because we took char or punct already and we need not start out[line+1] with space
					else if (ispunct(in[i + 1]) || (isalpha(in[i + 1]) && isspace(in[i + 2]))) {
						out.push_back(a + in[i + 1]);
						i += 2;
					}
					//	alpha followed by punct coming up? tolerated case: add both to 'a' and skip next 3 iter since a punct is assumed to be followed with a space (from the assignment specifics)
					else if (isalpha(in[i + 1]) && ispunct(in[i + 2])) {
						out.push_back(a + in[i + 1] + in[i + 2]);
						i += 3;
					}
					//	clear 'a' once it's stacked over out[final], and then on [i+1], 'a' takes chars again uptil width-2 and so on...
					a.clear();
				}	//	if
				//	we reach end of string and not necessarily width-1?	shove 'a' into the last element in vector anyway
				else if (i + 1 == in.length())
					out.push_back(a);
			}	// for i	
			return ERROR_NONE;
		}	// if-else{if-else} (text evaluation)
}	//	justify