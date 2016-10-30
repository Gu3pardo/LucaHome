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

#ifndef TOOLS_H
#define TOOLS_H

class Tools {
public:
	static std::string convertIntToStr(int);
	static int convertStrToInt(std::string);

	static std::string convertBoolToStr(bool);
	static bool convertStrToBool(std::string);

	static bool hasEnding(std::string, std::string);

	static std::vector<std::string> explode(std::string, std::string);

	static void sendSystemCommand(std::string);
	static std::string sendSystemCommandGetResult(std::string);
};

#endif
