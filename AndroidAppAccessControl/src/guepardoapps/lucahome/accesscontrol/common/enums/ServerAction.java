package guepardoapps.lucahome.accesscontrol.common.enums;

import java.io.Serializable;

public enum ServerAction implements Serializable {

	NULL(0, ""), 
	REQUEST_CODE(1, "REQUEST_CODE"), 
	LOGIN_FAILED(2, "LOGIN_FAILED"), 
	LOGIN_SUCCESS(3, "LOGIN_SUCCESS"), 
	ALARM_ACTIVE(4, "ALARM_ACTIVE"), 
	ACTIVATE_ACCESS_CONTROL(5, "ACTIVATE_ACCESS_CONTROL");

	private int _id;
	private String _action;

	private ServerAction(int id, String action) {
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

	public static ServerAction GetById(int id) {
		for (ServerAction e : values()) {
			if (e._id == id) {
				return e;
			}
		}
		return null;
	}

	public static ServerAction GetByString(String action) {
		for (ServerAction e : values()) {
			if (e._action.contains(action)) {
				return e;
			}
		}
		return null;
	}
}
