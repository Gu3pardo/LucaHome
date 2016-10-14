#include "remoteservice.h"

RemoteService::RemoteService() {
}

RemoteService::~RemoteService() {
}

//--------------------------Data-------------------------//

int RemoteService::getPort() {
	return _port;
}

int RemoteService::getDataGpio() {
	return _datagpio;
}

int RemoteService::getReceiverGpio() {
	return _receivergpio;
}

int RemoteService::getRaspberry() {
	return _raspberry;
}

//--------------------------Area-------------------------//

std::string RemoteService::getArea() {
	return _area;
}

std::vector<std::string> RemoteService::getAreas() {
	return _areas;
}

//-------------------------Sensor------------------------//

std::string RemoteService::getSensor() {
	return _sensor;
}

std::vector<std::string> RemoteService::getSensors() {
	return _sensors;
}

//---------------------------Url-------------------------//

std::string RemoteService::getUrl() {
	return _url;
}

std::vector<std::string> RemoteService::getUrls() {
	return _urls;
}

std::string RemoteService::getTemperatureGraphUrl() {
	std::stringstream url;
	url << _url << "/cgi-bin/webgui.py";
	return url.str();
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
		ChangeService changeService) {
	bool success = false;

	for (int index = 0; index < _gpios.size(); index++) {
		if (_gpios[index].getName() == name) {
			success = _gpios[index].setState(state);
			saveSettings(changeService);
			loadSettings();
			break;
		}
	}

	return success;
}

bool RemoteService::addGpio(std::vector<std::string> newGpioData,
		ChangeService changeService) {
	Gpio newGpio(newGpioData[3], atoi(newGpioData[4].c_str()),
			atoi(newGpioData[5].c_str()));
	_gpios.push_back(newGpio);
	saveSettings(changeService);
	loadSettings();
	return true;
}

bool RemoteService::updateGpio(Gpio updateGpio, ChangeService changeService) {
	for (int index = 0; index < _gpios.size(); index++) {
		if (_gpios[index].getName() == updateGpio.getName()) {
			_gpios[index] = updateGpio;
			saveSettings(changeService);
			loadSettings();
			return true;
		}
	}
	return false;
}

bool RemoteService::deleteGpio(std::string name, ChangeService changeService) {
	std::vector<Gpio>::iterator it = _gpios.begin();
	while (it != _gpios.end()) {
		if ((*it).getName() == name) {
			it = _gpios.erase(it);
			saveSettings(changeService);
			loadSettings();
			return true;
		} else {
			++it;
		}
	}
	return false;
}

bool RemoteService::setAllGpios(int state, ChangeService changeService) {
	bool success = true;

	for (int index = 0; index < _gpios.size(); index++) {
		success &= _gpios[index].setState(state);
	}

	saveSettings(changeService);
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

std::vector<Schedule> RemoteService::getSchedules() {
	return _schedules;
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
				<< "{State:" << Tools::convertIntToStr(_schedules[index].getStatus()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool RemoteService::setSchedule(std::string name, int state,
		ChangeService changeService) {
	bool success = false;

	for (int index = 0; index < _schedules.size(); index++) {
		if (_schedules[index].getName() == name) {
			_schedules[index].setStatus(state);
			saveSettings(changeService);
			loadSettings();
			success = true;
			break;
		}
	}

	return success;
}

bool RemoteService::addSchedule(std::vector<std::string> newScheduleData,
		ChangeService changeService) {
	Schedule newSchedule(newScheduleData[3], newScheduleData[4],
			newScheduleData[5], atoi(newScheduleData[6].c_str()),
			atoi(newScheduleData[7].c_str()), atoi(newScheduleData[8].c_str()),
			atoi(newScheduleData[9].c_str()), atoi(newScheduleData[10].c_str()),
			atoi(newScheduleData[11].c_str()));
	_schedules.push_back(newSchedule);
	saveSettings(changeService);
	loadSettings();
	return true;
}

bool RemoteService::updateSchedule(Schedule updateSchedule,
		ChangeService changeService) {
	for (int index = 0; index < _schedules.size(); index++) {
		if (_schedules[index].getName() == updateSchedule.getName()) {
			_schedules[index] = updateSchedule;
			saveSettings(changeService);
			loadSettings();
			return true;
		}
	}
	return false;
}

bool RemoteService::deleteSchedule(std::string name,
		ChangeService changeService) {
	std::vector<Schedule>::iterator it = _schedules.begin();
	while (it != _schedules.end()) {
		if ((*it).getName() == name) {
			it = _schedules.erase(it);
			saveSettings(changeService);
			loadSettings();
			return true;
		} else {
			++it;
		}
	}
	return false;
}

bool RemoteService::setAllSchedules(int state, ChangeService changeService) {
	Tools::convertIntToStr(state).c_str();
	bool success = true;

	for (int index = 0; index < _schedules.size(); index++) {
		success &= _schedules[index].setStatus(state);
	}

	saveSettings(changeService);
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
				<<"{Name:" << _sockets[index].getName() << "};"
				<<"{Area:" << _sockets[index].getArea() << "};"
				<<"{Code:" << _sockets[index].getCode() << "};"
				<<"{State:" << Tools::convertIntToStr(_sockets[index].getState()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool RemoteService::setSocket(std::string name, int state,
		ChangeService changeService) {
	bool success = false;

	for (int index = 0; index < _sockets.size(); index++) {
		if (_sockets[index].getName() == name) {
			success = _sockets[index].setState(state, _datagpio);
			saveSettings(changeService);
			loadSettings();
			break;
		}
	}

	return success;
}

bool RemoteService::addSocket(std::vector<std::string> newSocketData,
		ChangeService changeService) {
	Socket newSocket(newSocketData[3], newSocketData[4], newSocketData[5],
			atoi(newSocketData[6].c_str()));
	_sockets.push_back(newSocket);
	saveSettings(changeService);
	loadSettings();
	return true;
}

bool RemoteService::updateSocket(Socket updateSocket,
		ChangeService changeService) {
	for (int index = 0; index < _sockets.size(); index++) {
		if (_sockets[index].getName() == updateSocket.getName()) {
			_sockets[index] = updateSocket;
			saveSettings(changeService);
			loadSettings();
			return true;
		}
	}
	return false;
}

bool RemoteService::deleteSocket(std::string name,
		ChangeService changeService) {
	std::vector<Socket>::iterator it = _sockets.begin();
	while (it != _sockets.end()) {
		if ((*it).getName() == name) {
			it = _sockets.erase(it);
			saveSettings(changeService);
			loadSettings();
			return true;
		} else {
			++it;
		}
	}
	return false;
}

bool RemoteService::setAllSockets(int state, ChangeService changeService) {
	bool success = true;

	for (int index = 0; index < _sockets.size(); index++) {
		success &= _sockets[index].setState(state, _datagpio);
	}

	saveSettings(changeService);
	loadSettings();

	return success;
}

void RemoteService::initialize(FileController fileController) {
	_fileController = fileController;
	_settingsFile = "/etc/default/lucahome/settings";
	loadSettings();
}

//------------------------Private------------------------//

void RemoteService::saveSettings(ChangeService changeService) {
	std::string xmldata = _xmlService.generateSettingsXml(_port, _datagpio,
			_receivergpio, _raspberry, _areas, _sensors, _urls, _sockets,
			_gpios, _schedules);
	_fileController.saveFile(_settingsFile, xmldata);

	changeService.updateChange("Settings");
}

void RemoteService::loadSettings() {
	std::string settingsString = _fileController.readFile(_settingsFile);
	_xmlService.setContent(settingsString);

	_port = _xmlService.getPort();
	_datagpio = _xmlService.getDatagpio();
	_receivergpio = _xmlService.getReceivergpio();
	_raspberry = _xmlService.getRaspberry();

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
