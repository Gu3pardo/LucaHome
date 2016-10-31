package guepardoapps.common.converter.json;

import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Movie;

public final class JsonDataToMovieConverter {

	private static String TAG = "JsonDataToMovieConverter";
	private static String _searchParameter = "{movie:";
	private static Logger _logger;

	public static SerializableList<Movie> GetList(String[] stringArray) {
		if (Tools.StringsAreEqual(stringArray)) {
			return ParseStringToList(stringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(stringArray, _searchParameter);
			return ParseStringToList(usedEntry);
		}
	}

	public static Movie Get(String value) {
		if (Tools.GetStringCount(value, _searchParameter) == 1) {
			if (value.contains(_searchParameter)) {
				value = value.replace(_searchParameter, "").replace("};};", "");

				String[] data = value.split("\\};");
				Movie newValue = ParseStringToValue(data);
				if (newValue != null) {
					return newValue;
				}
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static SerializableList<Movie> ParseStringToList(String value) {
		if (Tools.GetStringCount(value, _searchParameter) > 1) {
			if (value.contains(_searchParameter)) {
				SerializableList<Movie> list = new SerializableList<Movie>();

				String[] entries = value.split("\\" + _searchParameter);
				for (String entry : entries) {
					entry = entry.replace(_searchParameter, "").replace("};};", "");
					
					String[] data = entry.split("\\};");
					Movie newValue = ParseStringToValue(data);
					if (newValue != null) {
						list.addValue(newValue);
					}
				}
				return list;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error(value + " has an error!");

		return null;
	}

	private static Movie ParseStringToValue(String[] data) {
		if (data.length == 6) {
			if (data[0].contains("{Title:") 
					&& data[1].contains("{Genre:") 
					&& data[2].contains("{Description:")
					&& data[3].contains("{Rating:") 
					&& data[4].contains("{Watched:") 
					&& data[5].contains("{Sockets:")) {
				
				String Title = data[0].replace("{Title:", "").replace("};", "");
				String Genre = data[1].replace("{Genre:", "").replace("};", "");
				String Description = data[2].replace("{Description:", "").replace("};", "");

				String RatingString = data[3].replace("{Rating:", "").replace("};", "");
				int rating = Integer.parseInt(RatingString);

				String WatchedString = data[4].replace("{Watched:", "").replace("};", "");
				int watched = Integer.parseInt(WatchedString);

				String SocketString = data[5].replace("{Sockets:", "").replace("};", "");
				String[] sockets = SocketString.split("\\|");

				Movie newValue = new Movie(Title, Genre, Description, rating, watched, sockets);
				return newValue;
			}
		}

		if (_logger == null) {
			_logger = new Logger(TAG);
		}
		_logger.Error("Data has an error!");

		return null;
	}
}