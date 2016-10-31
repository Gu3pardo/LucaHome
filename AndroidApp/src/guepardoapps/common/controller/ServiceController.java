package guepardoapps.common.controller;

import java.io.Serializable;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.Temperature;
import guepardoapps.common.classes.User;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.common.service.DownloadService;
import guepardoapps.common.service.NotificationService;
import guepardoapps.common.service.RESTService;
import guepardoapps.common.service.authentification.UserService;
import guepardoapps.toolset.controller.NetworkController;
import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.WeatherModel;

public class ServiceController implements Serializable {

	private static final long serialVersionUID = -9159391706553294158L;

	private static String TAG = "ServiceController";

	private Logger _logger;

	private Context _context;

	private NetworkController _networkController;

	private UserService _userService;

	public ServiceController(Context context) {
		_logger = new Logger(TAG);

		_context = context;

		_networkController = new NetworkController(_context, null);

		_userService = new UserService(_context);
	}

	public void StartRestService(String name, String action, String broadcast, LucaObject lucaObject,
			RaspberrySelection raspberrySelection) {
		Intent serviceIntent = new Intent(_context, RESTService.class);
		Bundle serviceData = new Bundle();

		User loggedInUser = _userService.LoadUser();

		serviceData.putString(Constants.BUNDLE_USER, loggedInUser.GetUserName());
		serviceData.putString(Constants.BUNDLE_PASSPHRASE, loggedInUser.GetPassword());

		serviceData.putString(Constants.BUNDLE_ACTION, action);
		serviceData.putString(Constants.BUNDLE_NAME, name);
		serviceData.putString(Constants.BUNDLE_BROADCAST, broadcast);
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, lucaObject);
		serviceData.putSerializable(Constants.BUNDLE_RASPBERRY_SELECTION, raspberrySelection);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void StartNotificationService(String title, String body, int notificationId, LucaObject lucaObject) {
		Intent serviceIntent = new Intent(_context, NotificationService.class);
		Bundle serviceData = new Bundle();

		serviceData.putString(Constants.BUNDLE_NOTIFICATION_TITLE, title);
		serviceData.putString(Constants.BUNDLE_NOTIFICATION_BODY, body);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ID, notificationId);
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, lucaObject);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void StartNotificationWithIconService(String title, String body, int notificationId, int notificationIcon,
			LucaObject lucaObject) {
		Intent serviceIntent = new Intent(_context, NotificationService.class);
		Bundle serviceData = new Bundle();

		serviceData.putString(Constants.BUNDLE_NOTIFICATION_TITLE, title);
		serviceData.putString(Constants.BUNDLE_NOTIFICATION_BODY, body);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ID, notificationId);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ICON, notificationIcon);
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, lucaObject);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void StartSocketNotificationService(int notificationId, SerializableList<WirelessSocket> socketList) {
		if (!_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			Toast.makeText(_context, "No LucaHome network!", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent serviceIntent = new Intent(_context, NotificationService.class);
		Bundle serviceData = new Bundle();

		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.WIRELESS_SOCKET);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ID, notificationId);
		serviceData.putSerializable(Constants.BUNDLE_SOCKET_LIST, socketList);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void StartTemperatureNotificationService(int notificationId, SerializableList<Temperature> temperatureList,
			WeatherModel currentWeather) {
		if (temperatureList == null) {
			_logger.Error("temperatureList is null!");
			return;
		}
		if (currentWeather == null) {
			_logger.Error("currentWeather is null!");
			return;
		}

		if (!_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			Toast.makeText(_context, "No LucaHome network!", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent serviceIntent = new Intent(_context, NotificationService.class);
		Bundle serviceData = new Bundle();

		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.TEMPERATURE);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ID, notificationId);
		serviceData.putSerializable(Constants.BUNDLE_TEMPERATURE_LIST, temperatureList);
		serviceData.putSerializable(Constants.BUNDLE_WEATHER_CURRENT, currentWeather);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void StartWeatherNotificationService(int notificationId, WeatherModel currentWeather,
			ForecastModel forecastWeather) {
		if (currentWeather == null) {
			_logger.Error("currentWeather is null!");
			return;
		}
		if (forecastWeather == null) {
			_logger.Error("forecastWeather is null!");
			return;
		}

		Intent serviceIntent = new Intent(_context, NotificationService.class);
		Bundle serviceData = new Bundle();

		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.WEATHER_DATA);
		serviceData.putInt(Constants.BUNDLE_NOTIFICATION_ID, notificationId);
		serviceData.putSerializable(Constants.BUNDLE_WEATHER_CURRENT, currentWeather);
		serviceData.putSerializable(Constants.BUNDLE_WEATHER_FORECAST, forecastWeather);

		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}

	public void CloseNotification(int notificationId) {
		NotificationManager notificationManager = (NotificationManager) _context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
	}

	public void StartWifiDownload() {
		Intent serviceIntent = new Intent(_context, DownloadService.class);
		_context.startService(serviceIntent);
	}

	public void StartSocketDownload() {
		Intent serviceIntent = new Intent(_context, DownloadService.class);

		Bundle serviceData = new Bundle();
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.WIRELESS_SOCKET);
		serviceIntent.putExtras(serviceData);

		_context.startService(serviceIntent);
	}

	public void StopSound() {
		StartRestService(TAG, Constants.ACTION_STOP_SOUND, Constants.BROADCAST_STOP_SOUND, LucaObject.SOUND,
				RaspberrySelection.BOTH);
		CloseNotification(Constants.ID_NOTIFICATION_SOUND);
	}
}
