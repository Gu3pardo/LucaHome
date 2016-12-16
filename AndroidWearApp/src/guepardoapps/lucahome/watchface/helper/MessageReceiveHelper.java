package guepardoapps.lucahome.watchface.helper;

import java.util.List;

import android.content.Context;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.converter.*;
import guepardoapps.lucahome.watchface.dto.*;

import guepardoapps.lucahome.wearcontrol.views.listitem.*;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.BroadcastController;

public class MessageReceiveHelper {

	private static final String CURRENT_WEATHER = "CurrentWeather:";
	private static final String PHONE_BATTERY = "PhoneBattery:";
	private static final String RASPBERRY_TEMPERATURE = "RaspberryTemperature:";

	private static final String BIRTHDAYS = "Birthdays:";
	private static final String SCHEDULES = "Schedules:";
	private static final String SOCKETS = "Sockets:";

	private static final String WIFI = "Wifi:";

	private static final String TAG = MessageReceiveHelper.class.getName();
	private Logger _logger;

	private Context _context;
	private BroadcastController _broadcastController;

	private MessageToBirthdayConverter _messageToBirthdayConverter;
	private MessageToRaspberryConverter _messageToRaspberryConverter;
	private MessageToScheduleConverter _messageToScheduleConverter;
	private MessageToSocketConverter _messageToSocketConverter;
	private MessageToWeatherConverter _messageToWeatherConverter;

	public MessageReceiveHelper(Context context) {
		_logger = new Logger(TAG);

		_context = context;
		_broadcastController = new BroadcastController(_context);

		_messageToBirthdayConverter = new MessageToBirthdayConverter();
		_messageToRaspberryConverter = new MessageToRaspberryConverter();
		_messageToScheduleConverter = new MessageToScheduleConverter();
		_messageToSocketConverter = new MessageToSocketConverter();
		_messageToWeatherConverter = new MessageToWeatherConverter();
	}

	public void HandleMessage(String message) {
		if (message == null) {
			_logger.Warn("message is null!");
			return;
		}

		_logger.Debug("HandleMessage: " + message);
		if (message.startsWith(CURRENT_WEATHER)) {
			message = message.replace(CURRENT_WEATHER, "");
			WeatherModelDto currentWeather = _messageToWeatherConverter.ConvertMessageToWeatherModel(message);
			if (currentWeather != null) {
				_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_CURRENT_WEATHER,
						Constants.BUNDLE_CURRENT_WEATHER, currentWeather);
			} else {
				_logger.Warn("CurrentWeather is null!");
			}
		} else if (message.startsWith(PHONE_BATTERY)) {
			message = message.replace(PHONE_BATTERY, "");
			_broadcastController.SendStringBroadcast(Constants.BROADCAST_UPDATE_PHONE_BATTERY,
					Constants.BUNDLE_PHONE_BATTERY, message);
		} else if (message.startsWith(RASPBERRY_TEMPERATURE)) {
			message = message.replace(RASPBERRY_TEMPERATURE, "");
			TemperatureDto raspberryTemperature = _messageToRaspberryConverter.ConvertMessageToRaspberryModel(message);
			if (raspberryTemperature != null) {
				_broadcastController.SendSerializableArrayBroadcast(Constants.BROADCAST_UPDATE_RASPBERRY_TEMPERATURE,
						new String[] { Constants.BUNDLE_RASPBERRY_TEMPERATURE }, new Object[] { raspberryTemperature });
			} else {
				_logger.Warn("raspberryTemperature1 or raspberryTemperature2 or both are null!");
			}
		} else if (message.startsWith(BIRTHDAYS)) {
			message = message.replace(BIRTHDAYS, "");
			List<BirthdayListViewItem> itemList = _messageToBirthdayConverter.ConvertMessageToBirthdayList(message);
			if (itemList != null) {
				_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_BIRTHDAY_LIST,
						Constants.BUNDLE_BIRTHDAY_LIST, itemList);
			} else {
				_logger.Warn("SocketList is null!");
			}
		} else if (message.startsWith(SOCKETS)) {
			message = message.replace(SOCKETS, "");
			List<SocketListViewItem> itemList = _messageToSocketConverter.ConvertMessageToSocketList(message);
			if (itemList != null) {
				_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_SOCKET_LIST,
						Constants.BUNDLE_SOCKET_LIST, itemList);
			} else {
				_logger.Warn("SocketList is null!");
			}
		} else if (message.startsWith(SCHEDULES)) {
			message = message.replace(SCHEDULES, "");
			List<ScheduleListViewItem> itemList = _messageToScheduleConverter.ConvertMessageToScheduleList(message);
			if (itemList != null) {
				_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_UPDATE_SCHEDULE_LIST,
						Constants.BUNDLE_SCHEDULE_LIST, itemList);
			} else {
				_logger.Warn("SocketList is null!");
			}
		} else if (message.startsWith(WIFI)) {
			message = message.replace(WIFI, "");
			_broadcastController.SendStringBroadcast(Constants.BROADCAST_UPDATE_WIFI_STATE, Constants.BUNDLE_WIFI_STATE,
					message);
		} else {
			_logger.Warn("Cannot handle message: " + message);
		}
	}
}
