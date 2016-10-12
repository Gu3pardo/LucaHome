#include "authentificationservice.h"

AuthentificationService::AuthentificationService() {
}

AuthentificationService::~AuthentificationService() {
}

std::string AuthentificationService::getUsersString() {
	std::stringstream out;

	for (int index = 0; index < _users.size(); index++) {
		out << _users[index].toString();
	}

	return out.str();
}

std::vector<std::string> AuthentificationService::getUserNames() {
	std::vector < std::string > userNames;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		userNames.push_back((*it).getName());
		++it;
	}

	return userNames;
}

bool AuthentificationService::authentificateUser(std::string userName,
		std::string password) {
	if (userName == "DUMMY" || password == "NO_PASSWORD") {
		syslog(LOG_INFO, "Dummy user or password cannot be used!");
		return false;
	}

	bool foundUser = false;
	bool authentificationSuccess = false;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		if ((*it).getName() == userName) {
			foundUser = true;

			if ((*it).getPassword() == password) {
				authentificationSuccess = true;
			} else {
				syslog(LOG_INFO, "Entered wrong password for %s!",
						userName.c_str());
			}

			break;
		} else {
			++it;
		}
	}

	if (!foundUser) {
		syslog(LOG_INFO, "Did not found user %s!", userName.c_str());
	}

	return authentificationSuccess;
}

bool AuthentificationService::authentificateUserAction(std::string userName,
		std::string password, int userAction) {
	if (userName == "DUMMY" || password == "NO_PASSWORD") {
		syslog(LOG_INFO, "Dummy user or password cannot be used!");
		return false;
	}

	bool foundUser = false;
	bool authentificationSuccess = false;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		if ((*it).getName() == userName) {
			foundUser = true;

			if ((*it).getPassword() == password) {
				if ((*it).getRole() >= userAction) {
					authentificationSuccess = true;
				} else {
					syslog(LOG_INFO, "Action not allowed for %s!",
							userName.c_str());
				}
			} else {
				syslog(LOG_INFO, "Entered wrong password for %s!",
						userName.c_str());
			}

			break;
		} else {
			++it;
		}
	}

	if (!foundUser) {
		syslog(LOG_INFO, "Did not found user %s!", userName.c_str());
	}

	return authentificationSuccess;
}

bool AuthentificationService::updatePassword(std::string userName,
		std::string newPassword) {
	if (userName == "DUMMY" || newPassword == "NO_PASSWORD") {
		syslog(LOG_INFO, "Dummy user or password cannot be used!");
		return false;
	}

	bool foundUser = false;
	bool error = true;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		if ((*it).getName() == userName) {
			foundUser = true;

			if ((*it).getPassword() != newPassword) {
				(*it).setPassword(newPassword);

				error = false;

				saveUsers();
				loadUsers();
			} else {
				syslog(LOG_INFO,
						"Could not update password of user %s! Password is equal to last!",
						userName.c_str());
			}

			break;
		} else {
			++it;
		}
	}

	if (!foundUser) {
		syslog(LOG_INFO, "Did not found user %s to update password!",
				userName.c_str());
	}

	return foundUser && !error;
}

bool AuthentificationService::resetPassword(std::string userName) {
	syslog(LOG_INFO, "reset password of user %s", userName.c_str());

	if (userName == "DUMMY") {
		syslog(LOG_INFO, "Dummy password cannot be reset!");
		return false;
	}

	return updatePassword(userName, _defaultPassword);
}

bool AuthentificationService::addUser(User newUser) {
	syslog(LOG_INFO, "Add user %s", newUser.getName().c_str());

	if (newUser.getName() == "DUMMY" || newUser.getPassword() == "NO_PASSWORD"
			|| newUser.getRole() == -1) {
		syslog(LOG_INFO, "Dummy user or password or role cannot be used!");
		return false;
	}

	bool error = false;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		if ((*it).getName() == newUser.getName()) {
			syslog(LOG_INFO, "Could not add user %s! Name already existing!",
					newUser.getName().c_str());
			error = true;
			break;
		} else {
			++it;
		}
	}

	if (!error) {
		_users.push_back(newUser);
		saveUsers();
		loadUsers();
	}

	return !error;
}

bool AuthentificationService::deleteUser(std::string deleteUserName) {
	syslog(LOG_INFO, "Delete user %s", deleteUserName.c_str());

	if (deleteUserName == "DUMMY") {
		syslog(LOG_INFO, "Dummy user cannot be deleted!");
		return false;
	}

	bool foundUser = false;

	std::vector<User>::iterator it = _users.begin();
	while (it != _users.end()) {
		if ((*it).getName() == deleteUserName) {
			foundUser = true;
			it = _users.erase(it);
			saveUsers();
			loadUsers();
			break;
		} else {
			++it;
		}
	}

	if (!foundUser) {
		syslog(LOG_INFO, "Did not found user %s to delete!",
				deleteUserName.c_str());
	}

	return foundUser;
}

void AuthentificationService::initialize(FileController fileController) {
	_fileController = fileController;
	_usersFile = "/etc/default/lucahome/users";
	_defaultPassword = "0000";
	loadUsers();
}

void AuthentificationService::saveUsers() {
	std::string xmldata = _xmlService.generateUsersXml(_users);
	_fileController.saveFile(_usersFile, xmldata);
}

void AuthentificationService::loadUsers() {
	std::string usersString = _fileController.readFile(_usersFile);
	_xmlService.setContent(usersString);
	_users = _xmlService.getUsers();
}
