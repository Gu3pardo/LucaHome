#include "birthdayservice.h"

BirthdayService::BirthdayService() {
}

BirthdayService::~BirthdayService() {
}

std::string BirthdayService::getBirthdaysString() {
	std::stringstream out;
	for (int index = 0; index < _birthdays.size(); index++) {
		out << _birthdays[index].toString();
	}
	return out.str();
}

std::vector<Birthday> BirthdayService::getBirthdays() {
	return _birthdays;
}

std::string BirthdayService::getBirthdaysRestString() {
	std::stringstream out;

	for (int index = 0; index < _birthdays.size(); index++) {
		out << "birthday:" << _birthdays[index].getName() << ":"
				<< Tools::convertIntToStr(_birthdays[index].getDay()) << ":"
				<< Tools::convertIntToStr(_birthdays[index].getMonth()) << ":"
				<< Tools::convertIntToStr(_birthdays[index].getYear()) << ";";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool BirthdayService::addBirthday(std::vector<std::string> newBirthdayData,
		ChangeService changeService) {
	syslog(LOG_INFO, "Add birthday %s", newBirthdayData[3].c_str());
	Birthday newBirthday(newBirthdayData[3], atoi(newBirthdayData[4].c_str()),
			atoi(newBirthdayData[5].c_str()), atoi(newBirthdayData[6].c_str()));
	_birthdays.push_back(newBirthday);
	saveBirthdays(changeService);
	loadBirthdays();
	return true;
}

bool BirthdayService::updateBirthday(
		std::vector<std::string> updateBirthdayData,
		ChangeService changeService) {
	syslog(LOG_INFO, "Update birthday %s", updateBirthdayData[3].c_str());
	Birthday updateBirthday(updateBirthdayData[3],
			atoi(updateBirthdayData[4].c_str()),
			atoi(updateBirthdayData[5].c_str()),
			atoi(updateBirthdayData[6].c_str()));
	for (int index = 0; index < _birthdays.size(); index++) {
		if (_birthdays[index].getName() == updateBirthday.getName()) {
			_birthdays[index] = updateBirthday;
			saveBirthdays(changeService);
			loadBirthdays();
			return true;
		}
	}
	return false;
}

bool BirthdayService::deleteBirthday(std::string name,
		ChangeService changeService) {
	syslog(LOG_INFO, "Delete birthday %s", name.c_str());
	std::vector<Birthday>::iterator it = _birthdays.begin();
	while (it != _birthdays.end()) {
		if ((*it).getName() == name) {
			it = _birthdays.erase(it);
			saveBirthdays(changeService);
			loadBirthdays();
			return true;
		} else {
			++it;
		}
	}
	return false;
}

void BirthdayService::initialize(FileController fileController,
		MailService mailService) {
	_fileController = fileController;
	_mailService = mailService;

	_birthdaysFile = "/etc/default/lucahome/birthdays";

	loadBirthdays();

	syslog(LOG_INFO, "Birthdays: %s", getBirthdaysString().c_str());
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
					<< " has birthday today! It is the " << Tools::convertIntToStr(age)
					<< "th birthday!";

			_mailService.sendMail(information.str());
		}
	}
}

void BirthdayService::saveBirthdays(ChangeService changeService) {
	std::string xmldata = _xmlService.generateBirthdaysXml(_birthdays);
	_fileController.saveFile(_birthdaysFile, xmldata);

	changeService.updateChange("Birthdays");
}

void BirthdayService::loadBirthdays() {
	std::string birthdaysString = _fileController.readFile(_birthdaysFile);
	_xmlService.setContent(birthdaysString);
	_birthdays = _xmlService.getBirthdays();
}
