package guepardoapps.common.converter.json;

import guepardoapps.common.Tools;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.WirelessSocket;

public final class JsonDataToSocketConverter {

	private static String TAG = "JsonDataToSocketConverter";

	private static Logger _logger;

	public static WirelessSocket Get(String wirelessSocketString) {
		if (Tools.GetStringCount(wirelessSocketString, "{socket:") == 1) {
			if (wirelessSocketString.contains("{socket:")) {
				wirelessSocketString = wirelessSocketString.replace("{socket:", "").replace("};};", "");

				String[] data = wirelessSocketString.split("\\};");
				if (data.length == 4) {
					if (data[0].contains("{Name:") && data[1].contains("{Area:") && data[2].contains("{Code:")
							&& data[3].contains("{State:")) {
						WirelessSocket newValue = ParseStringToSocket(data);
						return newValue;
					}
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(wirelessSocketString + " has an error!");
		return null;
	}

	public static SerializableList<WirelessSocket> GetList(String[] restStringArray) {
		if (Tools.StringsAreEqual(restStringArray)) {
			return ParseStringToList(restStringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(restStringArray, "{socket:");
			return ParseStringToList(usedEntry);
		}
	}

	private static WirelessSocket ParseStringToSocket(String[] data) {
		String name = data[0].replace("{Name:", "").replace("};", "");
		String area = data[1].replace("{Area:", "").replace("};", "");
		String code = data[2].replace("{Code:", "").replace("};", "");

		String isActivatedString = data[3].replace("{State:", "").replace("};", "");
		boolean isActivated = isActivatedString.contains("1");

		WirelessSocket newValue = new WirelessSocket(name, area, code, isActivated);
		return newValue;
	}

	private static SerializableList<WirelessSocket> ParseStringToList(String restString) {
		if (Tools.GetStringCount(restString, "{socket:") > 1) {
			if (restString.contains("{socket:")) {
				SerializableList<WirelessSocket> list = new SerializableList<WirelessSocket>();

				String[] entries = restString.split("\\{socket:");
				for (String entry : entries) {
					entry = entry.replace("{socket:", "").replace("};};", "");

					String[] data = entry.split("\\};");
					if (data.length == 4) {
						if (data[0].contains("{Name:") && data[1].contains("{Area:") && data[2].contains("{Code:")
								&& data[3].contains("{State:")) {
							WirelessSocket newValue = ParseStringToSocket(data);
							list.addValue(newValue);
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