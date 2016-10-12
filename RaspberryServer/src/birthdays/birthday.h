#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef BIRTHDAY_H
#define BIRTHDAY_H

class Birthday {

private:
	std::string name;
	int day;
	int month;
	int year;

public:
	Birthday();
	Birthday(std::string, int, int, int);
	~Birthday();

	void setName(std::string);
	void setDay(int);
	void setMonth(int);
	void setYear(int);

	std::string getName();
	int getDay();
	int getMonth();
	int getYear();

	std::string toString();
};

#endif
