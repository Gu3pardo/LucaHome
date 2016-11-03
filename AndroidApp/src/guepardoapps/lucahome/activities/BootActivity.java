package guepardoapps.lucahome.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.*;
import guepardoapps.common.controller.*;
import guepardoapps.common.converter.json.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.common.service.DialogService;
import guepardoapps.common.service.DownloadService;
import guepardoapps.common.service.NavigationService;
import guepardoapps.common.service.OpenWeatherService;
import guepardoapps.common.service.authentification.UserService;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.controller.*;
import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.OpenWeatherConstants;
import guepardoapps.toolset.openweather.OpenWeatherController;
import guepardoapps.toolset.openweather.WeatherModel;

public class BootActivity extends Activity {

	private static String TAG = "BootActivity";

	private boolean _downloadingData = false;

	private boolean _downloadSuccess;
	private int _textColor;
	private int _backgroundColor;

	private Bundle _serverData;

	private ProgressBar _percentProgressBar;
	private TextView _progressTextView;

	private Context _context;

	private Logger _logger;

	private DialogService _dialogService;
	private NavigationService _navigationService;
	private UserService _userService;

	private DialogController _dialogController;
	private NetworkController _networkController;
	private ReceiverController _receiverController;
	private ServiceController _serviceController;
	private SharedPrefController _sharedPrefController;

	private WeatherModel _currentWeather;
	private ForecastModel _forecastWeather;
	private OpenWeatherController _openWeatherController;

	private SerializableList<Birthday> _birthdayList = null;
	private SerializableList<Change> _changeList = null;
	private Information _information = null;
	private SerializableList<Movie> _movieList = null;
	private SerializableList<Schedule> _scheduleList = null;
	private SerializableList<Temperature> _temperatureList = null;
	private SerializableList<Timer> _timerList = null;
	private SerializableList<WirelessSocket> _wirelessSocketList = null;

	private User _user = null;

	private int _progressBarMax = 100;
	private int _progressBarSteps = 9;
	private int _progress = 0;

	private Handler _timeoutHandler = new Handler();
	private long _timeoutStartTime = 0L;
	private long _timeoutTimeInMillies = 0L;
	private int _timeCheck = 1000;
	private int _maxTimeout = 20000;

	private Runnable _timeoutCheck = new Runnable() {
		public void run() {
			_timeoutTimeInMillies = SystemClock.uptimeMillis() - _timeoutStartTime;
			_timeoutHandler.postDelayed(this, _timeCheck);

			_logger.Debug("Checking timeout! _timeoutTimeInMillies: " + String.valueOf(_timeoutTimeInMillies));

			if ((int) (_timeoutTimeInMillies) > _maxTimeout) {
				_logger.Warn("Timeout received!");
				stopTimeout();
				unregisterReceiver();
				_downloadSuccess = false;
				startMainActivity();
			}
		}
	};

	private Runnable _navigateToMainRunnable = new Runnable() {
		@Override
		public void run() {
			_user = _userService.LoadUser();
			if (_user != null) {
				_serverData.putSerializable(Constants.BUNDLE_LOGGED_IN_USER, _user);
				_navigationService.NavigateTo(MainActivity.class, _serverData, true);
			} else {
				_dialogService.ShowDialogSingle("No user!", "Please log in as a user!", "Log In", _logInRunnable,
						false);
			}
		}
	};

	private Runnable _logInRunnable = new Runnable() {
		@Override
		public void run() {
			_dialogService.ShowUserCredentialsDialog(_user, _navigateToMainRunnable, false);
		}
	};

	private Runnable _startDownloadRunnable = new Runnable() {
		@Override
		public void run() {
			startDownload(LucaObject.BIRTHDAY);
			startTimeout();
			isPlayingSound();
		}
	};

	private BroadcastReceiver _downloadStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				parseDownloads(intent);
			} catch (Exception e) {
				_logger.Error(e.getMessage());
			}
			updateProgressBar();
		}
	};

	private BroadcastReceiver _weatherModelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received weather broadcast...");
			_currentWeather = (WeatherModel) intent
					.getSerializableExtra(OpenWeatherConstants.BUNDLE_EXTRA_WEATHER_MODEL);
			updateProgressBar();
			startDownload(LucaObject.WEATHER_FORECAST);
		}
	};

	private BroadcastReceiver _forecastModelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received forecast broadcast...");
			_forecastWeather = (ForecastModel) intent
					.getSerializableExtra(OpenWeatherConstants.BUNDLE_EXTRA_FORECAST_MODEL);
			updateProgressBar();
			startDownload(LucaObject.WIRELESS_SOCKET);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_boot);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_percentProgressBar = (ProgressBar) findViewById(R.id.percentProgressBar);
		_percentProgressBar.setMax(_progressBarMax);
		_percentProgressBar.setProgress((int) (_progress * _progressBarMax / _progressBarSteps));

		_progressTextView = (TextView) findViewById(R.id.percentProgressTextView);
		_progressTextView.setText(String.valueOf(_progress) + " %");

		_context = this;

		_textColor = ContextCompat.getColor(_context, R.color.TextIcon);
		_backgroundColor = ContextCompat.getColor(_context, R.color.Background);

		_serverData = new Bundle();

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_dialogService = new DialogService(_context);
		_navigationService = new NavigationService(_context);
		_userService = new UserService(_context);

		_dialogController = new DialogController(_context, _textColor, _backgroundColor);
		_networkController = new NetworkController(_context, _dialogController);
		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		_openWeatherController = new OpenWeatherController(_context, Constants.CITY);

		_birthdayList = new SerializableList<Birthday>();
		_changeList = new SerializableList<Change>();
		_movieList = new SerializableList<Movie>();
		_scheduleList = new SerializableList<Schedule>();
		_temperatureList = new SerializableList<Temperature>();
		_timerList = new SerializableList<Timer>();
		_wirelessSocketList = new SerializableList<WirelessSocket>();

		if (!_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.SHARED_PREF_INSTALLED)) {
			install();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");

		_receiverController.RegisterReceiver(_downloadStateReceiver,
				new String[] { Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED,
						Constants.BROADCAST_DOWNLOAD_CHANGE_FINISHED, Constants.BROADCAST_DOWNLOAD_INFORMATION_FINISHED,
						Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED, Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED,
						Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED,
						Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED });

		_receiverController.RegisterReceiver(_weatherModelReceiver,
				new String[] { OpenWeatherConstants.GET_CURRENT_WEATHER_JSON_FINISHED });
		_receiverController.RegisterReceiver(_forecastModelReceiver,
				new String[] { OpenWeatherConstants.GET_FORECAST_WEATHER_JSON_FINISHED });

		if (!_downloadingData) {
			if (_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
				if (!_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.USER_DATA_ENTERED)) {
					_dialogService.ShowUserCredentialsDialog(_user, _startDownloadRunnable, false);
				} else {
					startDownload(LucaObject.BIRTHDAY);
					startTimeout();
					isPlayingSound();
				}
			} else {
				_logger.Warn("No LucaHome network! finishing...");
				Toast.makeText(_context, "No LucaHome network! finishing...", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		_logger.Debug("onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		unregisterReceiver();
	}

	private void install() {
		_sharedPrefController.SaveBooleanValue(Constants.SHARED_PREF_INSTALLED, true);

		_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_SOCKET_NOTIFICATION, true);
		_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_WEATHER_NOTIFICATION, true);
		_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_TEMPERATURE_NOTIFICATION, true);

		_sharedPrefController.SaveBooleanValue(Constants.START_AUDIO_APP, true);
		_sharedPrefController.SaveBooleanValue(Constants.START_OSMC_APP, true);

		_sharedPrefController.SaveIntegerValue(Constants.SOUND_RASPBERRY_SELECTION,
				RaspberrySelection.RASPBERRY_1.GetInt());
		
		//TODO install initial database with preset data!
	}

	private void startDownload(LucaObject lucaObject) {

		_logger.Debug("Starting download of " + lucaObject.toString());
		if (_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			_downloadingData = true;

			switch (lucaObject) {
			case BIRTHDAY:
				_serviceController.StartRestService(Constants.BIRTHDAY_DOWNLOAD, Constants.ACTION_GET_BIRTHDAYS,
						Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED, LucaObject.BIRTHDAY, RaspberrySelection.BOTH);
				break;
			case CHANGE:
				_serviceController.StartRestService(Constants.CHANGE_DOWNLOAD, Constants.ACTION_GET_CHANGES,
						Constants.BROADCAST_DOWNLOAD_CHANGE_FINISHED, LucaObject.CHANGE, RaspberrySelection.BOTH);
				break;
			case INFORMATION:
				_serviceController.StartRestService(Constants.INFORMATION_DOWNLOAD, Constants.ACTION_GET_INFORMATIONS,
						Constants.BROADCAST_DOWNLOAD_INFORMATION_FINISHED, LucaObject.INFORMATION,
						RaspberrySelection.BOTH);
				break;
			case MOVIE:
				_serviceController.StartRestService(Constants.MOVIE_DOWNLOAD, Constants.ACTION_GET_MOVIES,
						Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED, LucaObject.MOVIE, RaspberrySelection.BOTH);
				break;
			case SCHEDULE:
				_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, Constants.ACTION_GET_SCHEDULES,
						Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED, LucaObject.SCHEDULE, RaspberrySelection.BOTH);
				break;
			case TEMPERATURE:
				_serviceController.StartRestService(Constants.TEMPERATURE_DOWNLOAD, Constants.ACTION_GET_TEMPERATURES,
						Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED, LucaObject.TEMPERATURE,
						RaspberrySelection.BOTH);
				break;
			case WEATHER_CURRENT:
				_openWeatherController.loadCurrentWeather();
				break;
			case WEATHER_FORECAST:
				_openWeatherController.loadForecastWeather();
				break;
			case WIRELESS_SOCKET:
				_serviceController.StartRestService(Constants.SOCKET_DOWNLOAD, Constants.ACTION_GET_SOCKETS,
						Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED, LucaObject.WIRELESS_SOCKET,
						RaspberrySelection.BOTH);
				break;
			case DUMMY:
			default:
				_logger.Error("Cannot download object: " + lucaObject.toString());
				break;
			}
		}
	}

	private void startTimeout() {
		_logger.Debug("Starting timeoutController...");
		_timeoutStartTime = SystemClock.uptimeMillis();
		_timeoutHandler.postDelayed(_timeoutCheck, _timeCheck);
	}

	private void stopTimeout() {
		_timeoutHandler.removeCallbacks(_timeoutCheck);
		_timeoutStartTime = 0L;
		_timeoutTimeInMillies = 0L;
	}

	private void updateProgressBar() {
		_progress++;
		_percentProgressBar.setProgress((int) (_progress * _progressBarMax / _progressBarSteps));
		_progressTextView.setText(String.valueOf((int) (_progress * _progressBarMax / _progressBarSteps)) + " %");

		if (_progress > _progressBarSteps - 1) {
			_downloadSuccess = true;
			startMainActivity();
		}
	}

	private void parseDownloads(Intent intent) {
		LucaObject lucaObject = (LucaObject) intent.getSerializableExtra(Constants.BUNDLE_LUCA_OBJECT);

		if (lucaObject == null) {
			_logger.Error("_downloadStateReceiver received data with null lucaobject!");
			return;
		}

		switch (lucaObject) {
		case BIRTHDAY:
			String[] birthdayStringArray = intent.getStringArrayExtra(Constants.BIRTHDAY_DOWNLOAD);
			if (birthdayStringArray != null) {
				_birthdayList = JsonDataToBirthdayConverter.GetList(birthdayStringArray);
			}
			startDownload(LucaObject.CHANGE);
			break;
		case CHANGE:
			String[] changeStringArray = intent.getStringArrayExtra(Constants.CHANGE_DOWNLOAD);
			if (changeStringArray != null) {
				_changeList = JsonDataToChangeConverter.GetList(changeStringArray);
			}
			startDownload(LucaObject.INFORMATION);
			break;
		case INFORMATION:
			String[] informationStringArray = intent.getStringArrayExtra(Constants.INFORMATION_DOWNLOAD);
			if (informationStringArray != null) {
				_information = JsonDataToInformationConverter.Get(informationStringArray);
			}
			startDownload(LucaObject.MOVIE);
			break;
		case MOVIE:
			String[] movieStringArray = intent.getStringArrayExtra(Constants.MOVIE_DOWNLOAD);
			if (movieStringArray != null) {
				_movieList = JsonDataToMovieConverter.GetList(movieStringArray);
			}
			startDownload(LucaObject.TEMPERATURE);
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
			}
			startDownload(LucaObject.SCHEDULE);
			break;
		case SCHEDULE:
			String[] scheduleStringArray = intent.getStringArrayExtra(Constants.SCHEDULE_DOWNLOAD);
			if (scheduleStringArray != null && _wirelessSocketList != null) {
				_scheduleList = JsonDataToScheduleConverter.GetList(scheduleStringArray, _wirelessSocketList);
				_timerList = JsonDataToTimerConverter.GetList(scheduleStringArray, _wirelessSocketList);
			}
			break;
		case DUMMY:
		default:
			_logger.Error("Cannot parse object: " + lucaObject.toString());
			break;
		}
	}

	private void startMainActivity() {
		_logger.Debug("startMainActivity()");

		unregisterReceiver();

		stopTimeout();

		_serverData.putBoolean(Constants.BUNDLE_DOWNLOAD_SUCCESS, _downloadSuccess);
		_serverData.putSerializable(Constants.BUNDLE_BIRTHDAY_LIST, _birthdayList);
		_serverData.putSerializable(Constants.BUNDLE_CHANGE_LIST, _changeList);
		_serverData.putSerializable(Constants.BUNDLE_INFORMATION_SINGLE, _information);
		_serverData.putSerializable(Constants.BUNDLE_MOVIE_LIST, _movieList);
		_serverData.putSerializable(Constants.BUNDLE_SCHEDULE_LIST, _scheduleList);
		_serverData.putSerializable(Constants.BUNDLE_TEMPERATURE_LIST, _temperatureList);
		_serverData.putSerializable(Constants.BUNDLE_TIMER_LIST, _timerList);
		_serverData.putSerializable(Constants.BUNDLE_SOCKET_LIST, _wirelessSocketList);
		_serverData.putSerializable(Constants.BUNDLE_WEATHER_CURRENT, _currentWeather);
		_serverData.putSerializable(Constants.BUNDLE_WEATHER_FORECAST, _forecastWeather);

		if (_downloadSuccess) {
			showNotifications();
		}

		_navigateToMainRunnable.run();
	}

	private void unregisterReceiver() {
		_receiverController.UnregisterReceiver(_downloadStateReceiver);
		_receiverController.UnregisterReceiver(_forecastModelReceiver);
		_receiverController.UnregisterReceiver(_weatherModelReceiver);
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

	private void showNotifications() {
		if (_wirelessSocketList != null) {
			_serviceController.StartSocketNotificationService(Constants.ID_NOTIFICATION_WEAR, _wirelessSocketList);
		}
		if (_temperatureList != null && _currentWeather != null) {
			_serviceController.StartTemperatureNotificationService(Constants.ID_NOTIFICATION_TEMPERATURE,
					_temperatureList, _currentWeather);
		}
		if (_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_WEATHER_NOTIFICATION)) {
			_context.startService(new Intent(_context, OpenWeatherService.class));
		}
	}
	
	private void isPlayingSound(){
		Intent serviceIntent = new Intent(_context, DownloadService.class);

		Bundle serviceData = new Bundle();
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.SOUND);
		serviceIntent.putExtras(serviceData);

		_context.startService(serviceIntent);
	}
}
