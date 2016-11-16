package guepardoapps.lucahome.wearcontrol.common.helper;

import android.content.Context;

import guepardoapps.lucahome.wearcontrol.common.Constants;
import guepardoapps.lucahome.wearcontrol.dto.*;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.BroadcastController;
import guepardoapps.toolset.openweather.converter.WeatherConverter;

public class MessageReceiveHelper {

	private static final String CURRENT_WEATHER = "CurrentWeather:";
	private static final String RASPBERRY_TEMPERATURE = "RaspberryTemperature:";
	private static final String PHONE_BATTERY = "PhoneBattery:";

	private static final String TAG = MessageReceiveHelper.class.getName();
	private Logger _logger;

	private Context _context;
	private BroadcastController _broadcastController;

	public MessageReceiveHelper(Context context) {
		_logger = new Logger(TAG);

		_context = context;
		_broadcastController = new BroadcastController(_context);
	}

	public void HandleMessage(String message) {
		_logger.Debug("HandleMessage: " + message);
		if (message.startsWith(CURRENT_WEATHER)) {
			message = message.replace(CURRENT_WEATHER, "");
			WeatherModelDto currentWeather = convertMessageToWeatherModel(message);
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_CURRENT_WEATHER,
					Constants.BUNDLE_CURRENT_WEATHER, currentWeather);
		} else if (message.startsWith(RASPBERRY_TEMPERATURE)) {
			message = message.replace(RASPBERRY_TEMPERATURE, "");
			TemperatureDto raspberryTemperature1 = convertMessageToRaspberryModel(message, 0);
			TemperatureDto raspberryTemperature2 = convertMessageToRaspberryModel(message, 1);
			_broadcastController.SendSerializableArrayBroadcast(Constants.BROADCAST_UPDATE_RASPBERRY_TEMPERATURE,
					new String[] { Constants.BUNDLE_RASPBERRY_TEMPERATURE_1, Constants.BUNDLE_RASPBERRY_TEMPERATURE_2 },
					new Object[] { raspberryTemperature1, raspberryTemperature2 });
		} else if (message.startsWith(PHONE_BATTERY)) {
			message = message.replace(PHONE_BATTERY, "");
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_PHONE_BATTERY,
					Constants.BUNDLE_PHONE_BATTERY, message);
		} else {
			_logger.Warn("Cannot handle message: " + message);
		}
	}

	private WeatherModelDto convertMessageToWeatherModel(String message) {
		_logger.Debug(message);

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

	private TemperatureDto convertMessageToRaspberryModel(String message, int raspberry) {
		_logger.Debug("message: " + message);
		_logger.Debug("raspberry: " + String.valueOf(raspberry));

		String[] entries = message.split("\\&");
		if (entries.length == 6) {
			String temperature = entries[0 + raspberry * 3];
			temperature = temperature.replace("TEMPERATURE:", "");
			_logger.Debug("temperature is: " + temperature);

			String area = entries[1 + raspberry * 3];
			area = area.replace("AREA:", "");
			_logger.Debug("area is: " + area);

			String lastupdate = entries[2 + raspberry * 3];
			lastupdate = lastupdate.replace("LASTUPDATE:", "");
			_logger.Debug("lastupdate is: " + lastupdate);

			TemperatureDto raspberryTemperature = new TemperatureDto(temperature, area, lastupdate);
			_logger.Debug("Received raspberryTemperature: " + raspberryTemperature.toString());
			return raspberryTemperature;
		}

		_logger.Warn("Wrong size of entries: " + String.valueOf(entries.length));
		return null;
	}
}
