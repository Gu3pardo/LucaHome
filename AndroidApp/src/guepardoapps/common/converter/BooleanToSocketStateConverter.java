package guepardoapps.common.converter;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;

public final class BooleanToSocketStateConverter {

	private static String TAG = "BooleanToSocketStateConverter";

	private static Logger _logger;

	public static boolean GetBooleanState(String state) {
		if (state == Constants.ACTIVATED) {
			return true;
		} else if (state == Constants.DEACTIVATED) {
			return false;
		} else {
			_logger = new Logger(TAG);
			_logger.Error(state + " is not supported!");

			return false;
		}
	}

	public static String GetStringOfBoolean(boolean state) {
		if (state) {
			return Constants.ACTIVATED;
		} else {
			return Constants.DEACTIVATED;
		}
	}
}