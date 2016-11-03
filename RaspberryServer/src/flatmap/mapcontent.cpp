#include "mapcontent.h"

MapContent::MapContent() {
	_id = -1;
	_position = Point();
	_type = -1;
	_temperatureArea = "";
}

MapContent::MapContent(
		int id,
		Point position,
		int type,
		std::vector<std::string> schedules,
		std::vector<std::string> sockets,
		std::string temperatureArea)
{
	_id = id;
	_position = position;
	_type = type;
	_schedules = schedules;
	_sockets = sockets;
	_temperatureArea = temperatureArea;
}

MapContent::~MapContent() {
}

int MapContent::getId() {
	return _id;
}

Point MapContent::getPosition() {
	return _position;
}

int MapContent::getType() {
	return _type;
}

std::vector<std::string> MapContent::getSchedules() {
	return _schedules;
}

std::vector<std::string> MapContent::getSockets() {
	return _sockets;
}

std::string MapContent::getTemperatureArea() {
	return _temperatureArea;
}

std::string MapContent::getRestString() {
	std::string str = std::string("{mapcontent:")
			+ std::string("};{id:") + Tools::convertIntToStr(_id)
			+ std::string("};{position:") + _position.getString()
			+ std::string("};{type:") + Tools::convertIntToStr(_type)
			+ std::string("};{schedules:") + getSchedulesString()
			+ std::string("};{sockets:") + getSocketsString()
			+ std::string("};{temperatureArea:") + _temperatureArea
			+ std::string("};};");
	return str;
}

std::string MapContent::getFileString() {
	std::string str = Tools::convertIntToStr(_id)
			+ std::string(":") + _position.getString()
			+ std::string(":") + Tools::convertIntToStr(_type)
			+ std::string(":") + getSchedulesString()
			+ std::string(":") + getSocketsString()
			+ std::string(":") + _temperatureArea
			+ std::string(";");
	return str;
}

std::string MapContent::toString() {
	std::string str = std::string("MapContent { ")
			+ std::string("; id:") + Tools::convertIntToStr(_id)
			+ std::string("; position: ") + _position.toString()
			+ std::string("; type: ") + Tools::convertIntToStr(_type)
			+ std::string("; schedules: ") + getSchedulesString()
			+ std::string("; Sockets: ") + getSocketsString()
			+ std::string("; temperature: ") + _temperatureArea
			+ std::string(" }");
	return str;
}

std::string MapContent::getSchedulesString() {
	std::stringstream out;
	for (int index = 0; index < _schedules.size(); index++) {
		out << _schedules[index] << "|";
	}
	return out.str();
}

std::string MapContent::getSocketsString() {
	std::stringstream out;
	for (int index = 0; index < _sockets.size(); index++) {
		out << _sockets[index] << "|";
	}
	return out.str();
}
