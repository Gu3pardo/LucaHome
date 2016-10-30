#include "xmlparser.h"

XmlParser::XmlParser(std::string content) {
	_content = content;
}

XmlParser::~XmlParser() {
}

std::string XmlParser::findTag(std::string tag) {
	std::string tagContent;
	std::string openTag;
	std::string closeTag;
	openTag += std::string("<") + tag + std::string(">");
	closeTag += std::string("</") + tag + std::string(">");
	if (_content.length() > 0) {
		int openTag_pos = _content.find(openTag, 0);
		int closeTag_pos = _content.find(closeTag, openTag_pos);
		if (openTag_pos < closeTag_pos) {
			tagContent = _content.substr(openTag_pos + openTag.length(),
					closeTag_pos - (openTag_pos + openTag.length()));
		}
	}
	return tagContent;
}

std::vector<std::string> XmlParser::parseAreas() {
	std::string entries = findTag("areas");
	std::vector < std::string > areas;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				areas.push_back(lines[l]);
			}
		}
	}
	return areas;
}

std::vector<std::string> XmlParser::parseSensors() {
	std::string entries = findTag("sensors");
	std::vector < std::string > sensors;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				sensors.push_back(lines[l]);
			}
		}
	}
	return sensors;
}

std::vector<std::string> XmlParser::parseUrls() {
	std::string entries = findTag("urls");
	std::vector < std::string > urls;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				urls.push_back(lines[l]);
			}
		}
	}
	return urls;
}

std::vector<Birthday> XmlParser::parseBirthdays() {
	std::string entries = findTag("birthdays");
	std::vector<Birthday> birthdays;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Birthday b;
				b.setId(l);
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string))
						b.setName(words[0]);
					if (typeid(words.at(1)) == typeid(std::string))
						b.setDay(atoi(words[1].c_str()));
					if (typeid(words.at(2)) == typeid(std::string))
						b.setMonth(atoi(words[2].c_str()));
					if (typeid(words.at(3)) == typeid(std::string))
						b.setYear(atoi(words[3].c_str()));
				}
				birthdays.push_back(b);
			}
		}
	}
	return birthdays;
}

std::vector<Change> XmlParser::parseChanges() {
	std::string entries = findTag("changes");
	std::vector<Change> changes;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Change c;
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string))
						c.setType(words[0]);
					if (typeid(words.at(1)) == typeid(std::string))
						c.setHour(atoi(words[1].c_str()));
					if (typeid(words.at(2)) == typeid(std::string))
						c.setMinute(atoi(words[2].c_str()));
					if (typeid(words.at(3)) == typeid(std::string))
						c.setDay(atoi(words[3].c_str()));
					if (typeid(words.at(4)) == typeid(std::string))
						c.setMonth(atoi(words[4].c_str()));
					if (typeid(words.at(5)) == typeid(std::string))
						c.setYear(atoi(words[5].c_str()));
					if (typeid(words.at(6)) == typeid(std::string))
						c.setUser(words[6]);
				}
				changes.push_back(c);
			}
		}
	}
	return changes;
}

std::vector<Gpio> XmlParser::parseGpios() {
	std::string entries = findTag("gpios");
	std::vector<Gpio> gpios;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Gpio g;
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string))
						g.setName(words[0]);
					if (typeid(words.at(1)) == typeid(std::string))
						g.setGpio(atoi(words[1].c_str()));
					if (typeid(words.at(2)) == typeid(std::string))
						g.setState(atoi(words[2].c_str()));
				}
				gpios.push_back(g);
			}
		}
	}
	return gpios;
}

std::vector<Movie> XmlParser::parseMovies() {
	std::string entries = findTag("movies");
	std::vector<Movie> movies;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Movie newMovie;
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string)) {
						newMovie.setTitle(words[0]);
					}
					if (typeid(words.at(1)) == typeid(std::string)) {
						newMovie.setGenre(words[1]);
					}
					if (typeid(words.at(2)) == typeid(std::string)) {
						newMovie.setDescription(words[2]);
					}
					if (typeid(words.at(3)) == typeid(std::string)) {
						newMovie.setRating(atoi(words[3].c_str()));
					}
					if (typeid(words.at(4)) == typeid(std::string)) {
						newMovie.setWatched(atoi(words[4].c_str()));
					}
					if (typeid(words.at(5)) == typeid(std::string)) {
						std::vector < std::string > socketList = Tools::explode(
								"|", words.at(5));
						newMovie.setSockets(socketList);
					}
				}
				movies.push_back(newMovie);
			}
		}
	}
	return movies;
}

std::vector<Schedule> XmlParser::parseSchedules() {
	std::string entries = findTag("schedules");
	std::vector<Schedule> schedules;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Schedule s;
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string))
						s.setName(words[0]);
					if (typeid(words.at(1)) == typeid(std::string))
						s.setSocket(words[1]);
					if (typeid(words.at(2)) == typeid(std::string))
						s.setGpio(words[2]);
					if (typeid(words.at(3)) == typeid(std::string))
						s.setWeekday(atoi(words[3].c_str()));
					if (typeid(words.at(4)) == typeid(std::string))
						s.setHour(atoi(words[4].c_str()));
					if (typeid(words.at(5)) == typeid(std::string))
						s.setMinute(atoi(words[5].c_str()));
					if (typeid(words.at(6)) == typeid(std::string))
						s.setOnoff(atoi(words[6].c_str()));
					if (typeid(words.at(7)) == typeid(std::string))
						s.setIsTimer(atoi(words[7].c_str()));
					if (typeid(words.at(8)) == typeid(std::string))
						s.setPlaySound(atoi(words[8].c_str()));
					if (typeid(words.at(9)) == typeid(std::string))
						s.setPlayRaspberry(atoi(words[9].c_str()));
					if (typeid(words.at(10)) == typeid(std::string))
						s.setStatus(atoi(words[10].c_str()));
				}
				schedules.push_back(s);
			}
		}
	}
	return schedules;
}

std::vector<Socket> XmlParser::parseSockets() {
	std::string entries = findTag("sockets");
	std::vector<Socket> sockets;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				Socket s;
				for (int w = 0; w < words.size(); w++) {
					if (typeid(words.at(0)) == typeid(std::string))
						s.setName(words[0]);
					if (typeid(words.at(1)) == typeid(std::string))
						s.setArea(words[1]);
					if (typeid(words.at(2)) == typeid(std::string))
						s.setCode(words[2]);
					if (typeid(words.at(3)) == typeid(std::string))
						s.setState(atoi(words[3].c_str()), 0);
				}
				sockets.push_back(s);
			}
		}
	}
	return sockets;
}

std::vector<User> XmlParser::parseUsers() {
	std::string entries = findTag("users");
	std::vector<User> users;
	if (entries.length() > 0) {
		std::vector < std::string > lines = Tools::explode(";", entries);
		for (int l = 0; l < lines.size(); l++) {
			if (lines[l].length() > 0) {
				std::vector < std::string > words = Tools::explode(":",
						lines[l]);
				if (words.size() == 3) {
					User user(words[0].c_str(), words[1],
							atoi(words[2].c_str()));
					users.push_back(user);
				}
			}
		}
	}
	return users;
}
