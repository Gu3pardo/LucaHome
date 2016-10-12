#include "filecontroller.h"

FileController::FileController() {
}

FileController::~FileController() {
}

std::string FileController::readFile(std::string path) {
	std::string content;
	std::ifstream file;
	file.open(path.c_str());
	if (file.is_open()) {
		std::string line;
		while (!file.eof()) {
			getline(file, line, '\n');
			content.append(line);
		}
	} else {
		syslog(LOG_CRIT, "Can't read file: %s", path.c_str());
	}
	file.close();
	return content;
}

bool FileController::saveFile(std::string path, std::string content) {
	bool success;
	std::ofstream file;
	file.open(path.c_str(), std::ios::out);
	if (file.is_open()) {
		file << content;
		success = true;
	} else {
		syslog(LOG_ERR, "Can't write file: %s", path.c_str());
		success = false;
	}
	file.close();
	return success;
}
