#include "audioservice.h"

/*===============PUBLIC==============*/

AudioService::AudioService() {
	_isInitialized = false;
}

AudioService::~AudioService() {
}

void AudioService::initialize(std::string audioPath) {
	_audioPath = audioPath;

	_isInitialized = true;
	_isPlaying = false;
	_playingFile = "";

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
			out << "{IsPlaying:" << Tools::convertBoolToStr(_isPlaying) << "};"
					<< "\x00" << std::endl;
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
				if (Tools::hasEnding(data[4], ".mp3")) {
					if (play(data[4])) {
						return "data[4]";
					} else {
						return "Error 96:Could not start playing sound!";
					}
				} else {
					return "Error 93:Not a valid filename";
				}
			}
		} else {
			return "Error 92:Wrong data size for sound";
		}
	} else if (action == "STOP") {
		if (stop()) {
			return "stopplaying:1";
		} else {
			return "Error 90:Could not stop sound playing! Not initialized!";
		}
	} else if (action == "SET") {
		if (data[4] == "VOLUME") {
			if (data[5] == "INCREASE") {
				return setVolume("+");
			} else if (data[5] == "DECREASE") {
				return setVolume("-");
			} else {
				return "Error 95:Volume not set";
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
		_isPlaying = true;
		_playingFile = fileName;
		_exit(0);
	} else {
		wait();
	}

	return true;
}

bool AudioService::stop() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return false;
	}

	syslog(LOG_INFO, "Stop playing!");

	Tools::sendSystemCommand("killall omxplayer.bin");
	//system("killall omxplayer.bin"); //TODO: check which is working!
	_isPlaying = false;
	_playingFile = "Nothing playing";

	return true;
}

std::string AudioService::setVolume(std::string volume) {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return "Error 90:Could not stop sound playing! Not initialized!";
	}

	int newVolume = -1;

	if (volume == "+") {
		newVolume = _volume + 5;
	} else if (volume == "-") {
		newVolume = _volume + 5;
	} else {
		return "Error 95:Volume not set";
	}

	if (newVolume > 0 && newVolume < 100) {
		std::stringstream command;
		command << "amixer  sset PCM,0 " << newVolume << "%";
		syslog(LOG_INFO, "Set volume to %d!", newVolume);

		Tools::sendSystemCommand(command.str());

		if (newVolume == readVolume()) {
			std::stringstream out;
			out << "{volume:" << newVolume << "};" << "\x00" << std::endl;
			return out.str();
		}
	}

	return "Error 95:Volume not set";
}

std::vector<std::string> AudioService::scanFolder() {
	DIR *dir;
	struct dirent *ent;
	std::vector < std::string > musicEntries;

	if ((dir = opendir(_audioPath)) != NULL) {
		while ((ent = readdir(dir)) != NULL) {
			std::string fileName = ent->d_name;
			if (file_name[0] == '.') {
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
	for (int index = 0; index < musicEntries.size(); index++) {
		out << "{soundfile:" << musicEntries[index] << "};";
	}
	out << "};" << "\x00" << std::endl;

	return out.str();
}

int AudioService::readVolume() {
	return snd_mixer_selem_get_playback_volume();
}
