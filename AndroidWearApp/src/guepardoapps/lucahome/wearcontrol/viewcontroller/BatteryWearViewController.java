package guepardoapps.lucahome.wearcontrol.viewcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.view.Display;

import guepardoapps.lucahome.wearcontrol.common.Constants;
import guepardoapps.lucahome.wearcontrol.common.Tools;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

@SuppressWarnings("deprecation")
public class BatteryWearViewController {

	private static final String TAG = BatteryWearViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private ReceiverController _receiverController;
	private Tools _tools;

	private Display _display;

	private String _batteryWearValue;

	private BroadcastReceiver _batteryPhoneReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_batteryPhoneReceiver onReceive");

			IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = _context.registerReceiver(null, intentFilter);

			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			if (level != -1) {
				_batteryWearValue = String.valueOf(level) + "%";
			}
		}
	};

	public BatteryWearViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_receiverController = new ReceiverController(_context);
		_receiverController.RegisterReceiver(_batteryPhoneReceiver,
				new String[] { Constants.BROADCAST_UPDATE_PHONE_BATTERY });
		_tools = new Tools(_context);

		_display = _tools.GetDisplayDimension();

		_batteryWearValue = "-1%";
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_batteryPhoneReceiver);
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_EXTREMELY_SMALL);
		canvas.drawText("Bat W", _display.getWidth() - 65, 225, paint);
		canvas.drawText(_batteryWearValue, _display.getWidth() - 58, 242, paint);
	}
}
