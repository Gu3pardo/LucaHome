#include <string>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef POINT_H
#define POINT_H

class Point {

private:
	int _x;
	int _y;

public:
	Point();
	Point(int, int);
	~Point();

	int getX();
	int getY();

	std::string getString();
	std::string toString();
};

#endif
