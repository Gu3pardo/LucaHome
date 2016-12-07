#include "accesscontrolservice.h"

/*===============PUBLIC==============*/

AccessControlService::AccessControlService() {
}

AccessControlService::~AccessControlService() {
}

void AccessControlService::initialize(FileController fileController,
		MailService mailService, User accessUser, std::string accessControlIp,
		std::vector<std::string> mediaMirrorIps) {
	_fileController = fileController;
	_mailService = mailService;

	_accessUser = accessUser;

	_loginTries = 0;

	_accessControlActive = false;
	_countdownIsRunning = false;

	_accessControlIp = accessControlIp;
	_mediaMirrorIps = mediaMirrorIps;
}

std::string AccessControlService::activateAlarm() {
	if (_accessControlActive) {
		return "alarmAlreadyRunning";
	}
	_accessControlActive = true;
	accessControlAccessControlActive();
	return "alarmActivated";
}

std::string AccessControlService::checkCode(std::string code) {
	if (_accessUser.getPassword() == code) {
		_accessControlActive = false;
		_loginTries = 0;
		stopCountdown();
		accessControlLoginSuccessful();
		return "codeValid";
	} else {
		_loginTries++;
		accessControlLoginFailed();
		return "codeInvalid";
	}
}

void AccessControlService::doorOpened() {
	if (_accessControlActive) {
		if (!_countdownIsRunning) {
			startCountdown();
			accessControlRequestCode();
		}
	}
}

/*==============PRIVATE==============*/

void AccessControlService::startCountdown() {
	if (!_accessControlActive) {
		return;
	}
	if (_countdownIsRunning) {
		return;
	}
	_countdownIsRunning = true;

	//TODO reactivate while tested and working!
	//std::thread countdownThread(createCountdown);
}

void AccessControlService::stopCountdown() {
	if (!_countdownIsRunning) {
		return;
	}
	_countdownIsRunning = false;
}

void AccessControlService::countdownFinished() {
	mediaMirrorPlayAlarmSound();
	playAlarmSound();
	accessControlCountdownFinished();
}

void AccessControlService::accessControlRequestCode() {
	sendMessageToServer(_accessControlIp, 8080, "ACTION:REQUEST_CODE");
}

void AccessControlService::accessControlAccessControlActive() {
	sendMessageToServer(_accessControlIp, 8080,
			"ACTION:ACTIVATE_ACCESS_CONTROL");
}

void AccessControlService::accessControlLoginSuccessful() {
	sendMessageToServer(_accessControlIp, 8080, "ACTION:LOGIN_SUCCESS");
}

void AccessControlService::accessControlLoginFailed() {
	sendMessageToServer(_accessControlIp, 8080, "ACTION:LOGIN_FAILED");
	if (_loginTries >= 5) {
		mediaMirrorPlayAlarmSound();
		playAlarmSound();
		_mailService.sendMail("User entered 5 times a wrong code!");
		sendMessageToServer(_accessControlIp, 8080, "ACTION:ALARM_ACTIVE");
	}
}

void AccessControlService::accessControlCountdownFinished() {
	mediaMirrorPlayAlarmSound();
	playAlarmSound();
	_mailService.sendMail("User entered 5 times a wrong code!");
	sendMessageToServer(_accessControlIp, 8080, "ACTION:ALARM_ACTIVE");
}

void AccessControlService::mediaMirrorPlayAlarmSound() {
	for (int index = 0; index < _mediaMirrorIps.size(); index++) {
		sendMessageToServer(_mediaMirrorIps[index], 8080, "ACTION:PLAY_ALARM&DATA:");
	}
}

void AccessControlService::mediaMirrorStopAlarmSound() {
	for (int index = 0; index < _mediaMirrorIps.size(); index++) {
		sendMessageToServer(_mediaMirrorIps[index], 8080,
				"ACTION:STOP_ALARM&DATA:");
	}
}

void AccessControlService::playAlarmSound() {
	sendMessageToServer("127.0.0.1", 6677, "Scheduler:435435:SOUND:PLAY:ALARM");
}

void AccessControlService::stopAlarmSound() {
	sendMessageToServer("127.0.0.1", 6677, "Scheduler:435435:SOUND:STOP:ALARM");
}

void AccessControlService::sendMessageToServer(std::string ip, int port,
		std::string messageString) {
	if (ip.length() < 13) {
		return;
	}

	int serverSocket;
	struct sockaddr_in server;
	const char *message = messageString.c_str();

	serverSocket = socket(AF_INET, SOCK_STREAM, 0);
	if (serverSocket == -1) {
		//Error Could not create socket
		return;
	}
	puts("Socket created");

	server.sin_addr.s_addr = inet_addr(ip.c_str());
	server.sin_family = AF_INET;
	server.sin_port = htons(port);

	if (connect(serverSocket, (struct sockaddr *) &server, sizeof(server)) < 0) {
		//connect failed. Error
		return;
	}

	if (send(serverSocket, message, strlen(message), 0) < 0) {
		//Error Send failed
	}

	close(serverSocket);
}

void AccessControlService::createCountdown() {
	syslog(LOG_INFO, "Countdown Accesscontrol started!");

	int currentSec = 0;
	while (currentSec < 15 && _countdownIsRunning) {
		currentSec++;
		sleep(1);
	}
	if (_countdownIsRunning) {
		countdownFinished();
	}
}
