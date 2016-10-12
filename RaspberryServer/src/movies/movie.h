#include <string>
#include <ctime>
#include <cstring>
#include <iostream>

#include "../common/tools.h"

#ifndef MOVIE_H
#define MOVIE_H

class Movie {

private:
	std::string title;
	std::string genre;
	std::string description;
	int rating;
	int watched;

public:
	Movie();
	Movie(
			std::string,
			std::string,
			std::string,
			int,
			int);
	~Movie();

	void setTitle(std::string);
	void setGenre(std::string);
	void setDescription(std::string);
	void setRating(int);
	void setWatched(int);

	std::string getTitle();
	std::string getGenre();
	std::string getDescription();
	int getRating();
	int getWatched();

	std::string toString();
};

#endif
