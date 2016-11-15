#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef INFORMATION_H
#define INFORMATION_H

class Information {

private:
	std::string author;
	std::string company;
	std::string contact;
	std::string builddate;
	std::string serverversion;
	std::string websiteversion;
	std::string temperaturelogversion;
	std::string androidappversion;
	std::string androidwearversion;
	std::string androidaccessversion;

public:
	Information();
	Information(std::string, std::string, std::string, std::string, std::string,
			std::string, std::string, std::string, std::string, std::string);
	~Information();

	std::string getAuthor();
	std::string getCompany();
	std::string getContact();
	std::string getBuilddate();
	std::string getServerVersion();
	std::string getWebsiteVersion();
	std::string getTemperatureLogVersion();
	std::string getAndroidAppVersion();
	std::string getAndroidWearVersion();
	std::string getAndroidAccessVersion();

	std::string toString();
};

#endif
