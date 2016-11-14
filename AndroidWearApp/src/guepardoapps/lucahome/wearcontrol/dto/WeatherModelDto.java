package guepardoapps.lucahome.wearcontrol.dto;

import java.io.Serializable;

public class WeatherModelDto implements Serializable {

	private static final long serialVersionUID = -1107704056644507545L;

	private String _description;
	private String _temperature;
	private String _lastUpdate;

	public WeatherModelDto(String description, String temperature, String lastUpdate) {
		_description = description;
		_temperature = temperature;
		_lastUpdate = lastUpdate;
	}

	public String GetDescription() {
		return _description;
	}

	public String GetTemperature() {
		return _temperature;
	}

	public String GetLastUpdate() {
		return _lastUpdate;
	}

	public String GetWatchFaceText() {
		return _description + " " + _temperature + ", " + _lastUpdate.substring(0, _lastUpdate.length() - 3);
	}

	@Override
	public String toString() {
		return "[WeatherModel: Description: " + _description + "; Temperature: " + _temperature + "; LastUpdate: "
				+ _lastUpdate + "]";
	}
}