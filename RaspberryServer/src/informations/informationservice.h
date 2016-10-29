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

	std::string getRestString();
	std::string getString();

public:
	InformationService();
	~InformationService();

	void initialize(FileController);

	std::string performAction(std::string, std::vector<std::string>);
};

#endif
