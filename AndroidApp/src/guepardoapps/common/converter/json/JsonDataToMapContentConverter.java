package guepardoapps.common.converter.json;

import java.util.ArrayList;

import android.graphics.Point;

import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.*;
import guepardoapps.common.enums.DrawingType;

public final class JsonDataToMapContentConverter {

	private static String TAG = "JsonDataToMapContentConverter";
	private static String _searchParameter = "{mapcontent";
	private static Logger _logger;
	private static Point _size;

	public static SerializableList<MapContent> GetList(String[] stringArray, Point size) {
		_size = size;
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry);
		}
	}

	public static MapContent Get(String value, Point size) {
		_size = size;
		if (!value.contains("Error")) {
			if (Tools.GetStringCount(value, _searchParameter) == 1) {
				if (value.contains(_searchParameter)) {
					value = value.replace(_searchParameter, "").replace("};};", "");
					String[] data = value.split("\\};");
					MapContent newValue = ParseStringToValue(data);
					if (newValue != null) {
						return newValue;
					}
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static SerializableList<MapContent> ParseStringToList(String value) {
		if (!value.contains("Error")) {
			if (Tools.GetStringCount(value, _searchParameter) > 1) {
				if (value.contains(_searchParameter)) {
					SerializableList<MapContent> list = new SerializableList<MapContent>();

					String[] entries = value.split("\\" + _searchParameter);
					for (String entry : entries) {
						entry = entry.replace(_searchParameter, "").replace("};};", "").replace(":};", "");

						String[] data = entry.split("\\};");
						MapContent newValue = ParseStringToValue(data);
						if (newValue != null) {
							list.addValue(newValue);
						}
					}

					return list;
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static MapContent ParseStringToValue(String[] data) {
		if (_logger == null) {
			_logger = new Logger(TAG);
		}

		if (data.length == 6) {
			if (data[0].contains("{id:") && data[1].contains("{position:") && data[2].contains("{type:")
					&& data[3].contains("{schedules:") && data[4].contains("{sockets:")
					&& data[5].contains("{temperatureArea:")) {
				String idString = data[0].replace("{id:", "").replace("};", "");
				int id = Integer.parseInt(idString);

				String positionString = data[1].replace("{position:", "").replace("};", "");
				String[] coordinates = positionString.split("\\|");
				int x = -1;
				int y = -1;
				if (_size != null) {
					x = Integer.parseInt(coordinates[0]) * (_size.x / 100);
					y = Integer.parseInt(coordinates[1]) * (_size.y / 100);
				} else {
					_logger.Warn("_size is null!");
				}
				Point position = new Point();
				position.set(x, y);

				String typeString = data[2].replace("{type:", "").replace("};", "");
				int typeInteger = Integer.parseInt(typeString);
				DrawingType drawingType = DrawingType.GetById(typeInteger);

				ArrayList<String> scheduleList = new ArrayList<String>();
				String scheduleString = data[3].replace("{schedules:", "").replace("};", "");
				String[] schedulesArray = scheduleString.split("\\|");
				for (String entry : schedulesArray) {
					if (entry.length() > 0) {
						scheduleList.add(entry);
					}
				}

				ArrayList<String> socketList = new ArrayList<String>();
				String socketString = data[4].replace("{sockets:", "").replace("};", "");
				String[] socketsArray = socketString.split("\\|");
				for (String entry : socketsArray) {
					if (entry.length() > 0) {
						socketList.add(entry);
					}
				}

				String temperatureString = data[5].replace("{temperatureArea:", "").replace("};", "");

				MapContent newValue = new MapContent(id, position, drawingType, scheduleList, socketList,
						temperatureString);

				_logger.Debug("newValue: " + newValue.toString());

				return newValue;
			}
		}

		_logger.Error("Data has an error!");

		return null;
	}
}