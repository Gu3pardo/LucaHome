#include "remoteservice.h"

/*===============PUBLIC==============*/

RemoteService::RemoteService() {
}

RemoteService::~RemoteService() {
}

void RemoteService::initialize(FileController fileController) {
	_fileController = fileController;
	_settingsFile = "/etc/default/lucahome/settings";
	loadSettings();
}

std::string RemoteService::performAction(std::string action,
		std::vector<std::string> data, ChangeService changeService,
		std::string username) {
	//--------------------GET--------------------
	if (action == "GET") {
		if (data[4] == "RASPBERRY") {
			return Tools::convertIntToStr(getRaspberry());
		} else if (data[4] == "AREA") {
			return getArea();
		} else if (data[4] == "SENSOR") {
			return getSensor();
		} else if (data[4] == "URL") {
			if (data[5] == "MAIN") {
				return getUrl();
			} else if (data[5] == "TEMPERATURE") {
				return getTemperatureGraphUrl();
			} else {
				return "Error 120:Action not found for remote";
			}
		} else if (data[4] == "GPIO") {
			return getGpiosRestString();
		} else if (data[4] == "SCHEDULE") {
			return getSchedulesRestString();
		} else if (data[4] == "SOCKET") {
			return getSocketsRestString();
		} else {
			return "Error 120:Action not found for remote";
		}
	}
	//--------------------ADD--------------------
	else if (action == "ADD") {
		if (data[4] == "GPIO") {
			if (data.size() == 8) {
				if (addGpio(data, changeService, username)) {
					return "addgpio:1";
				} else {
					return "Error 51:Could not add gpio";
				}
			} else {
				return "Error 55:Wrong word size for gpio";
			}
		} else if (data[4] == "SCHEDULE") {
			if (data.size() == 16) {
				if (addSchedule(data, changeService, username)) {
					return "addschedule:1";
				} else {
					return "Error 61:Could not add schedule";
				}
			} else {
				return "Error 65:Wrong word size for schedule";
			}
		} else if (data[4] == "SOCKET") {
			if (data.size() == 9) {
				if (addSocket(data, changeService, username)) {
					return "addsocket:1";
				} else {
					return "Error 71:Could not add socket";
				}
			} else {
				return "Error 75:Wrong word size for socket";
			}
		} else {
			return "Error 120:Action not found for remote";
		}
	}
	//-------------------UPDATE------------------
	else if (action == "UPDATE") {
		if (data[4] == "GPIO") {
			if (data.size() == 8) {
				if (updateGpio(data, changeService, username)) {
					return "updategpio:1";
				} else {
					return "Error 51:Could not update gpio";
				}
			} else {
				return "Error 55:Wrong word size for gpio";
			}
		} else if (data[4] == "SCHEDULE") {
			if (data.size() == 16) {
				if (updateSchedule(data, changeService, username)) {
					return "updateschedule:1";
				} else {
					return "Error 61:Could not update schedule";
				}
			} else {
				return "Error 65:Wrong word size for schedule";
			}
		} else if (data[4] == "SOCKET") {
			if (data.size() == 9) {
				if (updateSocket(data, changeService, username)) {
					return "updatesocket:1";
				} else {
					return "Error 71:Could update add socket";
				}
			} else {
				return "Error 75:Wrong word size for socket";
			}
		} else {
			return "Error 120:Action not found for remote";
		}
	}
	//-------------------DELETE------------------
	else if (action == "DELETE") {
		if (data[4] == "GPIO") {
			if (deleteGpio(data[5], changeService, username)) {
				return "deletegpio:1";
			} else {
				return "Error 52:Could not delete gpio";
			}
		} else if (data[4] == "SCHEDULE") {
			if (deleteSchedule(data[5], changeService, username)) {
				return "deleteschedule:1";
			} else {
				return "Error 62:Could not delete schedule";
			}
		} else if (data[4] == "SOCKET") {
			if (deleteSocket(data[5], changeService, username)) {
				return "deletesocket:1";
			} else {
				return "Error 72:Could not delete socket";
			}
		} else {
			return "Error 120:Action not found for remote";
		}
	}
	//--------------------SET--------------------
	else if (action == "SET") {
		if (data[4] == "GPIO") {
			if (data.size() == 7) {
				if (data[5] == "ALL") {
					if (atoi(data[6].c_str()) == 1) {
						if (setAllGpios(1, changeService, username)) {
							return "activateAllGpios:1";
						} else {
							return "Error 53:Could not activate all gpios";
						}
					} else if (atoi(data[6].c_str()) == 0) {
						if (setAllGpios(0, changeService, username)) {
							return "deactivateAllGpios:1";
						} else {
							return "Error 54:Could not deactivate all gpios";
						}
					} else {
						return "Error 56:Invalid state for gpio";
					}
				} else {
					if (setGpio(data[5], atoi(data[6].c_str()), changeService,
							username)) {
						return "setgpio:1";
					} else {
						return "Error 50:Could not set gpio";
					}
				}
			} else {
				return "Error 55:Wrong word size for gpio";
			}
		} else if (data[4] == "SCHEDULE") {
			if (data.size() == 7) {
				if (data[5] == "ALL") {
					if (atoi(data[6].c_str()) == 1) {
						if (setAllSchedules(1, changeService, username)) {
							return "activateAllSchedules:1";
						} else {
							return "Error 63:Could not activate all schedules";
						}
					} else if (atoi(data[6].c_str()) == 0) {
						if (setAllSchedules(0, changeService, username)) {
							return "deactivateAllSchedules:1";
						} else {
							return "Error 64:Could not deactivate all schedules";
						}
					} else {
						return "Error 56:Invalid state for gpio";
					}
				} else {
					if (setSchedule(data[5], atoi(data[6].c_str()),
							changeService, username)) {
						return "setschedule:1";
					} else {
						return "Error 60:Could not set schedule";
					}
				}
			} else {
				return "Error 65:Wrong word size for schedule";
			}
		} else if (data[4] == "SOCKET") {
			if (data.size() == 7) {
				if (data[5] == "ALL") {
					if (atoi(data[6].c_str()) == 1) {
						if (setAllSockets(1, changeService, username)) {
							return "activateAllSockets:1";
						} else {
							return "Error 73:Could not activate all sockets";
						}
					} else if (atoi(data[6].c_str()) == 0) {
						if (setAllSockets(0, changeService, username)) {
							return "deactivateAllSockets:1";
						} else {
							return "Error 74:Could not deactivate all sockets";
						}
					} else {
						return "Error 56:Invalid state for gpio";
					}
				} else if (data[5] == "SOUND") {
					if (atoi(data[6].c_str()) == 1) {
						if (setSoundSocket(1, changeService, username)) {
							return "activateSoundSocket:1";
						} else {
							return "Error 76:Could not activate sound socket";
						}
					} else if (atoi(data[6].c_str()) == 0) {
						if (setSoundSocket(0, changeService, username)) {
							return "deactivateSoundSocket:1";
						} else {
							return "Error 77:Could not deactivate sound socket";
						}
					} else {
						return "Error 56:Invalid state for gpio";
					}
				} else {
					if (setSocket(data[5], atoi(data[6].c_str()), changeService,
							username)) {
						return "setsocket:1";
					} else {
						return "Error 70:Could not set socket";
					}
				}
			} else {
				return "Error 75:Wrong word size for socket";
			}
		} else {
			return "Error 120:Action not found for remote";
		}
	}
	//-------------------ERROR-------------------
	else {
		return "Error 120:Action not found for remote";
	}
}

bool RemoteService::activateSockets(std::vector<std::string> socketList,
		ChangeService changeService, std::string username) {
	if (socketList.size() == 1) {
		if (socketList[0] == "Error 44:No sockets available") {
			syslog(LOG_INFO, "Error 44:No sockets available");
			return false;
		}
	}

	bool success = true;

	for (int socketIndex = 0; socketIndex < socketList.size(); socketIndex++) {
		for (int index = 0; index < _sockets.size(); index++) {
			if (_sockets[index].getName() == socketList[socketIndex]) {
				syslog(LOG_INFO, "Found socket to activate: %s",
						_sockets[index].getName().c_str());

				success &= _sockets[index].setState(1, _datagpio);

				saveSettings(changeService, username);
				loadSettings();

				if (success) {
					syslog(LOG_INFO, "activated socket: %s",
							socketList[socketIndex].c_str());
				} else {
					syslog(LOG_INFO, "failed to activate socket: %s",
							socketList[socketIndex].c_str());
				}

				break;
			}
		}
	}

	return success;
}

int RemoteService::getRaspberry() {
	return _raspberry;
}

int RemoteService::getPort() {
	return _port;
}

int RemoteService::getReceiverGpio() {
	return _receivergpio;
}

std::vector<Schedule> RemoteService::getSchedules() {
	return _schedules;
}

std::string RemoteService::getArea() {
	return _area;
}

std::string RemoteService::getSensor() {
	return _sensor;
}

std::string RemoteService::getUrl() {
	return _url;
}

std::string RemoteService::getTemperatureGraphUrl() {
	std::stringstream url;
	url << _url << "/cgi-bin/webgui.py";
	return url.str();
}

std::string RemoteService::getAlarmSound() {
	return _alarmSound;
}

void RemoteService::reloadData() {
	loadSettings();
}

/*==============PRIVATE==============*/

void RemoteService::saveSettings(ChangeService changeService,
		std::string username) {
	std::string xmldata = _xmlService.generateSettingsXml(_port, _datagpio,
			_receivergpio, _raspberry, _alarmSound, _areas, _sensors, _urls,
			_sockets, _gpios, _schedules);
	_fileController.saveFile(_settingsFile, xmldata);

	changeService.updateChange("Settings", username);
}

void RemoteService::loadSettings() {
	std::string settingsString = _fileController.readFile(_settingsFile);
	_xmlService.setContent(settingsString);

	_port = _xmlService.getPort();
	_datagpio = _xmlService.getDatagpio();
	_receivergpio = _xmlService.getReceivergpio();
	_raspberry = _xmlService.getRaspberry();

	_alarmSound = _xmlService.getAlarmSound();

	_areas = _xmlService.getAreas();
	_area = _areas.at(_raspberry - 1);

	_sensors = _xmlService.getSensors();
	_sensor = _sensors.at(_raspberry - 1);

	_urls = _xmlService.getUrls();
	_url = _urls.at(_raspberry - 1);

	_gpios = _xmlService.getGpios();
	_schedules = _xmlService.getSchedules();
	_sockets = _xmlService.getSockets();
}

//--------------------------Data-------------------------//

int RemoteService::getDataGpio() {
	return _datagpio;
}

//--------------------------Area-------------------------//

std::vector<std::string> RemoteService::getAreas() {
	return _areas;
}

//-------------------------Sensor------------------------//

std::vector<std::string> RemoteService::getSensors() {
	return _sensors;
}

//---------------------------Url-------------------------//

std::vector<std::string> RemoteService::getUrls() {
	return _urls;
}

//-------------------------Gpios-------------------------//

std::string RemoteService::getGpiosString() {
	std::stringstream out;
	for (int index = 0; index < _gpios.size(); index++) {
		out << _gpios[index].toString();
	}
	return out.str();
}

std::vector<Gpio> RemoteService::getGpios() {
	return _gpios;
}

std::string RemoteService::getGpiosRestString() {
	std::stringstream out;

	for (int index = 0; index < _gpios.size(); index++) {
		out << "{gpio:"
				<< "{Name:" << _gpios[index].getName() << "};"
				<< "{Gpio:" << Tools::convertIntToStr(_gpios[index].getGpio()) << "};"
				<< "{State:" << Tools::convertIntToStr(_gpios[index].getState()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool RemoteService::setGpio(std::string name, int state,
		ChangeService changeService, std::string username) {
	bool success = false;

	for (int index = 0; index < _gpios.size(); index++) {
		if (_gpios[index].getName() == name) {
			success = _gpios[index].setState(state);

			saveSettings(changeService, username);
			loadSettings();

			break;
		}
	}

	return success;
}

bool RemoteService::addGpio(std::vector<std::string> newGpioData,
		ChangeService changeService, std::string username) {
	Gpio newGpio(newGpioData[5], atoi(newGpioData[6].c_str()),
			atoi(newGpioData[7].c_str()));
	_gpios.push_back(newGpio);

	saveSettings(changeService, username);
	loadSettings();

	return true;
}

bool RemoteService::updateGpio(std::vector<std::string> updateGpioData,
		ChangeService changeService, std::string username) {
	Gpio updateGpio(updateGpioData[5], atoi(updateGpioData[6].c_str()),
			atoi(updateGpioData[7].c_str()));
	for (int index = 0; index < _gpios.size(); index++) {
		if (_gpios[index].getName() == updateGpio.getName()) {
			_gpios[index] = updateGpio;

			saveSettings(changeService, username);
			loadSettings();

			return true;
		}
	}

	return false;
}

bool RemoteService::deleteGpio(std::string name, ChangeService changeService,
		std::string username) {
	std::vector<Gpio>::iterator it = _gpios.begin();
	while (it != _gpios.end()) {
		if ((*it).getName() == name) {
			it = _gpios.erase(it);

			saveSettings(changeService, username);
			loadSettings();

			return true;
		} else {
			++it;
		}
	}

	return false;
}

bool RemoteService::setAllGpios(int state, ChangeService changeService,
		std::string username) {
	bool success = true;

	for (int index = 0; index < _gpios.size(); index++) {
		success &= _gpios[index].setState(state);
	}

	saveSettings(changeService, username);
	loadSettings();

	return success;
}

//-----------------------Schedules-----------------------//

std::string RemoteService::getSchedulesString() {
	std::stringstream out;
	for (int index = 0; index < _schedules.size(); index++) {
		out << _schedules[index].toString();
	}
	return out.str();
}

std::string RemoteService::getSchedulesRestString() {
	std::stringstream out;

	for (int index = 0; index < _schedules.size(); index++) {
		out << "{schedule:"
				<< "{Name:" << _schedules[index].getName() << "};"
				<< "{Socket:" << _schedules[index].getSocket() << "};"
				<< "{Gpio:" << _schedules[index].getGpio() << "};"
				<< "{Weekday:" << Tools::convertIntToStr(_schedules[index].getWeekday()) << "};"
				<< "{Hour:" << Tools::convertIntToStr(_schedules[index].getHour()) << "};"
				<< "{Minute:" << Tools::convertIntToStr(_schedules[index].getMinute()) << "};"
				<< "{OnOff:" << Tools::convertIntToStr(_schedules[index].getOnoff()) << "};"
				<< "{IsTimer:" << Tools::convertIntToStr(_schedules[index].getIsTimer()) << "};"
				<< "{PlaySound:" << Tools::convertIntToStr(_schedules[index].getPlaySound()) << "};"
				<< "{Raspberry:" << Tools::convertIntToStr(_schedules[index].getPlayRaspberry()) << "};"
				<< "{State:" << Tools::convertIntToStr(_schedules[index].getStatus()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool RemoteService::setSchedule(std::string name, int state,
		ChangeService changeService, std::string username) {
	bool success = false;

	for (int index = 0; index < _schedules.size(); index++) {
		if (_schedules[index].getName() == name) {
			_schedules[index].setStatus(state);
			success = true;

			saveSettings(changeService, username);
			loadSettings();

			break;
		}
	}

	return success;
}

bool RemoteService::addSchedule(std::vector<std::string> newScheduleData,
		ChangeService changeService, std::string username) {
	Schedule newSchedule(newScheduleData[5], newScheduleData[6],
			newScheduleData[7], atoi(newScheduleData[8].c_str()),
			atoi(newScheduleData[9].c_str()), atoi(newScheduleData[10].c_str()),
			atoi(newScheduleData[11].c_str()),
			atoi(newScheduleData[12].c_str()),
			atoi(newScheduleData[13].c_str()),
			atoi(newScheduleData[14].c_str()),
			atoi(newScheduleData[15].c_str()));
	_schedules.push_back(newSchedule);

	saveSettings(changeService, username);
	loadSettings();

	return true;
}

bool RemoteService::updateSchedule(std::vector<std::string> updateScheduleData,
		ChangeService changeService, std::string username) {
	Schedule updateSchedule(updateScheduleData[5], updateScheduleData[6],
			updateScheduleData[7], atoi(updateScheduleData[8].c_str()),
			atoi(updateScheduleData[9].c_str()),
			atoi(updateScheduleData[10].c_str()),
			atoi(updateScheduleData[11].c_str()),
			atoi(updateScheduleData[12].c_str()),
			atoi(updateScheduleData[13].c_str()),
			atoi(updateScheduleData[14].c_str()),
			atoi(updateScheduleData[15].c_str()));
	for (int index = 0; index < _schedules.size(); index++) {
		if (_schedules[index].getName() == updateSchedule.getName()) {
			_schedules[index] = updateSchedule;

			saveSettings(changeService, username);
			loadSettings();

			return true;
		}
	}

	return false;
}

bool RemoteService::deleteSchedule(std::string name,
		ChangeService changeService, std::string username) {
	std::vector<Schedule>::iterator it = _schedules.begin();
	while (it != _schedules.end()) {
		if ((*it).getName() == name) {
			it = _schedules.erase(it);

			saveSettings(changeService, username);
			loadSettings();

			return true;
		} else {
			++it;
		}
	}

	return false;
}

bool RemoteService::setAllSchedules(int state, ChangeService changeService,
		std::string username) {
	Tools::convertIntToStr(state).c_str();
	bool success = true;

	for (int index = 0; index < _schedules.size(); index++) {
		success &= _schedules[index].setStatus(state);
	}

	saveSettings(changeService, username);
	loadSettings();

	return success;
}

//------------------------Sockets------------------------//

std::string RemoteService::getSocketsString() {
	std::stringstream out;
	for (int index = 0; index < _sockets.size(); index++) {
		out << _sockets[index].toString();
	}
	return out.str();
}

std::vector<Socket> RemoteService::getSockets() {
	return _sockets;
}

std::string RemoteService::getSocketsRestString() {
	std::stringstream out;

	for (int index = 0; index < _sockets.size(); index++) {
		out << "{socket:"
				<< "{Name:" << _sockets[index].getName() << "};"
				<< "{Area:" << _sockets[index].getArea() << "};"
				<< "{Code:" << _sockets[index].getCode() << "};"
				<< "{State:" << Tools::convertIntToStr(_sockets[index].getState()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool RemoteService::setSocket(std::string name, int state,
		ChangeService changeService, std::string username) {
	bool success = false;
	bool foundSocket = false;

	for (int index = 0; index < _sockets.size(); index++) {
		if (_sockets[index].getName() == name) {
			success = _sockets[index].setState(state, _datagpio);
			foundSocket = true;

			saveSettings(changeService, username);
			loadSettings();

			break;
		}
	}

	return success && foundSocket;
}

bool RemoteService::addSocket(std::vector<std::string> newSocketData,
		ChangeService changeService, std::string username) {
	Socket newSocket(newSocketData[5], newSocketData[6], newSocketData[7],
			atoi(newSocketData[8].c_str()));
	_sockets.push_back(newSocket);

	saveSettings(changeService, username);
	loadSettings();

	return true;
}

bool RemoteService::updateSocket(std::vector<std::string> updateSocketData,
		ChangeService changeService, std::string username) {
	Socket updateSocket(updateSocketData[5], updateSocketData[6],
			updateSocketData[7], atoi(updateSocketData[8].c_str()));
	for (int index = 0; index < _sockets.size(); index++) {
		if (_sockets[index].getName() == updateSocket.getName()) {
			_sockets[index] = updateSocket;

			saveSettings(changeService, username);
			loadSettings();

			return true;
		}
	}
	return false;
}

bool RemoteService::deleteSocket(std::string name, ChangeService changeService,
		std::string username) {
	std::vector<Socket>::iterator it = _sockets.begin();
	while (it != _sockets.end()) {
		if ((*it).getName() == name) {
			it = _sockets.erase(it);

			saveSettings(changeService, username);
			loadSettings();

			return true;
		} else {
			++it;
		}
	}
	return false;
}

bool RemoteService::setAllSockets(int state, ChangeService changeService,
		std::string username) {
	bool success = true;

	for (int index = 0; index < _sockets.size(); index++) {
		success &= _sockets[index].setState(state, _datagpio);
	}

	saveSettings(changeService, username);
	loadSettings();

	return success;
}

bool RemoteService::setSoundSocket(int state, ChangeService changeService,
		std::string username) {
	bool success = true;
	bool foundSocket = false;

	for (int index = 0; index < _sockets.size(); index++) {
		if (_sockets[index].getArea() == _area) {
			if (_sockets[index].getName().find("Sound") != std::string::npos) {
				success = _sockets[index].setState(state, _datagpio);
				foundSocket = true;

				saveSettings(changeService, username);
				loadSettings();

				break;
			}
		}
	}

	return success && foundSocket;
}
