#include "movie.h"

Movie::Movie() {
}

Movie::Movie(
		std::string _title,
		std::string _genre,
		std::string _description,
		int _rating,
		int _watched) {
  title = _title;
  genre = _genre;
  description = _description;
  rating = _rating;
  watched = _watched;
}

Movie::~Movie() {
}

void Movie::setTitle(std::string _title) {
	title = _title;
}

void Movie::setGenre(std::string _genre) {
	genre = _genre;
}

void Movie::setDescription(std::string _description) {
	description = _description;
}

void Movie::setRating(int _rating) {
	rating = _rating;
}

void Movie::setWatched(int _watched) {
	watched = _watched;
}

std::string Movie::getTitle() {
  return title;
}

std::string Movie::getGenre() {
  return genre;
}

std::string Movie::getDescription() {
  return description;
}

int Movie::getRating() {
  return rating;
}

int Movie::getWatched() {
  return watched;
}

std::string Movie::toString() {
	std::string str = std::string("Movie { title: ") + title
			+ std::string("; genre: ") + genre
			+ std::string("; description: ") + description
			+ std::string("; rating: ") + Tools::convertIntToStr(rating)
			+ std::string("; watched: ") + Tools::convertIntToStr(watched)
			+ std::string(" }");
		return str;
}
