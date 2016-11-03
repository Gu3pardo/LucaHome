#include "point.h"

Point::Point() {
	_x = -1;
	_y = -1;
}

Point::Point(int x, int y) {
	_x = x;
	_y = y;
}

Point::~Point() {
}

int Point::getX() {
	return _x;
}

int Point::getY() {
	return _y;
}

std::string Point::getString() {
	std::string str =
			Tools::convertIntToStr(_x)
			+ std::string("|")
			+ Tools::convertIntToStr(_y);
	return str;
}

std::string Point::toString() {
	std::string str = std::string("Point { ")
		+ std::string("; X: ") + Tools::convertIntToStr(_x)
		+ std::string("; Y: ") + Tools::convertIntToStr(_y)
		+ std::string(" }");
	return str;
}
