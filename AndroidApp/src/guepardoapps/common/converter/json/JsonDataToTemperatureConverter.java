package guepardoapps.common.converter.json;

import java.sql.Time;
import java.util.Calendar;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.Temperature;
import guepardoapps.common.enums.TemperatureType;

public final class JsonDataToTemperatureConverter {

	private static String TAG = "JsonDataToTemperatureConverter";
	private static Logger _logger;

	public static Temperature GetTemperature(String temperatureString) {
		if (temperatureString.contains("{temperature:")) {
			temperatureString = temperatureString.replace("{temperature:", "").replace("};};", "");

			String[] data = temperatureString.split("\\};");
			if (data.length == 4) {
				if (data[0].contains("{value:") && data[1].contains("{area:") && data[2].contains("{sensorPath:")
						&& data[3].contains("{graphPath:")) {
					Temperature newTemperature = ParseStringArrayToTemperature(data);
					return newTemperature;
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(temperatureString + " has an error!");

		return null;
	}

	public static SerializableList<Temperature> GetTemperatureList(String[] temperatureStringArray) {
		SerializableList<Temperature> temperatureList = new SerializableList<Temperature>();
		for (String entry : temperatureStringArray) {
			if (entry == null || entry.length() == 0) {
				continue;
			}

			if (entry.contains("{temperature:")) {
				entry = entry.replace("{temperature:", "").replace("};};", "");

				String[] data = entry.split("\\};");
				if (data.length == 4) {
					if (data[0].contains("{value:") && data[1].contains("{area:") && data[2].contains("{sensorPath:")
							&& data[3].contains("{graphPath:")) {
						Temperature newTemperature = ParseStringArrayToTemperature(data);
						temperatureList.addValue(newTemperature);
					}
				}
			}
		}

		return temperatureList;
	}

	private static Temperature ParseStringArrayToTemperature(String[] data) {
		String tempString = data[0].replace("{value:", "").replace("};", "");
		Double temperature = Double.parseDouble(tempString);

		String area = data[1].replace("{area:", "").replace("};", "");
		String sensorPath = data[2].replace("{sensorPath:", "").replace("};", "");

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		@SuppressWarnings("deprecation")
		Time time = new Time(hour, minute, second);

		String graphPath = data[3].replace("{graphPath:", "").replace("};", "");

		Temperature newTemperature = new Temperature(temperature, area, time, sensorPath, TemperatureType.RASPBERRY,
				graphPath);
		return newTemperature;
	}
}