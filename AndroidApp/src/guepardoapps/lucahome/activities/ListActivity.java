package guepardoapps.lucahome.activities;

import java.sql.Time;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import guepardoapps.common.BaseActivity;
import guepardoapps.common.Constants;
import guepardoapps.common.classes.*;
import guepardoapps.common.controller.*;
import guepardoapps.common.converter.json.JsonDataToBirthdayConverter;
import guepardoapps.common.converter.json.JsonDataToMovieConverter;
import guepardoapps.common.converter.json.JsonDataToScheduleConverter;
import guepardoapps.common.converter.json.JsonDataToSocketConverter;
import guepardoapps.common.converter.json.JsonDataToTimerConverter;
import guepardoapps.common.customadapter.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.TemperatureType;
import guepardoapps.common.service.NavigationService;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.WeatherModel;

public class ListActivity extends BaseActivity implements SensorEventListener {

	private static String TAG = "ListActivity";

	private LucaObject _lucaObject;

	private ProgressBar _progressBar;
	private ListView _listView;
	private Button _buttonAdd;

	private ListAdapter _listAdapter;

	private ReceiverController _receiverController;
	private ServiceController _serviceController;

	private SensorManager _sensorManager;
	private Sensor _sensor;
	private boolean _hasTemperatureSensor;
	private Handler _temperatureTimeoutHandler = new Handler();
	private int _temperatureTimeout = 10000;

	private Handler _downloadTimeoutHandler = new Handler();
	private int _downloadTimeout = 30000;

	private Runnable _temperatureTimeoutCheck = new Runnable() {
		public void run() {
			checkSensorAvailability();
		}
	};

	private Runnable _downloadTimeoutCheck = new Runnable() {
		public void run() {
			_receiverController.UnregisterReceiver(_downloadReceiver);
			_receiverController.UnregisterReceiver(_addReceiver);
			_receiverController.UnregisterReceiver(_startDownloadReceiver);

			Toast.makeText(_context, "Timeout of download received!", Toast.LENGTH_LONG).show();

			showList();
			checkButtonAddVisibility();
		}
	};

	private Runnable _showLoadingSpinner = new Runnable() {
		public void run() {
			_progressBar.setVisibility(View.VISIBLE);
			_listView.setVisibility(View.GONE);
			_buttonAdd.setVisibility(View.GONE);
		}
	};

	private BroadcastReceiver _temperatureReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast in _temperatureReceiver...");

			int id = intent.getIntExtra(Constants.BUNDLE_TEMPERATURE_ID, -1);
			Temperature updatedEntry;
			TemperatureType temperatureType = (TemperatureType) intent
					.getSerializableExtra(Constants.BUNDLE_TEMPERATURE_TYPE);
			switch (temperatureType) {
			case RASPBERRY:
				updatedEntry = (Temperature) intent.getSerializableExtra(Constants.BUNDLE_TEMPERATURE_SINGLE);
				_temperatureList.setValue(id, updatedEntry);
				break;
			case CITY:
				_currentWeather = (WeatherModel) intent.getSerializableExtra(Constants.BUNDLE_TEMPERATURE_SINGLE);
				updatedEntry = new Temperature(_currentWeather.GetTemperature(), _currentWeather.GetCity(),
						_currentWeather.GetLastUpdate(), "n.a.", TemperatureType.CITY, "n.a.");
				_temperatureList.setValue(id, updatedEntry);
				break;
			default:
				_logger.Warn(temperatureType.toString() + " is not supported!");
				break;
			}

			if (_temperatureList != null && _currentWeather != null) {
				_serviceController.StartTemperatureNotificationService(Constants.ID_NOTIFICATION_TEMPERATURE,
						_temperatureList, _currentWeather);
			}

			_listAdapter = new TemperatureListAdapter(_context, _temperatureList);
			_listView.setAdapter(_listAdapter);
		}
	};

	private BroadcastReceiver _downloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast in _downloadReceiver...");
			_receiverController.UnregisterReceiver(_downloadReceiver);

			stopDownloadTimeout();

			LucaObject lucaObject = (LucaObject) intent.getSerializableExtra(Constants.BUNDLE_LUCA_OBJECT);
			if (lucaObject == null) {
				_logger.Error("_downloadReceiver received data with null lucaobject!");
				return;
			}

			switch (lucaObject) {
			case BIRTHDAY:
				String[] birthdayStringArray = intent.getStringArrayExtra(Constants.BIRTHDAY_DOWNLOAD);
				if (birthdayStringArray != null) {
					_birthdayList = JsonDataToBirthdayConverter.GetList(birthdayStringArray);
					_listAdapter = new BirthdayListAdapter(_context, _birthdayList);
					_listView.setAdapter(_listAdapter);
				}
				break;
			case MOVIE:
				String[] movieStringArray = intent.getStringArrayExtra(Constants.MOVIE_DOWNLOAD);
				if (movieStringArray != null) {
					_movieList = JsonDataToMovieConverter.GetList(movieStringArray);
					_listAdapter = new MovieListAdapter(_context, _movieList);
					_listView.setAdapter(_listAdapter);
				}
				break;
			case SCHEDULE:
				String[] scheduleStringArray = intent.getStringArrayExtra(Constants.SCHEDULE_DOWNLOAD);
				if (scheduleStringArray != null && _wirelessSocketList != null) {
					_scheduleList = JsonDataToScheduleConverter.GetList(scheduleStringArray, _wirelessSocketList);
					_listAdapter = new ScheduleListAdapter(_context, _scheduleList);
					_listView.setAdapter(_listAdapter);
				}
				break;
			case TIMER:
				String[] timerStringArray = intent.getStringArrayExtra(Constants.SCHEDULE_DOWNLOAD);
				if (timerStringArray != null && _wirelessSocketList != null) {
					_timerList = JsonDataToTimerConverter.GetList(timerStringArray, _wirelessSocketList);
					_listAdapter = new TimerListAdapter(_context, _timerList);
					_listView.setAdapter(_listAdapter);
				}
				break;
			case WIRELESS_SOCKET:
				String[] socketStringArray = intent.getStringArrayExtra(Constants.SOCKET_DOWNLOAD);
				if (socketStringArray != null) {
					_wirelessSocketList = JsonDataToSocketConverter.GetList(socketStringArray);
					_listAdapter = new SocketListAdapter(_context, _wirelessSocketList);
					_listView.setAdapter(_listAdapter);
					_serviceController.StartSocketNotificationService(Constants.ID_NOTIFICATION_WEAR,
							_wirelessSocketList);
				}
				break;
			default:
				_logger.Error("Cannot parse object: " + lucaObject.toString());
				break;
			}

			showList();
			checkButtonAddVisibility();
		}
	};

	private BroadcastReceiver _addReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast in _addReceiver...");
			_receiverController.UnregisterReceiver(_addReceiver);

			LucaObject lucaObject = (LucaObject) intent.getSerializableExtra(Constants.BUNDLE_LUCA_OBJECT);
			if (lucaObject == null) {
				_logger.Error("_addReceiver received data with null lucaobject!");
				return;
			}

			String action = intent.getStringExtra(Constants.BUNDLE_ACTION);
			if (action == null) {
				_logger.Error("_addReceiver received data with null action!");
				return;
			}

			startDownloadTimeout();

			switch (lucaObject) {
			case BIRTHDAY:
				_receiverController.RegisterReceiver(_startDownloadReceiver,
						new String[] { Constants.BROADCAST_ADD_BIRTHDAY });
				_serviceController.StartRestService(Constants.BIRTHDAY_DOWNLOAD, action,
						Constants.BROADCAST_ADD_BIRTHDAY, lucaObject);
				break;
			case MOVIE:
				_receiverController.RegisterReceiver(_startDownloadReceiver,
						new String[] { Constants.BROADCAST_ADD_MOVIE });
				_serviceController.StartRestService(Constants.MOVIE_DOWNLOAD, action, Constants.BROADCAST_ADD_MOVIE,
						lucaObject);
				break;
			case SCHEDULE:
			case TIMER:
				_receiverController.RegisterReceiver(_startDownloadReceiver,
						new String[] { Constants.BROADCAST_ADD_SCHEDULE });
				_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, action,
						Constants.BROADCAST_ADD_SCHEDULE, lucaObject);
				break;
			case WIRELESS_SOCKET:
				_receiverController.RegisterReceiver(_startDownloadReceiver,
						new String[] { Constants.BROADCAST_ADD_SOCKET });
				_serviceController.StartRestService(Constants.SOCKET_DOWNLOAD, action, Constants.BROADCAST_ADD_SOCKET,
						lucaObject);
				break;
			default:
				_logger.Error("Cannot add object: " + lucaObject.toString());
				break;
			}
		}
	};

	private BroadcastReceiver _startDownloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast in _startDownloadReceiver...");
			_receiverController.UnregisterReceiver(_startDownloadReceiver);

			LucaObject lucaObject = (LucaObject) intent.getSerializableExtra(Constants.BUNDLE_LUCA_OBJECT);
			if (lucaObject == null) {
				_logger.Error("_downloadReceiver received data with null lucaobject!");
				return;
			}

			switch (lucaObject) {
			case BIRTHDAY:
				String[] birthdayAnswerArray = intent.getStringArrayExtra(Constants.BIRTHDAY_DOWNLOAD);
				boolean addBirthdaySuccess = true;
				for (String answer : birthdayAnswerArray) {
					if (answer.endsWith("1")) {
						addBirthdaySuccess &= true;
					} else {
						addBirthdaySuccess &= false;
					}
				}
				if (addBirthdaySuccess) {
					_receiverController.RegisterReceiver(_downloadReceiver,
							new String[] { Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED });
					_serviceController.StartRestService(Constants.BIRTHDAY_DOWNLOAD, Constants.ACTION_GET_BIRTHDAYS,
							Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED, lucaObject);
				} else {
					Toast.makeText(_context, "Add of birthday failed!", Toast.LENGTH_LONG).show();
				}
				break;
			case MOVIE:
				String[] movieAnswerArray = intent.getStringArrayExtra(Constants.MOVIE_DOWNLOAD);
				boolean addMovieSuccess = true;
				for (String answer : movieAnswerArray) {
					if (answer.endsWith("1")) {
						addMovieSuccess &= true;
					} else {
						addMovieSuccess &= false;
					}
				}
				if (addMovieSuccess) {
					_receiverController.RegisterReceiver(_downloadReceiver,
							new String[] { Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED });
					_serviceController.StartRestService(Constants.MOVIE_DOWNLOAD, Constants.ACTION_GET_MOVIES,
							Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED, lucaObject);
				} else {
					Toast.makeText(_context, "Add of movie failed!", Toast.LENGTH_LONG).show();
				}
				break;
			case SCHEDULE:
			case TIMER:
				String[] scheduleAnswerArray = intent.getStringArrayExtra(Constants.SCHEDULE_DOWNLOAD);
				boolean addScheduleSuccess = true;
				for (String answer : scheduleAnswerArray) {
					if (answer.endsWith("1")) {
						addScheduleSuccess &= true;
					} else {
						addScheduleSuccess &= false;
					}
				}
				if (addScheduleSuccess) {
					_receiverController.RegisterReceiver(_downloadReceiver,
							new String[] { Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED });
					_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, Constants.ACTION_GET_SCHEDULES,
							Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED, lucaObject);
				} else {
					Toast.makeText(_context, "Add of schedule failed!", Toast.LENGTH_LONG).show();
				}
				break;
			case WIRELESS_SOCKET:
				String[] socketAnswerArray = intent.getStringArrayExtra(Constants.SOCKET_DOWNLOAD);
				boolean addSocketSuccess = true;
				for (String answer : socketAnswerArray) {
					if (answer.endsWith("1")) {
						addSocketSuccess &= true;
					} else {
						addSocketSuccess &= false;
					}
				}
				if (addSocketSuccess) {
					_receiverController.RegisterReceiver(_downloadReceiver,
							new String[] { Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED });
					_serviceController.StartRestService(Constants.SOCKET_DOWNLOAD, Constants.ACTION_GET_SOCKETS,
							Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED, lucaObject);
				} else {
					Toast.makeText(_context, "Add of socket failed!", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				_logger.Error("Cannot start download object: " + lucaObject.toString());
				break;
			}
		}
	};

	private BroadcastReceiver _reloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast in _reloadReceiver...");

			switch (_lucaObject) {
			case BIRTHDAY:
				_receiverController.RegisterReceiver(_downloadReceiver,
						new String[] { Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED });
				_serviceController.StartRestService(Constants.BIRTHDAY_DOWNLOAD, Constants.ACTION_GET_BIRTHDAYS,
						Constants.BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED, _lucaObject);
				break;
			case MOVIE:
				_receiverController.RegisterReceiver(_downloadReceiver,
						new String[] { Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED });
				_serviceController.StartRestService(Constants.MOVIE_DOWNLOAD, Constants.ACTION_GET_MOVIES,
						Constants.BROADCAST_DOWNLOAD_MOVIE_FINISHED, _lucaObject);
				break;
			case SCHEDULE:
			case TIMER:
				_receiverController.RegisterReceiver(_downloadReceiver,
						new String[] { Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED });
				_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, Constants.ACTION_GET_SCHEDULES,
						Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED, _lucaObject);
				break;
			case WIRELESS_SOCKET:
				_receiverController.RegisterReceiver(_downloadReceiver,
						new String[] { Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED });
				_serviceController.StartRestService(Constants.SOCKET_DOWNLOAD, Constants.ACTION_GET_SOCKETS,
						Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED, LucaObject.WIRELESS_SOCKET);
				break;
			default:
				_logger.Debug("Nothing to do in _reloadReceiver for: " + _lucaObject.toString());
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_context = this;

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);

		_navigationService = new NavigationService(_context);

		setContentView(R.layout.view_skeleton_list);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			_logger.Debug("Extras are available!");
			_extrasAvailable = true;

			_lucaObject = (LucaObject) extras.getSerializable(Constants.BUNDLE_LUCA_OBJECT);
			_logger.Debug(_lucaObject.toString());

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

		initializeListView();
		initializeAddButton();

		if (_lucaObject == LucaObject.TEMPERATURE) {
			initializeSensor();
			_hasTemperatureSensor = checkSensorAvailability();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");

		switch (_lucaObject) {
		case BIRTHDAY:
			_receiverController.RegisterReceiver(_reloadReceiver, new String[] { Constants.BROADCAST_RELOAD_BIRTHDAY });
			break;
		case MOVIE:
			_receiverController.RegisterReceiver(_reloadReceiver, new String[] { Constants.BROADCAST_RELOAD_MOVIE });
			break;
		case SCHEDULE:
			_receiverController.RegisterReceiver(_reloadReceiver, new String[] { Constants.BROADCAST_RELOAD_SCHEDULE });
			break;
		case TEMPERATURE:
			_hasTemperatureSensor = checkSensorAvailability();
			_receiverController.RegisterReceiver(_temperatureReceiver,
					new String[] { Constants.BROADCAST_UPDATE_TEMPERATURE });
			break;
		case TIMER:
			_receiverController.RegisterReceiver(_reloadReceiver, new String[] { Constants.BROADCAST_RELOAD_TIMER });
			break;
		case WIRELESS_SOCKET:
			_receiverController.RegisterReceiver(_reloadReceiver, new String[] { Constants.BROADCAST_RELOAD_SOCKET });
			break;
		default:
			_logger.Debug("Nothing to do in onResume for: " + _lucaObject.toString());
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		_logger.Debug("onPause");
		if (_lucaObject == LucaObject.TEMPERATURE && _hasTemperatureSensor) {
			_sensorManager.unregisterListener(this);
			stopTemperatureTimeout();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		if (_lucaObject == LucaObject.TEMPERATURE && _hasTemperatureSensor) {
			stopTemperatureTimeout();
			_receiverController.UnregisterReceiver(_temperatureReceiver);
		}

		_receiverController.UnregisterReceiver(_downloadReceiver);
		_receiverController.UnregisterReceiver(_addReceiver);
		_receiverController.UnregisterReceiver(_startDownloadReceiver);
		_receiverController.UnregisterReceiver(_reloadReceiver);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			double temperature = event.values[0];
			temperature = Math.floor(temperature * 100) / 100;

			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			@SuppressWarnings("deprecation")
			Time time = new Time(hour, minute, second);

			Temperature newEntry = new Temperature(temperature, "Ambient temperature", time, "n.a.",
					TemperatureType.SMARTPHONE_SENSOR, "n.a.");

			for (int index = 0; index < _temperatureList.getSize(); index++) {
				if (_temperatureList.getValue(index).GetTemperatureType() == TemperatureType.SMARTPHONE_SENSOR) {
					_temperatureList.removeValue(_temperatureList.getValue(index));
				}
			}

			_temperatureList.addValue(newEntry);
			_listAdapter = new TemperatureListAdapter(_context, _temperatureList);
			_listView.setAdapter(_listAdapter);

			_sensorManager.unregisterListener(this);
			startTemperatureTimeout();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	private void initializeSensor() {
		_sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		_sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
	}

	private boolean checkSensorAvailability() {
		if (_sensor != null) {
			_logger.Debug("Sensor is available");
			_sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
			return true;
		} else {
			_logger.Debug("Sensor is not available");
			return false;
		}
	}

	private void startTemperatureTimeout() {
		_logger.Debug("Starting temperatureTimeoutController...");
		_temperatureTimeoutHandler.postDelayed(_temperatureTimeoutCheck, _temperatureTimeout);
	}

	private void stopTemperatureTimeout() {
		_temperatureTimeoutHandler.removeCallbacks(_temperatureTimeoutCheck);
	}

	private void startDownloadTimeout() {
		_logger.Debug("Starting downloadTimeoutController...");
		_downloadTimeoutHandler.postDelayed(_downloadTimeoutCheck, _downloadTimeout);
	}

	private void stopDownloadTimeout() {
		_downloadTimeoutHandler.removeCallbacks(_downloadTimeoutCheck);
	}

	private void initializeListView() {
		_listView = (ListView) findViewById(R.id.listView);
		_progressBar = (ProgressBar) findViewById(R.id.progressBarListView);

		_listAdapter = null;

		switch (_lucaObject) {
		case BIRTHDAY:
			_listAdapter = new BirthdayListAdapter(_context, _birthdayList);
			break;
		case CHANGE:
			_listAdapter = new ChangeListAdapter(_context, _changeList);
			break;
		case INFORMATION:
			_listAdapter = new InformationListAdapter(_context, _information);
			break;
		case MOVIE:
			_listAdapter = new MovieListAdapter(_context, _movieList);
			break;
		case SCHEDULE:
			_listAdapter = new ScheduleListAdapter(_context, _scheduleList);
			break;
		case TEMPERATURE:
			_listAdapter = new TemperatureListAdapter(_context, _temperatureList);
			break;
		case TIMER:
			_listAdapter = new TimerListAdapter(_context, _timerList);
			break;
		case WIRELESS_SOCKET:
			_listAdapter = new SocketListAdapter(_context, _wirelessSocketList);
			break;
		default:
			_logger.Error("Cannot display " + _lucaObject.toString());
			finish();
			break;
		}

		if (_listAdapter == null) {
			_logger.Error("listAdapter is null!");
			finish();
		}
		_listView.setAdapter(_listAdapter);

		showList();
	}

	private void initializeAddButton() {
		_buttonAdd = (Button) findViewById(R.id.buttonAddListView);
		_buttonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("onClick buttonAdd");

				switch (_lucaObject) {
				case BIRTHDAY:
					_receiverController.RegisterReceiver(_addReceiver,
							new String[] { Constants.BROADCAST_ADD_BIRTHDAY });
					_dialogService.ShowAddBirthdayDialog(_birthdayList.getSize(), _showLoadingSpinner);
					break;
				case MOVIE:
					_receiverController.RegisterReceiver(_addReceiver, new String[] { Constants.BROADCAST_ADD_MOVIE });
					_dialogService.ShowAddMovieDialog(_showLoadingSpinner);
					break;
				case SCHEDULE:
					_receiverController.RegisterReceiver(_addReceiver,
							new String[] { Constants.BROADCAST_ADD_SCHEDULE });
					_dialogService.ShowAddScheduleDialog(false, _showLoadingSpinner, _wirelessSocketList);
					break;
				case TIMER:
					_receiverController.RegisterReceiver(_addReceiver,
							new String[] { Constants.BROADCAST_ADD_SCHEDULE });
					_dialogService.ShowAddScheduleDialog(true, _showLoadingSpinner, _wirelessSocketList);
					break;
				case WIRELESS_SOCKET:
					_receiverController.RegisterReceiver(_addReceiver, new String[] { Constants.BROADCAST_ADD_SOCKET });
					_dialogService.ShowAddSocketDialog(_showLoadingSpinner);
					break;
				default:
					_logger.Error("Cannot add new " + _lucaObject.toString());
					break;
				}
			}
		});

		checkButtonAddVisibility();
	}

	private void showList() {
		_progressBar.setVisibility(View.GONE);
		_listView.setVisibility(View.VISIBLE);
	}

	private void checkButtonAddVisibility() {
		if (_lucaObject == LucaObject.CHANGE || _lucaObject == LucaObject.INFORMATION
				|| _lucaObject == LucaObject.TEMPERATURE) {
			_buttonAdd.setVisibility(View.GONE);
		} else {
			_buttonAdd.setVisibility(View.VISIBLE);
		}
	}
}
