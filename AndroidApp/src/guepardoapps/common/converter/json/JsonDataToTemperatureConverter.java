package guepardoapps.common.converter.json;

import java.sql.Time;
import java.util.Calendar;

import guepardoapps.common.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.Temperature;
import guepardoapps.common.enums.TemperatureType;

public final class JsonDataToTemperatureConverter {

	private static String TAG = "JsonDataToTemperatureConverter";
	private static String _searchParameter = "{temperature:";
	private static Logger _logger;

	public static SerializableList<Temperature> GetList(String[] stringArray) {
		SerializableList<Temperature> temperatureList = new SerializableList<Temperature>();
		for (String entry : stringArray) {
			if (entry == null || entry.length() == 0) {
				continue;
			}

			if (entry.contains(_searchParameter)) {
				entry = entry.replace(_searchParameter, "").replace("};};", "");

				String[] data = entry.split("\\};");
				Temperature newValue = ParseStringArrayToValue(data);
				if (newValue != null) {
					temperatureList.addValue(newValue);
				}
			}
		}

		return temperatureList;
	}

	public static Temperature Get(String value) {
		if (value.contains(_searchParameter)) {
			value = value.replace(_searchParameter, "").replace("};};", "");

			String[] data = value.split("\\};");
			Temperature newValue = ParseStringArrayToValue(data);
			if (newValue != null) {
				return newValue;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static Temperature ParseStringArrayToValue(String[] data) {
		if (data.length == 4) {
			if (data[0].contains("{value:") 
					&& data[1].contains("{area:") 
					&& data[2].contains("{sensorPath:")
					&& data[3].contains("{graphPath:")) {
				
				String tempString = data[0].replace("{value:", "").replace("};", "");
				Double temperature = Double.parseDouble(tempString);

				String area = data[1].replace("{area:", "").replace("};", "");
				String sensorPath = data[2].replace("{sensorPath:", "").replace("};", "");

				Calendar calendar = Calendar.getInstance();
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int second = calendar.get(Calendar.SECOND);
				@SuppressWarnings("deprecation")
				Time time = new Time(hour, minute, second);

				String graphPath = data[3].replace("{graphPath:", "").replace("};", "");

				Temperature newValue = new Temperature(temperature, area, time, sensorPath, TemperatureType.RASPBERRY,
						graphPath);
				return newValue;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error("Data has an error!");

		return null;
	}
}