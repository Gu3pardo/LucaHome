#include "changeservice.h"

ChangeService::ChangeService() {
}

ChangeService::~ChangeService() {
}

std::string ChangeService::getChangesString() {
	std::stringstream out;

	for (int index = 0; index < changes.size(); index++) {
		out << changes[index].toString();
	}

	return out.str();
}

std::vector<Change> ChangeService::getChanges() {
	return changes;
}

std::string ChangeService::getChangesRestString() {
	std::stringstream out;

	for (int index = 0; index < changes.size(); index++) {
		out << "change:" << changes[index].getType() << ":"
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

void ChangeService::updateChange(std::string type) {
	time_t now;
	struct tm now_info;

	now = time(0);
	localtime_r(&now, &now_info);

	std::string user = "Null";

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

void ChangeService::initialize(FileController _fileController) {
	fileController = _fileController;
	changesFile = "/etc/default/lucahome/changes";

	std::string changesString = fileController.readFile(changesFile);
	_xmlService.setContent(changesString);
	changes = _xmlService.getChanges();

	syslog(LOG_INFO, "Changes: %s", getChangesString().c_str());
}
