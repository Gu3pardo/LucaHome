package guepardoapps.common.classes;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Point;
import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.enums.DrawingType;

public class MapContent implements Serializable {

	private static final long serialVersionUID = 8764451572750126391L;

	private static String TAG = "MapContent";
	@SuppressWarnings("unused")
	private Logger _logger;

	private int _id;
	private Point _position;
	private DrawingType _drawingType;

	private ArrayList<String> _schedules;
	private ArrayList<String> _sockets;
	private String _temperatureArea;

	public MapContent(int id, Point position, DrawingType drawingType, ArrayList<String> schedules,
			ArrayList<String> sockets, String temperatureArea) {
		_logger = new Logger(TAG);

		_id = id;
		_position = position;
		_drawingType = drawingType;

		_schedules = schedules;
		_sockets = sockets;
		_temperatureArea = temperatureArea;
	}

	public int GetId() {
		return _id;
	}

	public Point GetPosition() {
		return _position;
	}

	public DrawingType GetDrawingType() {
		return _drawingType;
	}

	public ArrayList<String> GetSchedules() {
		return _schedules;
	}

	public ArrayList<String> GetSockets() {
		return _sockets;
	}

	public String GetTemperatureArea() {
		return _temperatureArea;
	}

	private String getSchedulesString() {
		String string = "";
		if (_schedules != null) {
			for (String entry : _schedules) {
				string += entry + "|";
			}
		}
		return string;
	}

	private String getSocketsString() {
		String string = "";
		if (_sockets != null) {
			for (String entry : _sockets) {
				string += entry + "|";
			}
		}
		return string;
	}

	public String GetCommandAdd() {
		return Constants.ACTION_ADD_MAP_CONTENT + String.valueOf(_id) + "&position=" + String.valueOf(_position.x) + "|"
				+ String.valueOf(_position.y) + "&type=" + String.valueOf(_drawingType.GetId()) + "&schedules="
				+ getSchedulesString() + "&sockets=" + getSocketsString() + "&temperature=" + _temperatureArea;
	}

	public String GetCommandUpdate() {
		return Constants.ACTION_UPDATE_MAP_CONTENT + String.valueOf(_id) + "&position=" + String.valueOf(_position.x)
				+ "|" + String.valueOf(_position.y) + "&type=" + String.valueOf(_drawingType.GetId()) + "&schedules="
				+ getSchedulesString() + "&sockets=" + getSocketsString() + "&temperature=" + _temperatureArea;
	}

	public String GetCommandDelete() {
		return Constants.ACTION_DELETE_MAP_CONTENT + String.valueOf(_id);
	}

	@Override
	public String toString() {
		return "MapContent: {Id: " + String.valueOf(_id) + "}{Position: " + _position.toString() + "}{DrawingType: "
				+ _drawingType.toString() + "}{Schedules: " + getSchedulesString() + "}{Sockets: " + getSocketsString()
				+ "}{Temperature: " + _temperatureArea + "}";
	}
}
