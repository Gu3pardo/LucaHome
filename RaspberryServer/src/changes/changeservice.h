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

#include "change.h"
#include "../controller/filecontroller.h"
#include "../xml/xmlservice.h"

#ifndef CHANGESERVICE_H
#define CHANGESERVICE_H

class ChangeService {
private:
	std::string changesFile;
	std::vector<Change> changes;

	FileController fileController;
	XmlService _xmlService;

public:
	ChangeService();
	~ChangeService();

	std::string getChangesString();
	std::vector<Change> getChanges();
	std::string getChangesRestString();

	void updateChange(std::string);

	void initialize(FileController);
};

#endif
