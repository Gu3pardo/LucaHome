package guepardoapps.common.classes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import guepardoapps.common.Constants;

public class Birthday implements Serializable {

	private static final long serialVersionUID = -7512285853029358631L;

	@SuppressWarnings("unused")
	private static String TAG = "Birthday";

	private String _name;
	private Calendar _birthday;

	private int _id;
	private int _notificationId;
	private boolean _notifyMe;

	private String _deleteBroadcastReceiverString;
	private String _hasBirthdayBroadcastReceiverString;

	public Birthday(String name, Calendar birthday, int id) {
		_name = name;
		_birthday = birthday;

		_id = id;
		_notificationId = Constants.ID_NOTIFICATION_BIRTHDAY + _id;
		_notifyMe = true;

		_deleteBroadcastReceiverString = Constants.BROADCAST_DELETE_BIRTHDAY + _name.toUpperCase(Locale.GERMAN).trim();
		_hasBirthdayBroadcastReceiverString = Constants.BROADCAST_HAS_BIRTHDAY
				+ _name.toUpperCase(Locale.GERMAN).trim();
	}

	public String GetName() {
		return _name;
	}

	public Calendar GetBirthday() {
		return _birthday;
	}

	public String GetBirthdayString() {
		String birthdayString = "";
		birthdayString += String.valueOf(_birthday.get(Calendar.DAY_OF_MONTH)) + "."
				+ String.valueOf(_birthday.get(Calendar.MONTH) + 1) + "."
				+ String.valueOf(_birthday.get(Calendar.YEAR));
		return birthdayString;
	}

	public int GetId() {
		return _id;
	}

	public int GetNotificationId() {
		return _notificationId;
	}

	public boolean GetNotifyMe() {
		return _notifyMe;
	}

	public String GetDeleteBroadcast() {
		return _deleteBroadcastReceiverString;
	}

	public String GetHasBirthdayBroadcast() {
		return _hasBirthdayBroadcastReceiverString;
	}

	public String GetCommandAdd() {
		return Constants.ACTION_ADD_BIRTHDAY + String.valueOf(_id) + "&name=" + _name + "&day="
				+ String.valueOf(_birthday.get(Calendar.DAY_OF_MONTH)) + "&month="
				+ String.valueOf(_birthday.get(Calendar.MONTH) + 1) + "&year="
				+ String.valueOf(_birthday.get(Calendar.YEAR));
	}

	public String GetCommandUpdate() {
		return Constants.ACTION_UPDATE_BIRTHDAY + String.valueOf(_id) + "&name=" + _name + "&day="
				+ String.valueOf(_birthday.get(Calendar.DAY_OF_MONTH)) + "&month="
				+ String.valueOf(_birthday.get(Calendar.MONTH) + 1) + "&year="
				+ String.valueOf(_birthday.get(Calendar.YEAR));
	}

	public String GetCommandDelete() {
		return Constants.ACTION_DELETE_BIRTHDAY + String.valueOf(_id);
	}

	public String toString() {
		return "{Birthday: {Name: " + _name + "};{Birthday: " + _birthday.toString() + "};{NotifyMe: "
				+ String.valueOf(_notifyMe) + "};{Id: " + String.valueOf(_id) + "};{NotificationId: "
				+ String.valueOf(_notificationId) + "};{DeleteBroadcastReceiverString: "
				+ _deleteBroadcastReceiverString + "};{HasBirthdayBroadcastReceiverString: "
				+ _hasBirthdayBroadcastReceiverString + "}}";
	}
}
