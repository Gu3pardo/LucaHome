#include "changeservice.h"

/*===============PUBLIC==============*/

ChangeService::ChangeService() {
}

ChangeService::~ChangeService() {
}

void ChangeService::initialize(FileController fileController) {
	_fileController = fileController;
	_changesFile = "/etc/default/lucahome/changes";

	std::string changesString = _fileController.readFile(_changesFile);
	_xmlService.setContent(changesString);
	_changes = _xmlService.getChanges();
}

void ChangeService::updateChange(std::string type, std::string user) {
	time_t now;
	struct tm now_info;

	now = time(0);
	localtime_r(&now, &now_info);

	for (int index = 0; index < _changes.size(); index++) {
		if (_changes[index].getType() == type) {
			_changes[index].setHour(now_info.tm_hour);
			_changes[index].setMinute(now_info.tm_min);
			_changes[index].setDay(now_info.tm_mday);
			_changes[index].setMonth(now_info.tm_mon + 1);
			_changes[index].setYear(now_info.tm_year + 1900);
			_changes[index].setUser(user);
		}
	}

	std::string xmldata = _xmlService.generateChangesXml(_changes);
	_fileController.saveFile(_changesFile, xmldata);
}

void ChangeService::reloadData() {
	std::string changesString = _fileController.readFile(_changesFile);
	_xmlService.setContent(changesString);
	_changes = _xmlService.getChanges();
}

std::string ChangeService::performAction(std::string action,
		std::vector<std::string> data) {
	if (action == "GET") {
		if (data.size() == 5) {
			if (data[4] == "REST") {
				return getRestString();
			} else if (data[4] == "WEBSITE") {
				return getString();
			} else {
				return "Error 102:Wrong action parameter for change";
			}
		} else {
			return "Error 101:Wrong data size for change";
		}
	} else {
		return "Error 100:Action not found for change";
	}
}

/*==============PRIVATE==============*/

std::string ChangeService::getRestString() {
	std::stringstream out;

	for (int index = 0; index < _changes.size(); index++) {
		out << "{change:"
				<< "{Type:" << _changes[index].getType() << "};"
				<< "{Hour:" << Tools::convertIntToStr(_changes[index].getHour()) << "};"
				<< "{Minute:" << Tools::convertIntToStr(_changes[index].getMinute()) << "};"
				<< "{Day:" << Tools::convertIntToStr(_changes[index].getDay()) << "};"
				<< "{Month:" << Tools::convertIntToStr(_changes[index].getMonth()) << "};"
				<< "{Year:" << Tools::convertIntToStr(_changes[index].getYear()) << "};"
				<< "{User:" << _changes[index].getUser() << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

std::string ChangeService::getString() {
	std::stringstream out;

	for (int index = 0; index < _changes.size(); index++) {
		out << "change:" << _changes[index].getType() << ":"
				<< Tools::convertIntToStr(_changes[index].getHour()) << ":"
				<< Tools::convertIntToStr(_changes[index].getMinute()) << ":"
				<< Tools::convertIntToStr(_changes[index].getDay()) << ":"
				<< Tools::convertIntToStr(_changes[index].getMonth()) << ":"
				<< Tools::convertIntToStr(_changes[index].getYear()) << ":"
				<< _changes[index].getUser() << ";";
	}

	out << "\x00" << std::endl;

	return out.str();
}
