  /*******************Programming Assignment 4***************/
  /********************Text Justification**********************/

  /*****************YOU MUST NOT EDIT THIS FILE**************/
  /*****************YOU MUST NOT SUBMIT THIS FILE**************/

  /*********Definition of types, error codes and function prototype**************************/


#include <iostream>
#include <string>
#include <vector>
#include "ctype.h"

using namespace std;

#define MIN_COL_WIDTH 10
#define MAX_COL_WIDTH 60

// error codes
#define ERROR_NONE 0
#define ERROR_NO_CHARS 1
#define ERROR_ILLEGAL_WIDTH 2

// function prototype
unsigned int justify(unsigned int width, string in, vector<string> &out);
