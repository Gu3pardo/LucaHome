package guepardoapps.common.converter.json;

import java.util.ArrayList;

import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Sound;

public final class JsonDataToSoundConverter {

	private static String TAG = "JsonDataToSoundConverter";
	private static String _searchParameter = "{soundfile:";
	private static Logger _logger;

	public static ArrayList<Sound> GetList(String value) {
		if (Tools.GetStringCount(value, _searchParameter) > 1) {
			if (value.contains(_searchParameter)) {
				ArrayList<Sound> list = new ArrayList<Sound>();

				String[] entries = value.split("\\" + _searchParameter);
				for (String entry : entries) {
					Sound newValue = Get(entry);
					if (newValue != null) {
						list.add(newValue);
					}
				}
				return list;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error("GetList " + value + " has an error!");

		return null;
	}

	public static Sound Get(String value) {
		value = value.replace(_searchParameter, "").replace("};};", "").replace("};", "");

		if (value.endsWith(".mp3") 
				&& !value.contains("{") 
				&& !value.contains("}")) {
			
			Sound newValue = new Sound(value, false);
			return newValue;
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error("Get: " + value + " has an error!");

		return null;
	}
}