package guepardoapps.common.converter;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.toolset.classes.TimeString;

public final class TimeToStringConverter {

	private static String TAG = "TimeToStringConverter";

	private static Logger _logger;

	public static TimeString GetTimeOfString(String timeString) {
		if (timeString.contains(":")) {
			String[] partString = timeString.split(":");
			if (partString.length == 2) {
				return new TimeString(partString[0], partString[1]);
			}
		}

		_logger = new Logger(TAG);
		_logger.Error("timeString has an error: " + timeString);

		return null;
	}

	public static String GetStringOfTime(TimeString timeString) {
		if (timeString != null) {
			return timeString.toString();
		}

		_logger = new Logger(TAG);
		_logger.Error("timeString is null!");

		return Constants.ERROR;
	}
}