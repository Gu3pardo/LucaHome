package guepardoapps.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.lucahome.R;

import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.NetworkController;
import guepardoapps.toolset.openweather.OpenWeatherConstants;

public class WIFIReceiver extends BroadcastReceiver {

	private static String TAG = "WIFIReceiver";

	private Logger _logger;

	private DialogController _dialogController;
	private NetworkController _networkController;
	private ServiceController _serviceController;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new Logger(TAG);

		int textColor = ContextCompat.getColor(context, R.color.TextIcon);
		int backgroundColor = ContextCompat.getColor(context, R.color.Background);

		_dialogController = new DialogController(context, textColor, backgroundColor);
		_networkController = new NetworkController(context, _dialogController);
		_serviceController = new ServiceController(context);

		if (_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			_logger.Debug("We are in the homenetwork!");
			_serviceController.StartWifiDownload();
		} else {
			_logger.Warn("We are NOT in the homenetwork!");
			_serviceController.CloseNotification(Constants.ID_NOTIFICATION_TEMPERATURE);
			_serviceController.CloseNotification(Constants.ID_NOTIFICATION_WEAR);
			_serviceController.CloseNotification(OpenWeatherConstants.FORECAST_NOTIFICATION_ID);
		}
	}
}