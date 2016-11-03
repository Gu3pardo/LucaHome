#include "mapcontentservice.h"

/*===============PUBLIC==============*/

MapContentService::MapContentService() {
}

MapContentService::~MapContentService() {
}

void MapContentService::initialize(FileController fileController) {
	_fileController = fileController;
	_mapcontentsFile = "/etc/default/lucahome/mapcontent";

	load();
}

std::string MapContentService::performAction(std::string action,
		std::vector<std::string> data, ChangeService changeService,
		std::string username) {
	if (action == "GET") {
		if (data[4] == "ALL") {
			return getRestString();
		}
	} else if (action == "ADD") {
		if (data.size() == 6) {
			if (add(data, changeService, username)) {
				return "addmapcontent:1";
			} else {
				return "Error 140:Could not add mapcontent";
			}
		} else {
			return "Error 141:Wrong word size for mapcontent";
		}
	} else if (action == "UPDATE") {
		if (data.size() == 6) {
			if (update(data, changeService, username)) {
				return "updatemapcontent:1";
			} else {
				return "Error 142:Could not update mapcontent";
			}
		} else {
			return "Error 141:Wrong word size for mapcontent";
		}
	} else if (action == "DELETE") {
		if (deleteEntry(atoi(data[4].c_str()), changeService, username)) {
			return "deletemapcontent:1";
		} else {
			return "Error 143:Could not delete mapcontent";
		}
	} else {
		return "Error 144:Action not found for mapcontent";
	}
}

/*==============PRIVATE==============*/

void MapContentService::save(ChangeService changeService,
		std::string username) {
	std::string xmldata = _xmlService.generateMapContentsXml(_mapcontents);
	_fileController.saveFile(_mapcontentsFile, xmldata);

	changeService.updateChange("MapContent", username);
}

void MapContentService::load() {
	std::string mapcontentString = _fileController.readFile(_mapcontentsFile);
	_xmlService.setContent(mapcontentString);

	_mapcontents = _xmlService.getMapContents();
}

std::string MapContentService::getRestString() {
	std::stringstream out;

	for (int index = 0; index < _mapcontents.size(); index++) {
		out << _mapcontents[index].getRestString();
	}
	out << "\x00" << std::endl;

	return out.str();
}

bool MapContentService::add(std::vector<std::string> words,
		ChangeService changeService, std::string username) {

	MapContent newEntry = createMapContent(words);
	_mapcontents.push_back(newEntry);

	save(changeService, username);
	load();

	syslog(LOG_INFO, "Added mapcontent %s", newEntry.toString().c_str());

	return true;
}

bool MapContentService::update(std::vector<std::string> words,
		ChangeService changeService, std::string username) {

	MapContent updatedEntry = createMapContent(words);

	for (int index = 0; index < _mapcontents.size(); index++) {
		if (_mapcontents[index].getId() == updatedEntry.getId()) {
			_mapcontents[index] = updatedEntry;

			save(changeService, username);
			load();

			syslog(LOG_INFO, "Updated mapcontent %s",
					updatedEntry.toString().c_str());

			return true;
		}
	}

	return false;
}

bool MapContentService::deleteEntry(int id, ChangeService changeService,
		std::string username) {
	std::vector<MapContent>::iterator it = _mapcontents.begin();
	while (it != _mapcontents.end()) {
		if ((*it).getId() == id) {
			it = _mapcontents.erase(it);

			save(changeService, username);
			load();

			syslog(LOG_INFO, "Deleted mapcontent %d", id);

			return true;
		} else {
			++it;
		}
	}
	return false;
}

MapContent MapContentService::createMapContent(std::vector<std::string> words) {
	int id = -1;
	int x = -1;
	int y = -1;
	int type = -1;
	std::vector < std::string > schedules;
	std::vector < std::string > sockets;
	std::string temperatureArea = "";

	if (typeid(words.at(0)) == typeid(std::string)) {
		id = atoi(words[0].c_str());
	}
	if (typeid(words.at(1)) == typeid(std::string)) {
		std::vector < std::string > coordinates = Tools::explode("|", words[1]);
		if (typeid(coordinates.at(0)) == typeid(std::string)
				&& typeid(coordinates.at(1)) == typeid(std::string)) {
			x = atoi(coordinates[0].c_str());
			y = atoi(coordinates[1].c_str());
		}
	}
	if (typeid(words.at(2)) == typeid(std::string)) {
		type = atoi(words[2].c_str());
	}
	if (typeid(words.at(3)) == typeid(std::string)) {
		schedules = Tools::explode("|", words[3]);
	}
	if (typeid(words.at(4)) == typeid(std::string)) {
		sockets = Tools::explode("|", words[4]);
	}
	if (typeid(words.at(5)) == typeid(std::string)) {
		temperatureArea = words[5].c_str();
	}

	Point position = Point(x, y);
	MapContent mapContent = MapContent(id, position, type, schedules, sockets,
			temperatureArea);

	return mapContent;
}
