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

#include "information.h"

#include "../common/tools.h"
#include "../controller/filecontroller.h"
#include "../xml/xmlservice.h"

#ifndef INFORMATIONSERVICE_H
#define INFORMATIONSERVICE_H

class InformationService {
private:
	std::string _informationsFile;
	Information _information;

	FileController _fileController;
	XmlService _xmlService;

	void loadInformations();

public:
	InformationService();
	~InformationService();

	std::string getInformationString();
	Information getInformation();
	std::string getInformationRestString();
	std::string getInformationWebsiteString();

	void initialize(FileController);
};

#endif
