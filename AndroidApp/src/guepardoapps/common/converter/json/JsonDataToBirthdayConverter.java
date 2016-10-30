package guepardoapps.common.converter.json;

import java.sql.Date;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Birthday;

public final class JsonDataToBirthdayConverter {

	private static String TAG = "JsonDataToBirthdayConverter";
	private static Logger _logger;

	public static Birthday Get(String birthdayString) {
		if (!birthdayString.contains("Error")) {
			if (Tools.GetStringCount(birthdayString, "{birthday:") == 1) {
				if (birthdayString.contains("{birthday:")) {
					birthdayString = birthdayString.replace("{birthday:", "").replace("};};", "");

					String[] data = birthdayString.split("\\};");
					if (data.length == 5) {
						if (data[0].contains("{id:") && data[1].contains("{name:") && data[2].contains("{day:")
								&& data[3].contains("{month:") && data[4].contains("{year:")) {
							Birthday newBirthday = ParseStringToBirthday(data);
							return newBirthday;
						}
					}
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(birthdayString + " has an error!");

		return null;
	}

	public static SerializableList<Birthday> GetList(String[] birthdayStringArray) {
		if (Tools.StringsAreEqual(birthdayStringArray)) {
			return ParseStringToList(birthdayStringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(birthdayStringArray, "{birthday:");
			return ParseStringToList(usedEntry);
		}
	}

	private static Birthday ParseStringToBirthday(String[] data) {
		String idString = data[0].replace("{id:", "").replace("};", "");
		int id = Integer.parseInt(idString);

		String name = data[1].replace("{name:", "").replace("};", "");

		String dayString = data[2].replace("{day:", "").replace("};", "");
		int day = Integer.parseInt(dayString);
		String monthString = data[3].replace("{month:", "").replace("};", "");
		int month = Integer.parseInt(monthString);
		String yearString = data[4].replace("{year:", "").replace("};", "");
		int year = Integer.parseInt(yearString);
		@SuppressWarnings("deprecation")
		Date birthday = new Date(year, month - 1, day);

		Birthday newBirthday = new Birthday(name, birthday, id);
		return newBirthday;
	}

	private static SerializableList<Birthday> ParseStringToList(String string) {
		if (!string.contains("Error")) {
			if (Tools.GetStringCount(string, "{birthday:") > 1) {
				if (string.contains("{birthday:")) {
					SerializableList<Birthday> birthdayList = new SerializableList<Birthday>();

					String[] birthdayEntries = string.split("\\{birthday:");
					for (String entry : birthdayEntries) {
						entry = entry.replace("{birthday:", "").replace("};};", "");

						String[] data = entry.split("\\};");
						if (data.length == 5) {
							if (data[0].contains("{id:") && data[1].contains("{name:") && data[2].contains("{day:")
									&& data[3].contains("{month:") && data[4].contains("{year:")) {
								Birthday newBirthday = ParseStringToBirthday(data);
								birthdayList.addValue(newBirthday);
							}
						}
					}

					return birthdayList;
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(string + " has an error!");

		return null;
	}
}