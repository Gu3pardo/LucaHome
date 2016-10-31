package guepardoapps.common.service.sockets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.classes.controller.SocketController;
import guepardoapps.common.controller.ReceiverController;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;

public class SocketActionService extends Service {

	private static String TAG = "SocketActionService";

	private Context _context;

	private Logger _logger;

	private ReceiverController _receiverController;
	private ServiceController _serviceController;
	private SocketController _socketController;

	private BroadcastReceiver _notificationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received Notification Action!");
			_serviceController.StartSocketDownload();
			stopSelf();
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_context = this;

		_logger = new Logger(TAG);
		_logger.Debug("Received new socket change!");

		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);
		_socketController = new SocketController(_context);

		WirelessSocket socket = (WirelessSocket) intent.getExtras().getSerializable(Constants.BUNDLE_SOCKET_DATA);
		_logger.Debug("socket: " + socket.toString());

		_receiverController.RegisterReceiver(_notificationReceiver, new String[] { socket.GetNotificationBroadcast() });
		_serviceController.StartRestService(socket.GetName(), socket.GetCommandSet(!socket.GetIsActivated()),
				socket.GetNotificationBroadcast(), LucaObject.WIRELESS_SOCKET, RaspberrySelection.BOTH);
		_socketController.CheckMedia(socket);

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_receiverController.UnregisterReceiver(_notificationReceiver);
	}
}
