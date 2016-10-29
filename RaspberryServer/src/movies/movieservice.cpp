#include "movieservice.h"

/*===============PUBLIC==============*/

MovieService::MovieService() {
}

MovieService::~MovieService() {
}

void MovieService::initialize(FileController fileController) {
	_fileController = fileController;
	_moviesFile = "/etc/default/lucahome/movies";

	loadMovies();
}

std::string MovieService::performAction(std::string action,
		std::vector<std::string> data, ChangeService changeService,
		std::string username, RemoteService remoteService) {
	if (action == "GET") {
		if (data[4] == "COUNT") {
			return Tools::convertIntToStr(getCount());
		} else if (data[4] == "ALL") {
			return getRestString(0, getCount() - 1);
		} else if (data[4] == "INDEX") {
			if (data.size() == 7) {
				return getRestString(atoi(data[5].c_str()),
						atoi(data[6].c_str()));
			} else {
				return "Error 47:Wrong word size for movie";
			}
		}
	} else if (action == "ADD") {
		if (data.size() == 10) {
			if (addMovie(data, changeService, username)) {
				return "addmovie:1";
			} else {
				return "Error 40:Could not add movie";
			}
		} else {
			return "Error 47:Wrong word size for movie";
		}
	} else if (action == "UPDATE") {
		if (data.size() == 10) {
			if (updateMovie(data, changeService, username)) {
				return "updatemovie:1";
			} else {
				return "Error 42:Could not update movie";
			}
		} else {
			return "Error 47:Wrong word size for movie";
		}
	} else if (action == "DELETE") {
		if (deleteMovie(data[4], changeService, username)) {
			return "deletemovie:1";
		} else {
			return "Error 41:Could not delete movie";
		}
	} else if (action == "START") {
		if (startMovie(data[4], changeService, username)) {
			if (remoteService.activateSockets(getMovieSockets(data[4]),
					changeService, username)) {
				return "startmovie:1";
			} else {
				return "Error 49:Could not activate movie sockets";
			}
		} else {
			return "Error 43:Could not start movie";
		}
	} else {
		return "Error 48:Action not found for movie";
	}
}

std::vector<std::string> MovieService::getMovieSockets(std::string movieTitle) {
	for (int index = 0; index < _movies.size(); index++) {
		if (_movies[index].getTitle() == movieTitle) {
			return _movies[index].getSockets();
		}
	}

	std::vector < std::string > error;
	error.push_back("Error 44:No sockets available");

	return error;
}

/*==============PRIVATE==============*/

void MovieService::saveMovies(ChangeService changeService,
		std::string username) {
	std::string xmldata = _xmlService.generateMoviesXml(_movies);
	_fileController.saveFile(_moviesFile, xmldata);

	changeService.updateChange("Movies", username);
}

void MovieService::loadMovies() {
	std::string moviesString = _fileController.readFile(_moviesFile);
	_xmlService.setContent(moviesString);

	_movies = _xmlService.getMovies();
}

std::string MovieService::generateRestEntry(Movie movie) {
	std::stringstream out;
	out << "{movie:" << "{Title:" << movie.getTitle() << "};" << "{Genre:"
			<< movie.getGenre() << "};" << "{Description:"
			<< movie.getDescription() << "};" << "{Rating:"
			<< Tools::convertIntToStr(movie.getRating()) << "};" << "{Watched:"
			<< Tools::convertIntToStr(movie.getWatched()) << "};" << "{Sockets:"
			<< movie.getSocketsString() << "};" << "};";
	return out.str();
}

int MovieService::getCount() {
	return _movies.size();
}

std::string MovieService::getRestString(int start, int end) {
	if (start > _movies.size() - 1) {
		return "Error 45:Start index higher then last index value";
	}

	if (end > _movies.size() - 1) {
		syslog(LOG_INFO, "Error 46:End index higher then last index value");
		end = _movies.size() - 1;
	}

	std::stringstream out;

	for (int index = start; index < end; index++) {
		out << generateRestEntry(_movies[index]);
	}
	out << "\x00" << std::endl;

	return out.str();
}

bool MovieService::addMovie(std::vector<std::string> newMovieData,
		ChangeService changeService, std::string username) {
	std::vector < std::string > socketList = Tools::explode("|",
			newMovieData[8]);

	Movie newMovie(newMovieData[3], newMovieData[4], newMovieData[5],
			socketList, atoi(newMovieData[6].c_str()),
			atoi(newMovieData[7].c_str()));
	_movies.push_back(newMovie);

	saveMovies(changeService, username);
	loadMovies();

	syslog(LOG_INFO, "Added movie %s", newMovieData[3].c_str());

	return true;
}

bool MovieService::updateMovie(std::vector<std::string> updateMovieData,
		ChangeService changeService, std::string username) {
	std::vector < std::string > socketList = Tools::explode("|",
			updateMovieData[8]);

	for (int index = 0; index < _movies.size(); index++) {
		if (_movies[index].getTitle() == updateMovieData[3]) {
			Movie updateMovie(updateMovieData[3], updateMovieData[4],
					updateMovieData[5], socketList,
					atoi(updateMovieData[6].c_str()),
					atoi(updateMovieData[7].c_str()));
			_movies[index] = updateMovie;

			saveMovies(changeService, username);
			loadMovies();

			syslog(LOG_INFO, "Updated movie %s", updateMovieData[3].c_str());

			return true;
		}
	}

	return false;
}

bool MovieService::deleteMovie(std::string title, ChangeService changeService,
		std::string username) {
	std::vector<Movie>::iterator it = _movies.begin();
	while (it != _movies.end()) {
		if ((*it).getTitle() == title) {
			it = _movies.erase(it);

			saveMovies(changeService, username);
			loadMovies();

			syslog(LOG_INFO, "Deleted movie %s", title.c_str());

			return true;
		} else {
			++it;
		}
	}
	return false;
}

bool MovieService::startMovie(std::string title, ChangeService changeService,
		std::string username) {
	for (int index = 0; index < _movies.size(); index++) {
		if (_movies[index].getTitle() == title) {
			_movies[index].increaseWatched();

			saveMovies(changeService, username);
			loadMovies();

			syslog(LOG_INFO, "Started movie %s", title.c_str());

			return true;
		}
	}
	return false;
}
