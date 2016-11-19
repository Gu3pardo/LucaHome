package guepardoapps.lucahome.watchface.common.converter;

import java.util.ArrayList;
import java.util.List;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;

import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;

public class MessageToMovieConverter {

	private static final String TAG = MessageToMovieConverter.class.getName();
	private Logger _logger;

	private static final String MOVIES = "Movies:";

	public MessageToMovieConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public List<MovieListViewItem> ConvertMessageToMovieList(String message) {
		if (message.startsWith(MOVIES)) {
			_logger.Debug("message starts with " + MOVIES + "! replacing!");
			message = message.replace(MOVIES, "");
		}
		_logger.Debug("message: " + message);

		String[] items = message.split("\\&");
		if (items.length > 0) {
			List<MovieListViewItem> list = new ArrayList<MovieListViewItem>();

			_logger.Info("Movie list");

			for (String entry : items) {
				if (entry != null) {
					_logger.Info(entry);

					MovieListViewItem item = new MovieListViewItem(R.drawable.movie, entry);
					list.add(item);
				} else {
					_logger.Warn("entry is null!");
				}
			}
			return list;
		}

		_logger.Warn("Found no schedule!");
		return null;
	}
}
