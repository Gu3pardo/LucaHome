package guepardoapps.lucahome.accesscontrol.viewcontroller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import guepardoapps.lucahome.accesscontrol.R;
import guepardoapps.lucahome.accesscontrol.common.Constants;
import guepardoapps.lucahome.accesscontrol.common.enums.AlarmState;
import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

public class IpAdressViewController {

	private static final String TAG = IpAdressViewController.class.getName();
	private Logger _logger;

	private boolean _isInitialized;

	private Context _context;
	private ReceiverController _receiverController;

	private TextView _ipAdressTextView;

	private BroadcastReceiver _alarmStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_alarmStateReceiver onReceive");
			AlarmState currentState = (AlarmState) intent.getSerializableExtra(Constants.BUNDLE_ALARM_STATE);
			if (currentState != null) {
				switch (currentState) {
				case ACCESS_SUCCESSFUL:
					_ipAdressTextView.setVisibility(View.VISIBLE);
					break;
				case ALARM_ACTIVE:
				case ACCESS_CONTROL_ACTIVE:
				case REQUEST_CODE:
					_ipAdressTextView.setVisibility(View.INVISIBLE);
					break;
				case ACCESS_FAILED:
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

	private BroadcastReceiver _updateViewReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_updateViewReceiver onReceive");
			String ipAddress = intent.getStringExtra(Constants.BUNDLE_IP_ADRESS);
			if (ipAddress != null) {
				_logger.Debug("ipAddress: " + ipAddress);
				_ipAdressTextView.setText(ipAddress);
			} else {
				_logger.Warn("ipAddress is null!");
			}
		}
	};

	public IpAdressViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
		_receiverController = new ReceiverController(_context);
	}

	public void onCreate() {
		_logger.Debug("onCreate");
		_ipAdressTextView = (TextView) ((Activity) _context).findViewById(R.id.ipAdressTextView);
	}

	public void onPause() {
		_logger.Debug("onPause");
	}

	public void onResume() {
		_logger.Debug("onResume");
		if (!_isInitialized) {
			_receiverController.RegisterReceiver(_alarmStateReceiver, new String[] { Constants.BROADCAST_ALARM_STATE });
			_receiverController.RegisterReceiver(_updateViewReceiver,
					new String[] { Constants.BROADCAST_UPDATE_IP_ADRESS });
			_isInitialized = true;
			_logger.Debug("Initializing!");
		} else {
			_logger.Warn("Is ALREADY initialized!");
		}
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_alarmStateReceiver);
		_receiverController.UnregisterReceiver(_updateViewReceiver);
		_isInitialized = false;
	}
}
