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
}

std::string AudioService::performAction(std::string action,
		std::vector<std::string> data) {
	if (action == "PLAY") {
		if (data.size() == 5) {
			if (data[4].length() < 5) {
				return "Error 93:Not a valid filename";
			} else {
				if (Tools::hasEnding(data[4], ".mp3")) {
					play(data[4]);
					return "startplaying:1";
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
			if (setVolume(data[5])) {
				return "setVolume:1";
			} else {
				return "Error 95:Volume not valid";
			}
		} else {
			return "Error 91:Action not found for sound";
		}
	} else {
		return "Error 91:Action not found for sound";
	}
}

/*==============PRIVATE==============*/

void AudioService::play(std::string fileName) {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return;
	}

	syslog(LOG_INFO, "Playing: %s", fileName.c_str());

	std::stringstream command;
	command << _audioPath << fileName;

	int pid;
	pid = fork();
	if (pid == 0) {
		execlp("/usr/bin/omxplayer", " ", command.str().c_str(), NULL);
		_exit(0);
	} else {
		wait();
	}
}

bool AudioService::stop() {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return false;
	}

	syslog(LOG_INFO, "Stop playing!");

	Tools::sendSystemCommand("killall omxplayer.bin");
	//system("killall omxplayer.bin"); //TODO: check which is working!

	return true;
}

bool AudioService::setVolume(std::string volume) {
	if (!_isInitialized) {
		syslog(LOG_INFO, "AudioService is not initialized!");
		return false;
	}

	int newVolume = atoi(volume.c_str());
	if (newVolume > 0 && newVolume < 100) {
		std::stringstream command;
		command << "amixer  sset PCM,0 " << newVolume << "%";
		syslog(LOG_INFO, "Set volume to %d!", newVolume);

		Tools::sendSystemCommand(command.str());

		return true;
	}

	return false;
}
