package guepardoapps.common.converter.json;

import java.sql.Time;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.enums.Weekday;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Schedule;;

public final class JsonDataToScheduleConverter {

	private static String TAG = "JsonDataToScheduleConverter";

	private static Logger _logger;

	public static Schedule Get(String restString, SerializableList<WirelessSocket> socketList) {

		if (Tools.GetStringCount(restString, "{schedule:") == 1) {
			if (restString.contains("{schedule:")) {
				restString = restString.replace("{schedule:", "").replace("};};", "");

				String[] data = restString.split("\\};");
				if (data.length == 9) {
					if (data[0].contains("{Name:") && data[1].contains("{Socket:") && data[2].contains("{Gpio:")
							&& data[3].contains("{Weekday:") && data[4].contains("{Hour:")
							&& data[5].contains("{Minute:") && data[6].contains("{OnOff:")
							&& data[7].contains("{IsTimer:") && data[8].contains("{State:")) {
						return ParseStringToSchedule(data, socketList);
					}
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");
		return null;
	}

	public static SerializableList<Schedule> GetList(String[] restStringArray,
			SerializableList<WirelessSocket> socketList) {
		if (Tools.StringsAreEqual(restStringArray)) {
			return ParseStringToList(restStringArray[0], socketList);
		} else {
			String usedEntry = Tools.SelectString(restStringArray, "{schedule:");
			return ParseStringToList(usedEntry, socketList);
		}
	}

	private static Schedule ParseStringToSchedule(String[] data, SerializableList<WirelessSocket> socketList) {
		String Name = data[0].replace("{Name:", "").replace("};", "");

		String socketName = data[1].replace("{Socket:", "").replace("};", "");
		WirelessSocket socket = null;
		for (int index = 0; index < socketList.getSize(); index++) {
			if (socketList.getValue(index).GetName().contains(socketName)) {
				socket = socketList.getValue(index);
				break;
			}
		}

		String WeekdayString = data[3].replace("{Weekday:", "").replace("};", "");
		int weekdayInteger = Integer.parseInt(WeekdayString);
		Weekday weekday = Weekday.GetById(weekdayInteger);

		String HourString = data[4].replace("{Hour:", "").replace("};", "");
		int Hour = Integer.parseInt(HourString);
		String MinuteString = data[5].replace("{Minute:", "").replace("};", "");
		int Minute = Integer.parseInt(MinuteString);
		@SuppressWarnings("deprecation")
		Time time = new Time(Hour, Minute, 0);

		String ActionString = data[6].replace("{OnOff:", "").replace("};", "");
		boolean action = ActionString.contains("1");

		String IsTimerString = data[7].replace("{IsTimer:", "").replace("};", "");
		boolean isTimer = IsTimerString.contains("1");

		String IsActiveString = data[8].replace("{State:", "").replace("};", "");
		boolean isActive = IsActiveString.contains("1");

		if (!isTimer) {
			Schedule newValue = new Schedule(Name, socket, weekday, time, action, isTimer, isActive);
			_logger.Debug(newValue.toString());
			return newValue;
		} else {
			return null;
		}
	}

	private static SerializableList<Schedule> ParseStringToList(String restString,
			SerializableList<WirelessSocket> socketList) {
		if (Tools.GetStringCount(restString, "{schedule:") > 0) {
			if (restString.contains("{schedule:")) {
				SerializableList<Schedule> list = new SerializableList<Schedule>();

				String[] entries = restString.split("\\{schedule:");
				for (String entry : entries) {
					entry = entry.replace("{schedule:", "").replace("};};", "");

					String[] data = entry.split("\\};");
					if (data.length == 9) {
						if (data[0].contains("{Name:") && data[1].contains("{Socket:") && data[2].contains("{Gpio:")
								&& data[3].contains("{Weekday:") && data[4].contains("{Hour:")
								&& data[5].contains("{Minute:") && data[6].contains("{OnOff:")
								&& data[7].contains("{IsTimer:") && data[8].contains("{State:")) {
							Schedule newValue = ParseStringToSchedule(data, socketList);
							if (newValue != null) {
								list.addValue(newValue);
							}
						}
					}
				}
				return list;
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");
		return null;
	}
}