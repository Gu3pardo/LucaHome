#include <string>
#include <ctime>
#include <cstring>
#include <iostream>
#include <syslog.h>

#include "../common/picontrol.h"
#include "../common/tools.h"

#ifndef SOCKET_H
#define SOCKET_H

class Socket {

private:
	std::string name;
	std::string area;
	std::string code;
	int state;

public:
	Socket();
	Socket(std::string, std::string, std::string, int);
	~Socket();

	void setName(std::string);
	void setArea(std::string);
	void setCode(std::string);
	bool setState(int, int);

	std::string getName();
	std::string getArea();
	std::string getCode();
	int getState();

	std::string toString();
};

#endif
