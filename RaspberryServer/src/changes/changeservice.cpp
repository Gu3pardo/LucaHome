#include "changeservice.h"

/*===============PUBLIC==============*/

ChangeService::ChangeService() {
}

ChangeService::~ChangeService() {
}

void ChangeService::initialize(FileController _fileController) {
	fileController = _fileController;
	changesFile = "/etc/default/lucahome/changes";

	std::string changesString = fileController.readFile(changesFile);
	_xmlService.setContent(changesString);
	changes = _xmlService.getChanges();
}

void ChangeService::updateChange(std::string type, std::string user) {
	time_t now;
	struct tm now_info;

	now = time(0);
	localtime_r(&now, &now_info);

	for (int index = 0; index < changes.size(); index++) {
		if (changes[index].getType() == type) {
			changes[index].setHour(now_info.tm_hour);
			changes[index].setMinute(now_info.tm_min);
			changes[index].setDay(now_info.tm_mday);
			changes[index].setMonth(now_info.tm_mon + 1);
			changes[index].setYear(now_info.tm_year + 1900);
			changes[index].setUser(user);
		}
	}

	std::string xmldata = _xmlService.generateChangesXml(changes);
	fileController.saveFile(changesFile, xmldata);
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

	for (int index = 0; index < changes.size(); index++) {
		out << "{change:"
				<< "{Type:" << changes[index].getType() << "};"
				<< "{Hour:" << Tools::convertIntToStr(changes[index].getHour()) << "};"
				<< "{Minute:" << Tools::convertIntToStr(changes[index].getMinute()) << "};"
				<< "{Day:" << Tools::convertIntToStr(changes[index].getDay()) << "};"
				<< "{Month:" << Tools::convertIntToStr(changes[index].getMonth()) << "};"
				<< "{Year:" << Tools::convertIntToStr(changes[index].getYear()) << "};"
				<< "{User:" << changes[index].getUser() << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

std::string ChangeService::getString() {
	std::stringstream out;

	for (int index = 0; index < changes.size(); index++) {
		out << "change:"
				<< changes[index].getType() << ":"
				<< Tools::convertIntToStr(changes[index].getHour()) << ":"
				<< Tools::convertIntToStr(changes[index].getMinute()) << ":"
				<< Tools::convertIntToStr(changes[index].getDay()) << ":"
				<< Tools::convertIntToStr(changes[index].getMonth()) << ":"
				<< Tools::convertIntToStr(changes[index].getYear()) << ":"
				<< changes[index].getUser() << ";";
	}

	out << "\x00" << std::endl;

	return out.str();
}
