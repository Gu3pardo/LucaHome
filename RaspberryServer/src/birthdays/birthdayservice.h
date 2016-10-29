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

	void saveBirthdays(ChangeService, std::string);
	void loadBirthdays();

	std::string getBirthdays();

	bool addBirthday(std::vector<std::string>, ChangeService, std::string);
	bool updateBirthday(std::vector<std::string>, ChangeService, std::string);
	bool deleteBirthday(int, ChangeService, std::string);

public:
	BirthdayService();
	~BirthdayService();

	void initialize(FileController, MailService);
	void checkBirthday();

	std::string performAction(std::string, std::vector<std::string>,
			ChangeService, std::string);
};

#endif
