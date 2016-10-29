package guepardoapps.lucahome.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;

import guepardoapps.common.BaseActivity;
import guepardoapps.common.Constants;
import guepardoapps.common.classes.*;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.OpenWeatherConstants;
import guepardoapps.toolset.openweather.WeatherModel;

public class SettingsActivity extends BaseActivity {

	private static String TAG = "SettingsActivity";

	private LinearLayout _socketLayout;

	private ServiceController _serviceController;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_settings);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;

		_serviceController = new ServiceController(_context);

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
		}

		initializeSwitches();
		initializeAppCheckboxes();
		initializeSocketNotificationCheckboxes();
	}

	private void initializeSwitches() {
		Switch displayNotification = (Switch) findViewById(R.id.switch_display_socket_notification);
		displayNotification.setChecked(
				_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_SOCKET_NOTIFICATION));
		displayNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_SOCKET_NOTIFICATION, isChecked);
				if (isChecked) {
					_socketLayout.setVisibility(View.VISIBLE);
					_serviceController.StartSocketNotificationService(Constants.ID_NOTIFICATION_WEAR,
							_wirelessSocketList);
				} else {
					_socketLayout.setVisibility(View.GONE);
					_serviceController.CloseNotification(Constants.ID_NOTIFICATION_WEAR);
				}
			}
		});

		Switch weatherNotification = (Switch) findViewById(R.id.switch_display_weather_notification);
		weatherNotification.setChecked(
				_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_WEATHER_NOTIFICATION));
		weatherNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_WEATHER_NOTIFICATION, isChecked);
				if (isChecked) {
					_serviceController.StartWeatherNotificationService(OpenWeatherConstants.FORECAST_NOTIFICATION_ID,
							_currentWeather, _forecastWeather);
				} else {
					_serviceController.CloseNotification(OpenWeatherConstants.FORECAST_NOTIFICATION_ID);
				}
			}
		});

		Switch temperatureNotification = (Switch) findViewById(R.id.switch_display_temperature_notification);
		temperatureNotification.setChecked(_sharedPrefController
				.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_TEMPERATURE_NOTIFICATION));
		temperatureNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_sharedPrefController.SaveBooleanValue(Constants.DISPLAY_TEMPERATURE_NOTIFICATION, isChecked);
				if (isChecked) {
					_serviceController.StartTemperatureNotificationService(Constants.ID_NOTIFICATION_TEMPERATURE,
							_temperatureList, _currentWeather);
				} else {
					_serviceController.CloseNotification(Constants.ID_NOTIFICATION_TEMPERATURE);
				}
			}
		});
	}

	private void initializeAppCheckboxes() {
		CheckBox audioStart = (CheckBox) findViewById(R.id.startAudioAppCheckbox);
		audioStart.setChecked(_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.START_AUDIO_APP));
		audioStart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_sharedPrefController.SaveBooleanValue(Constants.START_AUDIO_APP, isChecked);
			}
		});

		CheckBox osmcStart = (CheckBox) findViewById(R.id.startOsmcAppCheckbox);
		osmcStart.setChecked(_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.START_OSMC_APP));
		osmcStart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_sharedPrefController.SaveBooleanValue(Constants.START_OSMC_APP, isChecked);
			}
		});
	}

	private void initializeSocketNotificationCheckboxes() {
		_socketLayout = (LinearLayout) findViewById(R.id.notificationSocketLayout);

		if (_wirelessSocketList == null) {
			_logger.Warn("_wirelessSocketList is null!");
			return;
		}

		for (int index = 0; index < _wirelessSocketList.getSize(); index++) {
			WirelessSocket socket = _wirelessSocketList.getValue(index);
			final String key = socket.GetNotificationVisibilitySharedPrefKey();
			boolean notificationVisibility = _sharedPrefController.LoadBooleanValueFromSharedPreferences(key);

			CheckBox socketCheckbox = new CheckBox(getApplicationContext());
			socketCheckbox.setText(socket.GetName());
			socketCheckbox.setChecked(notificationVisibility);
			socketCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					_sharedPrefController.SaveBooleanValue(key, isChecked);
					_serviceController.StartSocketNotificationService(Constants.ID_NOTIFICATION_WEAR,
							_wirelessSocketList);
				}
			});

			_socketLayout.addView(socketCheckbox);
		}

		if (!_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.DISPLAY_SOCKET_NOTIFICATION)) {
			_socketLayout.setVisibility(View.GONE);
		}
	}
}
