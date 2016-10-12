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

#include "birthday.h"
#include "../changes/changeservice.h"
#include "../common/tools.h"
#include "../controller/filecontroller.h"
#include "../mail/mailservice.h"
#include "../xml/xmlservice.h"

#ifndef BIRTHDAYSERVICE_H
#define BIRTHDAYSERVICE_H

class BirthdayService {
private:
	std::string _birthdaysFile;
	std::vector<Birthday> _birthdays;

	FileController _fileController;
	MailService _mailService;
	XmlService _xmlService;

	void saveBirthdays(ChangeService);
	void loadBirthdays();

public:
	BirthdayService();
	~BirthdayService();

	std::string getBirthdaysString();
	std::vector<Birthday> getBirthdays();
	std::string getBirthdaysRestString();

	bool addBirthday(std::vector<std::string>, ChangeService);
	bool updateBirthday(std::vector<std::string>, ChangeService);
	bool deleteBirthday(std::string, ChangeService);

	void initialize(FileController, MailService);

	void checkBirthday();
};

#endif
