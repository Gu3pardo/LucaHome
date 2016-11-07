#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#ifndef LOG_H
#define LOG_H

class Log {

private:
	std::string _level;
	std::string _content;
	std::string _time;
	std::string _user;

public:
	Log(std::string, std::string, std::string, std::string);
	~Log();

	std::string getLevel();
	std::string getContent();
	std::string getTime();
	std::string getUser();

	std::string toFileString();
	std::string toString();
};

#endif
