#include <string>
#include <ctime>
#include <cstring>
#include <iostream>
#include <syslog.h>

#include "../common/picontrol.h"
#include "../common/tools.h"

#ifndef GPIO_H
#define GPIO_H

class Gpio {

private:
	std::string name;
	int gpio;
	int state;

public:
	Gpio();
	Gpio(std::string, int, int);
	~Gpio();

	void setName(std::string);
	void setGpio(int);
	bool setState(int);

	std::string getName();
	int getGpio();
	int getState();

	std::string toString();
};

#endif
