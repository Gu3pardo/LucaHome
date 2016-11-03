#include "xmlwriter.h"

/* ------------------- Public ------------------- */

XmlWriter::XmlWriter() {
}

XmlWriter::~XmlWriter() {
}

std::string XmlWriter::generateBirthdaysXml(std::vector<Birthday> birthdays) {
	std::stringstream xml;

	xml << "<birthdays>" << std::endl;
	for (int index = 0; index < birthdays.size(); index++) {
		xml << birthdays[index].getName() << ":"
				<< Tools::convertIntToStr(birthdays[index].getDay()) << ":"
				<< Tools::convertIntToStr(birthdays[index].getMonth()) << ":"
				<< Tools::convertIntToStr(birthdays[index].getYear()) << ";"
				<< std::endl;
	}
	xml << "</birthdays>" << std::endl;

	return xml.str();
}

std::string XmlWriter::generateChangesXml(std::vector<Change> changes) {
	std::stringstream xml;

	xml << "<changes>" << std::endl;
	for (int index = 0; index < changes.size(); index++) {
		xml << changes[index].getType() << ":"
				<< Tools::convertIntToStr(changes[index].getHour()) << ":"
				<< Tools::convertIntToStr(changes[index].getMinute()) << ":"
				<< Tools::convertIntToStr(changes[index].getDay()) << ":"
				<< Tools::convertIntToStr(changes[index].getMonth()) << ":"
				<< Tools::convertIntToStr(changes[index].getYear()) << ":"
				<< changes[index].getUser() << ";" << std::endl;
	}
	xml << "</changes>" << std::endl;

	return xml.str();
}

std::string XmlWriter::generateMapContentsXml(std::vector<MapContent> mapcontents) {
	std::stringstream xml;

	xml << "<mapcontent>" << std::endl;
	for (int index = 0; index < mapcontents.size(); index++) {
		xml << Tools::convertIntToStr(mapcontents[index].getId()) << ":"
				<< mapcontents[index].getPosition().getString() << ":"
				<< Tools::convertIntToStr(mapcontents[index].getType()) << ":"
				<< mapcontents[index].getSchedulesString() << ":"
				<< mapcontents[index].getSocketsString() << ":"
				<< mapcontents[index].getTemperatureArea() << ";"
				<< std::endl;
	}
	xml << "</mapcontent>" << std::endl;

	return xml.str();
}

std::string XmlWriter::generateMoviesXml(std::vector<Movie> movies) {
	std::stringstream xml;

	xml << "<movies>" << std::endl;
	for (int index = 0; index < movies.size(); index++) {

		xml << movies[index].getTitle() << ":" << movies[index].getGenre()
				<< ":" << movies[index].getDescription() << ":"
				<< Tools::convertIntToStr(movies[index].getRating()) << ":"
				<< Tools::convertIntToStr(movies[index].getWatched()) << ":"
				<< movies[index].getSocketsString() << ";" << std::endl;
	}
	xml << "</movies>" << std::endl;

	return xml.str();
}

std::string XmlWriter::generateSettingsXml(int port, int datagpio,
		int receivergpio, int raspberry, std::string alarmsound,
		std::vector<std::string> areas, std::vector<std::string> sensors,
		std::vector<std::string> urls, std::vector<Socket> sockets,
		std::vector<Gpio> gpios, std::vector<Schedule> schedules) {
	std::stringstream xml;

	xml << "<port>" << Tools::convertIntToStr(port) << "</port>" << std::endl
			<< std::endl;

	xml << "<datagpio>" << Tools::convertIntToStr(datagpio) << "</datagpio>"
			<< std::endl << std::endl;

	xml << "<receivergpio>" << Tools::convertIntToStr(receivergpio)
			<< "</receivergpio>" << std::endl << std::endl;

	xml << "<raspberry>" << Tools::convertIntToStr(raspberry) << "</raspberry>"
			<< std::endl << std::endl;

	xml << "<alarmsound>" << alarmsound << "</alarmsound>" << std::endl
			<< std::endl;

	xml << "<areas>" << std::endl;
	for (int index = 0; index < areas.size(); index++) {
		xml << areas[index] << ";" << std::endl;
	}
	xml << "</areas>" << std::endl << std::endl;

	xml << "<sensors>" << std::endl;
	for (int index = 0; index < sensors.size(); index++) {
		xml << sensors[index] << ";" << std::endl;
	}
	xml << "</sensors>" << std::endl << std::endl;

	xml << "<urls>" << std::endl;
	for (int index = 0; index < urls.size(); index++) {
		xml << urls[index] << ";" << std::endl;
	}
	xml << "</urls>" << std::endl << std::endl;

	xml << "<sockets>" << std::endl;
	for (int index = 0; index < sockets.size(); index++) {
		xml << sockets[index].getName() << ":" << sockets[index].getArea()
				<< ":" << sockets[index].getCode() << ":"
				<< Tools::convertIntToStr(sockets[index].getState()) << ";"
				<< std::endl;
	}
	xml << "</sockets>" << std::endl << std::endl;

	xml << "<gpios>" << std::endl;
	for (int index = 0; index < gpios.size(); index++) {
		xml << gpios[index].getName() << ":"
				<< Tools::convertIntToStr(gpios[index].getGpio()) << ":"
				<< Tools::convertIntToStr(gpios[index].getState()) << ";"
				<< std::endl;
	}
	xml << "</gpios>" << std::endl << std::endl;

	xml << "<schedules>" << std::endl;
	for (int index = 0; index < schedules.size(); index++) {
		xml << schedules[index].getName() << ":" << schedules[index].getSocket()
				<< ":" << schedules[index].getGpio() << ":"
				<< Tools::convertIntToStr(schedules[index].getWeekday()) << ":"
				<< Tools::convertIntToStr(schedules[index].getHour()) << ":"
				<< Tools::convertIntToStr(schedules[index].getMinute()) << ":"
				<< Tools::convertIntToStr(schedules[index].getOnoff()) << ":"
				<< Tools::convertIntToStr(schedules[index].getIsTimer()) << ":"
				<< Tools::convertIntToStr(schedules[index].getPlaySound()) << ":"
				<< Tools::convertIntToStr(schedules[index].getPlayRaspberry()) << ":"
				<< Tools::convertIntToStr(schedules[index].getStatus()) << ";"
				<< std::endl;
	}
	xml << "</schedules>" << std::endl << std::endl;

	return xml.str();
}

std::string XmlWriter::generateUsersXml(std::vector<User> users) {
	std::stringstream xml;

	xml << "<users>" << std::endl;
	for (int index = 0; index < users.size(); index++) {
		xml << users[index].getName() << ":" << users[index].getPassword()
				<< ":" << Tools::convertIntToStr(users[index].getRole()) << ";"
				<< std::endl;
	}
	xml << "</users>" << std::endl;

	return xml.str();
}
