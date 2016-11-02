package guepardoapps.common.classes.controller;

import java.util.Calendar;

import guepardoapps.common.Logger;
import guepardoapps.common.classes.Birthday;

public class BirthdayController {

	private static String TAG = "BirthdayController";
	private Logger _logger;

	private Calendar _calendar;

	public BirthdayController() {
		_logger = new Logger(TAG);
		_calendar = Calendar.getInstance();
		_logger.Debug("_calendar: " + _calendar.toString());
	}

	public boolean HasBirthday(Birthday birthday) {
		_logger.Debug("HasBirthday");
		_logger.Debug("Birthday: " + birthday.toString());
		if ((birthday.GetBirthday().get(Calendar.DAY_OF_MONTH) == _calendar.get(Calendar.DAY_OF_MONTH))
				&& (birthday.GetBirthday().get(Calendar.MONTH) == _calendar.get(Calendar.MONTH))) {
			_logger.Debug("HasBirthday: " + String.valueOf(true));
			return true;
		}
		_logger.Debug("HasBirthday: " + String.valueOf(false));
		return false;
	}

	public int GetAge(Birthday birthday) {
		_logger.Debug("GetAge");
		_logger.Debug("Birthday: " + birthday.toString());
		int age;
		if ((_calendar.get(Calendar.MONTH) + 1) > birthday.GetBirthday().get(Calendar.MONTH)
				|| (_calendar.get(Calendar.MONTH) + 1) == birthday.GetBirthday().get(Calendar.MONTH)
						&& _calendar.get(Calendar.DAY_OF_MONTH) >= birthday.GetBirthday().get(Calendar.DAY_OF_MONTH)) {
			age = _calendar.get(Calendar.YEAR) - birthday.GetBirthday().get(Calendar.YEAR);
		} else {
			age = _calendar.get(Calendar.YEAR) - birthday.GetBirthday().get(Calendar.YEAR) - 1;
		}
		_logger.Debug("Age: " + String.valueOf(age));
		return age;
	}
}
