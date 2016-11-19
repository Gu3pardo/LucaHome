package guepardoapps.lucahome.watchface.common.converter;

import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.watchface.dto.*;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.openweather.converter.WeatherConverter;

public class MessageToWeatherConverter {

	private static final String TAG = MessageToWeatherConverter.class.getName();
	private Logger _logger;

	private static final String CURRENT_WEATHER = "CurrentWeather:";

	public MessageToWeatherConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public WeatherModelDto ConvertMessageToWeatherModel(String message) {
		if (message.startsWith(CURRENT_WEATHER)) {
			_logger.Debug("message starts with " + CURRENT_WEATHER + "! replacing!");
			message = message.replace(CURRENT_WEATHER, "");
		}
		_logger.Debug("message: " + message);

		String[] entries = message.split("\\&");
		if (entries.length == 3) {
			String description = entries[0];
			description = description.replace("DESCRIPTION:", "");
			_logger.Debug("original description is: " + description);
			description = WeatherConverter.GetWeatherCondition(description).toString();
			_logger.Debug("converted description is: " + description);

			String temperature = entries[1];
			temperature = temperature.replace("TEMPERATURE:", "");
			_logger.Debug("temperature is: " + temperature);

			String lastupdate = entries[2];
			lastupdate = lastupdate.replace("LASTUPDATE:", "");
			_logger.Debug("lastupdate is: " + lastupdate);

			WeatherModelDto currentWeather = new WeatherModelDto(description, temperature, lastupdate);
			_logger.Debug("Received current weather: " + currentWeather.toString());
			return currentWeather;
		}

		_logger.Warn("Wrong size of entries: " + String.valueOf(entries.length));
		return null;
	}
}
