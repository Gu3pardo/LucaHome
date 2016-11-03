#include <string>
#include <cstring>
#include <iostream>

#include "mapcontent.h"

#include "../changes/changeservice.h"
#include "../common/tools.h"
#include "../controller/filecontroller.h"

#ifndef MAPCONTENTSERVICE_H
#define MAPCONTENT_H

class MapContentService {

private:
	std::string _mapcontentsFile;
	std::vector<MapContent> _mapcontents;

	FileController _fileController;
	XmlService _xmlService;

	void save(ChangeService, std::string);
	void load();

	std::string getRestString();

	bool add(std::vector<std::string>, ChangeService, std::string);
	bool update(std::vector<std::string>, ChangeService, std::string);
	bool deleteEntry(int, ChangeService, std::string);

	MapContent createMapContent(std::vector<std::string>);

public:
	MapContentService();
	~MapContentService();

	void initialize(FileController);

	std::string performAction(std::string, std::vector<std::string>,
			ChangeService, std::string);
};

#endif
