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

#ifndef XMLPARSER_H
#define XMLPARSER_H

class XmlParser {

private:
	std::string _content;

public:
	XmlParser (std::string);
	~XmlParser();

	std::string findTag(std::string);

	std::vector<std::string> parseAreas();
	std::vector<std::string> parseSensors();
	std::vector<std::string> parseUrls();

	std::vector<Birthday> parseBirthdays();
	std::vector<Change> parseChanges();
	std::vector<Gpio> parseGpios();
	std::vector<MapContent> parseMapContents();
	std::vector<Movie> parseMovies();
	std::vector<Schedule> parseSchedules();
	std::vector<Socket> parseSockets();
	std::vector<User> parseUsers();
};

#endif
