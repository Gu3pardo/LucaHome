#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef USER_H
#define USER_H

class User {

private:
	std::string _name;
	std::string _password;
	int _role;

public:
	User();
	User(std::string, std::string, int);
	~User();

	void setName(std::string);
	void setPassword(std::string);
	void setRole(int);

	std::string getName();
	std::string getPassword();
	int getRole();

	std::string toString();
};

#endif
