#include "informationservice.h"

/*===============PUBLIC==============*/

InformationService::InformationService() {
}

InformationService::~InformationService() {
}

void InformationService::initialize(FileController fileController) {
	_fileController = fileController;
	_informationsFile = "/etc/default/lucahome/infos";

	loadInformations();
}

std::string InformationService::performAction(std::string action,
		std::vector<std::string> data) {
	if (action == "GET") {
		if (data.size() == 5) {
			if (data[4] == "REST") {
				return getRestString();
			} else if (data[4] == "WEBSITE") {
				return getString();
			} else {
				return "Error 112:Wrong action parameter for information";
			}
		} else {
			return "Error 111:Wrong data size for information";
		}
	} else {
		return "Error 110:Action not found for information";
	}
}

/*==============PRIVATE==============*/

void InformationService::loadInformations() {
	std::string informationsString = _fileController.readFile(
			_informationsFile);
	_xmlService.setContent(informationsString);
	_information = _xmlService.getInformation();
}

std::string InformationService::getRestString() {
	std::stringstream out;

	out << "{information:"
			<< "{Author:" << _information.getAuthor() << "};"
			<< "{Company:" << _information.getCompany() << "};"
			<< "{Contact:" << _information.getContact() << "};"
			<< "{Build Date:" << _information.getBuilddate() << "};"
			<< "{Server Version:" << _information.getServerVersion() << "};"
			<< "{Website Version:" << _information.getWebsiteVersion() << "};"
			<< "{Temperature Log Version:" << _information.getTemperatureLogVersion() << "};"
			<< "{Android App Version:" << _information.getAndroidAppVersion() << "};"
			<< "};";

	out << "\x00" << std::endl;

	return out.str();
}

std::string InformationService::getString() {
	std::stringstream out;

	out << "information:" << "Author:" << _information.getAuthor() << ";"
			<< "information:" << "Company:" << _information.getCompany() << ";"
			<< "information:" << "Contact:" << _information.getContact() << ";"
			<< "information:" << "Build Date:" << _information.getBuilddate() << ";"
			<< "information:" << "Server Version:" << _information.getServerVersion() << ";"
			<< "information:" << "Website Version:" << _information.getWebsiteVersion() << ";"
			<< "information:" << "Temperature Log Version:" << _information.getTemperatureLogVersion() << ";"
			<< "information:" << "Android App Version:" << _information.getAndroidAppVersion() << ";";

	out << "\x00" << std::endl;

	return out.str();
}
