#include "log.h"

Log::Log(std::string level, std::string content, std::string time, std::string user) {
	_level = level;
	_content = content;
	_time = time;
	_user = user;
}

Log::~Log() {
}

std::string Log::getLevel() {
	return _level;
}

std::string Log::getContent() {
	return _content;
}

std::string Log::getTime() {
	return _time;
}

std::string Log::getUser() {
	return _user;
}

std::string Log::toFileString() {
	std::string str =
			_level + std::string(":")
			+ _content  + std::string(":")
			+ _time + std::string(":")
			+ _user + std::string(";");
	return str;
}

std::string Log::toString() {
	std::string str =
			std::string("Log { level: ") + _level
			+ std::string("; content: ") + _content
			+ std::string("; time: ") + _time
			+ std::string("; user: ") + _user
			+ std::string(" }");
	return str;
}
