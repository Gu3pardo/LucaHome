package guepardoapps.lucahome.accesscontrol.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import guepardoapps.lucahome.accesscontrol.common.Constants;

import guepardoapps.toolset.common.Logger;

public class ControlServiceStateService extends Service {

	private static final String TAG = ControlServiceStateService.class.getName();
	private Logger _logger;

	private boolean _isInitialized;
	private int _errorCount;

	private Context _context;

	private Handler _checkServicesHandler;

	private Runnable _checkServices = new Runnable() {
		public void run() {
			_logger.Debug("_checkServices");

			if (!isServiceRunning(MainService.class)) {
				_logger.Warn("MainService not running! Restarting!");
				_errorCount++;
				_logger.Warn("_errorCount: " + String.valueOf(_errorCount));
				if (_errorCount >= 5) {
					// TODO: send warning mail to me!
					_logger.Info("TODO: send warning mail to me!");
				} else {
					Intent serviceIntent = new Intent(_context, MainService.class);
					startService(serviceIntent);
				}
			} else {
				_errorCount = 0;
			}

			_checkServicesHandler.postDelayed(_checkServices, 60 * 1000);
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		if (!_isInitialized) {
			if (_logger == null) {
				_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
			}

			_isInitialized = true;
			_errorCount = 0;

			_context = this;

			_checkServicesHandler = new Handler();
			_checkServices.run();
		}

		_logger.Debug("onStartCommand");

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private boolean isServiceRunning(Class<?> serviceClass) {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
