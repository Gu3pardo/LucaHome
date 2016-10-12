#include "gpio.h"

Gpio::Gpio() {
}

Gpio::Gpio(std::string _name, int _gpio, int _state) {
	name = _name;
	gpio = _gpio;
	state = _state;
}

Gpio::~Gpio() {
}

void Gpio::setName(std::string _name) {
	name = _name;
}

void Gpio::setGpio(int _gpio) {
	gpio = _gpio;
}

bool Gpio::setState(int _state) {
	state = _state;
	syslog(LOG_INFO, "Set Gpio %d to %d", gpio, state);
	return PiControl::writeGpio(gpio, state);
}

std::string Gpio::getName() {
	return name;
}

int Gpio::getGpio() {
	return gpio;
}

int Gpio::getState() {
	return state;
}

std::string Gpio::toString() {
	std::string str = std::string("Gpio { name: ") + name
			+ std::string("; gpio: ") + Tools::convertIntToStr(gpio)
			+ std::string("; state: ") + Tools::convertIntToStr(state)
			+ std::string(" }");
	return str;
}
