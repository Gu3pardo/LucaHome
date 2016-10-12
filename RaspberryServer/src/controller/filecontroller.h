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

#ifndef FILECONTROLLER_H
#define FILECONTROLLER_H

class FileController {
public:
	FileController();
	~FileController();

	std::string readFile(std::string path);
	bool saveFile(std::string path, std::string content);
};

#endif
