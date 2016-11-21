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

	private TemperatureDto _raspberryTemperature1;
	private TemperatureDto _raspberryTemperature2;

	private BroadcastReceiver _temperatureReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_temperatureReceiver onReceive");

			TemperatureDto newRaspberryTemperature1 = (TemperatureDto) intent
					.getSerializableExtra(Constants.BUNDLE_RASPBERRY_TEMPERATURE_1);
			if (newRaspberryTemperature1 != null) {
				_raspberryTemperature1 = newRaspberryTemperature1;
				_logger.Debug("New _raspberryTemperature1: " + _raspberryTemperature1.toString());
			} else {
				_logger.Warn("newRaspberryTemperature1 is null!");
			}

			TemperatureDto newRaspberryTemperature2 = (TemperatureDto) intent
					.getSerializableExtra(Constants.BUNDLE_RASPBERRY_TEMPERATURE_2);
			if (newRaspberryTemperature2 != null) {
				_raspberryTemperature2 = newRaspberryTemperature2;
				_logger.Debug("New _raspberryTemperature2: " + _raspberryTemperature2.toString());
			} else {
				_logger.Warn("newRaspberryTemperature2 is null!");
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

		_raspberryTemperature1 = new TemperatureDto("18.9°C", "Workspace Jonas", "11:35:00");
		_raspberryTemperature2 = new TemperatureDto("17.6°C", "Sleeping Room", "11:36:00");
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_temperatureReceiver);
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_EXTREMELY_SMALL);
		canvas.drawText(_raspberryTemperature1.GetWatchFaceText(), Constants.DRAW_DEFAULT_OFFSET_X,
				_display.getHeight() - 90, paint);
		canvas.drawText(_raspberryTemperature2.GetWatchFaceText(), Constants.DRAW_DEFAULT_OFFSET_X,
				_display.getHeight() - 70, paint);
	}
}
