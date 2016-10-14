#include <string>
#include <cstring>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <algorithm>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <inttypes.h>
#include <errno.h>
#include <math.h>
#include <time.h>

#include <pthread.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <syslog.h>

#include "../common/picontrol.h"
#include "../mail/mailservice.h"

#ifndef TEMPERATURESERVICE_H
#define TEMPERATURESERVICE_H

class TemperatureService {
private:
	std::string _sensorPath;
	std::string _temperatureArea;
	MailService _mailService;

	int _warningCount;
	bool _isInitialized;

	int LED_ERROR_HIGH_TEMP;
	int LED_ERROR_LOW_TEMP;
	int LED_NORMAL_TEMP;

	double MIN_TEMP;
	double MAX_TEMP;

	double loadTemperature();

	void sendWarningMail(std::string);
	void enableLED(int);

public:
	TemperatureService();
	~TemperatureService();

	void controlTemperature();

	double getCurrentTemperature();
	std::string getCurrentTemperatureString();
	std::string getCurrentTemperatureRestString();

	void initialize(MailService, std::string, std::string);
};

#endif
