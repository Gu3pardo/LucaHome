package guepardoapps.common.classes.controller;

import java.util.Calendar;

import guepardoapps.common.Logger;
import guepardoapps.common.classes.Birthday;

@SuppressWarnings("deprecation")
public class BirthdayController {

	private static String TAG = "BirthdayController";

	@SuppressWarnings("unused")
	private Logger _logger;

	private Calendar _today;

	public BirthdayController() {
		_logger = new Logger(TAG);

		_today = Calendar.getInstance();
	}

	public boolean HasBirthday(Birthday birthday) {
		if (birthday.GetBirthday().getDay() == _today.get(Calendar.DAY_OF_MONTH)
				&& birthday.GetBirthday().getMonth() == _today.get(Calendar.MONTH)) {
			return true;
		}
		return false;
	}

	public int GetAge(Birthday birthday) {
		int age;

		if ((_today.get(Calendar.MONTH) + 1) >= birthday.GetBirthday().getMonth()
				&& _today.get(Calendar.DAY_OF_MONTH) >= birthday.GetBirthday().getDay()) {
			age = _today.get(Calendar.YEAR) - birthday.GetBirthday().getYear();
		} else {
			age = _today.get(Calendar.YEAR) - birthday.GetBirthday().getYear() - 1;
		}

		age -= 1900;

		return age;
	}
}
