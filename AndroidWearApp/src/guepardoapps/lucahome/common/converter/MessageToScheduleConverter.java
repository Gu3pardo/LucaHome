package guepardoapps.lucahome.common.converter;

import java.util.ArrayList;
import java.util.List;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;

public class MessageToScheduleConverter {

	private static final String TAG = MessageToScheduleConverter.class.getName();
	private Logger _logger;

	private static final String SCHEDULES = "Schedules:";

	public MessageToScheduleConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public List<ScheduleListViewItem> ConvertMessageToScheduleList(String message) {
		if (message.startsWith(SCHEDULES)) {
			_logger.Debug("message starts with " + SCHEDULES + "! replacing!");
			message = message.replace(SCHEDULES, "");
		}
		_logger.Debug("message: " + message);

		String[] items = message.split("\\&");
		if (items.length > 0) {
			List<ScheduleListViewItem> list = new ArrayList<ScheduleListViewItem>();
			for (String entry : items) {
				String[] data = entry.split("\\:");

				_logger.Info("Schedule dataContent");
				for (String dataContent : data) {
					_logger.Info(dataContent);
				}

				if (data.length == 5) {
					if (data[0] != null && data[1] != null && data[2] != null && data[3] != null && data[4] != null) {
						ScheduleListViewItem item = new ScheduleListViewItem(R.drawable.scheduler, data[0],
								data[1] + ":" + data[2] + ":" + data[3], (data[4].contains("1")));
						list.add(item);
					} else {
						_logger.Warn("data[0] or data[1] or data[2] or data[3] or data[4] is null!");
					}
				} else {
					_logger.Warn("Wrong size of scheduleData: " + String.valueOf(data.length));
				}
			}
			return list;
		}

		_logger.Warn("Found no schedule!");
		return null;
	}
}
