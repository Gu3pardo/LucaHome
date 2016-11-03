package guepardoapps.common.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.*;
import guepardoapps.common.controller.*;
import guepardoapps.common.converter.json.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.toolset.controller.SharedPrefController;
import guepardoapps.toolset.openweather.OpenWeatherConstants;
import guepardoapps.toolset.openweather.OpenWeatherController;
import guepardoapps.toolset.openweather.WeatherModel;

public class DownloadService extends Service {

	private static String TAG = "DownloadService";
	private Logger _logger;

	private Context _context;

	private ReceiverController _receiverController;
	private ServiceController _serviceController;

	private SharedPrefController _sharedPrefController;

	private WeatherModel _currentWeather;
	private OpenWeatherController _openWeatherController;

	private SerializableList<Temperature> _temperatureList = null;
	private SerializableList<WirelessSocket> _wirelessSocketList = null;

	private BroadcastReceiver _downloadStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				parseDownloads(intent);
			} catch (Exception e) {
				_logger.Error(e.getMessage());
			}
		}
	};

	private BroadcastReceiver _weatherModelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received weather broadcast...");
			_currentWeather = (WeatherModel) intent
					.getSerializableExtra(OpenWeatherConstants.BUNDLE_EXTRA_WEATHER_MODEL);
			startDownload(LucaObject.WIRELESS_SOCKET);
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_logger = new Logger(TAG);

		_context = this;

		_openWeatherController = new OpenWeatherController(_context, Constants.CITY);
		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		registerReceiver();

		Bundle data = intent.getExtras();
		if (data != null) {
			LucaObject lucaObject = (LucaObject) data.getSerializable(Constants.BUNDLE_LUCA_OBJECT);
			if (lucaObject != null) {
				startDownload(lucaObject);
			}
		} else {
			startDownload(LucaObject.TEMPERATURE);
		}

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void startDownload(LucaObject lucaObject) {
		_logger.Debug("Starting download of " + lucaObject.toString());

		switch (lucaObject) {
		case SOUND:
			_serviceController.StartRestService(TAG, Constants.ACTION_IS_SOUND_PLAYING,
					Constants.BROADCAST_IS_SOUND_PLAYING, LucaObject.SOUND, RaspberrySelection.BOTH);
			break;
		case TEMPERATURE:
			_serviceController.StartRestService(Constants.TEMPERATURE_DOWNLOAD, Constants.ACTION_GET_TEMPERATURES,
					Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED, LucaObject.TEMPERATURE, RaspberrySelection.BOTH);
			break;
		case WEATHER_CURRENT:
			_openWeatherController.loadCurrentWeather();
			break;
		case WIRELESS_SOCKET:
			_serviceController.StartRestService(Constants.SOCKET_DOWNLOAD, Constants.ACTION_GET_SOCKETS,
					Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED, LucaObject.WIRELESS_SOCKET, RaspberrySelection.BOTH);
			break;
		case DUMMY:
		default:
			_logger.Error("Cannot download object: " + lucaObject.toString());
			break;
		}
	}

	private void parseDownloads(Intent intent) {
		LucaObject lucaObject = (LucaObject) intent.getSerializableExtra(Constants.BUNDLE_LUCA_OBJECT);

		if (lucaObject == null) {
			_logger.Error("_downloadStateReceiver received data with null lucaobject!");
			return;
		}

		switch (lucaObject) {
		case SOUND:
			String[] isPlayingStringArray = intent.getStringArrayExtra(TAG);

			if (isPlayingStringArray != null) {
				if (isPlayingStringArray[0] != null) {
					_logger.Debug(isPlayingStringArray[0]);
					String[] data = isPlayingStringArray[0].split("\\};");

					String isPlayingString = data[0].replace("{IsPlaying:", "").replace("};", "");
					if (isPlayingString.contains("1")) {
						if (data.length == 4) {
							String playingFile = data[1].replace("{PlayingFile:", "").replace("};", "");
							//String volume = data[2].replace("{Volume:", "").replace("};", "");
							int raspberry = Integer.parseInt(data[3].replace("{Raspberry:", "").replace("};", ""));

							_serviceController.StartNotificationService(RaspberrySelection.RASPBERRY_1.toString(),
									playingFile, Constants.ID_NOTIFICATION_SONG + raspberry, LucaObject.SOUND);
						} else {
							_logger.Warn("data has wrong size!");
						}
					}
				}

				if (isPlayingStringArray[1] != null) {
					_logger.Debug(isPlayingStringArray[1]);
					String[] data = isPlayingStringArray[1].split("\\};");

					String isPlayingString = data[0].replace("{IsPlaying:", "").replace("};", "");
					if (isPlayingString.contains("1")) {
						if (data.length == 4) {
							String playingFile = data[1].replace("{PlayingFile:", "").replace("};", "");
							//String volume = data[2].replace("{Volume:", "").replace("};", "");
							int raspberry = Integer.parseInt(data[3].replace("{Raspberry:", "").replace("};", ""));

							_serviceController.StartNotificationService(RaspberrySelection.RASPBERRY_2.toString(),
									playingFile, Constants.ID_NOTIFICATION_SONG + raspberry, LucaObject.SOUND);
						} else {
							_logger.Warn("data has wrong size!");
						}
					}
				}
			}

			unregisterReceiver();
			stopSelf();
			break;
		case TEMPERATURE:
			String[] temperatureStringArray = intent.getStringArrayExtra(Constants.TEMPERATURE_DOWNLOAD);
			if (temperatureStringArray != null) {
				_temperatureList = JsonDataToTemperatureConverter.GetList(temperatureStringArray);
			}
			startDownload(LucaObject.WEATHER_CURRENT);
			break;
		case WEATHER_CURRENT:
		case WEATHER_FORECAST:
			_logger.Warn("Parsing weather download is not done here...");
			break;
		case WIRELESS_SOCKET:
			String[] socketStringArray = intent.getStringArrayExtra(Constants.SOCKET_DOWNLOAD);
			if (socketStringArray != null) {
				_wirelessSocketList = JsonDataToSocketConverter.GetList(socketStringArray);
				installNotificationSettings();
				showNotifications();
			}
			unregisterReceiver();
			stopSelf();
			break;
		case DUMMY:
		default:
			_logger.Error("Cannot parse object: " + lucaObject.toString());
			break;
		}
	}

	private void installNotificationSettings() {
		if (_wirelessSocketList == null) {
			_logger.Warn("_wirelessSocketList is null!");
			return;
		}

		for (int index = 0; index < _wirelessSocketList.getSize(); index++) {
			String key = _wirelessSocketList.getValue(index).GetNotificationVisibilitySharedPrefKey();
			if (!_sharedPrefController.Contains(key)) {
				_sharedPrefController.SaveBooleanValue(key, true);
			}
		}
	}

	private void registerReceiver() {
		_receiverController.RegisterReceiver(_downloadStateReceiver,
				new String[] { Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED,
						Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED, Constants.BROADCAST_IS_SOUND_PLAYING });
		_receiverController.RegisterReceiver(_weatherModelReceiver,
				new String[] { OpenWeatherConstants.GET_CURRENT_WEATHER_JSON_FINISHED });
	}

	private void unregisterReceiver() {
		_receiverController.UnregisterReceiver(_downloadStateReceiver);
		_receiverController.UnregisterReceiver(_weatherModelReceiver);
	}

	private void showNotifications() {
		if (_wirelessSocketList != null) {
			_serviceController.StartSocketNotificationService(Constants.ID_NOTIFICATION_WEAR, _wirelessSocketList);
		} else {
			_logger.Error("SocketList is null! Cannot display notification!");
		}
		if (_temperatureList != null && _currentWeather != null) {
			_serviceController.StartTemperatureNotificationService(Constants.ID_NOTIFICATION_TEMPERATURE,
					_temperatureList, _currentWeather);
		} else {
			if (_temperatureList == null) {
				_logger.Error("TemperatureList is null! Cannot display notification!");
			}
			if (_currentWeather == null) {
				_logger.Error("CurrentWeather is null! Cannot display notification!");
			}
		}
		if (_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_WEATHER_NOTIFICATION)) {
			_context.startService(new Intent(_context, OpenWeatherService.class));
		}
	}
}
