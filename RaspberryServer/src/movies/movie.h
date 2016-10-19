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
	std::vector<std::string> sockets;
	int rating;
	int watched;

public:
	Movie();
	Movie(std::string, std::string, std::string, std::vector<std::string>, int,
			int);
	~Movie();

	void setTitle(std::string);
	void setGenre(std::string);
	void setDescription(std::string);
	void setSockets(std::vector<std::string>);
	void setRating(int);
	void setWatched(int);
	void increaseWatched();

	std::string getTitle();
	std::string getGenre();
	std::string getDescription();
	std::vector<std::string> getSockets();
	std::string getSocketsString();
	int getRating();
	int getWatched();

	std::string toString();
};

#endif
