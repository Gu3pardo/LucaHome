package guepardoapps.lucahome.wearcontrol.services;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import guepardoapps.lucahome.wearcontrol.common.Constants;

import guepardoapps.toolset.common.Logger;

public class PhoneMessageService extends Service implements GoogleApiClient.ConnectionCallbacks {

	private static final String PHONE_MESSAGE_PATH = "/phone_message";
	private static final String TAG = PhoneMessageService.class.getName();

	private Logger _logger;

	private boolean _dataSend;

	private GoogleApiClient _apiClient;

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		if (_logger == null) {
			_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		}

		if (intent != null) {
			Bundle data = intent.getExtras();
			if (data != null) {
				String message = data.getString(Constants.BUNDLE_PHONE_MESSAGE_TEXT);
				if (message != null) {
					_logger.Debug("message: " + message);
					initGoogleApiClient();
					sendMessage(PHONE_MESSAGE_PATH, message);
				}
			} else {
				_logger.Warn("Data is null!");
			}
		} else {
			_logger.Warn("Intent is null!");
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
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		_logger.Debug("onConnectionSuspended");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
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
