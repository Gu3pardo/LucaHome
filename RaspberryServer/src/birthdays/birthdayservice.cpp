#include "birthdayservice.h"

/*===============PUBLIC==============*/

BirthdayService::BirthdayService() {
}

BirthdayService::~BirthdayService() {
}

void BirthdayService::initialize(FileController fileController,
		MailService mailService) {
	_fileController = fileController;
	_mailService = mailService;

	_birthdaysFile = "/etc/default/lucahome/birthdays";

	loadBirthdays();
}

void BirthdayService::checkBirthday() {
	syslog(LOG_INFO, "Checking birthdays!");

	time_t rawtime;
	tm * timeinfo;
	time(&rawtime);
	timeinfo = localtime(&rawtime);

	int day = timeinfo->tm_mday;	//day of the month 		1-31
	int month = timeinfo->tm_mon;	//month since January 	0-11
	month += 1;
	int year = timeinfo->tm_year;	//years since 1900
	year += 1900;

	for (int index = 0; index < _birthdays.size(); index++) {
		if (_birthdays[index].getDay() == day
				&& _birthdays[index].getMonth() == month) {
			int age = year - _birthdays[index].getYear();

			std::stringstream information;
			information << _birthdays[index].getName()
					<< " has birthday today! It is the "
					<< Tools::convertIntToStr(age) << "th birthday!";

			_mailService.sendMail(information.str());
		}
	}
}

std::string BirthdayService::performAction(std::string action,
		std::vector<std::string> data, ChangeService changeService,
		std::string username) {
	if (action == "GET") {
		return getBirthdays();
	} else if (action == "ADD") {
		if (data.size() == 9) {
			if (addBirthday(data, changeService, username)) {
				return "addbirthday:1";
			} else {
				return "Error 30:Could not add birthday";
			}
		} else {
			return "Error 33:Wrong word size for birthday";
		}
	} else if (action == "UPDATE") {
		if (data.size() == 9) {
			if (updateBirthday(data, changeService, username)) {
				return "updatebirthday:1";
			} else {
				return "Error 31:Could not update birthday";
			}
		} else {
			return "Error 33:Wrong word size for birthday";
		}
	} else if (action == "DELETE") {
		if (deleteBirthday(atoi(data[4].c_str()), changeService, username)) {
			return "deletebirthday:1";
		} else {
			return "Error 32:Could not delete birthday";
		}
	} else {
		return "Error 34:Action not found for birthday";
	}
}

/*==============PRIVATE==============*/

void BirthdayService::saveBirthdays(ChangeService changeService,
		std::string username) {
	std::string xmldata = _xmlService.generateBirthdaysXml(_birthdays);
	_fileController.saveFile(_birthdaysFile, xmldata);

	changeService.updateChange("Birthdays", username);
}

void BirthdayService::loadBirthdays() {
	std::string birthdaysString = _fileController.readFile(_birthdaysFile);
	_xmlService.setContent(birthdaysString);

	_birthdays = _xmlService.getBirthdays();
}

std::string BirthdayService::getBirthdays() {
	std::stringstream out;

	for (int index = 0; index < _birthdays.size(); index++) {
		out << "{birthday:"
				<< "{id:" << Tools::convertIntToStr(_birthdays[index].getId()) << "};"
				<< "{name:" << _birthdays[index].getName() << "};"
				<< "{day:" << Tools::convertIntToStr(_birthdays[index].getDay()) << "};"
				<< "{month:" << Tools::convertIntToStr(_birthdays[index].getMonth()) << "};"
				<< "{year:" << Tools::convertIntToStr(_birthdays[index].getYear()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool BirthdayService::addBirthday(std::vector<std::string> newBirthdayData,
		ChangeService changeService, std::string username) {
	Birthday newBirthday(atoi(newBirthdayData[3].c_str()), newBirthdayData[4],
			atoi(newBirthdayData[5].c_str()), atoi(newBirthdayData[6].c_str()),
			atoi(newBirthdayData[7].c_str()));
	_birthdays.push_back(newBirthday);

	saveBirthdays(changeService, username);
	loadBirthdays();

	syslog(LOG_INFO, "Added birthday %d", atoi(newBirthdayData[3].c_str()));

	return true;
}

bool BirthdayService::updateBirthday(
		std::vector<std::string> updateBirthdayData,
		ChangeService changeService, std::string username) {
	Birthday updateBirthday(atoi(updateBirthdayData[3].c_str()),
			updateBirthdayData[4], atoi(updateBirthdayData[5].c_str()),
			atoi(updateBirthdayData[6].c_str()),
			atoi(updateBirthdayData[7].c_str()));

	for (int index = 0; index < _birthdays.size(); index++) {
		if (_birthdays[index].getId() == updateBirthday.getId()) {
			_birthdays[index] = updateBirthday;

			saveBirthdays(changeService, username);
			loadBirthdays();

			syslog(LOG_INFO, "Updated birthday %d", atoi(updateBirthdayData[3].c_str()));

			return true;
		}
	}
	return false;
}

bool BirthdayService::deleteBirthday(int id, ChangeService changeService,
		std::string username) {
	std::vector<Birthday>::iterator it = _birthdays.begin();
	while (it != _birthdays.end()) {
		if ((*it).getId() == id) {
			it = _birthdays.erase(it);

			saveBirthdays(changeService, username);
			loadBirthdays();

			syslog(LOG_INFO, "Deleted birthday %d", id);

			return true;
		} else {
			++it;
		}
	}
	return false;
}
