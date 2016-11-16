package guepardoapps.lucahome.wearcontrol.services;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.wearcontrol.common.helper.MessageSendHelper;
import guepardoapps.lucahome.wearcontrol.viewcontroller.*;

import guepardoapps.toolset.common.Constants;
import guepardoapps.toolset.common.Logger;

@SuppressWarnings("deprecation")
public class WatchFaceService extends CanvasWatchFaceService {

	private static final String TAG = WatchFaceService.class.getName();
	private Logger _logger;

	private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

	@Override
	public Engine onCreateEngine() {
		return new Engine();
	}

	private class Engine extends CanvasWatchFaceService.Engine {

		private static final float STROKE_WIDTH = 4f;
		private static final int SHADOW_RADIUS = 6;

		private final Handler _updateTimeHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (R.id.message_update == message.what) {
					invalidate();
					if (shouldTimerBeRunning()) {
						long timeMs = System.currentTimeMillis();
						long delayMs = INTERACTIVE_UPDATE_RATE_MS - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
						_updateTimeHandler.sendEmptyMessageDelayed(R.id.message_update, delayMs);
					}
				}
			}
		};

		private final BroadcastReceiver _timeZoneReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				_time.clear(intent.getStringExtra("time-zone"));
				_time.setToNow();
			}
		};

		private boolean _registeredTimeZoneReceiver = false;

		private Time _time;

		private Paint _backgroundPaint;
		private Paint _handPaint;

		private boolean _ambient;

		private Bitmap _backgroundBitmap;
		private Bitmap _grayBackgroundBitmap;
		private int _watchHandColor;
		private int _watchHandShadowColor;

		private boolean _lowBitAmbient;
		private boolean _burnInProtection;

		private float _scale = 1;
		private Rect _cardBounds = new Rect();

		private Context _context;

		private BatteryPhoneViewController _batteryPhoneViewController;
		private BatteryWearViewController _batteryWearViewController;
		private CurrentWeatherViewController _currentWeatherViewController;
		private DateViewController _dateViewController;
		private StepViewController _stepViewController;
		private TemperatureViewController _temperatureViewController;

		@Override
		public void onCreate(SurfaceHolder holder) {
			super.onCreate(holder);

			_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
			_logger.Debug("onCreate");

			_context = WatchFaceService.this;

			startService(new Intent(_context, WearMessageListenerService.class));

			MessageSendHelper messageSendHelper = new MessageSendHelper(_context);
			messageSendHelper.SendMessage("Hello Phone!");

			_batteryPhoneViewController = new BatteryPhoneViewController(_context);
			_batteryWearViewController = new BatteryWearViewController(_context);
			_currentWeatherViewController = new CurrentWeatherViewController(_context);
			_dateViewController = new DateViewController(_context);
			_stepViewController = new StepViewController(_context);
			_temperatureViewController = new TemperatureViewController(_context);

			setWatchFaceStyle(
					new WatchFaceStyle.Builder((Service) _context).setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
							.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
							.setShowSystemUiTime(false).build());

			_backgroundPaint = new Paint();
			_backgroundPaint.setColor(Color.BLACK);

			final int backgroundResId = R.drawable.wallpaper1;

			_backgroundBitmap = BitmapFactory.decodeResource(getResources(), backgroundResId);
			_handPaint = new Paint();
			_handPaint.setColor(Color.WHITE);
			_handPaint.setStrokeWidth(STROKE_WIDTH);
			_handPaint.setAntiAlias(true);
			_handPaint.setStrokeCap(Paint.Cap.ROUND);
			_handPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, Color.BLACK);
			_handPaint.setStyle(Paint.Style.STROKE);

			Palette.generateAsync(_backgroundBitmap, new Palette.PaletteAsyncListener() {
				@Override
				public void onGenerated(Palette palette) {
					if (palette != null) {
						_watchHandColor = Color.WHITE;
						_watchHandShadowColor = Color.BLACK;
						setWatchHandColor();
					}
				}
			});

			_time = new Time();
		}

		private void setWatchHandColor() {
			_logger.Debug("setWatchHandColor");

			if (_ambient) {
				_handPaint.setColor(Color.WHITE);
				_handPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, Color.BLACK);
			} else {
				_handPaint.setColor(_watchHandColor);
				_handPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, _watchHandShadowColor);
			}
		}

		@Override
		public void onDestroy() {
			_logger.Debug("onDestroy");

			_batteryWearViewController.onDestroy();
			_batteryPhoneViewController.onDestroy();
			_currentWeatherViewController.onDestroy();
			_dateViewController.onDestroy();
			_stepViewController.onDestroy();
			_temperatureViewController.onDestroy();

			_updateTimeHandler.removeMessages(R.id.message_update);
			super.onDestroy();
		}

		@Override
		public void onPropertiesChanged(Bundle properties) {
			_logger.Debug("onPropertiesChanged");
			super.onPropertiesChanged(properties);
			_lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
			_burnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
		}

		@Override
		public void onTimeTick() {
			_logger.Debug("onTimeTick");
			super.onTimeTick();
			invalidate();
		}

		@Override
		public void onAmbientModeChanged(boolean inAmbientMode) {
			_logger.Debug("onAmbientModeChanged");
			super.onAmbientModeChanged(inAmbientMode);
			if (_ambient != inAmbientMode) {
				_ambient = inAmbientMode;
				if (_lowBitAmbient || _burnInProtection) {
					_handPaint.setAntiAlias(!inAmbientMode);
				}
				setWatchHandColor();
				invalidate();
			}

			updateTimer();
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			_logger.Debug("onSurfaceChanged");
			super.onSurfaceChanged(holder, format, width, height);

			_scale = ((float) width) / (float) _backgroundBitmap.getWidth();

			_backgroundBitmap = Bitmap.createScaledBitmap(_backgroundBitmap,
					(int) (_backgroundBitmap.getWidth() * _scale), (int) (_backgroundBitmap.getHeight() * _scale),
					true);

			if (!_burnInProtection || !_lowBitAmbient) {
				initGrayBackgroundBitmap();
			}
		}

		private void initGrayBackgroundBitmap() {
			_logger.Debug("initGrayBackgroundBitmap");
			_grayBackgroundBitmap = Bitmap.createBitmap(_backgroundBitmap.getWidth(), _backgroundBitmap.getHeight(),
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(_grayBackgroundBitmap);
			Paint grayPaint = new Paint();
			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.setSaturation(0);
			ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
			grayPaint.setColorFilter(filter);
			canvas.drawBitmap(_backgroundBitmap, 0, 0, grayPaint);
		}

		@Override
		public void onDraw(Canvas canvas, Rect bounds) {
			_logger.Debug("onDraw");
			_time.setToNow();

			if (_ambient && (_lowBitAmbient || _burnInProtection)) {
				canvas.drawColor(Color.BLACK);
			} else if (_ambient) {
				canvas.drawBitmap(_grayBackgroundBitmap, 0, 0, _backgroundPaint);
			} else {
				canvas.drawBitmap(_backgroundBitmap, 0, 0, _backgroundPaint);
			}

			if (_ambient) {
				canvas.drawRect(_cardBounds, _backgroundPaint);
			}

			_batteryWearViewController.Draw(canvas);
			_batteryPhoneViewController.Draw(canvas);
			_currentWeatherViewController.Draw(canvas);
			_dateViewController.DrawDate(canvas, _time);
			_dateViewController.DrawWeekOfYear(canvas, _time);
			_dateViewController.DrawTime(canvas, _time);
			_stepViewController.Draw(canvas);
			_temperatureViewController.Draw(canvas);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			_logger.Debug("onVisibilityChanged");
			super.onVisibilityChanged(visible);

			if (visible) {
				registerReceiver();

				_time.clear(TimeZone.getDefault().getID());
				_time.setToNow();
			} else {
				unregisterReceiver();
			}

			updateTimer();
		}

		@Override
		public void onPeekCardPositionUpdate(Rect rect) {
			_logger.Debug("onPeekCardPositionUpdate");
			super.onPeekCardPositionUpdate(rect);
			_cardBounds.set(rect);
		}

		private void registerReceiver() {
			_logger.Debug("registerReceiver");
			if (_registeredTimeZoneReceiver) {
				return;
			}
			_registeredTimeZoneReceiver = true;
			IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
			_context.registerReceiver(_timeZoneReceiver, filter);
		}

		private void unregisterReceiver() {
			_logger.Debug("unregisterReceiver");
			if (!_registeredTimeZoneReceiver) {
				return;
			}
			_registeredTimeZoneReceiver = false;
			_context.unregisterReceiver(_timeZoneReceiver);
		}

		private void updateTimer() {
			_logger.Debug("updateTimer");
			_updateTimeHandler.removeMessages(R.id.message_update);
			if (shouldTimerBeRunning()) {
				_updateTimeHandler.sendEmptyMessage(R.id.message_update);
			}
		}

		private boolean shouldTimerBeRunning() {
			_logger.Debug("shouldTimerBeRunning");
			return isVisible() && !isInAmbientMode();
		}
	}
}