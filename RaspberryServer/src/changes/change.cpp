#include "change.h"

Change::Change() {
}

Change::Change(std::string _type, int _hour, int _minute, int _day, int _month,
		int _year, std::string _user) {
	type = _type;
	hour = _hour;
	minute = _minute;
	day = _day;
	month = _month;
	year = _year;
	user = _user;
}

Change::~Change() {
}

void Change::setType(std::string _type) {
	type = _type;
}

void Change::setHour(int _hour) {
	hour = _hour;
}

void Change::setMinute(int _minute) {
	minute = _minute;
}

void Change::setDay(int _day) {
	day = _day;
}

void Change::setMonth(int _month) {
	month = _month;
}

void Change::setYear(int _year) {
	year = _year;
}

void Change::setUser(std::string _user) {
	user = _user;
}

std::string Change::getType() {
	return type;
}

int Change::getHour() {
	return hour;
}

int Change::getMinute() {
	return minute;
}

int Change::getDay() {
	return day;
}

int Change::getMonth() {
	return month;
}

int Change::getYear() {
	return year;
}

std::string Change::getUser() {
	return user;
}

std::string Change::toString() {
	std::string str = std::string("Change { type: ") + type
			+ std::string("; hour: ") + Tools::convertIntToStr(hour)
			+ std::string("; minute: ") + Tools::convertIntToStr(minute)
			+ std::string("; day: ") + Tools::convertIntToStr(day)
			+ std::string("; month: ") + Tools::convertIntToStr(month)
			+ std::string("; year: ") + Tools::convertIntToStr(year)
			+ std::string("; user: ") + user
			+ std::string(" }");
	return str;
}
