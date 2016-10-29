#include <string>
#include <cstring>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <algorithm>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <inttypes.h>
#include <errno.h>
#include <math.h>
#include <time.h>

#include <pthread.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <syslog.h>

#include "../common/tools.h"

#ifndef AUDIOSERVICE_H
#define AUDIOSERVICE_H

class AudioService {
private:
	std::string _audioPath;
	bool _isInitialized;

	void play(std::string);
	bool stop();
	bool setVolume(std::string);

public:
	AudioService();
	~AudioService();

	void initialize(std::string);

	std::string performAction(std::string, std::vector<std::string>);
};

#endif
