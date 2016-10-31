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
#include "receiver/receiverservice.h"
#include "remote/schedule.h"
#include "remote/remoteservice.h"
#include "temperature/temperatureservice.h"

#include "controller/filecontroller.h"

#define BUFLEN 512

using namespace std;

bool _updatedSchedules;
vector<Schedule> _schedules;
vector<ScheduleTask> _scheduleTasks;

FileController _fileController;

AudioService _audioService;
AuthentificationService _authentificationService;
BirthdayService _birthdayService;
ChangeService _changeService;
InformationService _informationService;
MailService _mailService;
MovieService _movieService;
ReceiverService _receiverService;
RemoteService _remoteService;
TemperatureService _temperatureService;

//TODO reactivate!
//RCSwitch _receiverSwitch;

pthread_mutex_t birthdaysMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t changesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t gpiosMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t moviesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t schedulesMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t socketsMutex = PTHREAD_MUTEX_INITIALIZER;

string executeCmd(string cmd) {
	syslog(LOG_INFO, "Received command: %s", cmd.c_str());

	if (cmd.length() < 20) {
		return "Error 21:statement too short";
	}

	vector < string > data = Tools::explode(":", cmd);
	if (data.size() < 1) {
		return "Error 13:No username";
	}
	if (data.size() < 2) {
		return "Error 12:No password";
	}
	if (data.size() < 3) {
		return "Error 24:No category";
	}
	if (data.size() < 4) {
		return "Error 23:No action";
	}

	string username = data[0];
	string password = data[1];
	string category = data[2];
	string action = data[3];

	if (username == "") {
		return "Error 13:No username";
	}
	if (password == "") {
		return "Error 12:No password";
	}
	if (category == "") {
		return "Error 24:No category";
	}
	if (action == "") {
		return "Error 23:No action";
	}
	if (data[4] == "") {
		return "Error 25:No action parameter";
	}

	//---------------Authentificate user--------------
	if (!_authentificationService.authentificateUser(username, password)) {
		syslog(LOG_INFO, "executeCmd: %s failed! authentificateUser failed!",
				cmd.c_str());
		return "Error 10:Authentification Failed";
	}
	if (!_authentificationService.authentificateUserAction(username, password,
			action)) {
		syslog(LOG_INFO,
				"executeCmd: %s failed! authentificateUserAction failed!",
				cmd.c_str());
		return "Error 11:UserAction cannot be performed";
	}

	//--------------------Birthday--------------------
	if (category == "BIRTHDAY") {
		return _birthdayService.performAction(action, data, _changeService,
				username);
	}
	//---------------------Changes--------------------
	else if (category == "CHANGE") {
		return _changeService.performAction(action, data);
	}
	//------------------Informations------------------
	else if (category == "INFORMATION") {
		return _informationService.performAction(action, data);
	}
	//---------------------Movies---------------------
	else if (category == "MOVIE") {
		return _movieService.performAction(action, data, _changeService,
				username, _remoteService);
	}
	//---------------------Remote---------------------
	else if (category == "REMOTE") {
		if (data[4] == "SCHEDULE") {
			if (action == "SET" || action == "ADD" || action == "DELETE") {
				_updatedSchedules = true;
			}
		}
		return _remoteService.performAction(action, data, _changeService,
				username);
	}
	//----------------------Sound---------------------
	else if (category == "SOUND") {
		return _audioService.performAction(action, data);
	}
	//--------------------Temperature-----------------
	else if (category == "TEMPERATURE") {
		return _temperatureService.performAction(action, data);
	}
	//----------------------User----------------------
	else if (category == "USER") {
		if (action == "VALIDATE") {
			return "validateuser:1";
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

			if (_updatedSchedules) {
				_schedules = _remoteService.getSchedules();
				_updatedSchedules = false;
			}

			_remoteService.reloadData();
			_changeService.reloadData();

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
		vector<ScheduleTask>::iterator it = _scheduleTasks.begin();
		while (it != _scheduleTasks.end()) {
			int found = 0;
			int done = 0;
			int active = 1;

			for (int s = 0; s < _schedules.size(); s++) {
				if (_schedules[s].getName() == (*it).getSchedule()) {
					found = 1;
					if (!_schedules[s].getStatus()) {
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
				it = _scheduleTasks.erase(it);
			} else {
				++it;
			}
		}

		// Add Schedules to Tasklist
		for (int s = 0; s < _schedules.size(); s++) {
			int found = 0;

			for (int st = 0; st < _scheduleTasks.size(); st++) {
				if (_schedules[s].getName()
						== _scheduleTasks[st].getSchedule()) {
					found = 1;
				}
			}

			if (!found && _schedules[s].getStatus()) {
				time_t newtime = time(0);
				struct tm tasktime;
				localtime_r(&newtime, &tasktime);
				tasktime.tm_hour = _schedules[s].getHour();
				tasktime.tm_min = _schedules[s].getMinute();
				tasktime.tm_sec = 0;

				newtime = mktime(&tasktime);
				ScheduleTask task(_schedules[s].getName(), newtime,
						_schedules[s].getWeekday());
				_scheduleTasks.push_back(task);
				syslog(LOG_INFO, "Adding Task '%s'",
						_schedules[s].getName().c_str());
			}
		}

		// Execute Tasks
		for (int st = 0; st < _scheduleTasks.size(); st++) {
			time_t tasktime = _scheduleTasks[st].getTime();
			struct tm tasktime_info;
			localtime_r(&tasktime, &tasktime_info);

			int scheduleWeekday = _scheduleTasks[st].getWeekday();
			string schedule = _scheduleTasks[st].getSchedule();

			if (_scheduleTasks[st].isDone() == 0 && scheduleWeekday == weekday
					&& tasktime_info.tm_hour == now_info.tm_hour
					&& tasktime_info.tm_min == now_info.tm_min) {

				syslog(LOG_INFO, "Executing Task '%s'", schedule.c_str());

				for (int s = 0; s < _schedules.size(); s++) {
					if (schedule == _schedules[s].getName()) {
						if (_schedules[s].getStatus()) {
							if (_schedules[s].getSocket() != "") {
								stringstream socket_out;
								socket_out
										<< "scheduler:435435:REMOTE:SET:SOCKET:"
										<< _schedules[s].getSocket() << ":"
										<< _schedules[s].getOnoff();

								pthread_mutex_lock(&socketsMutex);

								string response = executeCmd(socket_out.str());
								if (response != "setsocket:1") {
									syslog(LOG_INFO,
											"Setting socket %s by scheduler failed!",
											_schedules[s].getSocket().c_str());
								}

								pthread_mutex_unlock(&socketsMutex);
							}

							if (_schedules[s].getGpio() != "") {
								stringstream gpio_out;
								gpio_out << "scheduler:435435:REMOTE:SET:GPIO:"
										<< _schedules[s].getGpio() << ":"
										<< _schedules[s].getOnoff();

								pthread_mutex_lock(&gpiosMutex);

								string response = executeCmd(gpio_out.str());
								if (response != "setgpio:1") {
									syslog(LOG_INFO,
											"Setting gpio %s by scheduler failed!",
											_schedules[s].getGpio().c_str());
								}

								pthread_mutex_unlock(&gpiosMutex);
							}

							if (_schedules[s].getPlaySound() == 1
									&& _schedules[s].getIsTimer() != 1) {
								if (_schedules[s].getPlayRaspberry()
										== _remoteService.getRaspberry()) {
									stringstream sound_out;
									sound_out
											<< "scheduler:435435:SOUND:PLAY:ALARM:";
									string response = executeCmd(
											sound_out.str());
									if (response
											!= _remoteService.getAlarmSound()) {
										syslog(LOG_INFO,
												"Playing alarm failed! %s",
												_remoteService.getAlarmSound().c_str());
									}
								}
							}

							if (_schedules[s].getIsTimer() == 1) {
								if (_schedules[s].getPlaySound() == 1) {
									if (_schedules[s].getPlayRaspberry()
											== _remoteService.getRaspberry()) {
										stringstream sound_out;
										sound_out
												<< "scheduler:435435:SOUND:STOP:ALARM:";
										string response = executeCmd(
												sound_out.str());
										if (response != "stopplaying:1") {
											syslog(LOG_INFO,
													"Stopping alarm failed! %s",
													_remoteService.getAlarmSound().c_str());
										}
									}
								}

								stringstream schedule_delete_out;
								schedule_delete_out
										<< "scheduler:435435:REMOTE:DELETE:SCHEDULE:"
										<< _schedules[s].getName();

								string response = executeCmd(
										schedule_delete_out.str());
								if (response != "deleteschedule:1") {
									syslog(LOG_INFO,
											"Deleting schedule %s by scheduler failed!",
											_schedules[s].getName().c_str());
								}
							}
						}
					}
				}
				_scheduleTasks[st].setDone(1);
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
	//TODO: enable receiver later to test feature! only if receiver is mounted on RPi and store data to assets!
	if (receivergpio > 0 && receivergpio < 30) {
		foundValidGpio = true;

		//TODO reactivate!
		//_receiverSwitch = RCSwitch();
		//_receiverSwitch.enableReceive(receivergpio);
	} else {
		syslog(LOG_INFO, "receivergpio has invalid value: %d", receivergpio);
	}

	if (wiringPiSetup() == -1) {
		syslog(LOG_INFO, "wiringPiSetup failed, exiting...");
		foundValidGpio = false;
	}

	while (foundValidGpio) {
		//TODO reactivate!
		/*if (_receiverSwitch.available()) {
		 int value = _receiverSwitch.getReceivedValue();

		 if (value == 0) {
		 syslog(LOG_INFO, "Received value with unknown encoding!");
		 } else {
		 _receiverService.performAction(value);
		 }

		 _receiverSwitch.resetAvailable();
		 }*/
	}

	syslog(LOG_INFO, "Exiting *receiver");
	pthread_exit (NULL);
}

void *temperatureControl(void *arg) {
	syslog(LOG_INFO, "TemperatureControl started!");

	while (1) {
		_temperatureService.controlTemperature();
		//Check temperature every minute (60sec == 1min)
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

	_authentificationService.initialize(_fileController);
	_birthdayService.initialize(_fileController, _mailService);
	_changeService.initialize(_fileController);
	_informationService.initialize(_fileController);
	_movieService.initialize(_fileController);
	_remoteService.initialize(_fileController);
	_audioService.initialize("/media/lucahome/",
			_remoteService.getAlarmSound());
	_temperatureService.initialize(_mailService, _remoteService.getSensor(),
			_remoteService.getArea(), _remoteService.getTemperatureGraphUrl());

	std::ostringstream startMessage;
	startMessage << "Starting LucaHome at " << _remoteService.getArea();
	_mailService.sendMail(startMessage.str());

	_updatedSchedules = false;
	_schedules = _remoteService.getSchedules();

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
