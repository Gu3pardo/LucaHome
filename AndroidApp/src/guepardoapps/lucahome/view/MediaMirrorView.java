package guepardoapps.lucahome.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.viewcontroller.MediaMirrorController;

import guepardoapps.mediamirror.common.enums.*;

public class MediaMirrorView extends Activity {

	private static final String TAG = MediaMirrorView.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;
	private MediaMirrorController _mediaMirrorController;

	private EditText _centerTextInput;
	private Button _sendCenterTextButton;

	private EditText _youtubeIdInput;
	private Button _youtubeIdInputSendButton;

	private int _selectedYoutubeId;
	private Spinner _selectYoutubeIdSpinner;
	private Button _sendYoutubeIdButton;
	private Button _sendYoutubePlayButton;
	private Button _sendYoutubeStopButton;

	private EditText _youtubeSearchInput;
	private Button _youtubeSearchInputSendButton;

	private Button _selectTagesschauButton;
	private Button _selectHearthstoneButton;
	private Button _selectFailarmyButton;
	private Button _selectPeopleAreAwesomeButton;

	private int _selectedRssFeedId;
	private Spinner _selectRssFeedSpinner;
	private Button _sendRssFeedButton;

	private EditText _webviewInput;
	private Button _sendWebviewButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_mediamirror);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_logger = new LucaHomeLogger(TAG);
		_logger.Debug("onCreate");

		_selectedYoutubeId = -1;
		_selectedRssFeedId = -1;

		_context = this;
		_mediaMirrorController = new MediaMirrorController(_context);

		_centerTextInput = (EditText) findViewById(R.id.centerTextInput);
		_sendCenterTextButton = (Button) findViewById(R.id.centerTextSendButton);
		_sendCenterTextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _centerTextInput.getText().toString();
				_mediaMirrorController.SendCenterText(data);
			}
		});

		_youtubeIdInput = (EditText) findViewById(R.id.youtubeIdInput);
		_youtubeIdInputSendButton = (Button) findViewById(R.id.youtubeIdInputSendButton);
		_youtubeIdInputSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _youtubeIdInput.getText().toString();
				if (data != null) {
					_logger.Debug("data is: " + data);
					if (data.length() == 11) {
						_mediaMirrorController.SendYoutubeId(data);
					} else {
						_logger.Warn("Data has wrong length: " + String.valueOf(data.length()));
					}
				}
			}
		});

		_selectYoutubeIdSpinner = (Spinner) findViewById(R.id.youtubeIdSpinner);
		List<String> youtubeIds = new ArrayList<String>();
		for (YoutubeId entry : YoutubeId.values()) {
			youtubeIds.add(entry.GetTitle());
		}
		ArrayAdapter<String> youtubeIdDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, youtubeIds);
		youtubeIdDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_selectYoutubeIdSpinner.setAdapter(youtubeIdDataAdapter);
		_selectYoutubeIdSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				_selectedYoutubeId = position;
				_logger.Debug("Selected youtubeId " + String.valueOf(_selectedYoutubeId));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
		_sendYoutubeIdButton = (Button) findViewById(R.id.youtubeSendButton);
		_sendYoutubeIdButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_selectedYoutubeId != -1) {
					YoutubeId youtubeId = YoutubeId.GetById(_selectedYoutubeId);
					if (youtubeId != null) {
						_logger.Debug("youtubeId is: " + youtubeId.toString());
						_mediaMirrorController.SendYoutubeId(youtubeId.GetYoutubeId());
					} else {
						_logger.Warn("Youtube id is null!");
					}
				}
			}
		});
		_sendYoutubePlayButton = (Button) findViewById(R.id.youtubePlayButton);
		_sendYoutubePlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.SendPlayYoutube();
			}
		});
		_sendYoutubeStopButton = (Button) findViewById(R.id.youtubeStopButton);
		_sendYoutubeStopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.SendStopYoutube();
			}
		});

		_youtubeSearchInput = (EditText) findViewById(R.id.youtubeSearchInput);
		_youtubeSearchInputSendButton = (Button) findViewById(R.id.youtubeSearchInputSendButton);
		_youtubeSearchInputSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _youtubeSearchInput.getText().toString();
				if (data.length() > 3) {
					_mediaMirrorController.LoadVideos(data, 25);
				}
			}
		});

		_selectTagesschauButton = (Button) findViewById(R.id.selectTagesschauButton);
		_selectTagesschauButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.LoadVideos("Tagesschau in 100 Sekunden", 5);
			}
		});
		_selectHearthstoneButton = (Button) findViewById(R.id.selectHearthstoneButton);
		_selectHearthstoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.LoadVideos("Hearthstone", 30);
			}
		});
		_selectFailarmyButton = (Button) findViewById(R.id.selectFailarmyButton);
		_selectFailarmyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.LoadVideos("Failarmy", 10);
			}
		});
		_selectPeopleAreAwesomeButton = (Button) findViewById(R.id.selectPeopleAreAwesomeButton);
		_selectPeopleAreAwesomeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_mediaMirrorController.LoadVideos("People are awesome", 10);
			}
		});

		_selectRssFeedSpinner = (Spinner) findViewById(R.id.rssFeedSpinner);
		List<String> rssFeedIds = new ArrayList<String>();
		for (RSSFeed entry : RSSFeed.values()) {
			rssFeedIds.add(entry.GetTitle());
		}
		ArrayAdapter<String> rssFeedIdDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, rssFeedIds);
		rssFeedIdDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_selectRssFeedSpinner.setAdapter(rssFeedIdDataAdapter);
		_selectRssFeedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				_selectedRssFeedId = position;
				_logger.Debug("Selected RssFeedId " + String.valueOf(_selectedRssFeedId));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
		_sendRssFeedButton = (Button) findViewById(R.id.rssFeedSendButton);
		_sendRssFeedButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_selectedRssFeedId != -1) {
					_mediaMirrorController.SendRssFeedId(_selectedRssFeedId);
				}
			}
		});

		_webviewInput = (EditText) findViewById(R.id.webviewInput);
		_sendWebviewButton = (Button) findViewById(R.id.webviewSendButton);
		_sendWebviewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _webviewInput.getText().toString();
				_mediaMirrorController.SendWebviewUrl(data);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (_logger != null) {
			_logger.Debug("onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (_logger != null) {
			_logger.Debug("onPause");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (_logger != null) {
			_logger.Debug("onDestroy");
		}
		_mediaMirrorController.Dispose();
	}
}
