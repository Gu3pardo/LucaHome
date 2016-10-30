package guepardoapps.lucahome.activities;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import guepardoapps.common.BaseActivity;
import guepardoapps.common.Constants;
import guepardoapps.common.classes.*;
import guepardoapps.common.classes.controller.SoundController;
import guepardoapps.common.controller.*;
import guepardoapps.common.converter.json.JsonDataToSoundConverter;
import guepardoapps.common.customadapter.SoundListAdapter;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.lucahome.R;

public class SoundActivity extends BaseActivity {

	private static String TAG = "SoundActivity";

	private ArrayList<ArrayList<Sound>> _soundLists = new ArrayList<ArrayList<Sound>>(2);
	private ArrayList<Sound> _activeSoundList;
	private Sound _soundPlaying;
	private boolean _isPlaying = false;;
	private int _volume = -1;
	private RaspberrySelection _raspberrySelection;

	private ProgressBar _progressBar;
	private ListView _listView;
	private TextView _informationText;
	private TextView _volumeText;
	private ImageButton _buttonPlay;
	private ImageButton _buttonStop;
	private ImageButton _buttonIncreaseVolume;
	private ImageButton _buttonDecreaseVolume;
	private Button _buttonSelectRaspberry;

	private ListAdapter _listAdapter;

	private ReceiverController _receiverController;
	private ServiceController _serviceController;
	private SoundController _soundController;

	private BroadcastReceiver _isPlayingReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_receiverController.UnregisterReceiver(_isPlayingReceiver);

			String[] isPlayingStringArray = intent.getStringArrayExtra("SoundController");
			if (isPlayingStringArray != null) {
				if (isPlayingStringArray[0] != null) {
					String isPlayingString = isPlayingStringArray[0].replace("{IsPlaying:", "").replace("};", "");
					_isPlaying = isPlayingString.contains("1");
				}
			}

			if (_isPlaying) {
				_receiverController.RegisterReceiver(_currentPlayingSoundReceiver,
						new String[] { Constants.BROADCAST_PLAYING_FILE });
				_serviceController.StartRestService(TAG, Constants.ACTION_GET_PLAYING_FILE,
						Constants.BROADCAST_PLAYING_FILE, LucaObject.SOUND, _raspberrySelection);
			} else {
				startDownloadingSoundFiles();
			}
		}
	};

	private BroadcastReceiver _currentPlayingSoundReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_receiverController.UnregisterReceiver(_currentPlayingSoundReceiver);

			String[] playingFileStringArray = intent.getStringArrayExtra(TAG);
			if (playingFileStringArray != null) {
				if (playingFileStringArray[0] != null) {
					String playingFileString = playingFileStringArray[0].replace("{PlayingFile:", "").replace("};", "");
					_soundPlaying = new Sound(playingFileString, true);
					_informationText.setText(_soundPlaying.GetFileName());
				} else {
					_informationText.setText("Error loading!;");
				}
			} else {
				_informationText.setText("Error loading!;");
			}

			startDownloadingSoundFiles();
		}
	};

	private BroadcastReceiver _soundsDownloadedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_receiverController.UnregisterReceiver(_soundsDownloadedReceiver);

			String[] soundStringArray = intent.getStringArrayExtra(TAG);
			if (soundStringArray != null) {
				_soundLists.set(0, JsonDataToSoundConverter.GetList(soundStringArray[0]));
				_soundLists.set(1, JsonDataToSoundConverter.GetList(soundStringArray[1]));
				_activeSoundList = _soundLists.get(_raspberrySelection.GetInt());

				showList();

				_serviceController.StartRestService(TAG, Constants.ACTION_GET_VOLUME, Constants.BROADCAST_GET_VOLUME,
						LucaObject.SOUND, _raspberrySelection);
			} else {
				Toast.makeText(_context, "Failed to download sound list", Toast.LENGTH_LONG).show();
			}
		}
	};

	private BroadcastReceiver _raspberryChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			RaspberrySelection newRaspberrySelection = (RaspberrySelection) intent
					.getSerializableExtra(Constants.BUNDLE_RASPBERRY_SELETION);
			if (newRaspberrySelection != null) {
				_raspberrySelection = newRaspberrySelection;
				_activeSoundList = _soundLists.get(_raspberrySelection.GetInt());
				_buttonSelectRaspberry.setText(String.valueOf(_raspberrySelection.GetInt()));

				showList();
			}
		}
	};

	private BroadcastReceiver _startPlayingReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] soundFileArray = intent.getStringArrayExtra(TAG);
			if (soundFileArray == null) {
				soundFileArray = intent.getStringArrayExtra("SoundController");
				if (soundFileArray == null) {
					Toast.makeText(_context, "Failed to start sound!", Toast.LENGTH_LONG).show();
					return;
				} else {
					handleSendStartIntent(soundFileArray);
				}
			} else {
				handleSendStartIntent(soundFileArray);
			}
		}

		private void handleSendStartIntent(String[] soundFileArray) {
			if (soundFileArray != null) {
				String fileName = soundFileArray[0];
				if (fileName.contains("Error")) {
					_informationText.setText(fileName);
					return;
				}

				if (fileName != "") {
					boolean foundFile = false;
					for (int index = 0; index < _activeSoundList.size(); index++) {
						if (_activeSoundList.get(index).GetFileName().contains(fileName)) {
							_activeSoundList.get(index).SetIsPlaying(true);
							_soundPlaying = _activeSoundList.get(index);
							foundFile = true;
						} else {
							_activeSoundList.get(index).SetIsPlaying(false);
						}
					}

					if (foundFile) {
						_informationText.setText(_soundPlaying.GetFileName());
					} else {
						_informationText.setText("File not found: " + fileName);
					}
				}
			} else {
				Toast.makeText(_context, "Failed to start sound!", Toast.LENGTH_LONG).show();
			}
		}
	};

	private BroadcastReceiver _stopPlayingReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] stopStringArray = intent.getStringArrayExtra("SoundController");
			if (stopStringArray != null) {
				if (stopStringArray[0] != null) {
					if (stopStringArray[0].contains("1")) {
						_isPlaying = false;
						_soundPlaying = null;
					}

					_informationText.setText(stopStringArray[0]);
				}
			}

			if (_isPlaying) {
				_logger.Error(stopStringArray[0]);
				Toast.makeText(_context, "Failed to stop sound!", Toast.LENGTH_LONG).show();
			}
		}
	};

	private BroadcastReceiver _volumeDownloadedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] volumeStringArray = intent.getStringArrayExtra("SoundController");
			if (volumeStringArray == null) {
				volumeStringArray = intent.getStringArrayExtra(TAG);
			}

			if (volumeStringArray != null) {
				if (volumeStringArray[0] != null) {
					String volumeString = volumeStringArray[0].replace("{Volume:", "").replace("};", "");
					try {
						_volume = Integer.parseInt(volumeString);
					} catch (Exception ex) {
						_volume = -1;
						_logger.Error(ex.toString());
					} finally {
						_volumeText.setText(String.valueOf(_volume));
					}
					return;
				}
			}

			Toast.makeText(_context, "Failed to get volume!", Toast.LENGTH_SHORT).show();
		}
	};

	private Runnable _selectRaspberry1 = new Runnable() {
		@Override
		public void run() {
			if (checkFileInList(_soundPlaying, _soundLists.get(0))) {
				_soundController.SelectRaspberry(_raspberrySelection, RaspberrySelection.RASPBERRY_1, _soundPlaying);
			} else {
				Toast.makeText(_context,
						"Cannot change raspberry! Raspberry 1 has not file " + _soundPlaying.GetFileName(),
						Toast.LENGTH_LONG).show();
			}
		}
	};

	private Runnable _selectRaspberry2 = new Runnable() {
		@Override
		public void run() {
			if (checkFileInList(_soundPlaying, _soundLists.get(1))) {
				_soundController.SelectRaspberry(_raspberrySelection, RaspberrySelection.RASPBERRY_2, _soundPlaying);
			} else {
				Toast.makeText(_context,
						"Cannot change raspberry! Raspberry 2 has not file " + _soundPlaying.GetFileName(),
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = this;

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);
		_soundController = new SoundController(_context);

		setContentView(R.layout.view_sound);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		initializeViews();
		loadValues();

		_buttonSelectRaspberry.setText(String.valueOf(_raspberrySelection.GetInt()));
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");
		_receiverController.RegisterReceiver(_isPlayingReceiver, new String[] { Constants.BROADCAST_IS_SOUND_PLAYING });
		_receiverController.RegisterReceiver(_raspberryChangedReceiver,
				new String[] { Constants.BROADCAST_SET_RASPBERRY });
		_receiverController.RegisterReceiver(_startPlayingReceiver, new String[] { Constants.BROADCAST_START_SOUND });
		_receiverController.RegisterReceiver(_stopPlayingReceiver, new String[] { Constants.BROADCAST_STOP_SOUND });
		_receiverController.RegisterReceiver(_volumeDownloadedReceiver,
				new String[] { Constants.BROADCAST_GET_VOLUME });
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

		_receiverController.UnregisterReceiver(_currentPlayingSoundReceiver);
		_receiverController.UnregisterReceiver(_isPlayingReceiver);
		_receiverController.UnregisterReceiver(_raspberryChangedReceiver);
		_receiverController.UnregisterReceiver(_soundsDownloadedReceiver);
		_receiverController.UnregisterReceiver(_startPlayingReceiver);
		_receiverController.UnregisterReceiver(_stopPlayingReceiver);
		_receiverController.UnregisterReceiver(_volumeDownloadedReceiver);
	}

	private void loadValues() {
		_raspberrySelection = RaspberrySelection.GetById(
				_sharedPrefController.LoadIntegerValueFromSharedPreferences(Constants.SOUND_RASPBERRY_SELECTION));
		_soundController.CheckPlaying(_raspberrySelection);
	}

	private void initializeViews() {
		_progressBar = (ProgressBar) findViewById(R.id.progressBarSoundListView);
		_listView = (ListView) findViewById(R.id.soundListView);

		_informationText = (TextView) findViewById(R.id.soundInformation);
		if (_soundPlaying != null) {
			_informationText.setText(_soundPlaying.GetFileName());
		} else {
			_informationText.setText("Loading...");
		}

		_volumeText = (TextView) findViewById(R.id.soundVolumeView);
		if (_volume == -1) {
			_volumeText.setText("Loading...");
		} else {
			_volumeText.setText(String.valueOf(_volume));
		}

		_buttonPlay = (ImageButton) findViewById(R.id.soundButtonPlay);
		_buttonStop = (ImageButton) findViewById(R.id.soundButtonStop);
		_buttonIncreaseVolume = (ImageButton) findViewById(R.id.soundButtonIncreaseVolume);
		_buttonDecreaseVolume = (ImageButton) findViewById(R.id.soundButtonDecreaseVolume);
		_buttonSelectRaspberry = (Button) findViewById(R.id.soundSwitchRaspberry);

		_buttonPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_soundPlaying != null) {
					_soundController.StartSound(_soundPlaying, _raspberrySelection);
				}
			}
		});
		_buttonStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_soundController.StopSound(_raspberrySelection);
			}
		});
		_buttonIncreaseVolume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_soundController.IncreaseVolume(_raspberrySelection);
			}
		});
		_buttonDecreaseVolume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_soundController.DecreaseVolume(_raspberrySelection);
			}
		});
		_buttonSelectRaspberry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_dialogService.ShowDialogDouble("Select a raspberry", "", "Raspberry 1", _selectRaspberry1,
						"Raspberry 2", _selectRaspberry2, true);
			}
		});
	}

	private void showList() {
		_progressBar.setVisibility(View.GONE);
		_listAdapter = new SoundListAdapter(_context, _activeSoundList, _raspberrySelection);
		_listView.setAdapter(_listAdapter);
		_listView.setVisibility(View.VISIBLE);
	}

	private boolean checkFileInList(Sound sound, ArrayList<Sound> soundList) {
		if (sound == null) {
			return true;
		}

		for (Sound entry : soundList) {
			if (entry.GetFileName().contains(sound.GetFileName())) {
				return true;
			}
		}

		return false;
	}

	private void startDownloadingSoundFiles() {
		_receiverController.RegisterReceiver(_soundsDownloadedReceiver,
				new String[] { Constants.BROADCAST_GET_SOUNDS });
		_serviceController.StartRestService(TAG, Constants.ACTION_GET_SOUNDS, Constants.BROADCAST_GET_SOUNDS,
				LucaObject.SOUND, RaspberrySelection.BOTH);
	}
}
