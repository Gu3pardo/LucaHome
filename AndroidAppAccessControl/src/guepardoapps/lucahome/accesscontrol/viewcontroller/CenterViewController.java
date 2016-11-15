package guepardoapps.lucahome.accesscontrol.viewcontroller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import guepardoapps.lucahome.accesscontrol.R;
import guepardoapps.lucahome.accesscontrol.common.Constants;
import guepardoapps.lucahome.accesscontrol.common.enums.AlarmState;
import guepardoapps.lucahome.accesscontrol.services.RESTService;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

public class CenterViewController {

	private static final String TAG = CenterViewController.class.getName();
	private Logger _logger;

	private static final int MAX_CHAR_LENGTH = 10;
	private static final int LOGIN_TIMEOUT = 1500;

	private boolean _isInitialized;

	private Context _context;
	private ReceiverController _receiverController;

	private Button _activateAlarmButton;

	private TextView _alarmTextView;

	private RelativeLayout _inputRelativeLayout;
	private TextView _codeTextView;
	private TextView _loginNotificationTextView;
	private Button _buttonCode0;
	private Button _buttonCode1;
	private Button _buttonCode2;
	private Button _buttonCode3;
	private Button _buttonCode4;
	private Button _buttonCode5;
	private Button _buttonCode6;
	private Button _buttonCode7;
	private Button _buttonCode8;
	private Button _buttonCode9;
	private Button _buttonCodeReset;
	private Button _buttonCodeOk;

	private String _code;

	private Runnable _loginSuccessfulRunnable = new Runnable() {
		public void run() {
			_logger.Debug("_loginSuccessfulRunnable run");
			setVisibilities(View.VISIBLE, View.GONE, View.GONE);
		}
	};

	private BroadcastReceiver _alarmStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_alarmStateReceiver onReceive");
			AlarmState currentState = (AlarmState) intent.getSerializableExtra(Constants.BUNDLE_ALARM_STATE);
			if (currentState != null) {
				switch (currentState) {
				case ACCESS_CONTROL_ACTIVE:
					setVisibilities(View.GONE, View.GONE, View.GONE);
					break;
				case REQUEST_CODE:
					setVisibilities(View.GONE, View.GONE, View.VISIBLE);
					_loginNotificationTextView.setText("");
					_code = "";
					setCodeText(_code.length());
					break;
				case ACCESS_SUCCESSFUL:
					_loginNotificationTextView.setText("Code accepted!");
					_loginNotificationTextView.setTextColor(0xFF00FF00);
					_code = "";
					setCodeText(_code.length());
					Handler loginSuccessfulHandler = new Handler();
					loginSuccessfulHandler.postDelayed(_loginSuccessfulRunnable, LOGIN_TIMEOUT);
					break;
				case ALARM_ACTIVE:
					setVisibilities(View.GONE, View.VISIBLE, View.GONE);
					break;
				case ACCESS_FAILED:
					setVisibilities(View.GONE, View.GONE, View.VISIBLE);
					_loginNotificationTextView.setText("Entered wrong code");
					_loginNotificationTextView.setTextColor(0xFFFF0000);
					_code = "";
					setCodeText(_code.length());
					break;
				case NULL:
				default:
					_logger.Warn("State not supported!");
					break;
				}
			} else {
				_logger.Warn("model is null!");
			}
		}
	};

	public CenterViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
		_receiverController = new ReceiverController(_context);
	}

	public void onCreate() {
		_logger.Debug("onCreate");
		_alarmTextView = (TextView) ((Activity) _context).findViewById(R.id.alarmText);
		_inputRelativeLayout = (RelativeLayout) ((Activity) _context).findViewById(R.id.buttonArrayLayout);
		_codeTextView = (TextView) ((Activity) _context).findViewById(R.id.codeTextView);
		_loginNotificationTextView = (TextView) ((Activity) _context).findViewById(R.id.loginNotificationTextView);
		initializeButtons();
	}

	public void onPause() {
		_logger.Debug("onPause");
	}

	public void onResume() {
		_logger.Debug("onResume");
		if (!_isInitialized) {
			_receiverController.RegisterReceiver(_alarmStateReceiver, new String[] { Constants.BROADCAST_ALARM_STATE });
			_isInitialized = true;
			_logger.Debug("Initializing!");
		} else {
			_logger.Warn("Is ALREADY initialized!");
		}
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_alarmStateReceiver);
		_isInitialized = false;
	}

	private void initializeButtons() {
		_activateAlarmButton = (Button) ((Activity) _context).findViewById(R.id.activateAlarmButton);
		_activateAlarmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendRestAction(Constants.ACTION_ACTIVATE_ALARM);
			}
		});

		_buttonCode0 = (Button) ((Activity) _context).findViewById(R.id.code0Button);
		_buttonCode0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("0");
			}
		});
		_buttonCode1 = (Button) ((Activity) _context).findViewById(R.id.code1Button);
		_buttonCode1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("1");
			}
		});
		_buttonCode2 = (Button) ((Activity) _context).findViewById(R.id.code2Button);
		_buttonCode2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("2");
			}
		});
		_buttonCode3 = (Button) ((Activity) _context).findViewById(R.id.code3Button);
		_buttonCode3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("3");
			}
		});
		_buttonCode4 = (Button) ((Activity) _context).findViewById(R.id.code4Button);
		_buttonCode4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("4");
			}
		});
		_buttonCode5 = (Button) ((Activity) _context).findViewById(R.id.code5Button);
		_buttonCode5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("5");
			}
		});
		_buttonCode6 = (Button) ((Activity) _context).findViewById(R.id.code6Button);
		_buttonCode6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("6");
			}
		});
		_buttonCode7 = (Button) ((Activity) _context).findViewById(R.id.code7Button);
		_buttonCode7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("7");
			}
		});
		_buttonCode8 = (Button) ((Activity) _context).findViewById(R.id.code8Button);
		_buttonCode8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("8");
			}
		});
		_buttonCode9 = (Button) ((Activity) _context).findViewById(R.id.code9Button);
		_buttonCode9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCharToCode("9");
			}
		});
		_buttonCodeReset = (Button) ((Activity) _context).findViewById(R.id.codeResetButton);
		_buttonCodeReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_code = "";
				setCodeText(_code.length());
			}
		});
		_buttonCodeOk = (Button) ((Activity) _context).findViewById(R.id.codeOkButton);
		_buttonCodeOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (_code.length() > 0 && _code.length() <= MAX_CHAR_LENGTH) {
					sendRestAction(Constants.ACTION_SEND_CODE + "&code=" + _code);
				} else {
					_logger.Warn("Code is not valid!");
				}
			}
		});
	}

	private void setCodeText(int length) {
		String codeHide = "";
		for (int count = 0; count < length; count++) {
			codeHide += "*";
		}
		_codeTextView.setText(codeHide);
	}

	private void addCharToCode(String number) {
		if (_code.length() < MAX_CHAR_LENGTH) {
			_code += number;
			setCodeText(_code.length());
		} else {
			_logger.Warn("Code cannot be larger then " + String.valueOf(MAX_CHAR_LENGTH));
		}
	}

	private void setVisibilities(int alarmButtonVisbility, int alarmTextVisbility, int inputLayoutVisbility) {
		_activateAlarmButton.setVisibility(alarmButtonVisbility);
		_alarmTextView.setVisibility(alarmTextVisbility);
		_inputRelativeLayout.setVisibility(inputLayoutVisbility);
	}

	private void sendRestAction(String action) {
		if (action == null) {
			_logger.Warn("Action is null!");
			return;
		}
		_logger.Debug("action: " + action);

		Intent serviceIntent = new Intent(_context, RESTService.class);
		Bundle serviceData = new Bundle();
		serviceData.putString(Constants.BUNDLE_REST_ACTION, action);
		serviceIntent.putExtras(serviceData);
		_context.startService(serviceIntent);
	}
}
