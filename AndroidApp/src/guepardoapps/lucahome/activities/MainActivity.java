package guepardoapps.lucahome.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import guepardoapps.common.BaseActivity;
import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.TemperatureType;
import guepardoapps.common.service.NavigationService;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.WeatherModel;

public class MainActivity extends BaseActivity {

	private static String TAG = "MainActivity";

	private Logger _logger;

	private TextView _temperatureErlangen;

	private Runnable _updateUser = new Runnable() {
		@Override
		public void run() {
			_user = _userService.LoadUser();
			Toast.makeText(_context, "Not yet implemented!", Toast.LENGTH_SHORT).show();
			_logger.Warn("Save updated user to raspberry not yet implemented!");
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;

		_navigationService = new NavigationService(_context);

		checkDisplaySpecs();
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_temperatureErlangen = (TextView) findViewById(R.id.textViewTemperature);
		_temperatureErlangen.setText("Loading...");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_logger.Debug("Extras are available!");

			_downloadSuccess = extras.getBoolean(Constants.BUNDLE_DOWNLOAD_SUCCESS);

			_currentWeather = (WeatherModel) extras.getSerializable(Constants.BUNDLE_WEATHER_CURRENT);
			_forecastWeather = (ForecastModel) extras.getSerializable(Constants.BUNDLE_WEATHER_FORECAST);

			_birthdayList = (SerializableList<Birthday>) extras.getSerializable(Constants.BUNDLE_BIRTHDAY_LIST);
			_changeList = (SerializableList<Change>) extras.getSerializable(Constants.BUNDLE_CHANGE_LIST);
			_information = (Information) extras.getSerializable(Constants.BUNDLE_INFORMATION_SINGLE);
			_movieList = (SerializableList<Movie>) extras.getSerializable(Constants.BUNDLE_MOVIE_LIST);
			_scheduleList = (SerializableList<Schedule>) extras.getSerializable(Constants.BUNDLE_SCHEDULE_LIST);
			_temperatureList = (SerializableList<Temperature>) extras
					.getSerializable(Constants.BUNDLE_TEMPERATURE_LIST);
			for (int index = 0; index < _temperatureList.getSize(); index++) {
				if (_temperatureList.getValue(index).GetTemperatureType() == TemperatureType.CITY) {
					_temperatureList.removeValue(_temperatureList.getValue(index));
				}
			}
			Temperature newEntry = new Temperature(_currentWeather.GetTemperature(), _currentWeather.GetCity(),
					_currentWeather.GetLastUpdate(), "n.a.", TemperatureType.CITY, "n.a.");
			_temperatureList.addValue(newEntry);

			_timerList = (SerializableList<Timer>) extras.getSerializable(Constants.BUNDLE_TIMER_LIST);
			_wirelessSocketList = (SerializableList<WirelessSocket>) extras
					.getSerializable(Constants.BUNDLE_SOCKET_LIST);

			_temperatureErlangen.setText(_currentWeather.GetBasicText());
		}

		initializeImageButtons();
	}

	private void checkDisplaySpecs() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		_logger.Debug("Metrics: " + String.valueOf(displayMetrics));

		if (displayMetrics.widthPixels == 800) {
			_logger.Debug("isNexus7");
			setContentView(R.layout.view_main_nexus7);
		} else if (displayMetrics.xdpi == 360) {
			_logger.Debug("i9505 360dpi");
			setContentView(R.layout.view_main_i9505_360dpi);
		} else {
			_logger.Debug("normal view");
			setContentView(R.layout.view_main);
		}
	}

	public void CHANGE(View view) {
		NavigateToChilds(ListActivity.class, LucaObject.CHANGE);
	}

	public void INFORMATION(View view) {
		NavigateToChilds(ListActivity.class, LucaObject.INFORMATION);
	}

	public void TEMPERATURE(View view) {
		NavigateToChilds(ListActivity.class, LucaObject.TEMPERATURE);
	}

	public void USER(View view) {
		_dialogService.ShowUserDetailsDialog(_user, _updateUser);
	}

	private void initializeImageButtons() {
		ImageButton wirelessSocket = (ImageButton) findViewById(R.id.imageButtonSocket);
		wirelessSocket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.WIRELESS_SOCKET);
			}
		});

		ImageButton schedule = (ImageButton) findViewById(R.id.imageButtonSchedule);
		schedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.SCHEDULE);
			}
		});

		ImageButton timer = (ImageButton) findViewById(R.id.imageButtonTimer);
		timer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.TIMER);
			}
		});

		ImageButton temperature = (ImageButton) findViewById(R.id.imageButtonTemperature);
		temperature.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.TEMPERATURE);
			}
		});

		ImageButton movie = (ImageButton) findViewById(R.id.imageButtonMovie);
		movie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.MOVIE);
			}
		});

		ImageButton birthday = (ImageButton) findViewById(R.id.imageButtonBirthday);
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(ListActivity.class, LucaObject.BIRTHDAY);
			}
		});

		ImageButton sound = (ImageButton) findViewById(R.id.imageButtonSound);
		sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(SoundActivity.class, LucaObject.SOUND);
			}
		});

		ImageButton settings = (ImageButton) findViewById(R.id.imageButtonSettings);
		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NavigateToChilds(SettingsActivity.class, LucaObject.WIRELESS_SOCKET);
			}
		});
	}

	private void NavigateToChilds(Class<?> target, LucaObject lucaObject) {
		if (_downloadSuccess) {
			_navigationService.NavigateTo(target, CreateBundle(lucaObject), true);
		} else {
			_logger.Warn("Navigation not possible! Download of data failed!");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
