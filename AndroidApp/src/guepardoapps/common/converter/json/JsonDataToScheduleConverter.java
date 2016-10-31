package guepardoapps.common.converter.json;

import java.sql.Time;

import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.common.enums.Weekday;
import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Schedule;;

public final class JsonDataToScheduleConverter {

	private static String TAG = "JsonDataToScheduleConverter";
	private static String _searchParameter = "{schedule:";
	private static Logger _logger;

	public static SerializableList<Schedule> GetList(String[] stringArray,
			SerializableList<WirelessSocket> socketList) {
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0], socketList);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry, socketList);
		}
	}

	public static Schedule Get(String value, SerializableList<WirelessSocket> socketList) {
		if (Tools.GetStringCount(value, _searchParameter) == 1) {
			if (value.contains(_searchParameter)) {
				value = value.replace(_searchParameter, "").replace("};};", "");

				String[] data = value.split("\\};");
				Schedule newValue = ParseStringToValue(data, socketList);
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

	private static SerializableList<Schedule> ParseStringToList(String value,
			SerializableList<WirelessSocket> socketList) {
		if (Tools.GetStringCount(value, _searchParameter) > 0) {
			if (value.contains(_searchParameter)) {
				SerializableList<Schedule> list = new SerializableList<Schedule>();

				String[] entries = value.split("\\" + _searchParameter);
				for (String entry : entries) {
					entry = entry.replace(_searchParameter, "").replace("};};", "");

					String[] data = entry.split("\\};");
					Schedule newValue = ParseStringToValue(data, socketList);
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

	private static Schedule ParseStringToValue(String[] data, SerializableList<WirelessSocket> socketList) {
		if (data.length == 11) {
			if (data[0].contains("{Name:") 
					&& data[1].contains("{Socket:") 
					&& data[2].contains("{Gpio:")
					&& data[3].contains("{Weekday:") 
					&& data[4].contains("{Hour:") 
					&& data[5].contains("{Minute:")
					&& data[6].contains("{OnOff:") 
					&& data[7].contains("{IsTimer:") 
					&& data[8].contains("{PlaySound:")
					&& data[9].contains("{Raspberry:") 
					&& data[10].contains("{State:")) {
				
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

				String PlaySoundString = data[8].replace("{PlaySound:", "").replace("};", "");
				boolean playSound = PlaySoundString.contains("1");

				String PlayRaspberryString = data[9].replace("{Raspberry:", "").replace("};", "");
				RaspberrySelection playRaspberry = RaspberrySelection.GetById(Integer.parseInt(PlayRaspberryString));

				String IsActiveString = data[10].replace("{State:", "").replace("};", "");
				boolean isActive = IsActiveString.contains("1");

				if (!isTimer) {
					Schedule newValue = new Schedule(Name, socket, weekday, time, action, isTimer, playSound,
							playRaspberry, isActive);
					return newValue;
				} else {
					return null;
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error("Data has an error!");

		return null;
	}
}