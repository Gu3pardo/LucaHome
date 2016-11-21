package guepardoapps.lucahome.wearcontrol.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.wearcontrol.services.PhoneMessageService;

import guepardoapps.toolset.common.Logger;

public class MessageSendHelper {

	private static final String TAG = MessageSendHelper.class.getName();
	private Logger _logger;

	private Context _context;

	public MessageSendHelper(Context context) {
		_logger = new Logger(TAG);
		_context = context;
	}

	public void SendMessage(String message) {
		_logger.Debug("SendMessage");
		if (message != null) {
			_logger.Debug("message: " + message);

			Intent serviceIntent = new Intent(_context, PhoneMessageService.class);
			Bundle serviceData = new Bundle();
			serviceData.putString(Constants.BUNDLE_PHONE_MESSAGE_TEXT, message);
			serviceIntent.putExtras(serviceData);
			_context.startService(serviceIntent);
		} else {
			_logger.Warn("Message is null!");
		}
	}
}
