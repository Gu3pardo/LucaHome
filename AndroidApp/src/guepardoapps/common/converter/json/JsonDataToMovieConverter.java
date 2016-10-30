package guepardoapps.common.converter.json;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Movie;

public final class JsonDataToMovieConverter {

	private static String TAG = "JsonDataToMovieConverter";
	private static Logger _logger;

	public static Movie Get(String restString) {
		if (Tools.GetStringCount(restString, "{movie:") == 1) {
			if (restString.contains("{movie:")) {
				restString = restString.replace("{movie:", "").replace("};};", "");

				String[] data = restString.split("\\};");
				if (data.length == 6) {
					if (data[0].contains("{Title:") && data[1].contains("{Genre:") && data[2].contains("{Description:")
							&& data[3].contains("{Rating:") && data[4].contains("{Watched:")
							&& data[5].contains("{Sockets:")) {
						Movie newValue = ParseStringToMovie(data);
						return newValue;
					}
				}
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");

		return null;
	}

	public static SerializableList<Movie> GetList(String[] restStringArray) {
		if (Tools.StringsAreEqual(restStringArray)) {
			return ParseStringToList(restStringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(restStringArray, "{movie:");
			return ParseStringToList(usedEntry);
		}
	}

	private static SerializableList<Movie> ParseStringToList(String restString) {
		if (Tools.GetStringCount(restString, "{movie:") > 1) {
			if (restString.contains("{movie:")) {
				SerializableList<Movie> list = new SerializableList<Movie>();

				String[] entries = restString.split("\\{movie:");
				for (String entry : entries) {
					String[] data = entry.split("\\};");
					if (data.length == 6) {
						if (data[0].contains("{Title:") && data[1].contains("{Genre:")
								&& data[2].contains("{Description:") && data[3].contains("{Rating:")
								&& data[4].contains("{Watched:") && data[5].contains("{Sockets:")) {
							Movie newValue = ParseStringToMovie(data);
							list.addValue(newValue);
						}
					}
				}
				return list;
			}
		}

		_logger = new Logger(TAG);
		_logger.Error(restString + " has an error!");

		return null;
	}

	private static Movie ParseStringToMovie(String[] data) {
		if (data.length != 6) {
			_logger = new Logger(TAG);
			_logger.Error("data.length != 6 || Length: " + String.valueOf(data.length));

			return null;
		}

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