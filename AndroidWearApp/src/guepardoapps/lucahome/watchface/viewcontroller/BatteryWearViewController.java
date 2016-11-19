package guepardoapps.lucahome.watchface.viewcontroller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.BatteryManager;
import android.view.Display;

import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.watchface.common.Tools;

import guepardoapps.toolset.common.Logger;

@SuppressWarnings("deprecation")
public class BatteryWearViewController {

	private static final String TAG = BatteryWearViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private Tools _tools;

	private Display _display;

	public BatteryWearViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_tools = new Tools(_context);

		_display = _tools.GetDisplayDimension();
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");

		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = _context.registerReceiver(null, intentFilter);

		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		if (level != -1) {
			String battery = String.valueOf(level) + "%";

			Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_EXTREMELY_SMALL);
			canvas.drawText("Bat W", _display.getWidth() - 55, 225, paint);
			canvas.drawText(battery, _display.getWidth() - 55, 242, paint);
		}
	}
}
