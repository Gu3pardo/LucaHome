package guepardoapps.common.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Birthday;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.Movie;
import guepardoapps.common.classes.Schedule;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.Timer;
import guepardoapps.common.classes.User;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.classes.controller.SocketController;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.Weekday;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.MailController;
import guepardoapps.toolset.controller.SharedPrefController;

public class DialogService extends DialogController {

	private static String TAG = "DialogService";

	private Logger _logger;

	private Context _context;

	private LucaObject _lucaObject;
	private Birthday _birthday;
	private Movie _movie;
	private WirelessSocket _socket;
	private Schedule _schedule;
	private Timer _timer;

	private String _scheduleName;
	private String _scheduleSocketString;
	private String _scheduleWeekdayString;
	private String _scheduleActionString;
	private Time _scheduleTime;

	private boolean _isDialogOpen;
	private Dialog _dialog;

	private MailController _mailController;
	private ServiceController _serviceController;
	private SharedPrefController _sharedPrefController;
	private SocketController _socketController;

	private UserService _userService;

	private Runnable _storedRunnable = null;

	private Runnable _closeDialogCallback = new Runnable() {
		@Override
		public void run() {
			resetValues();
			if (_dialog != null) {
				_dialog.dismiss();
			}
			_isDialogOpen = false;
		}
	};

	private Runnable _userValidationCallback = new Runnable() {
		@Override
		public void run() {
			if (_userService.GetValidationResult()) {
				_sharedPrefController.SaveBooleanValue(Constants.USER_DATA_ENTERED, true);

				_closeDialogCallback.run();
				if (_storedRunnable != null) {
					_storedRunnable.run();
					_storedRunnable = null;
				}
			} else {
				final EditText userNameEdit = (EditText) _dialog.findViewById(R.id.dialog_user_input);
				userNameEdit.setText("Invalid user!");
				userNameEdit.selectAll();
			}
		}
	};

	private Runnable _sendMail = new Runnable() {
		@Override
		public void run() {
			_mailController.SendMail("guepardoapps@gmail.com");
		}
	};

	private Runnable _updateRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO implement

			checkOpenDialog();
			Toast.makeText(_context, "Update of objects not implemented!", Toast.LENGTH_LONG).show();
			_logger.Warn("Update of objects not implemented!");

			switch (_lucaObject) {
			case BIRTHDAY:
				break;
			case MOVIE:
				break;
			case WIRELESS_SOCKET:
				break;
			case SCHEDULE:
				break;
			case TIMER:
				break;
			default:
				_logger.Warn("Not possible to update object " + _lucaObject.toString());
				break;
			}
		}
	};

	private Runnable _deletePromptRunnable = new Runnable() {
		@Override
		public void run() {
			checkOpenDialog();

			if (_lucaObject == null) {
				_logger.Error("_lucaObject is null!");
				_closeDialogCallback.run();
				return;
			}

			switch (_lucaObject) {
			case BIRTHDAY:
				ShowDialogDouble("Delete Birthday", "Do you really want to delete the birthday?", "Yes",
						_deleteRunnable, "Cancel", _closeDialogCallback, false);
				_isDialogOpen = true;
				break;
			case MOVIE:
				ShowDialogDouble("Delete Movie", "Do you really want to delete the movie?", "Yes", _deleteRunnable,
						"Cancel", _closeDialogCallback, false);
				_isDialogOpen = true;
				break;
			case WIRELESS_SOCKET:
				ShowDialogDouble("Delete Socket", "Do you really want to delete the socket?", "Yes", _deleteRunnable,
						"Cancel", _closeDialogCallback, false);
				_isDialogOpen = true;
				break;
			case SCHEDULE:
				ShowDialogDouble("Delete Schedule", "Do you really want to delete the schedule?", "Yes",
						_deleteRunnable, "Cancel", _closeDialogCallback, false);
				_isDialogOpen = true;
				break;
			case TIMER:
				ShowDialogDouble("Delete Timer", "Do you really want to delete the timer?", "Yes", _deleteRunnable,
						"Cancel", _closeDialogCallback, false);
				_isDialogOpen = true;
				break;
			default:
				_logger.Warn("Not possible to delete object " + _lucaObject.toString());
				break;
			}
		}
	};

	private Runnable _deleteRunnable = new Runnable() {
		@Override
		public void run() {
			switch (_lucaObject) {
			case BIRTHDAY:
				if (_birthday == null) {
					_logger.Error("_birthday is null!");
					return;
				}
				_serviceController.StartRestService(_birthday.GetName(), _birthday.GetCommandDelete(),
						Constants.BROADCAST_RELOAD_BIRTHDAY, _lucaObject);
				break;
			case MOVIE:
				if (_movie == null) {
					_logger.Error("_movie is null!");
					return;
				}
				_serviceController.StartRestService(_movie.GetTitle(), _movie.GetCommandDelete(),
						Constants.BROADCAST_RELOAD_MOVIE, _lucaObject);
				break;
			case WIRELESS_SOCKET:
				if (_socket == null) {
					_logger.Error("_socket is null!");
					return;
				}
				_serviceController.StartRestService(_socket.GetName(), _socket.GetCommandDelete(),
						Constants.BROADCAST_RELOAD_SOCKET, _lucaObject);
				break;
			case SCHEDULE:
				if (_schedule == null) {
					_logger.Error("_schedule is null!");
					return;
				}
				_serviceController.StartRestService(_schedule.GetName(), _schedule.GetCommandDelete(),
						Constants.BROADCAST_RELOAD_SCHEDULE, _lucaObject);
				break;
			case TIMER:
				if (_timer == null) {
					_logger.Error("_timer is null!");
					return;
				}
				_serviceController.StartRestService(_timer.GetName(), _timer.GetCommandDelete(),
						Constants.BROADCAST_RELOAD_TIMER, _lucaObject);
				break;
			default:
				_logger.Warn("Still not possible to delete object " + _lucaObject.toString());
				break;
			}

			_closeDialogCallback.run();
		}
	};

	public DialogService(Context context) {
		super(context, ContextCompat.getColor(context, R.color.TextIcon),
				ContextCompat.getColor(context, R.color.Background));
		_logger = new Logger(TAG);

		_context = context;

		_isDialogOpen = false;

		_mailController = new MailController(_context);
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);
		_socketController = new SocketController(_context);

		_userService = new UserService(_context);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void ShowTemperatureGraphDialog(String graphPath) {
		checkOpenDialog();

		createDialog("ShowTemperatureGraphDialog: " + graphPath, R.layout.dialog_temperature_graph);

		final ProgressBar progressBar = (ProgressBar) _dialog.findViewById(R.id.temperature_dialog_progressbar);

		final WebView webView = (WebView) _dialog.findViewById(R.id.temperature_dialog_webview);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.setInitialScale(100);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(false);
		webView.loadUrl("http://" + graphPath);
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
			}
		});

		Button btnOk = (Button) _dialog.findViewById(R.id.temperature_dialog_button);
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_closeDialogCallback.run();
			}
		});

		showDialog(false);
	}

	public void ShowUserCredentialsDialog(User user, final Runnable runnable, boolean isCancelable) {
		checkOpenDialog();

		createDialog("ShowUserCredentialsDialog", R.layout.dialog_user_data_update);

		final EditText userNameEdit = (EditText) _dialog.findViewById(R.id.dialog_user_input);
		final EditText passwordEdit = (EditText) _dialog.findViewById(R.id.dialog_password_input);

		if (user != null) {
			userNameEdit.setText(user.GetUserName());
			userNameEdit.selectAll();
			passwordEdit.setText(user.GetPassword());
		}

		Button btnSave = (Button) _dialog.findViewById(R.id.dialog_user_save_button);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = userNameEdit.getText().toString();
				if (userName.length() < 3 || userName.contains("Enter valid username!")
						|| userName.contains("Invalid user!")) {
					userNameEdit.setText("Enter valid username!");
					userNameEdit.selectAll();
					return;
				}

				String password = passwordEdit.getText().toString();
				if (password.length() < 4) {
					passwordEdit.selectAll();
					return;
				}

				_sharedPrefController.SaveStringValue(Constants.USER_NAME, userName);
				_sharedPrefController.SaveStringValue(Constants.USER_PASSPHRASE, password);

				if (runnable != null) {
					_storedRunnable = runnable;
				}

				_userService.ValidateUser(new User(userName, password), _userValidationCallback);
			}
		});

		showDialog(isCancelable);
	}

	public void ShowUserDetailsDialog(final User user, final Runnable runnable) {
		checkOpenDialog();

		createDialog("ShowUserDetailsDialog", R.layout.dialog_user_data);

		TextView userNameTextView = (TextView) _dialog.findViewById(R.id.dialog_user_name);
		userNameTextView.setText(user.GetUserName());

		TextView passwordTextView = (TextView) _dialog.findViewById(R.id.dialog_user_password);
		passwordTextView.setText(user.GetPassword());

		Button btnClose = (Button) _dialog.findViewById(R.id.dialog_user_close_button);
		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_closeDialogCallback.run();
			}
		});

		Button btnUpdate = (Button) _dialog.findViewById(R.id.dialog_user_update_button);
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_closeDialogCallback.run();
				Toast.makeText(_context, "Not yet implemented!", Toast.LENGTH_SHORT).show();
				_logger.Warn("Update user not yet implemented!");
				// ShowUserCredentialsDialog(user, updateUserRunnable, true);
			}
		});

		showDialog(false);
	}

	public void ShowAddBirthdayDialog(final int id, final Runnable runnable) {
		checkOpenDialog();

		createDialog("ShowAddBirthdayDialog", R.layout.dialog_add_birthday);

		final EditText birthdayNameEdit = (EditText) _dialog.findViewById(R.id.dialog_birthday_name_input);

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		final DatePicker birthdayDatePicker = (DatePicker) _dialog.findViewById(R.id.dialog_birthday_datepicker);
		birthdayDatePicker.init(year, month, day, null);

		Button btnSave = (Button) _dialog.findViewById(R.id.dialog_birthday_save_button);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = birthdayNameEdit.getText().toString();
				if (name.length() < 3) {
					Toast.makeText(_context, "Name too short!", Toast.LENGTH_LONG).show();
					return;
				}

				int birthdayDay = birthdayDatePicker.getDayOfMonth();
				int birthdayMonth = birthdayDatePicker.getMonth();
				int birthdayYear = birthdayDatePicker.getYear();
				@SuppressWarnings("deprecation")
				Date birthdayDate = new Date(birthdayYear, birthdayMonth, birthdayDay);

				Birthday newBirthday = new Birthday(name, birthdayDate, id);
				_logger.Debug("new Birthday: " + newBirthday.toString());

				runnable.run();

				sendBroadCast(Constants.BROADCAST_ADD_BIRTHDAY, LucaObject.BIRTHDAY, newBirthday.GetCommandAdd());

				_closeDialogCallback.run();
			}
		});

		showDialog(true);
	}

	public void ShowAddMovieDialog(final Runnable runnable) {
		checkOpenDialog();

		createDialog("ShowAddMovieDialog", R.layout.dialog_add_movie);

		final EditText movieTitleEdit = (EditText) _dialog.findViewById(R.id.dialog_movie_title_input);
		final EditText movieGenreEdit = (EditText) _dialog.findViewById(R.id.dialog_movie_genre_input);
		final EditText movieDescriptionEdit = (EditText) _dialog.findViewById(R.id.dialog_movie_description_input);

		final RatingBar movieRatingbar = (RatingBar) _dialog.findViewById(R.id.dialog_movie_description_ratingbar);
		movieRatingbar.setEnabled(true);

		Button btnSave = (Button) _dialog.findViewById(R.id.dialog_movie_save_button);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = movieTitleEdit.getText().toString();
				if (title.length() < 3) {
					Toast.makeText(_context, "Title too short!", Toast.LENGTH_LONG).show();
					return;
				}

				String genre = movieGenreEdit.getText().toString();
				String description = movieDescriptionEdit.getText().toString();

				int rating = Math.round(movieRatingbar.getRating());

				Movie newMovie = new Movie(title, genre, description, rating, 0, null);
				_logger.Debug("new Movie: " + newMovie.toString());

				runnable.run();

				sendBroadCast(Constants.BROADCAST_ADD_MOVIE, LucaObject.MOVIE, newMovie.GetCommandAdd());

				_closeDialogCallback.run();
			}
		});

		showDialog(true);
	}

	public void ShowAddSocketDialog(final Runnable runnable) {
		checkOpenDialog();

		createDialog("ShowAddSocketDialog", R.layout.dialog_add_socket);

		final EditText socketNameEdit = (EditText) _dialog.findViewById(R.id.dialog_socket_name_input);
		final EditText socketAreaEdit = (EditText) _dialog.findViewById(R.id.dialog_socket_area_input);
		final EditText socketCodeEdit = (EditText) _dialog.findViewById(R.id.dialog_socket_code_input);

		Button btnSave = (Button) _dialog.findViewById(R.id.dialog_socket_save_button);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = socketNameEdit.getText().toString();
				if (name.length() < 3) {
					Toast.makeText(_context, "Name too short!", Toast.LENGTH_LONG).show();
					return;
				}
				String area = socketAreaEdit.getText().toString();
				if (area.length() < 3) {
					Toast.makeText(_context, "Area too short!", Toast.LENGTH_LONG).show();
					return;
				}
				String code = socketCodeEdit.getText().toString();
				if (!_socketController.ValidateSocketCode(code)) {
					Toast.makeText(_context, "Code invalid!", Toast.LENGTH_LONG).show();
					return;
				}

				WirelessSocket newSocket = new WirelessSocket(name, area, code, false);
				_logger.Debug("new Socket: " + newSocket.toString());

				runnable.run();

				sendBroadCast(Constants.BROADCAST_ADD_SOCKET, LucaObject.WIRELESS_SOCKET, newSocket.GetCommandAdd());

				_closeDialogCallback.run();
			}
		});

		showDialog(true);
	}

	@SuppressWarnings("deprecation")
	public void ShowAddScheduleDialog(final boolean isTimer, final Runnable runnable,
			final SerializableList<WirelessSocket> socketList) {
		checkOpenDialog();

		createDialog("ShowAddScheduleDialog", R.layout.dialog_add_schedule);

		final EditText scheduleNameEdit = (EditText) _dialog.findViewById(R.id.dialog_schedule_name_input);

		final Spinner scheduleSocketSelect = (Spinner) _dialog.findViewById(R.id.dialog_schedule_socket_select);
		List<String> sockets = new ArrayList<String>();
		for (int socketIndex = 0; socketIndex < socketList.getSize(); socketIndex++) {
			sockets.add(socketList.getValue(socketIndex).GetName());
		}
		ArrayAdapter<String> socketDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, sockets);
		socketDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scheduleSocketSelect.setAdapter(socketDataAdapter);
		scheduleSocketSelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				_scheduleSocketString = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		final Spinner scheduleWeekdaySelect = (Spinner) _dialog.findViewById(R.id.dialog_schedule_weekday_select);
		List<String> weekdays = new ArrayList<String>();
		for (Weekday weekday : Weekday.values()) {
			weekdays.add(weekday.GetEnglishDay());
		}
		ArrayAdapter<String> weekdayDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, weekdays);
		weekdayDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scheduleWeekdaySelect.setAdapter(weekdayDataAdapter);
		scheduleWeekdaySelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				_scheduleWeekdayString = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		final Spinner scheduleActionSelect = (Spinner) _dialog.findViewById(R.id.dialog_schedule_action_select);
		List<String> actions = new ArrayList<String>();
		actions.add("ON");
		actions.add("OFF");
		ArrayAdapter<String> actionDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, actions);
		actionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		scheduleActionSelect.setAdapter(actionDataAdapter);
		scheduleActionSelect.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				_scheduleActionString = arg0.getItemAtPosition(arg2).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		final TimePicker scheduleTimePicker = (TimePicker) _dialog.findViewById(R.id.dialog_schedule_time_picker);
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		scheduleTimePicker.setIs24HourView(true);
		scheduleTimePicker.setCurrentHour(hour);
		scheduleTimePicker.setCurrentMinute(minute);

		Button btnSave = (Button) _dialog.findViewById(R.id.dialog_schedule_save_button);
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_scheduleName = scheduleNameEdit.getText().toString();
				if (_scheduleName.length() < 3) {
					Toast.makeText(_context, "Name too short!", Toast.LENGTH_LONG).show();
					return;
				}
				if (_scheduleSocketString == null || _scheduleSocketString == "") {
					Toast.makeText(_context, "Please select a socket!", Toast.LENGTH_LONG).show();
					return;
				}
				if (_scheduleWeekdayString == null || _scheduleWeekdayString == "") {
					Toast.makeText(_context, "Please select a weekday!", Toast.LENGTH_LONG).show();
					return;
				}
				if (_scheduleActionString == null || _scheduleActionString == "") {
					Toast.makeText(_context, "Please select an action!", Toast.LENGTH_LONG).show();
					return;
				}

				WirelessSocket socket = null;
				for (int socketIndex = 0; socketIndex < socketList.getSize(); socketIndex++) {
					if (socketList.getValue(socketIndex).GetName().contains(_scheduleName)) {
						socket = socketList.getValue(socketIndex);
						break;
					}
				}
				if (socket == null) {
					Toast.makeText(_context, "Please select a valid socket!", Toast.LENGTH_LONG).show();
					return;
				}

				Weekday weekday = Weekday.GetByEnglishString(_scheduleWeekdayString);
				if (weekday == Weekday.DUMMY || weekday == null) {
					Toast.makeText(_context, "Please select a valid weekday!", Toast.LENGTH_LONG).show();
					return;
				}

				int scheduleHour = scheduleTimePicker.getCurrentHour();
				int scheduleMinute = scheduleTimePicker.getCurrentMinute();
				_scheduleTime = new Time(scheduleHour, scheduleMinute, 0);

				boolean action;
				if (_scheduleActionString.contains("ON")) {
					action = true;
				} else if (_scheduleActionString.contains("OFF")) {
					action = false;
				} else {
					Toast.makeText(_context, "Please select a valid action!", Toast.LENGTH_LONG).show();
					return;
				}

				runnable.run();

				if (isTimer) {
					Timer newTimer = new Timer(_scheduleName, socket, weekday, _scheduleTime, action, true);
					_logger.Debug("new Timer: " + newTimer.toString());
					sendBroadCast(Constants.BROADCAST_ADD_SCHEDULE, LucaObject.TIMER, newTimer.GetCommandAdd());
				} else {
					Schedule newSchedule = new Schedule(_scheduleName, socket, weekday, _scheduleTime, action, false,
							true);
					_logger.Debug("new Schedule: " + newSchedule.toString());
					sendBroadCast(Constants.BROADCAST_ADD_SCHEDULE, LucaObject.SCHEDULE, newSchedule.GetCommandAdd());
				}
				_closeDialogCallback.run();
			}
		});

		showDialog(true);
	}

	public void ShowSendInformationMailDialog() {
		ShowDialogSingle("Send Mail", "", "Send", _sendMail, true);
	}

	public void ShowUpdateBirthdayDialog(Birthday value) {
		_lucaObject = LucaObject.BIRTHDAY;
		_birthday = value;
		ShowDialogTriple("Birthday", _birthday.GetName(), "Update", _updateRunnable, "Delete", _deletePromptRunnable,
				"Cancel", _closeDialogCallback, false);
	}

	public void ShowUpdateMovieDialog(Movie value) {
		_lucaObject = LucaObject.MOVIE;
		_movie = value;
		ShowDialogTriple("Movie", _movie.GetTitle(), "Update", _updateRunnable, "Delete", _deletePromptRunnable,
				"Cancel", _closeDialogCallback, false);
	}

	public void ShowUpdateSocketDialog(WirelessSocket value) {
		_lucaObject = LucaObject.WIRELESS_SOCKET;
		_socket = value;
		ShowDialogTriple("Socket", _socket.GetName(), "Update", _updateRunnable, "Delete", _deletePromptRunnable,
				"Cancel", _closeDialogCallback, false);
	}

	public void ShowUpdateScheduleDialog(Schedule value) {
		_lucaObject = LucaObject.SCHEDULE;
		_schedule = value;
		ShowDialogTriple("Schedule", _schedule.GetName(), "Update", _updateRunnable, "Delete", _deletePromptRunnable,
				"Cancel", _closeDialogCallback, false);
	}

	public void ShowUpdateTimerDialog(Timer value) {
		_lucaObject = LucaObject.TIMER;
		_timer = value;
		ShowDialogTriple("Timer", _timer.GetName(), "Update", _updateRunnable, "Delete", _deletePromptRunnable,
				"Cancel", _closeDialogCallback, false);
	}

	private void createDialog(String dialogType, int layout) {
		_logger.Debug(dialogType);

		_dialog = new Dialog(_context);

		_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		_dialog.setContentView(layout);
	}

	@SuppressWarnings("deprecation")
	private void showDialog(boolean isCancelable) {
		_dialog.setCancelable(isCancelable);
		_dialog.show();

		Window window = _dialog.getWindow();
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		_isDialogOpen = true;
	}

	private void checkOpenDialog() {
		if (_isDialogOpen) {
			_logger.Warn("Closing other Dialog...");
			_closeDialogCallback.run();
		}
	}

	private void sendBroadCast(String broadcast, LucaObject lucaObject, String action) {
		Intent broadcastIntent = new Intent(broadcast);

		Bundle broadcastData = new Bundle();
		broadcastData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, lucaObject);
		broadcastData.putString(Constants.BUNDLE_ACTION, action);
		broadcastIntent.putExtras(broadcastData);

		_context.sendBroadcast(broadcastIntent);
	}

	private void resetValues() {
		_lucaObject = null;
		_birthday = null;
		_movie = null;
		_socket = null;
		_schedule = null;
		_timer = null;
	}
}