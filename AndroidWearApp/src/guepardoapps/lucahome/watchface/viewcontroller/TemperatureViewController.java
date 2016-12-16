package guepardoapps.lucahome.watchface.viewcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.Tools;
import guepardoapps.lucahome.watchface.dto.TemperatureDto;
import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

@SuppressWarnings("deprecation")
public class TemperatureViewController {

	private static final String TAG = TemperatureViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private ReceiverController _receiverController;
	private Tools _tools;

	private Display _display;

	private TemperatureDto _raspberryTemperature;

	private BroadcastReceiver _temperatureReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_temperatureReceiver onReceive");

			TemperatureDto newRaspberryTemperature = (TemperatureDto) intent
					.getSerializableExtra(Constants.BUNDLE_RASPBERRY_TEMPERATURE);
			if (newRaspberryTemperature != null) {
				_raspberryTemperature = newRaspberryTemperature;
				_logger.Debug("New _raspberryTemperature: " + _raspberryTemperature.toString());
			} else {
				_logger.Warn("newRaspberryTemperature is null!");
			}
		}
	};

	public TemperatureViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_receiverController = new ReceiverController(_context);
		_receiverController.RegisterReceiver(_temperatureReceiver,
				new String[] { Constants.BROADCAST_UPDATE_RASPBERRY_TEMPERATURE });
		_tools = new Tools(_context);

		_display = _tools.GetDisplayDimension();

		_raspberryTemperature = new TemperatureDto("18.9°C", "Workspace Jonas", "11:35:00");
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_temperatureReceiver);
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_EXTREMELY_SMALL);
		canvas.drawText(_raspberryTemperature.GetWatchFaceText(), Constants.DRAW_DEFAULT_OFFSET_X,
				_display.getHeight() - 90, paint);
	}
}
