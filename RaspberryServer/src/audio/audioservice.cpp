#include "audioservice.h"

AudioService::AudioService() {
	_isInitialized = false;
}

AudioService::~AudioService() {
}

//--------------------------Public-----------------------//

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
		execlp("/usr/bin/omxplayer", " ", command, NULL);
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

	system("killall omxplayer.bin");

	return true;
}

void AudioService::initialize(std::string audioPath) {
	_audioPath = audioPath;
	_isInitialized = true;
}
