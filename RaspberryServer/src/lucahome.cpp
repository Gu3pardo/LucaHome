#include <string>
#include <sstream>

namespace patch {
template<typename T> std::string to_string(const T& n) {
	std::ostringstream stm;
	stm << n;
	return stm.str();
}
}

#include <string>
#include <cstring>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <algorithm>

#include <pthread.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <syslog.h>

#include "common/tools.h"

#include "audio/audioservice.h"
#include "authentification/authentificationservice.h"
#include "birthdays/birthdayservice.h"
#include "changes/changeservice.h"
#include "informations/informationservice.h"
#include "mail/mailservice.h"
#include "movies/movieservice.h"
#include "remote/schedule.h"
#include "remote/remoteservice.h"
#include "temperature/temperatureservice.h"

#include "controller/filecontroller.h"

#define BUFLEN 512

using namespace std;

vector<Schedule> schedules;
vector<ScheduleTask> scheduleTasks;

FileController _fileController;

AudioService _audioService;
AuthentificationService _authentificationService;
BirthdayService _birthdayService;
ChangeService _changeService;
InformationService _informationService;
MailService _mailService;
MovieService _movieService;
RemoteService _remoteService;
TemperatureService _temperatureService;

pthread_mutex_t birthdaysMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t changesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t gpiosMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t moviesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t schedulesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t socketsMutex = PTHREAD_MUTEX_INITIALIZER;

string executeCmd(string cmd) {
	syslog(LOG_INFO, "Received command: %s", cmd.c_str());

	if (cmd.length() < 3) {
		return "Error 21:statement too short";
	}

	vector < string > words = Tools::explode(":", cmd);
	if (words.size() < 1) {
		return "Error 13:No username";
	}
	if (words.size() < 2) {
		return "Error 12:No password";
	}

	string userName = words[0];
	string userPassword = words[1];

	if (!_authentificationService.authentificateUser(userName, userPassword)) {
		syslog(LOG_INFO, "executeCmd: %s failed! authentificateUser failed!",
				cmd.c_str());
		return "Error 10:Authentification Failed";
	}
	if (words.size() < 4) {
		return "Error 22:invalid statement";
	}

	string action = words[2];
	string name = words[3];

	//---------------------Error----------------------
	if (action == "") {
		return "Error 23:empty statement";
	}

	//--------------------Birthday--------------------
	else if (action == "getbirthdays") {
		return _birthdayService.getBirthdaysRestString();
	} else if (action == "addbirthday" && words.size() == 7) {
		if (_birthdayService.addBirthday(words, _changeService)) {
			return "addbirthday:1";
		} else {
			return "Error 30:Could not add birthday";
		}
	} else if (action == "updatebirthday" && words.size() == 7) {
		if (_birthdayService.updateBirthday(words, _changeService)) {
			return "updatebirthday:1";
		} else {
			return "Error 31:Could not update birthday";
		}
	} else if (action == "deletebirthday") {
		if (_birthdayService.deleteBirthday(name, _changeService)) {
			return "deletebirthday:1";
		} else {
			return "Error 32:Could not delete birthday";
		}
	}

	//---------------------Changes--------------------
	else if (action == "getchanges") {
		return _changeService.getChangesRestString();
	}

	//------------------Informations------------------
	else if (action == "getinformations") {
		return _informationService.getInformationRestString();
	}

	//---------------------Movies---------------------
	else if (action == "getmovies") {
		return _movieService.getMoviesRestString();
	} else if (action == "addmovie" && words.size() == 8) {
		if (_movieService.addMovie(words, _changeService)) {
			return "addmovie:1";
		} else {
			return "Error 40:Could not add movie";
		}
	} else if (action == "deletemovie") {
		if (_movieService.deleteMovie(name, _changeService)) {
			return "deletemovie:1";
		} else {
			return "Error 41:Could not delete movie";
		}
	} else if (action == "updatemovie" && words.size() == 8) {
		if (_movieService.updateMovie(words, _changeService)) {
			return "updatemovie:1";
		} else {
			return "Error 42:Could not update movie";
		}
	}

	//--------------------Raspberry-------------------
	else if (action == "getraspberry") {
		return Tools::convertIntToStr(_remoteService.getRaspberry());
	}

	//-----------------------Area---------------------
	else if (action == "getarea") {
		return _remoteService.getArea();
	}

	//----------------------Sensor--------------------
	else if (action == "getsensor") {
		return _remoteService.getSensor();
	}

	//------------------------Url---------------------
	else if (action == "geturl") {
		return _remoteService.getUrl();
	}

	//--------------------Temperature-----------------
	else if (action == "getcurrenttemperature") {
		return _temperatureService.getCurrentTemperatureString();
	} else if (action == "gettemperaturegraphurl") {
		return _remoteService.getTemperatureGraphUrl();
	}

	//-----------------------Gpio---------------------
	else if (action == "getgpios") {
		return _remoteService.getGpiosRestString();
	} else if (action == "setgpio" && words.size() == 5) {
		if (_remoteService.setGpio(name, atoi(words[4].c_str()),
				_changeService)) {
			return "setgpio:1";
		} else {
			return "Error 50:Could not set gpio";
		}
	} else if (action == "addgpio" && words.size() == 6) {
		if (_remoteService.addGpio(words, _changeService)) {
			return "addgpio:1";
		} else {
			return "Error 51:Could not add gpio";
		}
	} else if (action == "deletegpio") {
		if (_remoteService.deleteGpio(name, _changeService)) {
			return "deletegpio:1";
		} else {
			return "Error 52:Could not delete gpio";
		}
	} else if (action == "activateAllGpios") {
		if (_remoteService.setAllGpios(1, _changeService)) {
			return "activateAllGpios:1";
		} else {
			return "Error 53:Could not activate all gpios";
		}
	} else if (action == "deactivateAllGpios") {
		if (_remoteService.setAllGpios(0, _changeService)) {
			return "deactivateAllGpios:1";
		} else {
			return "Error 54:Could not deactivate all gpios";
		}
	}

	//---------------------Schedule-------------------
	else if (action == "getschedules") {
		return _remoteService.getSchedulesRestString();
	} else if (action == "setschedule" && words.size() == 5) {
		if (_remoteService.setSchedule(name, atoi(words[4].c_str()),
				_changeService)) {
			schedules = _remoteService.getSchedules();
			return "setschedule:1";
		} else {
			return "Error 60:Could not set schedule";
		}
	} else if (action == "addschedule" && words.size() == 12) {
		if (_remoteService.addSchedule(words, _changeService)) {
			schedules = _remoteService.getSchedules();
			return "addschedule:1";
		} else {
			return "Error 61:Could not add schedule";
		}
	} else if (action == "deleteschedule") {
		if (_remoteService.deleteSchedule(name, _changeService)) {
			schedules = _remoteService.getSchedules();
			return "deleteschedule:1";
		} else {
			return "Error 62:Could not delete schedule";
		}
	} else if (action == "activateAllSchedules") {
		if (_remoteService.setAllSchedules(1, _changeService)) {
			schedules = _remoteService.getSchedules();
			return "activateAllSchedules:1";
		} else {
			return "Error 63:Could not activate all schedules";
		}
	} else if (action == "deactivateAllSchedules") {
		if (_remoteService.setAllSchedules(0, _changeService)) {
			schedules = _remoteService.getSchedules();
			return "deactivateAllSchedules:1";
		} else {
			return "Error 64:Could not deactivate all schedules";
		}
	}

	//----------------------Socket--------------------
	else if (action == "getsockets") {
		return _remoteService.getSocketsRestString();
	} else if (action == "setsocket" && words.size() == 5) {
		if (_remoteService.setSocket(name, atoi(words[4].c_str()),
				_changeService)) {
			return "setsocket:1";
		} else {
			return "Error 70:Could not set socket";
		}
	} else if (action == "addsocket" && words.size() == 7) {
		if (_remoteService.addSocket(words, _changeService)) {
			return "addsocket:1";
		} else {
			return "Error 71:Could not add socket";
		}
	} else if (action == "deletesocket") {
		if (_remoteService.deleteSocket(name, _changeService)) {
			return "deletesocket:1";
		} else {
			return "Error 72:Could not delete socket";
		}
	} else if (action == "activateAllSockets") {
		if (_remoteService.setAllSockets(1, _changeService)) {
			return "activateAllSockets:1";
		} else {
			return "Error 73:Could not activate all sockets";
		}
	} else if (action == "deactivateAllSockets") {
		if (_remoteService.setAllSockets(0, _changeService)) {
			return "deactivateAllSockets:1";
		} else {
			return "Error 74:Could not deactivate all sockets";
		}
	}

	//----------------------Sound---------------------
	else if (action == "stopplaying") {
		if (_audioService.stop()) {
			return "stopplaying:1";
		} else {
			return "Error 90:Could not stop sound playing! Not initialized!";
		}
	}

	return "Error 20:action not found";
}

void *server(void *arg) {
	syslog(LOG_INFO, "Server started!");

	int socketResult, connection, answer, clientLength;
	struct sockaddr_in clientAddress, serverAddress;
	char message[BUFLEN];

	int port = _remoteService.getPort();

	socketResult = socket(AF_INET, SOCK_DGRAM, 0);
	if (socketResult < 0) {
		syslog(LOG_CRIT, "Cant't open socket");
	}

	serverAddress.sin_family = AF_INET;
	serverAddress.sin_addr.s_addr = htonl(INADDR_ANY);
	serverAddress.sin_port = htons(port);

	connection = bind(socketResult, (struct sockaddr *) &serverAddress,
			sizeof(serverAddress));
	if (connection < 0) {
		syslog(LOG_CRIT, "Can't bind socket to port %d", port);
		exit(1);
	}

	syslog(LOG_INFO, "Server listen on port %u", port);

	while (1) {
		memset(message, 0x0, BUFLEN);
		clientLength = sizeof(clientAddress);

		answer = recvfrom(socketResult, message, BUFLEN, 0,
				(struct sockaddr *) &clientAddress, (socklen_t*) &clientLength);

		if (answer < 0) {
			syslog(LOG_ERR, "Can't receive data");
			continue;
		} else {
			syslog(LOG_INFO, "Received: %s", message);

			pthread_mutex_lock(&changesMutex);
			pthread_mutex_lock(&birthdaysMutex);
			pthread_mutex_lock(&moviesMutex);
			pthread_mutex_lock(&schedulesMutex);
			pthread_mutex_lock(&gpiosMutex);
			pthread_mutex_lock(&socketsMutex);

			// Parse and execute request
			string response = executeCmd(message);

			pthread_mutex_unlock(&socketsMutex);
			pthread_mutex_unlock(&gpiosMutex);
			pthread_mutex_unlock(&schedulesMutex);
			pthread_mutex_unlock(&moviesMutex);
			pthread_mutex_unlock(&birthdaysMutex);
			pthread_mutex_unlock(&changesMutex);

			int sendResult = sendto(socketResult, response.c_str(),
					strlen(response.c_str()), 0,
					(struct sockaddr *) &clientAddress, clientLength);
			if (sendResult < 0) {
				syslog(LOG_ERR, "Can't send data");
			}
			syslog(LOG_INFO, "Sent: %s", response.c_str());
		}
	}
	close(socketResult);
	syslog(LOG_INFO, "Exiting *server");
	pthread_exit (NULL);
}

void *scheduler(void *arg) {
	syslog(LOG_INFO, "Scheduler started!");

	time_t now;
	struct tm now_info;
	int weekday;

	while (1) {
		now = time(0);
		localtime_r(&now, &now_info);

		time_t rawtime;
		tm * timeinfo;
		time(&rawtime);
		timeinfo = localtime(&rawtime);
		weekday = timeinfo->tm_wday;

		pthread_mutex_lock(&schedulesMutex);

		// Search for done, deleted and inactive Schedules
		vector<ScheduleTask>::iterator it = scheduleTasks.begin();
		while (it != scheduleTasks.end()) {
			int found = 0;
			int done = 0;
			int active = 1;

			for (int s = 0; s < schedules.size(); s++) {
				if (schedules[s].getName() == (*it).getSchedule()) {
					found = 1;
					if (!schedules[s].getStatus()) {
						active = 0;
					}
				}
			}

			if ((*it).isDone()) {
				double deltaTime = difftime((*it).getTime(), now);
				if (deltaTime < -61) {
					done = 1;
				}
			}

			if (!found || !active || done) {
				syslog(LOG_INFO, "Removing Task '%s'",
						(*it).getSchedule().c_str());
				it = scheduleTasks.erase(it);
			} else {
				++it;
			}
		}

		// Add Schedules to Tasklist
		for (int s = 0; s < schedules.size(); s++) {
			int found = 0;

			for (int st = 0; st < scheduleTasks.size(); st++) {
				if (schedules[s].getName() == scheduleTasks[st].getSchedule()) {
					found = 1;
				}
			}

			if (!found && schedules[s].getStatus()) {
				time_t newtime = time(0);
				struct tm tasktime;
				localtime_r(&newtime, &tasktime);
				tasktime.tm_hour = schedules[s].getHour();
				tasktime.tm_min = schedules[s].getMinute();
				tasktime.tm_sec = 0;

				newtime = mktime(&tasktime);
				ScheduleTask task(schedules[s].getName(), newtime,
						schedules[s].getWeekday());
				scheduleTasks.push_back(task);
				syslog(LOG_INFO, "Adding Task '%s'",
						schedules[s].getName().c_str());
			}
		}

		// Execute Tasks
		for (int st = 0; st < scheduleTasks.size(); st++) {
			time_t tasktime = scheduleTasks[st].getTime();
			struct tm tasktime_info;
			localtime_r(&tasktime, &tasktime_info);

			int scheduleWeekday = scheduleTasks[st].getWeekday();
			string schedule = scheduleTasks[st].getSchedule();

			if (scheduleTasks[st].isDone() == 0 && scheduleWeekday == weekday
					&& tasktime_info.tm_hour == now_info.tm_hour
					&& tasktime_info.tm_min == now_info.tm_min) {

				syslog(LOG_INFO, "Executing Task '%s'", schedule.c_str());

				for (int s = 0; s < schedules.size(); s++) {
					if (schedule == schedules[s].getName()) {
						if (schedules[s].getStatus()) {
							if (schedules[s].getSocket() != "") {
								stringstream socket_out;
								socket_out << "scheduler:0000:setsocket:"
										<< schedules[s].getSocket() << ":"
										<< schedules[s].getOnoff();

								pthread_mutex_lock(&gpiosMutex);
								pthread_mutex_lock(&socketsMutex);

								string response = executeCmd(socket_out.str());
								if (response != "setsocket:1") {
									syslog(LOG_INFO,
											"Setting socket %s by scheduler failed!",
											schedules[s].getSocket().c_str());
								}

								pthread_mutex_unlock(&socketsMutex);
								pthread_mutex_unlock(&gpiosMutex);

							}

							if (schedules[s].getGpio() != "") {
								stringstream gpio_out;
								gpio_out << "scheduler:0000:setgpio:"
										<< schedules[s].getGpio() << ":"
										<< schedules[s].getOnoff();

								pthread_mutex_lock(&gpiosMutex);
								pthread_mutex_lock(&socketsMutex);

								string response = executeCmd(gpio_out.str());
								if (response != "setgpio:1") {
									syslog(LOG_INFO,
											"Setting gpio %s by scheduler failed!",
											schedules[s].getGpio().c_str());
								}

								pthread_mutex_unlock(&socketsMutex);
								pthread_mutex_unlock(&gpiosMutex);
							}

							if (schedules[s].getIsTimer() == 1) {
								stringstream schedule_delete_out;
								schedule_delete_out
										<< "scheduler:0000:deleteschedule:"
										<< schedules[s].getName();

								string response = executeCmd(
										schedule_delete_out.str());
								if (response != "deleteschedule:1") {
									syslog(LOG_INFO,
											"Deleting schedule %s by scheduler failed!",
											schedules[s].getName().c_str());
								}
							}
						}
					}
				}
				scheduleTasks[st].setDone(1);
			}
		}
		pthread_mutex_unlock(&schedulesMutex);
		sleep(10);
	}
	syslog(LOG_INFO, "Exiting *scheduler");
	pthread_exit (NULL);
}

void *receiver(void *arg) {
	syslog(LOG_INFO, "Receiver started!");

	int receivergpio = _remoteService.getReceiverGpio();

	bool foundValidGpio = false;
	if (receivergpio > 0 && receivergpio < 30) {
		foundValidGpio = true;
	} else {
		syslog(LOG_INFO, "receivergpio has invalid value: %d", receivergpio);
	}

	if (wiringPiSetup() == -1) {
		syslog(LOG_INFO, "wiringPiSetup failed, exiting...");
		foundValidGpio = false;
	}

	//TODO: enable receiver later to test feature! only if receiver is mounted on RPi!
	foundValidGpio = false;

	while (foundValidGpio) {
		//TODO: add some stuff here!
		//like checking sockets or getting informed
		//about someone entering the room (using motion sensors sending data to rpi via 433MHz)
	}

	syslog(LOG_INFO, "Exiting *receiver");
	pthread_exit (NULL);
}

void *temperatureControl(void *arg) {
	syslog(LOG_INFO, "TemperatureControl started!");

	while (1) {
		_temperatureService.controlTemperature();
		sleep(60);
	}

	syslog(LOG_INFO, "Exiting *temperatureControl");
	pthread_exit (NULL);
}

void *birthdayControl(void *arg) {
	syslog(LOG_INFO, "BirthdayControl started!");

	while (1) {
		_birthdayService.checkBirthday();
		//Check birthdays once a day (86400sec == 1440min == 24h)
		sleep(86400);
	}

	syslog(LOG_INFO, "Exiting *birthdayControl");
	pthread_exit (NULL);
}

int main(void) {
	openlog("lucahome", LOG_PID | LOG_CONS, LOG_USER);
	syslog(LOG_INFO, "Starting LucaHome!");

	time_t now = time(0);
	syslog(LOG_INFO, "Current Scheduler-Time: %s", ctime(&now));

	_audioService.initialize("/etc/default/lucahome/");
	_authentificationService.initialize(_fileController);
	_birthdayService.initialize(_fileController, _mailService);
	_changeService.initialize(_fileController);
	_informationService.initialize(_fileController);
	_movieService.initialize(_fileController);
	_remoteService.initialize(_fileController);
	_temperatureService.initialize(_mailService, _remoteService.getSensor(), _remoteService.getArea());

	std::ostringstream startMessage;
	startMessage << "Starting LucaHome at " << _remoteService.getArea();
	_mailService.sendMail(startMessage.str());

	schedules = _remoteService.getSchedules();

	pthread_t scheduleThread, serverThread, receiverThread, temperatureThread,
			birthdayThread;

	pthread_create(&serverThread, NULL, server, NULL);
	pthread_create(&scheduleThread, NULL, scheduler, NULL);
	pthread_create(&receiverThread, NULL, receiver, NULL);
	pthread_create(&temperatureThread, NULL, temperatureControl, NULL);
	pthread_create(&birthdayThread, NULL, birthdayControl, NULL);

	pthread_join(serverThread, NULL);
	pthread_join(scheduleThread, NULL);
	pthread_join(receiverThread, NULL);
	pthread_join(temperatureThread, NULL);
	pthread_join(birthdayThread, NULL);

	pthread_mutex_destroy(&socketsMutex);
	pthread_mutex_destroy(&gpiosMutex);
	pthread_mutex_destroy(&schedulesMutex);
	pthread_mutex_destroy(&moviesMutex);
	pthread_mutex_destroy(&birthdaysMutex);
	pthread_mutex_destroy(&changesMutex);

	closelog();
}
