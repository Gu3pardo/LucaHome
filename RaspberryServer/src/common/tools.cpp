#include "tools.h"

std::string Tools::convertIntToStr(int Number) {
	std::ostringstream ss;
	ss << Number;
	return ss.str();
}

int Tools::convertStrToInt(std::string str) {
	std::stringstream ss(str);
	int i;
	ss >> i;
	return i;
}

std::string Tools::convertBoolToStr(bool Boolean) {
	std::ostringstream ss;
	ss << Boolean;
	return ss.str();
}

bool Tools::convertStrToBool(std::string str) {
	std::stringstream ss(str);
	bool b;
	ss >> b;
	return b;
}

bool Tools::hasEnding(std::string fullString, std::string ending) {
	if (fullString.length() >= ending.length()) {
		return (0
				== fullString.compare(fullString.length() - ending.length(),
						ending.length(), ending));
	} else {
		return false;
	}
}

std::vector<std::string> Tools::explode(std::string delimiter,
		std::string str) {
	std::vector < std::string > arr;
	int strleng = str.length();
	int delleng = delimiter.length();
	if (delleng == 0)
		return arr;
	int i = 0;
	int k = 0;
	while (i < strleng) {
		int j = 0;
		while (i + j < strleng && j < delleng && str[i + j] == delimiter[j])
			j++;
		if (j == delleng) {
			arr.push_back(str.substr(k, i - k));
			i += delleng;
			k = i;
		} else {
			i++;
		}
	}
	arr.push_back(str.substr(k, i - k));
	return arr;
}

void Tools::sendSystemCommand(std::string commandString) {
	syslog(LOG_INFO, "Sending command: %s", commandString.c_str());
	const char * commandChar = commandString.c_str();
	char command[50];
	strcpy(command, commandChar);
	system(command);
}

std::string Tools::sendSystemCommandGetResult(std::string commandString) {
	const char * command = commandString.c_str();

	FILE* pipe = popen(command, "r");
	if (!pipe) {
		return "Error 80:System command error. Pipe Error!";
	}

	char buffer[128];
	std::string result = "";
	while (!feof(pipe)) {
		if (fgets(buffer, 128, pipe) != NULL) {
			result += buffer;
		}
	}
	pclose(pipe);
	return (result);
}
