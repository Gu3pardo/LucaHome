package guepardoapps.common.classes;

import java.io.Serializable;
import java.sql.Time;
import java.util.Locale;

import guepardoapps.common.Constants;
import guepardoapps.common.converter.BooleanToScheduleStateConverter;
import guepardoapps.common.enums.Weekday;

public class Schedule implements Serializable {

	private static final long serialVersionUID = 7735669237381408318L;

	@SuppressWarnings("unused")
	private static String TAG = "Schedule";

	protected String _name;
	protected WirelessSocket _socket;
	protected Weekday _weekday;
	protected Time _time;
	protected boolean _action;
	protected boolean _isTimer;
	protected boolean _isActive;

	private String _setBroadcastReceiverString;
	protected String _deleteBroadcastReceiverString;

	public Schedule(String name, WirelessSocket socket, Weekday weekday, Time time, boolean action, boolean isTimer,
			boolean isActive) {
		_name = name;
		_socket = socket;
		_weekday = weekday;
		_time = time;
		_action = action;
		_isTimer = isTimer;
		_isActive = isActive;

		_setBroadcastReceiverString = Constants.BROADCAST_SET_SCHEDULE + _name.toUpperCase(Locale.GERMAN);
		_deleteBroadcastReceiverString = Constants.BROADCAST_DELETE_SCHEDULE + _name.toUpperCase(Locale.GERMAN);
	}

	public String GetName() {
		return _name;
	}

	public WirelessSocket GetSocket() {
		return _socket;
	}

	public Weekday GetWeekday() {
		return _weekday;
	}

	public Time GetTime() {
		return _time;
	}

	public boolean GetAction() {
		return _action;
	}

	public boolean GetIsTimer() {
		return _isTimer;
	}

	public boolean GetIsActive() {
		return _isActive;
	}

	public String GetIsActiveString() {
		return BooleanToScheduleStateConverter.GetStringOfBoolean(_isActive);
	}

	public String GetSetBroadcast() {
		return _setBroadcastReceiverString;
	}

	public String GetDeleteBroadcast() {
		return _deleteBroadcastReceiverString;
	}

	public String GetCommandSet(boolean newState) {
		return Constants.ACTION_SET_SCHEDULE + _name + ((newState) ? Constants.STATE_ON : Constants.STATE_OFF);
	}

	@SuppressWarnings("deprecation")
	public String GetCommandAdd() {
		return Constants.ACTION_ADD_SCHEDULE + _name + "&socket=" + _socket.GetName() + "&gpio=" + "" + "&weekday="
				+ String.valueOf(_weekday.GetInt()) + "&hour=" + String.valueOf(_time.getHours()) + "&minute="
				+ String.valueOf(_time.getMinutes()) + "&onoff=" + String.valueOf(_isActive) + "&isTimer="
				+ String.valueOf(_isTimer);
	}

	public String GetCommandDelete() {
		return Constants.ACTION_DELETE_SCHEDULE + _name;
	}

	public String toString() {
		return "{Schedule: {Name: " + _name + "};{WirelessSocket: " + _socket.toString() + "};{Weekday: "
				+ _weekday.toString() + "};{Time: " + _time.toString() + "};{Action: " + String.valueOf(_action)
				+ "};{isTimer: " + String.valueOf(_isTimer) + "};{IsActive: "
				+ BooleanToScheduleStateConverter.GetStringOfBoolean(_isActive) + "};{SetBroadcastReceiverString: "
				+ _setBroadcastReceiverString + "};{DeleteBroadcastReceiverString: " + _deleteBroadcastReceiverString
				+ "}}";
	}
}