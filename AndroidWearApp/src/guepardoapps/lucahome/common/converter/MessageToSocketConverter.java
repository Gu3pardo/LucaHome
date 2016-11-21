package guepardoapps.lucahome.common.converter;

import java.util.ArrayList;
import java.util.List;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;

public class MessageToSocketConverter {

	private static final String TAG = MessageToSocketConverter.class.getName();
	private Logger _logger;

	private static final String SOCKETS = "Sockets:";

	public MessageToSocketConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public List<SocketListViewItem> ConvertMessageToSocketList(String message) {
		if (message.startsWith(SOCKETS)) {
			_logger.Debug("message starts with " + SOCKETS + "! replacing!");
			message = message.replace(SOCKETS, "");
		}
		_logger.Debug("message: " + message);

		String[] items = message.split("\\&");
		if (items.length > 0) {
			List<SocketListViewItem> list = new ArrayList<SocketListViewItem>();
			for (String entry : items) {
				String[] data = entry.split("\\:");

				_logger.Info("Socket dataContent");
				for (String dataContent : data) {
					_logger.Info(dataContent);
				}

				if (data.length == 2) {
					if (data[0] != null && data[1] != null) {
						SocketListViewItem item = new SocketListViewItem(R.drawable.socket, data[0],
								(data[1].contains("1")));
						list.add(item);
					} else {
						_logger.Warn("data[0] or data[1] is null!");
					}
				} else {
					_logger.Warn("Wrong size of socketData: " + String.valueOf(data.length));
				}
			}
			return list;
		}

		_logger.Warn("Found no socket!");
		return null;
	}
}
