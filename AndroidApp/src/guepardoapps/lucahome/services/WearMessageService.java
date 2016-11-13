package guepardoapps.lucahome.services;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;

public class WearMessageService extends Service implements GoogleApiClient.ConnectionCallbacks {

	private static final String WEAR_MESSAGE_PATH = "/message";
	private static final String TAG = WearMessageService.class.getName();
	private static final int MAX_SEND_TRY_COUNT = 5;

	private LucaHomeLogger _logger;

	private boolean _connected;
	private boolean _dataSend;

	private String _messageText;
	private GoogleApiClient _apiClient;

	private Handler _sendHandler = new Handler();
	private int _sendTimeout = 500;
	private int _sendTryCount;

	private Runnable _sendRunnable = new Runnable() {
		public void run() {
			_logger.Debug("_sendRunnable run");
			if (_connected) {
				sendMessage(WEAR_MESSAGE_PATH, _messageText);
			} else {
				if (_sendTryCount < MAX_SEND_TRY_COUNT) {
					_sendHandler.postDelayed(_sendRunnable, _sendTimeout);
					_sendTryCount++;
				} else {
					_logger.Warn("Tried too many times to send message! Cancel send! Stopping service!");
					stopSelf();
				}
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_logger = new LucaHomeLogger(TAG);

		Bundle data = intent.getExtras();
		_messageText = data.getString(Constants.BUNDLE_WEAR_MESSAGE_TEXT);
		if (_messageText != null) {
			_logger.Debug("messageText: " + _messageText);
			initGoogleApiClient();
			_sendTryCount = 0;
			_sendRunnable.run();
		}

		finish();
		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onConnected(Bundle arg0) {
		_logger.Debug("onConnected");
		_connected = true;
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		_logger.Debug("onConnectionSuspended");
		_connected = false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		_sendHandler.removeCallbacks(_sendRunnable);
	}

	private void initGoogleApiClient() {
		_logger.Debug("initGoogleApiClient");
		_apiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
		_apiClient.connect();
	}

	private void sendMessage(final String path, final String text) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				_logger.Debug("sendMessage run");
				NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(_apiClient).await();
				for (Node node : nodes.getNodes()) {
					MessageApi.SendMessageResult result = Wearable.MessageApi
							.sendMessage(_apiClient, node.getId(), path, text.getBytes()).await();

					if (result != null) {
						_logger.Debug("result: " + result);
					} else {
						_logger.Warn("result is null");
					}
				}
				_dataSend = true;
			}
		}).start();
	}

	private void finish() {
		_logger.Debug("finish");
		if (_dataSend) {
			stopSelf();
		} else {
			_logger.Warn("Data not send yet!");
		}
	}
}
