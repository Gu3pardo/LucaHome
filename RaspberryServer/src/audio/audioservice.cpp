#include "audioservice.h"

/*===============PUBLIC==============*/

AudioService::AudioService() {
	_isInitialized = false;
}

AudioService::~AudioService() {
}

void AudioService::initialize(std::string audioPath, std::string alarmSound,
		int raspberry) {
	_audioPath = audioPath;
	_alarmSound = alarmSound;

	_isInitialized = true;
	_isPlaying = false;
	_playingFile = "";
	_raspberry = raspberry;

	_volume = readVolume();
	_soundFiles = scanFolder();
}

std::string AudioService::performAction(std::string action,
		std::vector<std::string> data) {
	if (action == "GET") {
		if (data[4] == "FILES") {
			return getSoundFilesRestString();
		} else if (data[4] == "VOLUME") {
			std::stringstream out;
			out << "{Volume:" << Tools::convertIntToStr(readVolume()) << "};"
					<< "\x00" << std::endl;
			return out.str();
		} else if (data[4] == "PLAYING") {
			std::stringstream out;
			if (_isPlaying) {
				out << "{IsPlaying:1};"
						<< "{PlayingFile:" << _playingFile << "};"
						<< "{Volume:" << Tools::convertIntToStr(readVolume()) << "};"
						<< "{Raspberry:" << Tools::convertIntToStr(_raspberry) << "};"
						<< "\x00" << std::endl;

			} else {
				out << "{IsPlaying:0};" << "\x00" << std::endl;
			}
			return out.str();
		} else if (data[4] == "PLAYINGFILE") {
			std::stringstream out;
			out << "{PlayingFile:" << _playingFile << "};" << "\x00"
					<< std::endl;
			return out.str();
		} else {
			return "Error 91:Action not found for sound";
		}
	} else if (action == "PLAY") {
		if (data.size() == 5) {
			if (data[4].length() < 5) {
				return "Error 93:Not a valid filename";
			} else {
				if (data[4] == "ALARM") {
					if (play(_alarmSound)) {
						return _alarmSound;
					} else {
						return "Error 96:Could not start playing sound!";
					}
				} else {
					if (Tools::hasEnding(data[4], ".mp3")) {
						if (play(data[4])) {
							return data[4];
						} else {
							return "Error 96:Could not start playing sound!";
						}
					} else {
						return "Error 93:Not a valid filename";
					}
				}
			}
		} else {
			return "Error 92:Wrong data size for sound";
		}
	} else if (action == "STOP") {
		if (stop()) {
			return "stopplaying:1";
		} else {
			return "Error 90:Could not stop sound playing!";
		}
	} else if (action == "SET") {
		if (data[4] == "VOLUME") {
			if (data[5] == "INCREASE") {
				return setVolume("+");
			} else if (data[5] == "DECREASE") {
				return setVolume("-");
			} else {
				return "Error 97:Set sound volume command not found!";
			}
		} else {
			return "Error 91:Action not found for sound";
		}
	} else {
		return "Error 91:Action not found for sound";
	}
}

/*==============PRIVATE==============*/

bool AudioService::play(std::string fileName) {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return false;
	}

	if (_isPlaying) {
		if (!stop()) {
			syslog(LOG_INFO, "Stop playing failed!");
			return false;
		}
	}

	syslog(LOG_INFO, "Playing: %s", fileName.c_str());

	std::stringstream command;
	command << _audioPath << fileName;

	int pid;
	pid = fork();
	if (pid == 0) {
		execlp("/usr/bin/omxplayer", " ", command.str().c_str(), NULL);
		_playingFile = fileName;
		_exit(0);
	} else {
		wait();
	}

	_isPlaying = true;

	return true;
}

bool AudioService::stop() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return false;
	}

	syslog(LOG_INFO, "Stop playing!");

	Tools::sendSystemCommand("killall omxplayer.bin");

	_isPlaying = false;
	_playingFile = "-/-";

	return true;
}

std::string AudioService::setVolume(std::string volumeAction) {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return "Error 99:Could not set volume! Not initialized!";
	}

	int volume;

	if (volumeAction == "+") {
		volume = _volume + 5;
	} else if (volumeAction == "-") {
		volume = _volume - 5;
	} else {
		return "Error 98:Volume action not valid!";
	}

	if (volume >= 0 && volume <= 100) {
		std::stringstream command;
		command << "amixer  sset PCM,0 " << volume << "%";
		Tools::sendSystemCommand(command.str());

		if (volume == readVolume()) {
			_volume = volume;

			std::stringstream response;
			response << "{Volume:" << _volume << "};";

			return response.str();
		}
	}

	return "Error 95:Volume not set";
}

std::vector<std::string> AudioService::scanFolder() {
	DIR *dir;
	struct dirent *ent;
	std::vector < std::string > musicEntries;

	const char *audioPathChar = _audioPath.c_str();

	if ((dir = opendir(audioPathChar)) != NULL) {
		while ((ent = readdir(dir)) != NULL) {
			std::string fileName = ent->d_name;
			if (fileName[0] == '.') {
				continue;
			}
			if (Tools::hasEnding(fileName, ".mp3")) {
				musicEntries.push_back(fileName);
			}
		}
		closedir(dir);
	}

	return musicEntries;
}

std::string AudioService::getSoundFilesRestString() {
	std::stringstream out;

	out << "{Music:";
	for (int index = 0; index < _soundFiles.size(); index++) {
		out << "{soundfile:" << _soundFiles[index] << "};";
	}
	out << "};" << "\x00" << std::endl;

	return out.str();
}

int AudioService::readVolume() {
	std::stringstream command;
	command << "amixer  sget PCM";
	std::string response = Tools::sendSystemCommandGetResult(command.str());

	if (response.find("Mono: Playback") != std::string::npos) {
		int startIndex = response.find("Mono: Playback");
		if (response.find("%]") != std::string::npos) {
			int endIndex = response.find("%]");
			int length = endIndex - startIndex;

			std::string data = response.substr(startIndex, length);
			if (data.find("[") != std::string::npos) {
				int newStartIndex = data.find("[") + 1;
				int newLength = data.size() - newStartIndex;

				std::string volume = data.substr(newStartIndex, newLength);
				if (volume.find_first_not_of("0123456789")
						== std::string::npos) {
					return Tools::convertStrToInt(volume);
				} else {
					syslog(LOG_INFO, "failed volume is: %s", volume.c_str());
				}
			}
		}
	}

	return -1;
}
