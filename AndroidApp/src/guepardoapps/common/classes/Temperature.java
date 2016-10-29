package guepardoapps.common.classes;

import java.io.Serializable;
import java.sql.Time;

import guepardoapps.common.enums.TemperatureType;

public class Temperature implements Serializable {

	private static final long serialVersionUID = -9073455030083339156L;

	private double _temperature;
	private String _area;
	private Time _lastUpdate;
	private String _sensorPath;
	private TemperatureType _temperatureType;
	private String _graphPath;

	public Temperature(double temperature, String area, Time lastUpdate, String sensorPath,
			TemperatureType temperatureType, String graphPath) {
		_temperature = temperature;
		_area = area;
		_lastUpdate = lastUpdate;
		_sensorPath = sensorPath;
		_temperatureType = temperatureType;
		_graphPath = graphPath;
	}

	public double GetTemperatureValue() {
		return _temperature;
	}

	public void SetTemperature(double temperature) {
		_temperature = temperature;
	}

	public String GetTemperatureString() {
		return String.valueOf(_temperature) + "�C";
	}

	public String GetArea() {
		return _area;
	}

	public Time GetLastUpdate() {
		return _lastUpdate;
	}

	public void SetLastUpdate(Time lastUpdate) {
		_lastUpdate = lastUpdate;
	}

	public String GetSensorPath() {
		return _sensorPath;
	}

	public TemperatureType GetTemperatureType() {
		return _temperatureType;
	}

	public String GetGraphPath() {
		return _graphPath;
	}

	public String toString() {
		return "{Temperature: {Value: " + GetTemperatureString() + "};{Area: " + _area + "};{LastUpdate: "
				+ _lastUpdate.toString() + "};{SensorPath: " + _sensorPath + "};{TemperatureType: "
				+ _temperatureType.toString() + "};{GraphPath: " + _graphPath + "}}";
	}
}
