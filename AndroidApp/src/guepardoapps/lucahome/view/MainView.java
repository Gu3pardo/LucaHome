package guepardoapps.lucahome.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.enums.MainServiceAction;
import guepardoapps.lucahome.dto.UserDto;
import guepardoapps.lucahome.services.DialogService;
import guepardoapps.lucahome.services.NavigationService;
import guepardoapps.lucahome.services.UserService;

import guepardoapps.toolset.openweather.model.WeatherModel;

import guepardoapps.toolset.controller.BroadcastController;
import guepardoapps.toolset.controller.ReceiverController;

public class MainView extends Activity {

	private static final String TAG = MainView.class.getName();
	private LucaHomeLogger _logger;

	private TextView _temperatureErlangen;

	private boolean _isInitialized;

	private Context _context;

	private BroadcastController _broadcastController;
	private DialogService _dialogService;
	private NavigationService _navigationService;
	private ReceiverController _receiverController;
	private UserService _userService;

	private Runnable _updateUser = new Runnable() {
		@Override
		public void run() {
			Toast.makeText(_context, "Not yet implemented!", Toast.LENGTH_SHORT).show();
			_logger.Warn("Save updated user to raspberry not yet implemented!");
		}
	};

	private BroadcastReceiver _updateWeatherViewReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_updateWeatherViewReceiver onReceive");
			WeatherModel currentWeather = (WeatherModel) intent.getSerializableExtra(Constants.BUNDLE_WEATHER_CURRENT);
			if (currentWeather != null) {
				_logger.Debug(currentWeather.toString());
				_temperatureErlangen.setText(currentWeather.GetBasicText());
			} else {
				_logger.Warn("currentWeather is null!");
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_logger = new LucaHomeLogger(TAG);
		_logger.Debug("onCreate");

		checkDisplaySpecs();
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		initializeButtons();

		_temperatureErlangen = (TextView) findViewById(R.id.textViewTemperature);
		_temperatureErlangen.setText("Loading...");

		_context = this;

		_broadcastController = new BroadcastController(_context);
		_dialogService = new DialogService(_context);
		_navigationService = new NavigationService(_context);
		_receiverController = new ReceiverController(_context);
		_userService = new UserService(_context);
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");
		if (!_isInitialized) {
			if (_receiverController != null && _broadcastController != null) {
				_isInitialized = true;
				_receiverController.RegisterReceiver(_updateWeatherViewReceiver,
						new String[] { Constants.BROADCAST_UPDATE_WEATHER_VIEW });
				_broadcastController.SendSerializableArrayBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
						new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
						new Object[] { MainServiceAction.GET_WEATHER_CURRENT });
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
		_receiverController.UnregisterReceiver(_updateWeatherViewReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void CHANGE(View view) {
		_navigationService.NavigateTo(ChangeView.class);
	}

	public void INFORMATION(View view) {
		_navigationService.NavigateTo(InformationView.class);
	}

	public void TEMPERATURE(View view) {
		_navigationService.NavigateTo(TemperatureView.class);
	}

	public void USER(View view) {
		UserDto user = _userService.LoadUser();
		_dialogService.ShowUserDetailsDialog(user, _updateUser);
	}

	private void initializeButtons() {
		ImageButton wirelessSocket = (ImageButton) findViewById(R.id.imageButtonSocket);
		wirelessSocket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(SocketView.class);
			}
		});

		ImageButton flatMap = (ImageButton) findViewById(R.id.buttonFlatMap);
		flatMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(MapView.class);
			}
		});

		ImageButton schedule = (ImageButton) findViewById(R.id.imageButtonSchedule);
		schedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(ScheduleView.class);
			}
		});

		ImageButton timer = (ImageButton) findViewById(R.id.imageButtonTimer);
		timer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(TimerView.class);
			}
		});

		ImageButton temperature = (ImageButton) findViewById(R.id.imageButtonTemperature);
		temperature.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(TemperatureView.class);
			}
		});

		ImageButton movie = (ImageButton) findViewById(R.id.imageButtonMovie);
		movie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(MovieView.class);
			}
		});

		ImageButton birthday = (ImageButton) findViewById(R.id.imageButtonBirthday);
		birthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(BirthdayView.class);
			}
		});

		ImageButton sound = (ImageButton) findViewById(R.id.imageButtonSound);
		sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(SoundView.class);
			}
		});

		ImageButton smartmirror = (ImageButton) findViewById(R.id.imageButtonSmartMirror);
		smartmirror.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(MediaMirrorView.class);
			}
		});

		ImageButton settings = (ImageButton) findViewById(R.id.imageButtonSettings);
		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_navigationService.NavigateTo(SettingsView.class);
			}
		});
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
}
