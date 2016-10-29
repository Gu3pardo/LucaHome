package guepardoapps.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.service.SocketActionService;

public class SocketActionReceiver extends BroadcastReceiver {

	private String TAG = "SocketActionReceiver";

	private Logger _logger;

	private ServiceController _serviceController;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new Logger(TAG);
		_logger.Debug("Received new socket change!");

		Bundle details = intent.getExtras();

		String action = details.getString(Constants.BUNDLE_ACTION);
		if (action.contains("ALL_SOCKETS")) {
			_serviceController = new ServiceController(context);
			_serviceController.StartRestService("SHOW_NOTIFICATION_SOCKET", Constants.ACTION_DEACTIVATE_ALL_SOCKETS, "",
					LucaObject.WIRELESS_SOCKET);
		} else if (action.contains("SINGLE_SOCKET")) {
			WirelessSocket socket = (WirelessSocket) details.getSerializable(Constants.BUNDLE_SOCKET_DATA);
			_logger.Debug("socket: " + socket.toString());

			Intent serviceIntent = new Intent(context, SocketActionService.class);

			Bundle serviceData = new Bundle();
			serviceData.putSerializable(Constants.BUNDLE_SOCKET_DATA, socket);
			serviceIntent.putExtras(serviceData);

			context.startService(serviceIntent);
		} else {
			_logger.Error("Action contains errors: " + action);
			Toast.makeText(context, "Action contains errors: " + action, Toast.LENGTH_LONG).show();
		}
	}
}