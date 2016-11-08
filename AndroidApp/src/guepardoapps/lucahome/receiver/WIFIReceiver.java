package guepardoapps.lucahome.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.controller.BroadcastController;
import guepardoapps.lucahome.common.controller.ServiceController;
import guepardoapps.lucahome.common.enums.MainServiceAction;

import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.NetworkController;

import guepardoapps.toolset.openweather.common.OpenWeatherConstants;

public class WIFIReceiver extends BroadcastReceiver {

	private static String TAG = WIFIReceiver.class.getName();
	private LucaHomeLogger _logger;

	private BroadcastController _broadcastController;
	private DialogController _dialogController;
	private NetworkController _networkController;
	private ServiceController _serviceController;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new LucaHomeLogger(TAG);

		int textColor = ContextCompat.getColor(context, R.color.TextIcon);
		int backgroundColor = ContextCompat.getColor(context, R.color.Background);

		_broadcastController = new BroadcastController(context);
		_dialogController = new DialogController(context, textColor, backgroundColor);
		_networkController = new NetworkController(context, _dialogController);
		_serviceController = new ServiceController(context);

		if (_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			_logger.Debug("We are in the homenetwork!");
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
					new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
					new Object[] { MainServiceAction.DOWNLOAD_ALL });
		} else {
			_logger.Warn("We are NOT in the homenetwork!");
			_serviceController.CloseNotification(Constants.ID_NOTIFICATION_TEMPERATURE);
			_serviceController.CloseNotification(Constants.ID_NOTIFICATION_WEAR);
			_serviceController.CloseNotification(OpenWeatherConstants.FORECAST_NOTIFICATION_ID);
		}
	}
}