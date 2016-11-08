package guepardoapps.lucahome.common.controller;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.lucahome.common.LucaHomeLogger;

public class BroadcastController {

	private static String TAG = BroadcastController.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;

	public BroadcastController(Context context) {
		_logger = new LucaHomeLogger(TAG);
		_context = context;
	}

	public void SendSerializableBroadcast(String broadcast, String[] bundleNames, Object[] models) {
		_logger.Debug("Send Serializable Broadcast: " + broadcast);

		if (bundleNames.length != models.length) {
			_logger.Warn("Cannot send broadcast! length are not equal!");
			return;
		}

		Intent broadcastIntent = new Intent(broadcast);
		Bundle broadcastData = new Bundle();
		for (int index = 0; index < bundleNames.length; index++) {
			broadcastData.putSerializable(bundleNames[index], (Serializable) models[index]);
		}
		broadcastIntent.putExtras(broadcastData);

		_context.sendBroadcast(broadcastIntent);
	}

	public void SendIntBroadcast(String broadcast, String bundleName, int data) {
		_logger.Debug("Send Integer Broadcast: " + broadcast);

		Intent broadcastIntent = new Intent(broadcast);
		Bundle broadcastData = new Bundle();
		broadcastData.putInt(bundleName, data);
		broadcastIntent.putExtras(broadcastData);

		_context.sendBroadcast(broadcastIntent);
	}
}
