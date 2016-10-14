#include "movieservice.h"

MovieService::MovieService() {
}

MovieService::~MovieService() {
}

std::string MovieService::getMoviesString() {
	std::stringstream out;
	for (int index = 0; index < _movies.size(); index++) {
		out << _movies[index].toString();
	}
	return out.str();
}

std::vector<Movie> MovieService::getMovies() {
	return _movies;
}

std::string MovieService::getMoviesRestString() {
	std::stringstream out;

	for (int index = 0; index < _movies.size(); index++) {
		out << "{movie:"
				<< "{Title:" << _movies[index].getTitle() << "};"
				<< "{Genre:" << _movies[index].getGenre() << "};"
				<< "{Description:" << _movies[index].getDescription() << "};"
				<< "{Rating:" << Tools::convertIntToStr(_movies[index].getRating()) << "};"
				<< "{Watched:" << Tools::convertIntToStr(_movies[index].getWatched()) << "};"
				<< "};";
	}

	out << "\x00" << std::endl;

	return out.str();
}

bool MovieService::addMovie(std::vector<std::string> newMovieData,
		ChangeService changeService) {
	syslog(LOG_INFO, "Add movie %s", newMovieData[3].c_str());
	Movie newMovie(newMovieData[3], newMovieData[4], newMovieData[5],
			atoi(newMovieData[6].c_str()), atoi(newMovieData[7].c_str()));
	_movies.push_back(newMovie);
	saveMovies(changeService);
	loadMovies();
	return true;
}

bool MovieService::updateMovie(std::vector<std::string> updateMovieData,
		ChangeService changeService) {
	syslog(LOG_INFO, "Update movie %s", updateMovieData[3].c_str());
	for (int index = 0; index < _movies.size(); index++) {
		if (_movies[index].getTitle() == updateMovieData[3]) {
			Movie updateMovie(updateMovieData[3], updateMovieData[4],
					updateMovieData[5], atoi(updateMovieData[6].c_str()),
					atoi(updateMovieData[7].c_str()));
			_movies[index] = updateMovie;
			saveMovies(changeService);
			loadMovies();
			return true;
		}
	}
	return false;
}

bool MovieService::deleteMovie(std::string title, ChangeService changeService) {
	syslog(LOG_INFO, "Delete movie %s", title.c_str());
	std::vector<Movie>::iterator it = _movies.begin();
	while (it != _movies.end()) {
		if ((*it).getTitle() == title) {
			it = _movies.erase(it);
			saveMovies(changeService);
			loadMovies();
			return true;
		} else {
			++it;
		}
	}
	return false;
}

void MovieService::initialize(FileController fileController) {
	_fileController = fileController;
	_moviesFile = "/etc/default/lucahome/movies";

	loadMovies();

	syslog(LOG_INFO, "Movies: %s", getMoviesString().c_str());
}

void MovieService::saveMovies(ChangeService changeService) {
	std::string xmldata = _xmlService.generateMoviesXml(_movies);
	_fileController.saveFile(_moviesFile, xmldata);

	changeService.updateChange("Movies");
}

void MovieService::loadMovies() {
	std::string moviesString = _fileController.readFile(_moviesFile);
	_xmlService.setContent(moviesString);
	_movies = _xmlService.getMovies();
}
