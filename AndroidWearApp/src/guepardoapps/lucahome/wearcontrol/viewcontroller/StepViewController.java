package guepardoapps.lucahome.wearcontrol.viewcontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;

import guepardoapps.lucahome.wearcontrol.common.Constants;
import guepardoapps.lucahome.wearcontrol.common.Tools;

import guepardoapps.toolset.common.Logger;

public class StepViewController implements SensorEventListener {

	private static final String TAG = StepViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private Tools _tools;

	private Display _display;

	private double _stepCount;
	private SensorManager _sensorManager;
	private Sensor _stepSensor;

	public StepViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_tools = new Tools(_context);
		_display = _tools.GetDisplayDimension();

		_stepCount = 0;
		_sensorManager = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);
		_stepSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
		if (_stepSensor != null) {
			_logger.Debug("Found StepSensor!");
			_sensorManager.registerListener(this, _stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	public void onDestroy() {
		_sensorManager.unregisterListener(this);
	}

	@SuppressWarnings("deprecation")
	public void Draw(Canvas canvas) {
		_logger.Debug("Draw");
		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_SMALL);
		canvas.drawText("STEPS", _display.getWidth() - 73, _display.getHeight() - 135, paint);
		canvas.drawText(String.valueOf((int) _stepCount), _display.getWidth() - 58, _display.getHeight() - 120, paint);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_STEP_COUNTER:
			_stepCount = event.values[0];
			_logger.Info("New stepCount: " + String.valueOf(_stepCount));
			break;
		default:
			_logger.Info("Currently not supported: " + event.sensor.toString());
			break;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int arg1) {
	}
}
