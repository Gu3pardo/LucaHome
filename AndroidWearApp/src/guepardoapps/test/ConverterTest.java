package guepardoapps.test;

import java.util.List;

import android.content.Context;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.converter.*;

import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.BroadcastController;

@SuppressWarnings("unused")
public class ConverterTest {

	private static final String TAG = ConverterTest.class.getName();
	private Logger _logger;

	private Context _context;
	private BroadcastController _broadcastController;

	private static final String CURRENT_WEATHER = "CurrentWeather:";
	private static final String PHONE_BATTERY = "PhoneBattery:";
	private static final String RASPBERRY_TEMPERATURE = "RaspberryTemperature:";

	private static final String BIRTHDAYS = "Birthdays:";
	private static final String SCHEDULES = "Schedules:";
	private static final String SOCKETS = "Sockets:";

	private MessageToBirthdayConverter _messageToBirthdayConverter;
	private MessageToRaspberryConverter _messageToRaspberryConverter;
	private MessageToScheduleConverter _messageToScheduleConverter;
	private MessageToSocketConverter _messageToSocketConverter;
	private MessageToWeatherConverter _messageToWeatherConverter;

	public ConverterTest(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_broadcastController = new BroadcastController(_context);

		_messageToBirthdayConverter = new MessageToBirthdayConverter();
		_messageToRaspberryConverter = new MessageToRaspberryConverter();
		_messageToScheduleConverter = new MessageToScheduleConverter();
		_messageToSocketConverter = new MessageToSocketConverter();
		_messageToWeatherConverter = new MessageToWeatherConverter();
	}

	public void PerformTests() {
		if (!Constants.TESTING_ENABLED) {
			_logger.Info("Testing disabled!");
			return;
		}

		try {
			testBirthdayConverter();
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
		try {
			testRaspberryConverter();
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
		try {
			testScheduleConverter();
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
		try {
			testSocketConverter();
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
		try {
			testWeatherConverter();
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
	}

	private void testBirthdayConverter() {
		_logger.Info("testBirthdayConverter");

		String testString = BIRTHDAYS + "Jonas Schubert:1990:0:2&" + "Sandra Huber:1988:1:12&"
				+ "Marina Heinel:1993:11:11&";
		List<BirthdayListViewItem> testList = _messageToBirthdayConverter.ConvertMessageToBirthdayList(testString);

		if (testList != null) {
			if (testList.size() == 3) {
				_logger.Info("testBirthdayConverter Size is correct! " + String.valueOf(testList.size()));
			} else {
				_logger.Error("testBirthdayConverter Size has wrong value: " + String.valueOf(testList.size()));
			}
			for (BirthdayListViewItem entry : testList) {
				if (entry != null) {
					_logger.Info(entry.toString());
				} else {
					_logger.Error("testBirthdayConverter Entry is null!");
				}
			}
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_BIRTHDAY_LIST,
					Constants.BUNDLE_BIRTHDAY_LIST, testList);
			_logger.Info("Sent broadcast...");
		} else {
			_logger.Error("testBirthdayConverter list is null!");
		}
	}

	private void testRaspberryConverter() {
		_logger.Info("testRaspberryConverter");
		// TODO Auto-generated method stub

	}

	private void testScheduleConverter() {
		_logger.Info("testScheduleConverter");

		String testString = SCHEDULES + "LightOn_Mo:MO, 06:30:00 set Light to true:1&"
				+ "LightOff_Mo:MO, 06:40:00 set Light to true:0&" + "LightOn_Tu:TU, 06:30:00 set Light to true:1&"
				+ "LightOff_Tu:TU, 06:40:00 set Light to true:0&";
		List<ScheduleListViewItem> testList = _messageToScheduleConverter.ConvertMessageToScheduleList(testString);

		if (testList != null) {
			if (testList.size() == 4) {
				_logger.Info("testScheduleConverter Size is correct! " + String.valueOf(testList.size()));
			} else {
				_logger.Error("testScheduleConverter Size has wrong value: " + String.valueOf(testList.size()));
			}
			for (ScheduleListViewItem entry : testList) {
				if (entry != null) {
					_logger.Info(entry.toString());
				} else {
					_logger.Error("testScheduleConverter Entry is null!");
				}
			}
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_SCHEDULE_LIST,
					Constants.BUNDLE_SCHEDULE_LIST, testList);
			_logger.Info("Sent broadcast...");
		} else {
			_logger.Error("testScheduleConverter list is null!");
		}
	}

	private void testSocketConverter() {
		_logger.Info("testSocketConverter");

		String testString = SOCKETS + "TV:1&" + "Storage:1&" + "PC:1&" + "Light:0&" + "Heating:0&";
		List<SocketListViewItem> testList = _messageToSocketConverter.ConvertMessageToSocketList(testString);

		if (testList != null) {
			if (testList.size() == 5) {
				_logger.Info("testSocketConverter Size is correct! " + String.valueOf(testList.size()));
			} else {
				_logger.Error("testSocketConverter Size has wrong value: " + String.valueOf(testList.size()));
			}
			for (SocketListViewItem entry : testList) {
				if (entry != null) {
					_logger.Info(entry.toString());
				} else {
					_logger.Error("testSocketConverter Entry is null!");
				}
			}
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_SOCKET_LIST,
					Constants.BUNDLE_SOCKET_LIST, testList);
			_logger.Info("Sent broadcast...");
		} else {
			_logger.Error("testSocketConverter list is null!");
		}
	}

	private void testWeatherConverter() {
		_logger.Info("testWeatherConverter");
		// TODO Auto-generated method stub

	}
}
