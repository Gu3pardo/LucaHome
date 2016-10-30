package guepardoapps.common.converter.json;

import guepardoapps.common.classes.Logger;

import java.util.ArrayList;

import guepardoapps.common.Tools;
import guepardoapps.common.classes.Sound;

public final class JsonDataToSoundConverter {

	private static String TAG = "JsonDataToSoundConverter";
	private static String _searchParameter = "{soundfile:";
	private static Logger _logger;

	public static ArrayList<Sound> GetList(String restString) {
		if (Tools.GetStringCount(restString, _searchParameter) > 1) {
			if (restString.contains(_searchParameter)) {
				ArrayList<Sound> list = new ArrayList<Sound>();

				String[] entries = restString.split("\\" + _searchParameter);
				for (String entry : entries) {
					Sound newValue = Get(entry);
					if (newValue != null) {
						list.add(newValue);
					}
				}
				return list;
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");

		return null;
	}

	public static Sound Get(String restString) {
		if (Tools.GetStringCount(restString, _searchParameter) == 1) {
			if (restString.contains(_searchParameter)) {
				restString = restString.replace(_searchParameter, "").replace("};};", "");
				String[] data = restString.split("\\};");
				if (data.length == 1) {
					Sound newValue = new Sound(data[0], false);
					return newValue;
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");

		return null;
	}
}