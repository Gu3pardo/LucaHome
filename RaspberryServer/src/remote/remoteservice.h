#include <string>
#include <cstring>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <algorithm>

#include <pthread.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <syslog.h>

#include "gpio.h"
#include "schedule.h"
#include "socket.h"
#include "../changes/changeservice.h"
#include "../controller/filecontroller.h"
#include "../xml/xmlservice.h"

#ifndef REMOTESERVICE_H
#define REMOTESERVICE_H

class RemoteService {
private:
	std::string _settingsFile;

	int _port;
	int _datagpio;
	int _receivergpio;
	int _raspberry;

	std::string _area;
	std::vector<std::string> _areas;

	std::string _sensor;
	std::vector<std::string> _sensors;

	std::string _url;
	std::vector<std::string> _urls;

	std::vector<Gpio> _gpios;
	std::vector<Schedule> _schedules;
	std::vector<Socket> _sockets;

	FileController _fileController;
	XmlService _xmlService;

	void saveSettings(ChangeService, std::string);
	void loadSettings();

	int getDataGpio();

	std::vector<std::string> getAreas();
	std::vector<std::string> getSensors();

	std::vector<std::string> getUrls();

	std::string getGpiosString();
	std::vector<Gpio> getGpios();
	std::string getGpiosRestString();

	bool setGpio(std::string, int, ChangeService, std::string);
	bool addGpio(std::vector<std::string>, ChangeService, std::string);
	bool updateGpio(Gpio, ChangeService, std::string);
	bool deleteGpio(std::string, ChangeService, std::string);
	bool setAllGpios(int, ChangeService, std::string);

	std::string getSchedulesString();
	std::string getSchedulesRestString();

	bool setSchedule(std::string, int, ChangeService, std::string);
	bool addSchedule(std::vector<std::string>, ChangeService, std::string);
	bool updateSchedule(Schedule, ChangeService, std::string);
	bool deleteSchedule(std::string, ChangeService, std::string);
	bool setAllSchedules(int, ChangeService, std::string);

	std::string getSocketsString();
	std::vector<Socket> getSockets();
	std::string getSocketsRestString();

	bool setSocket(std::string, int, ChangeService, std::string);
	bool addSocket(std::vector<std::string>, ChangeService, std::string);
	bool updateSocket(Socket, ChangeService, std::string);
	bool deleteSocket(std::string, ChangeService, std::string);
	bool setAllSockets(int, ChangeService, std::string);

public:
	RemoteService();
	~RemoteService();

	void initialize(FileController);

	std::string performAction(std::string, std::vector<std::string>,
			ChangeService, std::string);

	bool activateSockets(std::vector<std::string>, ChangeService, std::string);

	int getRaspberry();
	int getPort();
	int getReceiverGpio();

	std::vector<Schedule> getSchedules();

	std::string getArea();
	std::string getSensor();
	std::string getUrl();
	std::string getTemperatureGraphUrl();
};

#endif
