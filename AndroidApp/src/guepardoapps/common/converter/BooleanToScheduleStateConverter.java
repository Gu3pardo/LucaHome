package guepardoapps.common.converter;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;

public final class BooleanToScheduleStateConverter {

	private static String TAG = "BooleanToScheduleStateConverter";

	private static Logger _logger;

	public static boolean GetBooleanState(String state) {
		if (state == Constants.SCHEDULE_ACTIVE) {
			return true;
		} else if (state == Constants.SCHEDULE_INACTIVE) {
			return false;
		} else {
			_logger = new Logger(TAG);
			_logger.Error(state + " is not supported!");

			return false;
		}
	}

	public static String GetStringOfBoolean(boolean state) {
		if (state) {
			return Constants.SCHEDULE_ACTIVE;
		} else {
			return Constants.SCHEDULE_INACTIVE;
		}
	}
}