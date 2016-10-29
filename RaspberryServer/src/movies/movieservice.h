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

#include "movie.h"
#include "../changes/changeservice.h"
#include "../controller/filecontroller.h"
#include "../remote/remoteservice.h"
#include "../xml/xmlservice.h"

#include "../common/tools.h"

#ifndef MOVIESERVICE_H
#define MOVIESERVICE_H

class MovieService {
private:
	std::string _moviesFile;
	std::vector<Movie> _movies;

	FileController _fileController;
	XmlService _xmlService;

	void saveMovies(ChangeService, std::string);
	void loadMovies();

	std::string generateRestEntry(Movie);
	int getCount();

	std::string getRestString(int, int);

	bool addMovie(std::vector<std::string>, ChangeService, std::string);
	bool updateMovie(std::vector<std::string>, ChangeService, std::string);
	bool deleteMovie(std::string, ChangeService, std::string);

	bool startMovie(std::string, ChangeService, std::string);

public:
	MovieService();
	~MovieService();

	void initialize(FileController);

	std::string performAction(std::string, std::vector<std::string>,
			ChangeService, std::string, RemoteService);

	std::vector<std::string> getMovieSockets(std::string);
};

#endif
