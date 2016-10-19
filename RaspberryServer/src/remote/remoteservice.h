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

	void saveSettings(ChangeService);
	void loadSettings();

public:
	RemoteService();
	~RemoteService();

	int getPort();
	int getDataGpio();
	int getReceiverGpio();
	int getRaspberry();

	std::string getArea();
	std::vector<std::string> getAreas();

	std::string getSensor();
	std::vector<std::string> getSensors();

	std::string getUrl();
	std::vector<std::string> getUrls();
	std::string getTemperatureGraphUrl();

	std::string getGpiosString();
	std::vector<Gpio> getGpios();
	std::string getGpiosRestString();

	bool setGpio(std::string, int, ChangeService);
	bool addGpio(std::vector<std::string>, ChangeService);
	bool updateGpio(Gpio, ChangeService);
	bool deleteGpio(std::string, ChangeService);
	bool setAllGpios(int, ChangeService);

	std::string getSchedulesString();
	std::vector<Schedule> getSchedules();
	std::string getSchedulesRestString();

	bool setSchedule(std::string, int, ChangeService);
	bool addSchedule(std::vector<std::string>, ChangeService);
	bool updateSchedule(Schedule, ChangeService);
	bool deleteSchedule(std::string, ChangeService);
	bool setAllSchedules(int, ChangeService);

	std::string getSocketsString();
	std::vector<Socket> getSockets();
	std::string getSocketsRestString();

	bool setSocket(std::string, int, ChangeService);
	bool addSocket(std::vector<std::string>, ChangeService);
	bool updateSocket(Socket, ChangeService);
	bool deleteSocket(std::string, ChangeService);
	bool setAllSockets(int, ChangeService);
	bool activateSockets(std::vector<std::string>, ChangeService);

	void initialize(FileController);
};

#endif
