#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

/* ---------------------- Schedule ---------------------- */

#ifndef SCHEDULE_H
#define SCHEDULE_H

class Schedule {

private:
	std::string name;
	std::string socket;
	std::string gpio;
	int weekday;
	int hour;
	int minute;
	int onoff;
	bool isTimer;
	int status;

public:
	Schedule();
	Schedule(std::string, std::string, std::string, int, int, int, int, int,
			int);
	~Schedule();

	void setName(std::string);
	void setSocket(std::string);
	void setGpio(std::string);
	void setWeekday(int);
	void setHour(int);
	void setMinute(int);
	void setOnoff(int);
	void setIsTimer(int);
	bool setStatus(int);

	std::string getName();
	std::string getSocket();
	std::string getGpio();
	int getWeekday();
	int getHour();
	int getMinute();
	int getOnoff();
	int getIsTimer();
	int getStatus();

	std::string toString();
};

#endif

/* ---------------------- ScheduleTask ---------------------- */

#ifndef SCHEDULETASK_H
#define SCHEDULETASK_H

class ScheduleTask {

private:
	std::string schedule;
	time_t time;
	int weekday;
	int done;
	int active;

public:
	ScheduleTask();
	ScheduleTask(std::string, time_t, int);
	~ScheduleTask();

	void setSchedule(std::string);
	void setTime(time_t);
	void setWeekday(int);
	void setDone(int);

	std::string getSchedule();
	time_t getTime();
	int getWeekday();
	int isDone();

	std::string toString();
};

#endif
