package guepardoapps.common.enums;

import java.io.Serializable;

public enum RaspberrySelection implements Serializable {
	BOTH("Both", 0), 
	RASPBERRY_1("Raspberry_1", 1), 
	RASPBERRY_2("Raspberry_2", 2), 
	DUMMY("Dummy", -1);

	private String _string;
	private int _int;

	private RaspberrySelection(String stringValue, int intValue) {
		_string = stringValue;
		_int = intValue;
	}

	@Override
	public String toString() {
		return _string;
	}

	public int GetInt() {
		return _int;
	}

	public static RaspberrySelection GetById(int intValue) {
		for (RaspberrySelection e : values()) {
			if (e._int == intValue) {
				return e;
			}
		}
		return null;
	}

	public static RaspberrySelection GetByString(String string) {
		for (RaspberrySelection e : values()) {
			if (e._string.contains(string)) {
				return e;
			}
		}
		return null;
	}
}
