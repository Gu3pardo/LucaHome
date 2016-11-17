package guepardoapps.lucahome.watchface.dto;

import java.io.Serializable;

public class TemperatureDto implements Serializable {

	private static final long serialVersionUID = 415036116852931544L;

	private String _temperature;
	private String _area;
	private String _lastUpdate;

	public TemperatureDto(String temperature, String area, String lastUpdate) {
		_temperature = temperature;
		_area = area;
		_lastUpdate = lastUpdate;
	}

	public String GetTemperature() {
		return _temperature;
	}

	public String GetArea() {
		return _area;
	}

	public String GetLastUpdate() {
		return _lastUpdate;
	}

	public String GetWatchFaceText() {
		return _area + ": " + _temperature + ", " + _lastUpdate.substring(0, _lastUpdate.length() - 3);
	}

	public String toString() {
		return "{Temperature: {Value: " + _temperature + "};{Area: " + _area + "};{LastUpdate: " + _lastUpdate + "}}";
	}
}
