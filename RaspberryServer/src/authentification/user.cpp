#include "user.h"

User::User() {
	_name = "DUMMY";
	_password = "NO_PASSWORD";
	_role = -1;
}

User::User(std::string name, std::string password, int role) {
	_name = name;
	_password = password;
	_role = role;
}

User::~User() {
}

void User::setName(std::string name) {
	_name = name;
}

void User::setPassword(std::string password) {
	_password = password;
}

void User::setRole(int role) {
	_role = role;
}

std::string User::getName() {
	return _name;
}

std::string User::getPassword() {
	return _password;
}

int User::getRole() {
	return _role;
}

std::string User::toString() {
	std::string str = std::string("User { name: ") + _name
			+ std::string("; password: ") + _password + std::string("; role: ")
			+ Tools::convertIntToStr(_role) + std::string(" }");
	return str;
}
