package guepardoapps.common.converter.json;

import guepardoapps.common.classes.SerializableList;

import java.util.Calendar;

import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Birthday;

public final class JsonDataToBirthdayConverter {

	private static String TAG = "JsonDataToBirthdayConverter";
	private static String _searchParameter = "{birthday:";
	private static Logger _logger;

	public static SerializableList<Birthday> GetList(String[] stringArray) {
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry);
		}
	}

	public static Birthday Get(String value) {
		if (!value.contains("Error")) {
			if (Tools.GetStringCount(value, _searchParameter) == 1) {
				if (value.contains(_searchParameter)) {
					value = value.replace(_searchParameter, "").replace("};};", "");

					String[] data = value.split("\\};");
					Birthday newValue = ParseStringToValue(data);
					if (newValue != null) {
						return newValue;
					}
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static SerializableList<Birthday> ParseStringToList(String value) {
		if (!value.contains("Error")) {
			if (Tools.GetStringCount(value, _searchParameter) > 1) {
				if (value.contains(_searchParameter)) {
					SerializableList<Birthday> list = new SerializableList<Birthday>();

					String[] entries = value.split("\\" + _searchParameter);
					for (String entry : entries) {
						entry = entry.replace(_searchParameter, "").replace("};};", "");

						String[] data = entry.split("\\};");
						Birthday newValue = ParseStringToValue(data);
						if (newValue != null) {
							list.addValue(newValue);
						}
					}

					return list;
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static Birthday ParseStringToValue(String[] data) {
		if (data.length == 5) {
			if (data[0].contains("{id:") 
					&& data[1].contains("{name:") 
					&& data[2].contains("{day:")
					&& data[3].contains("{month:") 
					&& data[4].contains("{year:")) {

				String idString = data[0].replace("{id:", "").replace("};", "");
				int id = Integer.parseInt(idString);

				String name = data[1].replace("{name:", "").replace("};", "");

				String dayString = data[2].replace("{day:", "").replace("};", "");
				int day = Integer.parseInt(dayString);
				String monthString = data[3].replace("{month:", "").replace("};", "");
				int month = Integer.parseInt(monthString);
				String yearString = data[4].replace("{year:", "").replace("};", "");
				int year = Integer.parseInt(yearString);
				Calendar birthday = Calendar.getInstance();
				birthday.set(Calendar.DAY_OF_MONTH, day);
				birthday.set(Calendar.MONTH, month - 1);
				birthday.set(Calendar.YEAR, year);

				Birthday newValue = new Birthday(name, birthday, id);
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