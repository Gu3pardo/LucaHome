#include "mailservice.h"

MailService::MailService() {
}

MailService::~MailService() {
}

//--------------------------Public-----------------------//

void MailService::sendMail(std::string message) {
	int maxMessageLength = 256;
	std::string command = "usr/bin/python /etc/default/lucahome/mail.py";

	if (command.length() + message.length() > maxMessageLength) {
		syslog(LOG_INFO, "Error! Message too long! Message: %s; Length: %d",
				message.c_str(), message.length());
		return;
	}

	syslog(LOG_INFO, "Send mail with message: %s", message.c_str());

	char buffer[maxMessageLength];
	int length;

	length = sprintf(buffer, "%s %s", command.c_str(), message.c_str());

	std::string str(buffer);

	system(buffer);
}
