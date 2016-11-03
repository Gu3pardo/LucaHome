#include <string>
#include <vector>
#include <iostream>
#include <typeinfo>
#include <stdlib.h>
#include <sstream>
#include <syslog.h>

#include "../remote/gpio.h"
#include "../remote/schedule.h"
#include "../remote/socket.h"

#include "../authentification/user.h"
#include "../birthdays/birthday.h"
#include "../changes/change.h"
#include "../movies/movie.h"

#include "../flatmap/mapcontent.h"
#include "../flatmap/point.h"

#include "../common/tools.h"

#ifndef XMLWRITER_H
#define XMLWRITER_H

class XmlWriter {

private:

public:
	XmlWriter();
	~XmlWriter();

	static std::string generateBirthdaysXml(std::vector<Birthday>);
	static std::string generateChangesXml(std::vector<Change>);
	static std::string generateMapContentsXml(std::vector<MapContent>);
	static std::string generateMoviesXml(std::vector<Movie>);
	static std::string generateSettingsXml(int, int, int, int, std::string,
			std::vector<std::string>, std::vector<std::string>,
			std::vector<std::string>, std::vector<Socket>, std::vector<Gpio>,
			std::vector<Schedule>);
	static std::string generateUsersXml(std::vector<User>);
};

#endif
