package guepardoapps.lucahome.wearcontrol.enums;

import java.io.Serializable;

import guepardoapps.lucahome.wearcontrol.views.*;

public enum TargetActivity implements Serializable {
	
	SOCKET("Sockets", SocketView.class), 
	SCHEDULE("Schedules", ScheduleView.class), 
	MOVIES("Movies", MovieView.class), 
	BIRTHDAYS("Birthdays", BirthdayView.class);

	private String _name;
	private Class<?> _activity;

	private TargetActivity(String name, Class<?> activity) {
		_name = name;
		_activity = activity;
	}

	public String GetName() {
		return _name;
	}

	public Class<?> GetActivity() {
		return _activity;
	}

	public static TargetActivity GetByString(String value) {
		for (TargetActivity e : values()) {
			if (e._name.contains(value)) {
				return e;
			}
		}
		return null;
	}
}
