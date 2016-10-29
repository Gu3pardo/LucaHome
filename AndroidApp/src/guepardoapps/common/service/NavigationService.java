package guepardoapps.common.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.common.classes.Logger;

public class NavigationService {

	private static String TAG = "NavigationService";

	private Logger _logger;
	private Context _context;

	public NavigationService(Context context) {
		_logger = new Logger(TAG);
		_context = context;
	}

	public void NavigateTo(Class<?> target, Bundle data, boolean finish) {
		_logger.Debug("Navigate to " + target.toString());

		Intent navigateTo = new Intent(_context, target);
		if (data != null) {
			_logger.Debug("data is not null!");
			navigateTo.putExtras(data);
		}
		_context.startActivity(navigateTo);

		if (finish) {
			((Activity) _context).finish();
		}
	}
}
