package guepardoapps.lucahome.accesscontrol.viewcontroller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.view.View;
import android.widget.TextView;

import guepardoapps.lucahome.accesscontrol.R;
import guepardoapps.lucahome.accesscontrol.common.Constants;
import guepardoapps.lucahome.accesscontrol.common.enums.AlarmState;
import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

public class BatteryViewController {

	private static final String TAG = BatteryViewController.class.getName();
	private Logger _logger;

	private static final int BATTERY_LEVEL_WARNING = 15;
	private static final int BATTERY_LEVEL_CRITICAL = 5;

	private boolean _isInitialized;

	private Context _context;
	private ReceiverController _receiverController;

	private View _batteryAlarmView;
	private TextView _batteryValueTextView;

	private BroadcastReceiver _alarmStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_alarmStateReceiver onReceive");
			AlarmState currentState = (AlarmState) intent.getSerializableExtra(Constants.BUNDLE_ALARM_STATE);
			if (currentState != null) {
				switch (currentState) {
				case ACCESS_SUCCESSFUL:
					_batteryValueTextView.setVisibility(View.VISIBLE);
					_batteryAlarmView.setVisibility(View.VISIBLE);
					break;
				case ALARM_ACTIVE:
				case ACCESS_CONTROL_ACTIVE:
				case REQUEST_CODE:
					_batteryValueTextView.setVisibility(View.INVISIBLE);
					_batteryAlarmView.setVisibility(View.INVISIBLE);
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

	private BroadcastReceiver _batteryInfoReveicer = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			_batteryValueTextView.setText(String.valueOf(level) + "%");
			if (level > BATTERY_LEVEL_WARNING) {
				_batteryAlarmView.setBackgroundResource(R.drawable.circle_green);
			} else if (level <= BATTERY_LEVEL_WARNING && level > BATTERY_LEVEL_CRITICAL) {
				_batteryAlarmView.setBackgroundResource(R.drawable.circle_yellow);
			} else {
				_batteryAlarmView.setBackgroundResource(R.drawable.circle_red);
			}
		}
	};

	public BatteryViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
		_receiverController = new ReceiverController(_context);
	}

	public void onCreate() {
		_logger.Debug("onCreate");

		_batteryAlarmView = (View) ((Activity) _context).findViewById(R.id.batteryAlarm);
		_batteryValueTextView = (TextView) ((Activity) _context).findViewById(R.id.batteryTextView);
	}

	public void onPause() {
		_logger.Debug("onPause");
	}

	public void onResume() {
		_logger.Debug("onResume");
		if (!_isInitialized) {
			_logger.Debug("Initializing!");
			_receiverController.RegisterReceiver(_alarmStateReceiver, new String[] { Constants.BROADCAST_ALARM_STATE });
			_context.registerReceiver(_batteryInfoReveicer, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			_isInitialized = true;
		} else {
			_logger.Warn("Is ALREADY initialized!");
		}
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_alarmStateReceiver);
		_context.unregisterReceiver(_batteryInfoReveicer);
		_isInitialized = false;
	}
}
