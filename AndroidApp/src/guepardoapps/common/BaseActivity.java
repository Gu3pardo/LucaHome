package guepardoapps.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import guepardoapps.common.classes.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.service.DialogService;
import guepardoapps.common.service.NavigationService;
import guepardoapps.common.service.UserService;
import guepardoapps.lucahome.activities.MainActivity;

import guepardoapps.toolset.controller.SharedPrefController;
import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.WeatherModel;

public class BaseActivity extends Activity {

	private static String TAG = "BaseActivity";

	protected boolean _downloadSuccess;
	protected boolean _extrasAvailable = false;

	protected WeatherModel _currentWeather;
	protected ForecastModel _forecastWeather;

	protected SerializableList<Birthday> _birthdayList = null;
	protected SerializableList<Change> _changeList = null;
	protected Information _information = null;
	protected SerializableList<Movie> _movieList = null;
	protected SerializableList<Schedule> _scheduleList = null;
	protected SerializableList<Temperature> _temperatureList = null;
	protected SerializableList<Timer> _timerList = null;
	protected SerializableList<WirelessSocket> _wirelessSocketList = null;

	protected User _user = null;

	protected Logger _logger;

	protected Context _context;
	protected SharedPrefController _sharedPrefController;

	protected DialogService _dialogService;
	protected NavigationService _navigationService;
	protected UserService _userService;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		_dialogService = new DialogService(_context);
		_navigationService = new NavigationService(_context);
		_userService = new UserService(_context);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_logger.Debug("Extras are available!");
			_extrasAvailable = true;

			_downloadSuccess = extras.getBoolean(Constants.BUNDLE_DOWNLOAD_SUCCESS);

			_birthdayList = (SerializableList<Birthday>) extras.getSerializable(Constants.BUNDLE_BIRTHDAY_LIST);

			_changeList = (SerializableList<Change>) extras.getSerializable(Constants.BUNDLE_CHANGE_LIST);
			_information = (Information) extras.getSerializable(Constants.BUNDLE_INFORMATION_SINGLE);

			_movieList = (SerializableList<Movie>) extras.getSerializable(Constants.BUNDLE_MOVIE_LIST);

			_scheduleList = (SerializableList<Schedule>) extras.getSerializable(Constants.BUNDLE_SCHEDULE_LIST);

			_temperatureList = (SerializableList<Temperature>) extras
					.getSerializable(Constants.BUNDLE_TEMPERATURE_LIST);

			_currentWeather = (WeatherModel) extras.getSerializable(Constants.BUNDLE_WEATHER_CURRENT);
			_forecastWeather = (ForecastModel) extras.getSerializable(Constants.BUNDLE_WEATHER_FORECAST);

			_timerList = (SerializableList<Timer>) extras.getSerializable(Constants.BUNDLE_TIMER_LIST);

			_wirelessSocketList = (SerializableList<WirelessSocket>) extras
					.getSerializable(Constants.BUNDLE_SOCKET_LIST);

			_user = (User) extras.getSerializable(Constants.BUNDLE_LOGGED_IN_USER);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");
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
	}

	protected Bundle CreateBundle(LucaObject lucaObject) {
		if (!_extrasAvailable) {
			_logger.Warn("No extras available!");
			return null;
		}

		Bundle data = new Bundle();

		if (lucaObject != null) {
			data.putSerializable(Constants.BUNDLE_LUCA_OBJECT, lucaObject);
		}

		data.putBoolean(Constants.BUNDLE_DOWNLOAD_SUCCESS, _downloadSuccess);
		data.putSerializable(Constants.BUNDLE_BIRTHDAY_LIST, _birthdayList);
		data.putSerializable(Constants.BUNDLE_CHANGE_LIST, _changeList);
		data.putSerializable(Constants.BUNDLE_INFORMATION_SINGLE, _information);
		data.putSerializable(Constants.BUNDLE_MOVIE_LIST, _movieList);
		data.putSerializable(Constants.BUNDLE_SCHEDULE_LIST, _scheduleList);
		data.putSerializable(Constants.BUNDLE_TEMPERATURE_LIST, _temperatureList);
		data.putSerializable(Constants.BUNDLE_TIMER_LIST, _timerList);
		data.putSerializable(Constants.BUNDLE_SOCKET_LIST, _wirelessSocketList);
		data.putSerializable(Constants.BUNDLE_WEATHER_CURRENT, _currentWeather);
		data.putSerializable(Constants.BUNDLE_WEATHER_FORECAST, _forecastWeather);

		data.putSerializable(Constants.BUNDLE_LOGGED_IN_USER, _user);

		return data;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			_navigationService.NavigateTo(MainActivity.class, CreateBundle(null), true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
