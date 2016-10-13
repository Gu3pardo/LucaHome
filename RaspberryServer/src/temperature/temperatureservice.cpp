#include "temperatureservice.h"

TemperatureService::TemperatureService() {
	_isInitialized = false;

	//TODO: add gpios for LEDs
	LED_ERROR_HIGH_TEMP = -1;
	LED_ERROR_LOW_TEMP = -1;
	LED_NORMAL_TEMP = -1;

	MIN_TEMP = 15;
	MAX_TEMP = 25;
}

TemperatureService::~TemperatureService() {
}

//--------------------------Public-----------------------//

void TemperatureService::controlTemperature() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "TemperatureService is not initialized!");
		return;
	}

	double currentTemperature = loadTemperature();

	if (currentTemperature < MIN_TEMP) {
		syslog(LOG_INFO, "Temperature low! %d", currentTemperature);

		std::ostringstream data;
		data << "Current temperature at " << _temperatureArea << " is too low! "
				<< currentTemperature << "°C!";

		sendWarningMail(data.str());
		enableLED(LED_ERROR_LOW_TEMP);
	} else if (currentTemperature > MAX_TEMP) {
		syslog(LOG_INFO, "Temperature high! %d", currentTemperature);

		std::ostringstream data;
		data << "Current temperature at " << _temperatureArea
				<< " is too high! " << currentTemperature << "°C!";

		sendWarningMail(data.str());
		enableLED(LED_ERROR_HIGH_TEMP);
	} else {
		if (_warningCount > 0) {
			syslog(LOG_INFO,
					"Temperature was for %d minutes not in normal values!",
					_warningCount);
		}

		_warningCount = 0;
		enableLED(LED_NORMAL_TEMP);
	}
}

double TemperatureService::getCurrentTemperature() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "TemperatureService is not initialized!");
		return -1;
	}

	return loadTemperature();
}

std::string TemperatureService::getCurrentTemperatureString() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "TemperatureService is not initialized!");
		return "";
	}

	std::ostringstream data;
	data << loadTemperature();

	return data.str();
}

void TemperatureService::initialize(MailService mailService,
		std::string sensorId, std::string temperatureArea) {
	_mailService = mailService;

	std::ostringstream path;
	path << "/sys/bus/w1/devices/" << sensorId << "/w1_slave";
	_sensorPath = path.str();

	_temperatureArea = temperatureArea;

	_warningCount = 0;
	_isInitialized = true;
}

//-------------------------Private-----------------------//

double TemperatureService::loadTemperature() {
	const char *charPath = _sensorPath.c_str();
	FILE *device = fopen(charPath, "r");

	double temperature = -1;
	char crcVar[5];

	if (device != NULL) {
		if (!ferror(device)) {
			fscanf(device, "%*x %*x %*x %*x %*x %*x %*x %*x %*x : crc=%*x %s",
					crcVar);
			if (strncmp(crcVar, "YES", 3) == 0) {
				fscanf(device, "%*x %*x %*x %*x %*x %*x %*x %*x %*x t=%lf",
						&temperature);
				temperature /= 1000.0;
			} else {
				syslog(LOG_INFO, "Error strncmp!");
			}
		} else {
			syslog(LOG_INFO, "Error with device!");
		}
	} else {
		syslog(LOG_INFO, "Check connections %s", _sensorPath.c_str());
	}

	fclose(device);

	return temperature;
}

void TemperatureService::sendWarningMail(std::string warning) {
	if (_warningCount % 5 != 0) {
		syslog(LOG_INFO,
				"Already send a mail within the last five or more minutes!");
		return;
	}

	_mailService.sendMail(warning);
	_warningCount++;
}

void TemperatureService::enableLED(int led) {
	if (led == -1) {
		//syslog(LOG_INFO, "LED has wrong value! Cannot enable -1!");
		return;
	}

	PiControl::writeGpio(LED_ERROR_HIGH_TEMP, 0);
	PiControl::writeGpio(LED_ERROR_LOW_TEMP, 0);
	PiControl::writeGpio(LED_NORMAL_TEMP, 0);

	if (led == LED_ERROR_HIGH_TEMP) {
		PiControl::writeGpio(LED_ERROR_HIGH_TEMP, 1);
	} else if (led == LED_ERROR_LOW_TEMP) {
		PiControl::writeGpio(LED_ERROR_LOW_TEMP, 1);
	} else if (led == LED_NORMAL_TEMP) {
		PiControl::writeGpio(LED_NORMAL_TEMP, 1);
	} else {
		syslog(LOG_INFO, "LED has wrong value! Cannot enable %d!", led);
	}
}
