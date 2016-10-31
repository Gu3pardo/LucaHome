package guepardoapps.common.receiver.sound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import guepardoapps.common.Logger;
import guepardoapps.common.controller.ServiceController;

public class StopSoundReceiver extends BroadcastReceiver {

	private String TAG = "StopSoundReceiver";

	private Logger _logger;

	private ServiceController _serviceController;

	@Override
	public void onReceive(Context context, Intent intent) {
		_logger = new Logger(TAG);
		_logger.Debug("Received stop sounds!");

		_serviceController = new ServiceController(context);
		_serviceController.StopSound();
	}
}