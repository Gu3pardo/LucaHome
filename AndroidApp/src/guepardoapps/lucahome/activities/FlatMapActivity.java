package guepardoapps.lucahome.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.*;
import guepardoapps.common.classes.controller.MapContentController;
import guepardoapps.common.classes.controller.ScheduleController;
import guepardoapps.common.classes.controller.SocketController;
import guepardoapps.common.classes.controller.TimerController;
import guepardoapps.common.controller.ReceiverController;
import guepardoapps.common.converter.json.JsonDataToMapContentConverter;
import guepardoapps.common.converter.json.JsonDataToScheduleConverter;
import guepardoapps.common.converter.json.JsonDataToSocketConverter;
import guepardoapps.common.converter.json.JsonDataToTimerConverter;
import guepardoapps.common.enums.DrawingType;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.openweather.ForecastModel;
import guepardoapps.toolset.openweather.WeatherModel;

public class FlatMapActivity extends ListActivity {

	private static String TAG = "FlatMapActivity";

	private Button _buttonAddRaspberry;
	private Button _buttonAddArduino;
	private Button _buttonAddSocket;
	private Button _buttonRemove;

	private RelativeLayout _mapPaintView;
	private ImageView _mapImageView;

	private boolean _addRaspberry;
	private boolean _addArduino;
	private boolean _addSocket;
	private boolean _remove;

	private SerializableList<MapContent> _mapContentList;

	private MapContentController _mapContentController;
	private ScheduleController _scheduleController;
	private SocketController _socketController;
	private TimerController _timerController;

	private BroadcastReceiver _reloadMapContentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_mapContentController.LoadMapContents();
		}
	};

	private BroadcastReceiver _reloadSchedulesReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_scheduleController.LoadSchedules();
		}
	};

	private BroadcastReceiver _reloadSocketsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_socketController.LoadSockets();
		}
	};

	private BroadcastReceiver _reloadTimerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_timerController.LoadTimer();
		}
	};

	private BroadcastReceiver _downloadMapContentFinishedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] answerArray = intent.getStringArrayExtra("MapContentController");
			if (answerArray != null) {
				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				Point size = new Point();
				size.x = displayMetrics.widthPixels;
				size.y = displayMetrics.heightPixels;

				_mapContentList = JsonDataToMapContentConverter.GetList(answerArray, size);
				for (int index = 0; index < _mapContentList.getSize(); index++) {
					addView(_mapContentList.getValue(index), _mapContentList.getValue(index).GetPosition(), false);
				}
			}
		}
	};

	private BroadcastReceiver _downloadSocketFinishedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] answerArray = intent.getStringArrayExtra(Constants.SOCKET_DOWNLOAD);
			if (answerArray != null) {
				_wirelessSocketList = JsonDataToSocketConverter.GetList(answerArray);
			}
		}
	};

	private BroadcastReceiver _downloadScheduleFinishedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] answerArray = intent.getStringArrayExtra(Constants.SCHEDULE_DOWNLOAD);
			if (answerArray != null && _wirelessSocketList != null) {
				_scheduleList = JsonDataToScheduleConverter.GetList(answerArray, _wirelessSocketList);
				_timerList = JsonDataToTimerConverter.GetList(answerArray, _wirelessSocketList);
			}
		}
	};

	private OnTouchListener _mapOnTouchListener = new View.OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			_logger.Debug("_mapOnTouchListener onTouch");
			_logger.Debug("view: " + view.toString());
			_logger.Debug("motionEvent: " + motionEvent.toString());

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				Point clickPosition = new Point();
				clickPosition.set((int) motionEvent.getX(), (int) motionEvent.getY());

				if (_addRaspberry) {
					_logger.Debug("_addRaspberry");
					DrawingType drawingType = DrawingType.RASPBERRY;

					MapContent newMapContent = new MapContent(-1, clickPosition, drawingType, null, null, null);
					_logger.Debug("newMapContent: " + newMapContent.toString());

					// addView(newMapContent, clickPosition, true);
					_logger.Warn("Reactivate addView while adding new raspberry via dialog!");
					Toast.makeText(_context, "Reactivate addView while adding new raspberry via dialog!",
							Toast.LENGTH_SHORT).show();

					_addRaspberry = false;
					_buttonAddRaspberry.setBackgroundResource(R.drawable.add_round);

					return true;
				} else if (_addArduino) {
					_logger.Debug("_addArduino");
					DrawingType drawingType = DrawingType.ARDUINO;

					MapContent newMapContent = new MapContent(-1, clickPosition, drawingType, null, null, null);
					_logger.Debug("newMapContent: " + newMapContent.toString());

					// addView(newMapContent, clickPosition, true);
					_logger.Warn("Reactivate addView while adding new arduino via dialog!");
					Toast.makeText(_context, "Reactivate addView while adding new arduino via dialog!",
							Toast.LENGTH_SHORT).show();

					_addArduino = false;
					_buttonAddArduino.setBackgroundResource(R.drawable.add_round);

					return true;
				} else if (_addSocket) {
					_logger.Debug("_addSocket");
					DrawingType drawingType = DrawingType.SOCKET;

					MapContent newMapContent = new MapContent(-1, clickPosition, drawingType, null, null, null);
					_logger.Debug("newMapContent: " + newMapContent.toString());

					// addView(newMapContent, clickPosition, true);
					_logger.Warn("Reactivate addView while adding new socket via dialog!");
					Toast.makeText(_context, "Reactivate addView while adding new socket via dialog!",
							Toast.LENGTH_SHORT).show();

					_addSocket = false;
					_buttonAddSocket.setBackgroundResource(R.drawable.add_round);

					return true;
				} else if (_remove) {
					_logger.Debug("_remove");

					return true;
				}
			}

			return false;
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;

		_mapContentController = new MapContentController(_context);
		_receiverController = new ReceiverController(_context);
		_scheduleController = new ScheduleController(_context);
		_socketController = new SocketController(_context);
		_timerController = new TimerController(_context);

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

		setContentView(R.layout.view_flat_map);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_addRaspberry = false;
		_addArduino = false;
		_addSocket = false;
		_remove = false;

		_mapPaintView = (RelativeLayout) findViewById(R.id.mapPaintView);
		_mapPaintView.setOnTouchListener(_mapOnTouchListener);

		_mapImageView = (ImageView) findViewById(R.id.mapImageView);
		_mapImageView.setOnTouchListener(_mapOnTouchListener);

		initializeButtons();
		_mapContentController.LoadMapContents();
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");

		_receiverController.RegisterReceiver(_downloadMapContentFinishedReceiver,
				new String[] { Constants.BROADCAST_UPDATE_MAP_CONTENT_VIEW });
		_receiverController.RegisterReceiver(_downloadScheduleFinishedReceiver,
				new String[] { Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED });
		_receiverController.RegisterReceiver(_downloadSocketFinishedReceiver,
				new String[] { Constants.BROADCAST_DOWNLOAD_SOCKET_FINISHED });

		_receiverController.RegisterReceiver(_reloadMapContentReceiver,
				new String[] { Constants.BROADCAST_RELOAD_MAP_CONTENT });
		_receiverController.RegisterReceiver(_reloadSchedulesReceiver,
				new String[] { Constants.BROADCAST_RELOAD_SCHEDULE });
		_receiverController.RegisterReceiver(_reloadSocketsReceiver,
				new String[] { Constants.BROADCAST_RELOAD_SOCKETS });
		_receiverController.RegisterReceiver(_reloadTimerReceiver, new String[] { Constants.BROADCAST_RELOAD_TIMER });
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");

		_receiverController.UnregisterReceiver(_downloadMapContentFinishedReceiver);
		_receiverController.UnregisterReceiver(_downloadScheduleFinishedReceiver);
		_receiverController.UnregisterReceiver(_downloadSocketFinishedReceiver);
		_receiverController.UnregisterReceiver(_reloadMapContentReceiver);
		_receiverController.UnregisterReceiver(_reloadSchedulesReceiver);
		_receiverController.UnregisterReceiver(_reloadSocketsReceiver);
		_receiverController.UnregisterReceiver(_reloadTimerReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			_navigationService.NavigateTo(ListActivity.class, CreateBundle(_lucaObject), true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initializeButtons() {
		_buttonAddRaspberry = (Button) findViewById(R.id.buttonAddRaspberry);
		_buttonAddRaspberry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_addRaspberry) {
					_buttonAddRaspberry.setBackgroundResource(R.drawable.add_round);
				} else {
					if (_remove) {
						_remove = false;
						_buttonRemove.setBackgroundResource(R.drawable.remove_round);
					}
					if (_addArduino) {
						_addArduino = false;
						_buttonAddArduino.setBackgroundResource(R.drawable.add_round);
					}
					if (_addSocket) {
						_addSocket = false;
						_buttonAddSocket.setBackgroundResource(R.drawable.add_round);
					}
					_buttonAddRaspberry.setBackgroundResource(R.drawable.yellow_round);
				}
				_addRaspberry = !_addRaspberry;
				_logger.Debug("_addRaspberry: " + String.valueOf(_addRaspberry));
			}
		});

		_buttonAddArduino = (Button) findViewById(R.id.buttonAddArduino);
		_buttonAddArduino.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_addArduino) {
					_buttonAddArduino.setBackgroundResource(R.drawable.add_round);
				} else {
					if (_remove) {
						_remove = false;
						_buttonRemove.setBackgroundResource(R.drawable.remove_round);
					}
					if (_addRaspberry) {
						_addRaspberry = false;
						_buttonAddRaspberry.setBackgroundResource(R.drawable.add_round);
					}
					if (_addSocket) {
						_addSocket = false;
						_buttonAddSocket.setBackgroundResource(R.drawable.add_round);
					}
					_buttonAddArduino.setBackgroundResource(R.drawable.yellow_round);
				}
				_addArduino = !_addArduino;
				_logger.Debug("_addArduino: " + String.valueOf(_addArduino));
			}
		});

		_buttonAddSocket = (Button) findViewById(R.id.buttonAddSocket);
		_buttonAddSocket.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_addSocket) {
					_buttonAddSocket.setBackgroundResource(R.drawable.add_round);
				} else {
					if (_remove) {
						_remove = false;
						_buttonRemove.setBackgroundResource(R.drawable.remove_round);
					}
					if (_addRaspberry) {
						_addRaspberry = false;
						_buttonAddRaspberry.setBackgroundResource(R.drawable.add_round);
					}
					if (_addArduino) {
						_addArduino = false;
						_buttonAddArduino.setBackgroundResource(R.drawable.add_round);
					}
					_buttonAddSocket.setBackgroundResource(R.drawable.yellow_round);
				}
				_addSocket = !_addSocket;
				_logger.Debug("_addSocket: " + String.valueOf(_addSocket));
			}
		});

		_buttonRemove = (Button) findViewById(R.id.buttonRemove);
		_buttonRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_remove) {
					_buttonRemove.setBackgroundResource(R.drawable.remove_round);
				} else {
					if (_addSocket) {
						_addSocket = false;
						_buttonAddSocket.setBackgroundResource(R.drawable.add_round);
					}
					if (_addRaspberry) {
						_addRaspberry = false;
						_buttonAddRaspberry.setBackgroundResource(R.drawable.add_round);
					}
					if (_addArduino) {
						_addArduino = false;
						_buttonAddArduino.setBackgroundResource(R.drawable.add_round);
					}
					_buttonRemove.setBackgroundResource(R.drawable.yellow_round);
				}
				_remove = !_remove;
				_logger.Debug("_remove: " + String.valueOf(_remove));
			}
		});
	}

	private void addView(final MapContent newMapContent, Point clickPosition, boolean save) {
		final TextView newTextView = _mapContentController.CreateEntry(newMapContent, clickPosition,
				_wirelessSocketList);
		newTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_remove) {
					delete();
				} else {
					showInformation();
				}
			}

			private void delete() {
				_logger.Debug("onClick _remove");
				Runnable deleteRunnable = new Runnable() {
					@Override
					public void run() {
						// _mapContentController.DeleteMapContent(newMapContent);
						// _mapPaintView.removeView(newTextView);

						// TODO implement delete socket

						_logger.Warn("Reactivate delete!");
						Toast.makeText(_context, "Reactivate delete!", Toast.LENGTH_SHORT).show();

						_dialogService.closeDialogCallback.run();
					}
				};
				_dialogService.ShowDialogDouble("Delete this drawing?", "", "Yes", deleteRunnable, "No",
						_dialogService.closeDialogCallback, true);
			}

			private void showInformation() {
				_logger.Debug("onClick !_remove");
				switch (newMapContent.GetDrawingType()) {
				case RASPBERRY:
					Toast.makeText(_context, "Here is a raspberry!", Toast.LENGTH_SHORT).show();
					// TODO show details
					break;
				case ARDUINO:
					Toast.makeText(_context, "Here is an arduino!", Toast.LENGTH_SHORT).show();
					// TODO show details
					break;
				case SOCKET:
					showSocketDetailsDialog(newMapContent);
					break;
				case TEMPERATURE:
					showTemperatureDetailsDialog(newMapContent);
					break;
				default:
					_logger.Warn("drawingType: " + newMapContent.toString() + " is not supported!");
					return;
				}
			}

			private void showSocketDetailsDialog(MapContent newMapContent) {
				ArrayList<String> socketList = newMapContent.GetSockets();

				WirelessSocket socket = null;
				SerializableList<Schedule> scheduleList = new SerializableList<Schedule>();
				SerializableList<Timer> timerList = new SerializableList<Timer>();

				if (socketList != null) {
					if (socketList.size() == 1) {
						String socketName = socketList.get(0);

						for (int index = 0; index < _wirelessSocketList.getSize(); index++) {
							if (_wirelessSocketList.getValue(index).GetName().contains(socketName)) {
								socket = _wirelessSocketList.getValue(index);
								break;
							}
						}

						if (socket == null) {
							_logger.Warn("Socket not found! " + socketName);
							return;
						}

						for (int index = 0; index < _scheduleList.getSize(); index++) {
							if (_scheduleList.getValue(index).GetSocket().GetName().contains(socketName)) {
								scheduleList.addValue(_scheduleList.getValue(index));
							}
						}

						for (int index = 0; index < _timerList.getSize(); index++) {
							if (_timerList.getValue(index).GetSocket().GetName().contains(socketName)) {
								timerList.addValue(_timerList.getValue(index));
							}
						}

						_dialogService.ShowMapSocketDialog(socket, scheduleList, timerList);
					} else {
						_logger.Warn("SocketList to big!" + String.valueOf(socketList.size()));
					}
				} else {
					_logger.Warn("SocketList is null!");
				}
			}

			private void showTemperatureDetailsDialog(MapContent newMapContent) {
				String temperatureArea = newMapContent.GetTemperatureArea();
				for (int index = 0; index < _temperatureList.getSize(); index++) {
					if (_temperatureList.getValue(index).GetArea().contains(temperatureArea)) {
						_dialogService.ShowTemperatureGraphDialog(_temperatureList.getValue(index).GetGraphPath());
						break;
					}
				}
			}
		});

		if (save) {
			_mapContentController.AddMapContent(newMapContent);
		}

		_mapPaintView.addView(newTextView);
	}
}
