package guepardoapps.lucahome.common.converter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;

public class MessageToBirthdayConverter {

	private static final String TAG = MessageToBirthdayConverter.class.getName();
	private Logger _logger;

	private static final String BIRTHDAYS = "Birthdays:";

	public MessageToBirthdayConverter() {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
	}

	public List<BirthdayListViewItem> ConvertMessageToBirthdayList(String message) {
		if (message.startsWith(BIRTHDAYS)) {
			_logger.Debug("message starts with " + BIRTHDAYS + "! replacing!");
			message = message.replace(BIRTHDAYS, "");
		}
		_logger.Debug("message: " + message);

		String[] items = message.split("\\&");
		if (items.length > 0) {
			List<BirthdayListViewItem> list = new ArrayList<BirthdayListViewItem>();
			for (String entry : items) {
				String[] data = entry.split("\\:");

				_logger.Info("Birthday dataContent");
				for (String dataContent : data) {
					_logger.Info(dataContent);
				}

				if (data.length == 4) {
					if (data[0] != null && data[1] != null && data[2] != null && data[3] != null) {
						Calendar birthday = Calendar.getInstance();
						birthday.set(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
						BirthdayListViewItem item = new BirthdayListViewItem(R.drawable.birthday, data[0], birthday);
						list.add(item);
					} else {
						_logger.Warn("data[0] or data[1] or data[2] or data[3] is null!");
					}
				} else {
					_logger.Warn("Wrong size of birthdayData: " + String.valueOf(data.length));
				}
			}
			return list;
		}

		_logger.Warn("Found no birthday!");
		return null;
	}
}
