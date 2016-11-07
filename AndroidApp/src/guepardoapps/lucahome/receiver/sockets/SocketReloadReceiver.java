package guepardoapps.lucahome.receiver.sockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import guepardoapps.lucahome.common.Logger;
import guepardoapps.lucahome.common.controller.ServiceController;

public class SocketReloadReceiver extends BroadcastReceiver {

	private String TAG = "SocketReloadReceiver";

	private Logger _logger;

	private ServiceController _serviceController;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new Logger(TAG);
		_logger.Debug("Received reload sockets!");

		_serviceController = new ServiceController(context);
		_serviceController.StartSocketDownload();
	}
}