#include "logger.h"

/*===============PUBLIC==============*/

Logger::Logger() {
}

Logger::~Logger() {
}

void Logger::initialize(FileController fileController) {
	_fileController = fileController;
	_logPath = "/etc/default/lucahome/log/";
	_logFiles = scanFolder();
}

void Logger::addLog(std::string level, std::vector<std::string> data,
		std::string username) {
	std::string content;
	std::string timeString;

	std::stringstream contentOut;
	for (int index = 0; index < data.size(); index++) {
		contentOut << data[index] << "_";
	}
	contentOut << "\x00";

	content = contentOut.str();

	time_t now;
	struct tm now_info;
	now = time(0);
	localtime_r(&now, &now_info);

	std::stringstream timeOut;
	timeOut << Tools::convertIntToStr(now_info.tm_mday) << "."
			<< Tools::convertIntToStr(now_info.tm_mon + 1) << "."
			<< Tools::convertIntToStr(now_info.tm_year + 1900) << "|"
			<< Tools::convertIntToStr(now_info.tm_hour) << ":"
			<< Tools::convertIntToStr(now_info.tm_min) << ";";
	timeOut << "\x00";

	timeString = timeOut.str();

	std::stringstream logFileOut;
	logFileOut << "LOG_"
			<< Tools::convertIntToStr(now_info.tm_year + 1900)
			<< Tools::convertIntToStr(now_info.tm_mon + 1)
			<< Tools::convertIntToStr(now_info.tm_mday);
	logFileOut << "\x00";

	std::string logFile = logFileOut.str();

	bool foundLogFile = false;
	for (int index = 0; index < _logFiles.size(); index++) {
		if (_logFiles[index] == logFile) {
			foundLogFile = true;
			break;
		}
	}

	std::stringstream logFilePath;
	logFilePath << _logPath << logFile;

	if (!foundLogFile) {
		FILE * file = fopen(logFilePath.str().c_str(),"w");
		fclose(file);

		_logFiles = scanFolder();
	}

	std::string logsString = _fileController.readFile(logFilePath.str());
	_xmlService.setContent(logsString);
	_logs = _xmlService.getLogs();

	Log newLog = Log(level, content, timeString, username);
	_logs.push_back(newLog);

	std::string xmldata = _xmlService.generateLoggerXml(_logs);
	_fileController.saveFile(logFilePath.str(), xmldata);
}

/*==============PRIVATE==============*/

std::vector<std::string> Logger::scanFolder() {
	DIR *dir;
	struct dirent *ent;
	std::vector < std::string > logFiles;

	const char *logPathChar = _logPath.c_str();

	if ((dir = opendir(logPathChar)) != NULL) {
		while ((ent = readdir(dir)) != NULL) {
			std::string fileName = ent->d_name;
			if (fileName[0] == '.') {
				continue;
			}
			logFiles.push_back(fileName);
		}
		closedir(dir);
	}

	return logFiles;
}
