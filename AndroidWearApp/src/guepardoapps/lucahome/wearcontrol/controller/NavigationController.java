package guepardoapps.lucahome.wearcontrol.controller;

import android.content.Context;
import android.content.Intent;

import guepardoapps.lucahome.common.Constants;

import guepardoapps.lucahome.wearcontrol.enums.TargetActivity;

import guepardoapps.toolset.common.Logger;

public class NavigationController {

	private static final String TAG = NavigationController.class.getName();
	private Logger _logger;

	private Context _context;

	public NavigationController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
	}

	public void NavigateTo(String target) {
		_logger.Debug("NavigateTo: " + target);
		TargetActivity targetActivity = TargetActivity.GetByString(target);
		if (targetActivity != null) {
			_logger.Debug("targetActivity found!");
			Class<?> newTarget = targetActivity.GetActivity();
			if (newTarget != null) {
				_logger.Debug("newTarget found!");
				Intent navigateTo = new Intent(_context, newTarget);
				_context.startActivity(navigateTo);
			}
		}
	}
}
