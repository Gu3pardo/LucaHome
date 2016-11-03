#include <string>
#include <cstring>
#include <iostream>

#include "point.h"
#include "../common/tools.h"

#ifndef MAPCONTENT_H
#define MAPCONTENT_H

class MapContent {

private:
	int _id;
	Point _position;
	int _type;
	std::vector<std::string> _schedules;
	std::vector<std::string> _sockets;
	std::string _temperatureArea;

public:
	MapContent();
	MapContent(int, Point, int, std::vector<std::string>,
			std::vector<std::string>, std::string);
	~MapContent();

	int getId();
	Point getPosition();
	int getType();
	std::vector<std::string> getSchedules();
	std::vector<std::string> getSockets();
	std::string getTemperatureArea();

	std::string getRestString();
	std::string getFileString();
	std::string toString();

	std::string getSchedulesString();
	std::string getSocketsString();
};

#endif
