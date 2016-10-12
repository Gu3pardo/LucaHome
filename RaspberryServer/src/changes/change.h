#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef CHANGE_H
#define CHANGE_H

class Change {

private:
	std::string type;
	int hour;
	int minute;
	int day;
	int month;
	int year;
	std::string user;

public:
	Change();
	Change(std::string, int, int, int, int, int, std::string);
	~Change();

	void setType(std::string);
	void setHour(int);
	void setMinute(int);
	void setDay(int);
	void setMonth(int);
	void setYear(int);
	void setUser(std::string);

	std::string getType();
	int getHour();
	int getMinute();
	int getDay();
	int getMonth();
	int getYear();
	std::string getUser();

	std::string toString();
};

#endif
