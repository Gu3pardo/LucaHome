package guepardoapps.lucahome.watchface.viewcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.Tools;
import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

@SuppressWarnings("deprecation")
public class BatteryPhoneViewController {

	private static final String TAG = BatteryPhoneViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private ReceiverController _receiverController;
	private Tools _tools;

	private Display _display;

	private String _batteryPhoneValue;

	private BroadcastReceiver _batteryPhoneReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_batteryPhoneReceiver onReceive");
			String message = intent.getStringExtra(Constants.BUNDLE_PHONE_BATTERY);
			if (message != null) {
				_batteryPhoneValue = message;
			}
		}
	};

	public BatteryPhoneViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_receiverController = new ReceiverController(_context);
		_receiverController.RegisterReceiver(_batteryPhoneReceiver,
				new String[] { Constants.BROADCAST_UPDATE_PHONE_BATTERY });
		_tools = new Tools(_context);

		_display = _tools.GetDisplayDimension();

		_batteryPhoneValue = "94%";
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_batteryPhoneReceiver);
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_EXTREMELY_SMALL);
		canvas.drawText("Bat P", _display.getWidth() - 55, 190, paint);
		canvas.drawText(_batteryPhoneValue, _display.getWidth() - 55, 207, paint);
	}
}
