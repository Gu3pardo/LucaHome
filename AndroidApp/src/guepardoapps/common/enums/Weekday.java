package guepardoapps.common.enums;

import java.io.Serializable;

public enum Weekday implements Serializable{
	
	SU("Su", 0, "Sunday", "Sonntag"), 
	MO("Mo", 1, "Monday", "Montag"), 
	TU("Tu", 2, "Tuesday", "Dienstag"), 
	WE("We", 3, "Wednesday", "Mittwoch"), 
	TH("Th", 4, "Thursday", "Donnerstag"), 
	FR("Fr", 5, "Friday", "Freitag"), 
	SA("Sa", 6, "Saturday", "Samstag"), 
	DUMMY("Du", 99, "Dummy", "Dummy");

	private String _shortStringDay;
	private int _intDay;
	private String _englishStringDay;
	private String _germanStringDay;

	private Weekday(String shortStringDay, int intDay, String englishStringDay, String germanStringDay) {
		_shortStringDay = shortStringDay;
		_intDay = intDay;
		_englishStringDay = englishStringDay;
		_germanStringDay = germanStringDay;
	}

	@Override
	public String toString() {
		return _shortStringDay;
	}

	public int GetInt() {
		return _intDay;
	}

	public String GetEnglishDay() {
		return _englishStringDay;
	}

	public String GetGermanDay() {
		return _germanStringDay;
	}

	public static Weekday GetById(int day) {
		for (Weekday e : values()) {
			if (e._intDay == day) {
				return e;
			}
		}
		return null;
	}

	public static Weekday GetByEnglishString(String day) {
		for (Weekday e : values()) {
			if (e._englishStringDay.contains(day)) {
				return e;
			}
		}
		return null;
	}

	public static Weekday GetByGermanString(String day) {
		for (Weekday e : values()) {
			if (e._germanStringDay.contains(day)) {
				return e;
			}
		}
		return null;
	}
}
