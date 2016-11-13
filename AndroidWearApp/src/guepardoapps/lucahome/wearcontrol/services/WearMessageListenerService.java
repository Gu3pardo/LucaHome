package guepardoapps.lucahome.wearcontrol.services;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import android.os.Bundle;

import guepardoapps.lucahome.wearcontrol.common.Constants;
import guepardoapps.lucahome.wearcontrol.common.helper.MessageHelper;

import guepardoapps.toolset.common.Logger;

public class WearMessageListenerService extends WearableListenerService
		implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = WearMessageListenerService.class.getName();
	private Logger _logger;

	private MessageHelper _messageHelper;

	private static final String WEAR_MESSAGE_PATH = "/message";
	private GoogleApiClient _apiClient;

	@Override
	public void onCreate() {
		super.onCreate();
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_messageHelper = new MessageHelper(this);

		initGoogleApiClient();
	}

	@Override
	public void onDestroy() {
		_logger.Debug("onDestroy");
		if (_apiClient != null) {
			_apiClient.unregisterConnectionCallbacks(this);
			Wearable.MessageApi.removeListener(_apiClient, this);
			if (_apiClient.isConnected()) {
				_apiClient.disconnect();
			}
		}
		super.onDestroy();
	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent) {
		_logger.Debug("onMessageReceived");
		if (messageEvent.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) {
			String message = new String(messageEvent.getData());
			_messageHelper.HandleMessage(message);
		} else {
			_logger.Warn("Path is not " + WEAR_MESSAGE_PATH);
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		_logger.Debug("onConnected");
		Wearable.MessageApi.addListener(_apiClient, this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		_logger.Debug("onConnectionSuspended");
	}

	private void initGoogleApiClient() {
		_logger.Debug("initGoogleApiClient");
		_apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).build();

		if (_apiClient != null && !(_apiClient.isConnected() || _apiClient.isConnecting())) {
			_logger.Debug("_apiClient.connect");
			_apiClient.connect();
		}
	}
}
