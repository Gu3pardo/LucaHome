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

#include "user.h"
#include "../controller/filecontroller.h"
#include "../xml/xmlservice.h"

#ifndef AUTHENTIFICATIONSERVICE_H
#define AUTHENTIFICATIONSERVICE_H

class AuthentificationService {
private:
	std::vector<User> _users;

	std::string _usersFile;
	std::string _defaultPassword;

	FileController _fileController;
	XmlService _xmlService;

	void saveUsers();
	void loadUsers();

	int getActionId(std::string);

public:
	AuthentificationService();
	~AuthentificationService();

	void initialize(FileController);

	std::string getUsersString();
	std::vector<std::string> getUserNames();

	bool authentificateUser(std::string, std::string);
	bool authentificateUserAction(std::string, std::string, std::string);

	bool updatePassword(std::string, std::string);
	bool resetPassword(std::string);

	bool addUser(User);
	bool deleteUser(std::string);

	std::string performaAction(std::string, std::vector<std::string>);
};

#endif
