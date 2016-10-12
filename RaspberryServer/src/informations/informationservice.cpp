#include "informationservice.h"

InformationService::InformationService() {
}

InformationService::~InformationService() {
}

std::string InformationService::getInformationString() {
	std::stringstream out;
	out << _information.toString();
	return out.str();
}

Information InformationService::getInformation() {
	return _information;
}

std::string InformationService::getInformationRestString() {
	std::stringstream out;

	out << "information:" << "Author:" << _information.getAuthor() << ";"
			<< "information:" << "Company:" << _information.getCompany() << ";"
			<< "information:" << "Contact:" << _information.getContact() << ";"
			<< "information:" << "Build Date:" << _information.getBuilddate()
			<< ";" << "information:" << "Server Version:"
			<< _information.getServerVersion() << ";" << "information:"
			<< "Website Version:" << _information.getWebsiteVersion() << ";"
			<< "information:" << "Temperature Log Version:"
			<< _information.getTemperatureLogVersion() << ";" << "information:"
			<< "Android App Version:" << _information.getAndroidAppVersion()
			<< ";" << "\x00" << std::endl;

	return out.str();
}

void InformationService::loadInformations() {
	std::string informationsString = _fileController.readFile(
			_informationsFile);
	_xmlService.setContent(informationsString);
	_information = _xmlService.getInformation();
}

void InformationService::initialize(FileController fileController) {
	_fileController = fileController;
	_informationsFile = "/etc/default/lucahome/infos";

	loadInformations();

	syslog(LOG_INFO, "Informations: %s", getInformationString().c_str());
}
