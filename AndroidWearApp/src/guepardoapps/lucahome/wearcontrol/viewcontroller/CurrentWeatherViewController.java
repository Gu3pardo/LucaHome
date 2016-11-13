package guepardoapps.lucahome.wearcontrol.viewcontroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;

import guepardoapps.lucahome.wearcontrol.common.Constants;
import guepardoapps.lucahome.wearcontrol.common.Tools;
import guepardoapps.lucahome.wearcontrol.dto.WeatherModelDto;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

@SuppressWarnings("deprecation")
public class CurrentWeatherViewController {

	private static final String TAG = CurrentWeatherViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private ReceiverController _receiverController;
	private Tools _tools;

	private Display _display;

	private WeatherModelDto _currentWeather;

	private BroadcastReceiver _currentWeatherReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_currentWeatherReceiveer onReceive");
			WeatherModelDto newWeather = (WeatherModelDto) intent
					.getSerializableExtra(Constants.BUNDLE_CURRENT_WEATHER);
			if (newWeather != null) {
				_currentWeather = newWeather;
				_logger.Debug("New current weather: " + _currentWeather.toString());
			}
		}
	};

	public CurrentWeatherViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_receiverController = new ReceiverController(_context);
		_receiverController.RegisterReceiver(_currentWeatherReceiver,
				new String[] { Constants.BROADCAST_UPDATE_CURRENT_WEATHER });
		_tools = new Tools(_context);

		_display = _tools.GetDisplayDimension();

		_currentWeather = new WeatherModelDto("Clear", "5.23°C", "11:35");
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_currentWeatherReceiver);
	}

	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_VERY_SMALL);
		canvas.drawText(_currentWeather.GetWatchFaceText(), Constants.DRAW_DEFAULT_OFFSET_X, _display.getHeight() - 120,
				paint);
	}
}
