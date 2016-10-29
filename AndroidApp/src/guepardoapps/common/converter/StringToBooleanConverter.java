package guepardoapps.common.converter;

import guepardoapps.common.classes.Logger;

public final class StringToBooleanConverter {

	private static String TAG = "StringToBooleanConverter";

	private static Logger _logger;

	public static boolean GetBoolean(String string) {
		if (string == "true" || string == "1") {
			return true;
		} else if (string == "false" || string == "0") {
			return false;
		} else {
			_logger = new Logger(TAG);
			_logger.Error(string + " is not supported!");

			return false;
		}
	}

	public static String GetString(boolean bool) {
		if (bool) {
			return "true";
		} else {
			return "false";
		}
	}
}