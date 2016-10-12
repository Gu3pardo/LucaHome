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
#include "../xml/xmlservice.h"

#ifndef MOVIESERVICE_H
#define MOVIESERVICE_H

class MovieService {
private:
	std::string _moviesFile;
	std::vector<Movie> _movies;

	FileController _fileController;
	XmlService _xmlService;

	void saveMovies(ChangeService);
	void loadMovies();

public:
	MovieService();
	~MovieService();

	std::string getMoviesString();
	std::vector<Movie> getMovies();
	std::string getMoviesRestString();

	bool addMovie(std::vector<std::string>, ChangeService);
	bool updateMovie(std::vector<std::string>, ChangeService);
	bool deleteMovie(std::string, ChangeService);

	void initialize(FileController);
};

#endif
