package guepardoapps.lucahome.common.converter;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.watchface.dto.*;

import guepardoapps.toolset.common.Logger;

public class MessageToRaspberryConverter {

	private static final String TAG = MessageToRaspberryConverter.class.getName();
	private Logger _logger;

	private static final String RASPBERRY_TEMPERATURE = "RaspberryTemperature:";

	public MessageToRaspberryConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public TemperatureDto ConvertMessageToRaspberryModel(
			String message/* , int raspberry */) {
		if (message.startsWith(RASPBERRY_TEMPERATURE)) {
			_logger.Debug("message starts with " + RASPBERRY_TEMPERATURE + "! replacing!");
			message = message.replace(RASPBERRY_TEMPERATURE, "");
		}
		_logger.Debug("message: " + message);

		// _logger.Debug("raspberry: " + String.valueOf(raspberry));

		String[] entries = message.split("\\&");
		if (entries.length == 6) {
			String temperature = entries[0 /* + raspberry * 3 */];
			temperature = temperature.replace("TEMPERATURE:", "");
			_logger.Debug("temperature is: " + temperature);

			String area = entries[1 /* + raspberry * 3 */];
			area = area.replace("AREA:", "");
			_logger.Debug("area is: " + area);

			String lastupdate = entries[2 /* + raspberry * 3 */];
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
