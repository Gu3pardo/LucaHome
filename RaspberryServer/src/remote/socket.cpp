#include "socket.h"

Socket::Socket() {

}

Socket::Socket(std::string _name, std::string _area, std::string _code,
		int _state) {
	name = _name;
	area = _area;
	code = _code;
	state = _state;
}

Socket::~Socket() {
}

void Socket::setName(std::string _name) {
	name = _name;
}

void Socket::setArea(std::string _area) {
	area = _area;
}

void Socket::setCode(std::string _code) {
	code = _code;
}

bool Socket::setState(int _state, int _datagpio) {
	state = _state;
	if (_datagpio != 0) {
		syslog(LOG_INFO, "Set Socket %s to %d", name.c_str(), state);
		return PiControl::send433Mhz(_datagpio, code, state);
	}
	return false;
}

std::string Socket::getName() {
	return name;
}

std::string Socket::getArea() {
	return area;
}

std::string Socket::getCode() {
	return code;
}

int Socket::getState() {
	return state;
}

std::string Socket::toString() {
	std::string str = std::string("Socket { name: ") + name
			+ std::string("; area: ") + area + std::string("; code: ") + code
			+ std::string("; state: ") + Tools::convertIntToStr(state)
			+ std::string(" }");
	return str;
}
