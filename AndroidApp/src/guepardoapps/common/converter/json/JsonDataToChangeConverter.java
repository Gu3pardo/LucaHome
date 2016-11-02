package guepardoapps.common.converter.json;

import java.sql.Date;
import java.sql.Time;

import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Change;

public final class JsonDataToChangeConverter {

	private static String TAG = "JsonDataToChangeConverter";
	private static String _searchParameter = "{change:";
	private static Logger _logger;

	public static SerializableList<Change> GetList(String[] stringArray) {
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry);
		}
	}

	public static Change Get(String value) {
		if (Tools.GetStringCount(value, _searchParameter) == 1) {
			if (value.contains(_searchParameter)) {
				value = value.replace(_searchParameter, "").replace("};};", "");

				String[] data = value.split("\\};");
				Change newValue = ParseStringToValue(data);
				if (newValue != null) {
					return newValue;
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static SerializableList<Change> ParseStringToList(String value) {
		if (Tools.GetStringCount(value, _searchParameter) > 1) {
			if (value.contains(_searchParameter)) {
				SerializableList<Change> list = new SerializableList<Change>();

				String[] entries = value.split("\\" + _searchParameter);
				for (String entry : entries) {
					entry = entry.replace(_searchParameter, "").replace("};};", "");

					String[] data = entry.split("\\};");
					Change newValue = ParseStringToValue(data);
					if (newValue != null) {
						list.addValue(newValue);
					}
				}
				return list;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static Change ParseStringToValue(String[] data) {
		if (data.length == 7) {
			if (data[0].contains("{Type:") 
					&& data[1].contains("{Hour:") 
					&& data[2].contains("{Minute:")
					&& data[3].contains("{Day:") 
					&& data[4].contains("{Month:") 
					&& data[5].contains("{Year:")
					&& data[6].contains("{User:")) {
				
				String type = data[0].replace("{Type:", "").replace("};", "");

				String dayString = data[3].replace("{Day:", "").replace("};", "");
				int day = Integer.parseInt(dayString);
				String monthString = data[4].replace("{Month:", "").replace("};", "");
				int month = Integer.parseInt(monthString) - 1;
				String yearString = data[5].replace("{Year:", "").replace("};", "");
				int year = Integer.parseInt(yearString);
				@SuppressWarnings("deprecation")
				Date date = new Date(year, month, day);

				String hourString = data[1].replace("{Hour:", "").replace("};", "");
				int hour = Integer.parseInt(hourString);
				String minuteString = data[2].replace("{Minute:", "").replace("};", "");
				int minute = Integer.parseInt(minuteString);
				@SuppressWarnings("deprecation")
				Time time = new Time(hour, minute, 0);

				String user = data[6].replace("{User:", "").replace("};", "");

				Change newValue = new Change(type, date, time, user);
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