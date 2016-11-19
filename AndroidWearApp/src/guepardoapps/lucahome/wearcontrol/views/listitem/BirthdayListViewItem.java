package guepardoapps.lucahome.wearcontrol.views.listitem;

import java.io.Serializable;
import java.util.Calendar;

public class BirthdayListViewItem implements Serializable {

	private static final long serialVersionUID = -1602786243668599596L;

	@SuppressWarnings("unused")
	private static final String TAG = BirthdayListViewItem.class.getName();

	private int _imageResource;
	private String _name;
	private Calendar _birthday;

	public BirthdayListViewItem(int imageResource, String name, Calendar birthday) {
		_imageResource = imageResource;
		_name = name;
		_birthday = birthday;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetName() {
		return _name;
	}

	public String GetBirthday() {
		String day = String.valueOf(_birthday.get(Calendar.DAY_OF_MONTH));
		while (day.length() < 2) {
			day = "0" + day;
		}

		String month = "";
		switch (_birthday.get(Calendar.MONTH)) {
		case Calendar.JANUARY:
			month = "January";
			break;
		case Calendar.FEBRUARY:
			month = "February";
			break;
		case Calendar.MARCH:
			month = "March";
			break;
		case Calendar.APRIL:
			month = "April";
			break;
		case Calendar.MAY:
			month = "May";
			break;
		case Calendar.JUNE:
			month = "June";
			break;
		case Calendar.JULY:
			month = "July";
			break;
		case Calendar.AUGUST:
			month = "August";
			break;
		case Calendar.SEPTEMBER:
			month = "September";
			break;
		case Calendar.OCTOBER:
			month = "October";
			break;
		case Calendar.NOVEMBER:
			month = "November";
			break;
		case Calendar.DECEMBER:
			month = "December";
			break;
		default:
			month = "NULL";
			break;
		}

		return day + "." + month + " " + String.valueOf(_birthday.get(Calendar.YEAR));
	}

	public Calendar GetCalendar() {
		return _birthday;
	}

	public int GetAge() {
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.MONTH) > _birthday.get(Calendar.MONTH)) {
			return now.get(Calendar.YEAR) - _birthday.get(Calendar.YEAR);
		} else if (now.get(Calendar.MONTH) == _birthday.get(Calendar.MONTH)) {
			if (now.get(Calendar.DAY_OF_MONTH) >= _birthday.get(Calendar.DAY_OF_MONTH)) {
				return now.get(Calendar.YEAR) - _birthday.get(Calendar.YEAR);
			} else {
				return now.get(Calendar.YEAR) - _birthday.get(Calendar.YEAR) - 1;
			}
		} else {
			return now.get(Calendar.YEAR) - _birthday.get(Calendar.YEAR) - 1;
		}
	}

	public boolean HasBirthday() {
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.DAY_OF_MONTH) == _birthday.get(Calendar.DAY_OF_MONTH)
				&& now.get(Calendar.MONTH) == _birthday.get(Calendar.MONTH)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "_imageResource: " + String.valueOf(_imageResource) + " _name: " + String.valueOf(_name) + " _birthday: "
				+ _birthday.toString();
	}
}
