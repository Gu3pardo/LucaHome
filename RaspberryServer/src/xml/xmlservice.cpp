#include "xmlservice.h"

/* ------------------- Public ------------------- */

XmlService::XmlService() {
}
XmlService::XmlService(std::string _content) {
	content = _content;
}

XmlService::~XmlService() {
}

std::string XmlService::generateBirthdaysXml(std::vector<Birthday> birthdays) {
	XmlWriter writer;
	return writer.generateBirthdaysXml(birthdays);
}

std::string XmlService::generateChangesXml(std::vector<Change> changes) {
	XmlWriter writer;
	return writer.generateChangesXml(changes);
}

std::string XmlService::generateMoviesXml(std::vector<Movie> movies) {
	XmlWriter writer;
	return writer.generateMoviesXml(movies);
}

std::string XmlService::generateSettingsXml(int port, int datagpio,
		int receivergpio, int raspberry, std::vector<std::string> areas,
		std::vector<std::string> sensors, std::vector<std::string> urls,
		std::vector<Socket> sockets, std::vector<Gpio> gpios,
		std::vector<Schedule> schedules) {
	XmlWriter writer;
	return writer.generateSettingsXml(port, datagpio, receivergpio, raspberry,
			areas, sensors, urls, sockets, gpios, schedules);
}

std::string XmlService::generateUsersXml(std::vector<User> users) {
	XmlWriter writer;
	return writer.generateUsersXml(users);
}

std::string XmlService::getContent() {
	return content;
}

void XmlService::setContent(std::string _content) {
	content = _content;
}

std::vector<Birthday> XmlService::getBirthdays() {
	XmlParser parser(content);
	return parser.parseBirthdays();
}

std::vector<Change> XmlService::getChanges() {
	XmlParser parser(content);
	return parser.parseChanges();
}

Information XmlService::getInformation() {
	XmlParser parser(content);
	Information information(parser.findTag("author"), parser.findTag("company"),
			parser.findTag("contact"), parser.findTag("builddate"),
			parser.findTag("serverversion"), parser.findTag("websiteversion"),
			parser.findTag("temperaturelogversion"),
			parser.findTag("appversion"));
	return information;
}

std::vector<Movie> XmlService::getMovies() {
	XmlParser parser(content);
	return parser.parseMovies();
}

int XmlService::getPort() {
	XmlParser parser(content);
	return atoi(parser.findTag("port").c_str());
}

int XmlService::getDatagpio() {
	XmlParser parser(content);
	return atoi(parser.findTag("datagpio").c_str());
}

int XmlService::getReceivergpio() {
	XmlParser parser(content);
	return atoi(parser.findTag("receivergpio").c_str());
}

int XmlService::getRaspberry() {
	XmlParser parser(content);
	return atoi(parser.findTag("raspberry").c_str());
}

std::vector<std::string> XmlService::getAreas() {
	XmlParser parser(content);
	return parser.parseAreas();
}

std::vector<std::string> XmlService::getSensors() {
	XmlParser parser(content);
	return parser.parseSensors();
}

std::vector<std::string> XmlService::getUrls() {
	XmlParser parser(content);
	return parser.parseUrls();
}

std::vector<Socket> XmlService::getSockets() {
	XmlParser parser(content);
	return parser.parseSockets();
}

std::vector<Gpio> XmlService::getGpios() {
	XmlParser parser(content);
	return parser.parseGpios();
}

std::vector<Schedule> XmlService::getSchedules() {
	XmlParser parser(content);
	return parser.parseSchedules();
}

std::vector<User> XmlService::getUsers() {
	XmlParser parser(content);
	return parser.parseUsers();
}
