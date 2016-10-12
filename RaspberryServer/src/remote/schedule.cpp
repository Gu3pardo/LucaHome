#include "schedule.h"

/* ---------------------- Schedule ---------------------- */

Schedule::Schedule() {
}

Schedule::Schedule(
		std::string _name,
		std::string _socket,
		std::string _gpio,
		int _weekday,
		int _hour,
		int _minute,
		int _onoff,
		int _isTimer,
		int _status) {
    name = _name;
    socket = _socket;
    gpio = _gpio;
    weekday = _weekday;
    hour = _hour;
    minute = _minute;
    onoff = _onoff;
    isTimer = _isTimer;
    status = _status;
}

Schedule::~Schedule() {
}

void Schedule::setName(std::string _name) {
	name = _name;
}

void Schedule::setSocket(std::string _socket) {
	socket = _socket;
}

void Schedule::setGpio(std::string _gpio) {
	gpio = _gpio;
}

void Schedule::setWeekday(int _weekday) {
	weekday = _weekday;
}

void Schedule::setHour(int _hour) {
	hour = _hour;
}

void Schedule::setMinute(int _minute) {
	minute = _minute;
}

void Schedule::setOnoff(int _onoff) {
	onoff = _onoff;
}

void Schedule::setIsTimer(int _isTimer) {
	isTimer = _isTimer;
}

bool Schedule::setStatus(int _status) {
	status = _status;
	return true;
}

std::string Schedule::getName() {
  return name;
}

std::string Schedule::getSocket() {
  return socket;
}

std::string Schedule::getGpio() {
  return gpio;
}

int Schedule::getWeekday() {
  return weekday;
}

int Schedule::getHour() {
  return hour;
}

int Schedule::getMinute() {
  return minute;
}

int Schedule::getOnoff() {
  return onoff;
}

int Schedule::getIsTimer() {
  return isTimer;
}

int Schedule::getStatus() {
  return status;
}

std::string Schedule::toString() {
	std::string str =
			std::string("Schedule { name: ") + name
			+ std::string("; socket: ") + socket
			+ std::string("; gpio: ") + gpio
			+ std::string("; weekday: ") + Tools::convertIntToStr(weekday)
			+ std::string("; time: ") + Tools::convertIntToStr(hour)
			+ std::string(":") + Tools::convertIntToStr(minute)
			+ std::string("; onoff: ") + Tools::convertIntToStr(onoff)
			+ std::string("; isTimer: ") + Tools::convertIntToStr(isTimer)
			+ std::string("; status: ") + Tools::convertIntToStr(status)
			+ std::string(" }");
	return str;
}

/* ---------------------- ScheduleTask ---------------------- */

ScheduleTask::ScheduleTask() {
	done = 0;
}

ScheduleTask::ScheduleTask(
		std::string _schedule,
		time_t _time,
		int _weekday) {
	schedule = _schedule;
	memcpy(&time, &_time, sizeof(time_t));
	weekday = _weekday;
	done = 0;
}

ScheduleTask::~ScheduleTask() {
}

void ScheduleTask::setSchedule(std::string _schedule) {
	schedule = _schedule;
}

void ScheduleTask::setTime(time_t _time) {
	time = _time;
}

void ScheduleTask::setWeekday(int _weekday) {
	weekday = _weekday;
}

void ScheduleTask::setDone(int _done) {
	done = _done;
}

std::string ScheduleTask::getSchedule() {
	return schedule;
}

time_t ScheduleTask::getTime() {
	return time;
}

int ScheduleTask::getWeekday() {
	return weekday;
}

int ScheduleTask::isDone() {
	return done;
}

std::string ScheduleTask::toString() {
  std::string str =
		  std::string("ScheduleTask { name: ") + schedule +
		  std::string("; weekday: ") + Tools::convertIntToStr(weekday) +
		  std::string("; time: ") + ctime(&time) +
		  std::string("; done: ") + Tools::convertIntToStr(done) +
		  std::string(" }");
  return str;
}
