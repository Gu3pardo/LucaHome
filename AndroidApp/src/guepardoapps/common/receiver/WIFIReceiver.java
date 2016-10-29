package guepardoapps.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.NetworkController;
import guepardoapps.toolset.openweather.OpenWeatherConstants;

public class WIFIReceiver extends BroadcastReceiver {

	private static String TAG = "WIFIReceiver";
	private Logger _logger;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new Logger(TAG);

		int textColor = ContextCompat.getColor(context, R.color.TextIcon);
		int backgroundColor = ContextCompat.getColor(context, R.color.Background);

		DialogController dialogController = new DialogController(context, textColor, backgroundColor);
		NetworkController networkController = new NetworkController(context, dialogController);
		ServiceController serviceController = new ServiceController(context);

		if (networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			_logger.Debug("We are in the homenetwork!");
			serviceController.StartWifiDownload();
		} else {
			_logger.Debug("We are NOT in the homenetwork!");
			serviceController.CloseNotification(Constants.ID_NOTIFICATION_TEMPERATURE);
			serviceController.CloseNotification(Constants.ID_NOTIFICATION_WEAR);
			serviceController.CloseNotification(OpenWeatherConstants.FORECAST_NOTIFICATION_ID);
		}
	}
}