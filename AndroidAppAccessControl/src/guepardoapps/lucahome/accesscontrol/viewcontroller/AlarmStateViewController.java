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

public class AlarmStateViewController {

	private static final String TAG = AlarmStateViewController.class.getName();
	private Logger _logger;

	private boolean _isInitialized;

	private Context _context;
	private ReceiverController _receiverController;

	private View _alarmStateIndicator;
	private TextView _alarmStateTextView;

	private BroadcastReceiver _alarmStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_alarmStateReceiver onReceive");
			AlarmState currentState = (AlarmState) intent.getSerializableExtra(Constants.BUNDLE_ALARM_STATE);
			if (currentState != null) {
				switch (currentState) {
				case ACCESS_CONTROL_ACTIVE:
					_alarmStateIndicator.setBackgroundResource(R.drawable.circle_blue);
					_alarmStateTextView.setText(R.string.accessControlActive);
					break;
				case REQUEST_CODE:
					_alarmStateIndicator.setBackgroundResource(R.drawable.circle_yellow);
					_alarmStateTextView.setText(R.string.enterAccessCode);
					break;
				case ACCESS_SUCCESSFUL:
					_alarmStateIndicator.setBackgroundResource(R.drawable.circle_green);
					_alarmStateTextView.setText(R.string.accessSuccessful);
					break;
				case ALARM_ACTIVE:
					_alarmStateIndicator.setBackgroundResource(R.drawable.circle_red);
					_alarmStateTextView.setText(R.string.alarmActive);
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

	public AlarmStateViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
		_receiverController = new ReceiverController(_context);
	}

	public void onCreate() {
		_logger.Debug("onCreate");

		_alarmStateIndicator = (View) ((Activity) _context).findViewById(R.id.alarmStateIndicator);
		_alarmStateTextView = (TextView) ((Activity) _context).findViewById(R.id.alarmStateText);
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
}
