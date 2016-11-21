package guepardoapps.lucahome.wearcontrol.views;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;

import guepardoapps.lucahome.wearcontrol.sensor.helper.GravityDto;

import guepardoapps.test.SensorDataLogger;

import guepardoapps.toolset.common.Logger;

public class SensorView extends Activity implements SensorEventListener {

	private static final String TAG = SensorView.class.getName();
	private Logger _logger;

	private TextView _valueXView;
	private TextView _valueYView;
	private TextView _valueZView;
	private TextView _valueDirectionHandView;

	private Button _graphViewButton;
	private GraphView _gravityGraph;

	private SensorManager _sensorManager;
	private Sensor _sensor;
	private boolean _hasGravitySensor;
	private GravityDto _lastGravityDto = new GravityDto(0, 0, 0);

	private static final int MAX_DATA_POINTS = 250;
	private static final double GRAVITY_CHANGE_BORDER = 0.01;
	private static final boolean SENSOR_EVALUATION_ENABLED = true;

	private boolean _graphEnabled = false;
	private LineGraphSeries<DataPoint> _gravityXSeries;
	private LineGraphSeries<DataPoint> _gravityYSeries;
	private LineGraphSeries<DataPoint> _gravityZSeries;

	private Context _context;
	private SensorDataLogger _sensorDataLogger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_sensor);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;
		_sensorDataLogger = new SensorDataLogger(_context);

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.basicSensorWatchViewStub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				_valueXView = (TextView) findViewById(R.id.gravityXValue);
				_valueYView = (TextView) findViewById(R.id.gravityYValue);
				_valueZView = (TextView) findViewById(R.id.gravityZValue);
				_valueDirectionHandView = (TextView) findViewById(R.id.directionOfHandValue);

				_graphViewButton = (Button) findViewById(R.id.gravityGraphButton);
				_graphViewButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						_graphEnabled = !_graphEnabled;
						if (!_graphEnabled) {
							initializeSeries();
							_graphViewButton.setText("Enable Graph");
						} else {
							_graphViewButton.setText("Disable Graph");
						}
					}
				});
				_gravityGraph = (GraphView) findViewById(R.id.gravityGraph);
				_gravityGraph.getViewport().setXAxisBoundsManual(true);
				_gravityGraph.getViewport().setYAxisBoundsManual(true);
				_gravityGraph.getViewport().setMinX(0);
				_gravityGraph.getViewport().setMaxX(MAX_DATA_POINTS);
				_gravityGraph.getViewport().setMinY(-10);
				_gravityGraph.getViewport().setMaxY(10);

				initializeSeries();

				_gravityGraph.addSeries(_gravityXSeries);
				_gravityGraph.addSeries(_gravityYSeries);
				_gravityGraph.addSeries(_gravityZSeries);

				initializeSensor();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		_logger.Debug("onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		_logger.Debug("onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		if (_hasGravitySensor) {
			_sensorManager.unregisterListener(this);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
			float[] gravity = sensorEvent.values;
			if (gravity.length >= 3) {
				GravityDto gravityDto = new GravityDto(gravity[0], gravity[1], gravity[2]);
				
				if (gravityHasSignificantChange(gravityDto)) {
					if (_graphEnabled) {
						_gravityXSeries.appendData(
								new DataPoint(_gravityXSeries.getHighestValueX() + 1, gravityDto.GetX()), true,
								MAX_DATA_POINTS);
						_gravityYSeries.appendData(
								new DataPoint(_gravityYSeries.getHighestValueX() + 1, gravityDto.GetY()), true,
								MAX_DATA_POINTS);
						_gravityZSeries.appendData(
								new DataPoint(_gravityZSeries.getHighestValueX() + 1, gravityDto.GetZ()), true,
								MAX_DATA_POINTS);
					}

					if (SENSOR_EVALUATION_ENABLED) {
						_sensorDataLogger.AddGravityDto(gravityDto);
					}

					_valueXView.setText(String.valueOf(gravityDto.GetX()));
					_valueYView.setText(String.valueOf(gravityDto.GetY()));
					_valueZView.setText(String.valueOf(gravityDto.GetZ()));

					_valueDirectionHandView.setText(gravityDto.GetHandDirection());
				}
			}
		}
	}

	private void initializeSeries() {
		_gravityXSeries = new LineGraphSeries<>(new DataPoint[] { new DataPoint(0, 0), new DataPoint(1, 10),
				new DataPoint(2, -10), new DataPoint(3, 0), new DataPoint(4, 0) });
		_gravityXSeries.setColor(Color.RED);

		_gravityYSeries = new LineGraphSeries<>(new DataPoint[] { new DataPoint(0, 0), new DataPoint(1, 9),
				new DataPoint(2, -9), new DataPoint(3, 0), new DataPoint(4, 0) });
		_gravityYSeries.setColor(Color.GREEN);

		_gravityZSeries = new LineGraphSeries<>(new DataPoint[] { new DataPoint(0, 0), new DataPoint(1, 8),
				new DataPoint(2, -8), new DataPoint(3, 0), new DataPoint(4, 0) });
		_gravityZSeries.setColor(Color.BLUE);
	}

	private void initializeSensor() {
		_sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		_sensor = _sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		_hasGravitySensor = checkSensorAvailability();
	}

	private boolean checkSensorAvailability() {
		if (_sensor != null) {
			_logger.Debug("Sensor is available");
			_sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
			return true;
		} else {
			_logger.Debug("Sensor is not available");
			return false;
		}
	}

	private boolean gravityHasSignificantChange(GravityDto gravityDto) {
		if ((_lastGravityDto.GetX() - gravityDto.GetX() > GRAVITY_CHANGE_BORDER
				|| _lastGravityDto.GetX() - gravityDto.GetX() < -GRAVITY_CHANGE_BORDER)
				|| (_lastGravityDto.GetY() - gravityDto.GetY() > GRAVITY_CHANGE_BORDER
						|| _lastGravityDto.GetY() - gravityDto.GetY() < -GRAVITY_CHANGE_BORDER)
				|| (_lastGravityDto.GetZ() - gravityDto.GetZ() > GRAVITY_CHANGE_BORDER
						|| _lastGravityDto.GetZ() - gravityDto.GetZ() < -GRAVITY_CHANGE_BORDER)) {
			return true;
		}
		return false;
	}
}