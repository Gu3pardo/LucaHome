#include "information.h"

Information::Information() {
}

Information::Information(std::string _author, std::string _company,
		std::string _contact, std::string _builddate,
		std::string _serverversion, std::string _websiteversion,
		std::string _temperaturelogversion, std::string _androidappversion,
		std::string _androidwearversion, std::string _androidaccessversion) {
	author = _author;
	company = _company;
	contact = _contact;
	builddate = _builddate;
	serverversion = _serverversion;
	websiteversion = _websiteversion;
	temperaturelogversion = _temperaturelogversion;
	androidappversion = _androidappversion;
	androidwearversion = _androidwearversion;
	androidaccessversion = _androidaccessversion;
}

Information::~Information() {
}

std::string Information::getAuthor() {
	return author;
}

std::string Information::getCompany() {
	return company;
}

std::string Information::getContact() {
	return contact;
}

std::string Information::getBuilddate() {
	return builddate;
}

std::string Information::getServerVersion() {
	return serverversion;
}

std::string Information::getWebsiteVersion() {
	return websiteversion;
}

std::string Information::getTemperatureLogVersion() {
	return temperaturelogversion;
}

std::string Information::getAndroidAppVersion() {
	return androidappversion;
}

std::string Information::getAndroidWearVersion() {
	return androidwearversion;
}

std::string Information::getAndroidAccessVersion() {
	return androidaccessversion;
}

std::string Information::toString() {
	std::string str = std::string("Information { author: ") + author
			+ std::string("; company: ") + company
			+ std::string("; contact: ") + contact
			+ std::string("; builddate: ") + builddate
			+ std::string("; serverversion: ") + serverversion
			+ std::string("; websiteversion: ") + websiteversion
			+ std::string("; temperaturelogversion: ") + temperaturelogversion
			+ std::string("; androidappversion: ") + androidappversion
			+ std::string("; androidwearversion: ") + androidwearversion
			+ std::string("; androidaccessversion: ") + androidaccessversion
			+ std::string(" }");
	return str;
}
