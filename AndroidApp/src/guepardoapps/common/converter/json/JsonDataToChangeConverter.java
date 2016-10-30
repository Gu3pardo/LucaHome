package guepardoapps.common.converter.json;

import java.sql.Date;
import java.sql.Time;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Change;

public final class JsonDataToChangeConverter {

	private static String TAG = "JsonDataToChangeConverter";
	private static Logger _logger;

	public static Change Get(String restString) {
		if (Tools.GetStringCount(restString, "{change:") == 1) {
			if (restString.contains("{change:")) {
				restString = restString.replace("{change:", "").replace("};};", "");

				String[] data = restString.split("\\};");
				if (data.length == 7) {
					if (data[0].contains("{Type:") && data[1].contains("{Hour:") && data[2].contains("{Minute:")
							&& data[3].contains("{Day:") && data[4].contains("{Month:") && data[5].contains("{Year:")
							&& data[6].contains("{User:")) {
						Change newValue = ParseStringToChange(data);
						return newValue;
					}
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");

		return null;
	}

	public static SerializableList<Change> GetList(String[] restStringArray) {
		if (Tools.StringsAreEqual(restStringArray)) {
			return ParseStringToList(restStringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(restStringArray, "{change:");
			return ParseStringToList(usedEntry);
		}
	}

	private static Change ParseStringToChange(String[] data) {
		String type = data[0].replace("{Type:", "").replace("};", "");

		String dayString = data[3].replace("{Day:", "").replace("};", "");
		int day = Integer.parseInt(dayString);
		String monthString = data[4].replace("{Month:", "").replace("};", "");
		int month = Integer.parseInt(monthString);
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

	private static SerializableList<Change> ParseStringToList(String usedEntry) {
		if (Tools.GetStringCount(usedEntry, "{change:") > 1) {
			if (usedEntry.contains("{change:")) {
				SerializableList<Change> list = new SerializableList<Change>();

				String[] entries = usedEntry.split("\\{change:");
				for (String entry : entries) {
					entry = entry.replace("{change:", "").replace("};};", "");

					String[] data = entry.split("\\};");
					if (data.length == 7) {
						if (data[0].contains("{Type:") && data[1].contains("{Hour:") && data[2].contains("{Minute:")
								&& data[3].contains("{Day:") && data[4].contains("{Month:")
								&& data[5].contains("{Year:") && data[6].contains("{User:")) {
							Change newValue = ParseStringToChange(data);
							list.addValue(newValue);
						}
					}
				}
				return list;
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(usedEntry + " has an error!");

		return null;
	}
}