package guepardoapps.common.converter.json;

import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.WirelessSocket;

public final class JsonDataToSocketConverter {

	private static String TAG = "JsonDataToSocketConverter";
	private static String _searchParameter = "{socket:";
	private static Logger _logger;

	public static SerializableList<WirelessSocket> GetList(String[] stringArray) {
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry);
		}
	}

	public static WirelessSocket Get(String value) {
		if (Tools.GetStringCount(value, _searchParameter) == 1) {
			if (value.contains(_searchParameter)) {
				value = value.replace(_searchParameter, "").replace("};};", "");

				String[] data = value.split("\\};");
				WirelessSocket newValue = ParseStringToValue(data);
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

	private static SerializableList<WirelessSocket> ParseStringToList(String value) {
		if (Tools.GetStringCount(value, _searchParameter) > 1) {
			if (value.contains(_searchParameter)) {
				SerializableList<WirelessSocket> list = new SerializableList<WirelessSocket>();

				String[] entries = value.split("\\" + _searchParameter);
				for (String entry : entries) {
					entry = entry.replace(_searchParameter, "").replace("};};", "");

					String[] data = entry.split("\\};");
					WirelessSocket newValue = ParseStringToValue(data);
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

	private static WirelessSocket ParseStringToValue(String[] data) {
		if (data.length == 4) {
			if (data[0].contains("{Name:") 
					&& data[1].contains("{Area:") 
					&& data[2].contains("{Code:")
					&& data[3].contains("{State:")) {

				String name = data[0].replace("{Name:", "").replace("};", "");
				String area = data[1].replace("{Area:", "").replace("};", "");
				String code = data[2].replace("{Code:", "").replace("};", "");

				String isActivatedString = data[3].replace("{State:", "").replace("};", "");
				boolean isActivated = isActivatedString.contains("1");

				WirelessSocket newValue = new WirelessSocket(name, area, code, isActivated);
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