package guepardoapps.lucahome.common.converter;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.Logger;

public final class BooleanToScheduleStateConverter {

	private static String TAG = BooleanToScheduleStateConverter.class.getName();
	private static Logger _logger;

	public static boolean GetBooleanState(String state) {
		if (state == Constants.ACTIVE) {
			return true;
		} else if (state == Constants.INACTIVE) {
			return false;
		} else {
			_logger = new Logger(TAG);
			_logger.Error(state + " is not supported!");

			return false;
		}
	}

	public static String GetStringOfBoolean(boolean state) {
		if (state) {
			return Constants.ACTIVE;
		} else {
			return Constants.INACTIVE;
		}
	}
}