package guepardoapps.lucahome.accesscontrol.common.enums;

import java.io.Serializable;

public enum AlarmState implements Serializable {

	NULL(0, ""), 
	ACCESS_CONTROL_ACTIVE(1, "ACCESS_CONTROL_ACTIVE"), 
	REQUEST_CODE(2, "REQUEST_CODE"), 
	ACCESS_SUCCESSFUL(3, "ACCESS_SUCCESSFUL"), 
	ACCESS_FAILED(4, "ACCESS_FAILED"), 
	ALARM_ACTIVE(5, "ALARM_ACTIVE");

	private int _id;
	private String _action;

	private AlarmState(int id, String action) {
		_id = id;
		_action = action;
	}

	public int GetId() {
		return _id;
	}

	@Override
	public String toString() {
		return _action;
	}

	public static AlarmState GetById(int id) {
		for (AlarmState e : values()) {
			if (e._id == id) {
				return e;
			}
		}
		return null;
	}

	public static AlarmState GetByString(String action) {
		for (AlarmState e : values()) {
			if (e._action.contains(action)) {
				return e;
			}
		}
		return null;
	}
}
