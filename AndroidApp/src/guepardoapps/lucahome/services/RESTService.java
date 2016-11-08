package guepardoapps.lucahome.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.controller.BroadcastController;
import guepardoapps.lucahome.common.enums.LucaObject;
import guepardoapps.lucahome.common.enums.MainServiceAction;
import guepardoapps.lucahome.common.enums.RaspberrySelection;

import guepardoapps.toolset.controller.DialogController;
import guepardoapps.toolset.controller.NetworkController;

public class RESTService extends Service {

	private static String TAG = RESTService.class.getName();
	private LucaHomeLogger _logger;

	private String _action;
	private String[] _actions;
	private RaspberrySelection _raspberrySelection;

	private Context _context;
	private NetworkController _networkController;

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		if (_logger == null) {
			_logger = new LucaHomeLogger(TAG);
		}

		if (_context == null) {
			_context = this;
		}
		if (_networkController == null) {
			if (_context != null) {
				_networkController = new NetworkController(_context,
						new DialogController(_context, ContextCompat.getColor(_context, R.color.TextIcon),
								ContextCompat.getColor(_context, R.color.Background)));
			}
		}

		if (!_networkController.IsNetworkAvailable()) {
			_logger.Warn("No network available!");
			return 0;
		}

		if (!_networkController.IsHomeNetwork(Constants.LUCAHOME_SSID)) {
			_logger.Warn("No LucaHome network! ...");
			return 0;
		}

		Bundle data = intent.getExtras();
		_action = data.getString(Constants.BUNDLE_ACTION);

		String user = data.getString(Constants.BUNDLE_USER);
		String password = data.getString(Constants.BUNDLE_PASSPHRASE);
		if (user == null) {
			_logger.Warn("No user!");
			return 101;
		}
		if (password == null) {
			_logger.Warn("No password!");
			return 102;
		}

		_raspberrySelection = (RaspberrySelection) data.getSerializable(Constants.BUNDLE_RASPBERRY_SELECTION);

		if (_raspberrySelection == null) {
			_logger.Error("_raspberrySelection is null!");
			_raspberrySelection = RaspberrySelection.BOTH;
		}

		String url1 = "";
		String url2 = "";

		switch (_raspberrySelection) {
		case RASPBERRY_1:
			url1 = Constants.REST_URL_RPI1 + user + "&password=" + password + "&action=" + _action;
			_actions = new String[] { url1 };
			break;
		case RASPBERRY_2:
			url2 = Constants.REST_URL_RPI2 + user + "&password=" + password + "&action=" + _action;
			_actions = new String[] { url2 };
			break;
		case BOTH:
		case DUMMY:
		default:
			url1 = Constants.REST_URL_RPI1 + user + "&password=" + password + "&action=" + _action;
			url2 = Constants.REST_URL_RPI2 + user + "&password=" + password + "&action=" + _action;
			_actions = new String[] { url1, url2 };
			break;
		}

		String name = data.getString(Constants.BUNDLE_NAME);
		String broadcast = data.getString(Constants.BUNDLE_BROADCAST);
		LucaObject lucaObject = (LucaObject) data.getSerializable(Constants.BUNDLE_LUCA_OBJECT);

		SendActionTask task = new SendActionTask();
		task.setValues(name, broadcast, lucaObject, _actions.length);
		task.execute(_actions);

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class SendActionTask extends AsyncTask<String, Void, String> {
		private static final String TAG = "RESTService";

		private LucaHomeLogger _logger;

		private String _name;
		private String _broadcast;
		private LucaObject _lucaObject;

		private String[] _answer;

		public void setValues(String name, String broadcast, LucaObject lucaObject, int answerSize) {
			_logger = new LucaHomeLogger(TAG);

			_name = name;
			_broadcast = broadcast;
			_lucaObject = lucaObject;
			_answer = new String[answerSize];
		}

		@Override
		protected String doInBackground(String... actions) {
			String response = "";
			int answerIndex = 0;
			boolean downloadSuccess = false;

			for (String action : actions) {
				try {
					response = "";

					URL url = new URL(action);
					URLConnection connection = url.openConnection();
					InputStream inputStream = connection.getInputStream();

					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

					String line;
					while ((line = reader.readLine()) != null) {
						response += line;
					}

					downloadSuccess = true;
					_answer[answerIndex] = response;
					_logger.Debug(response);

				} catch (IOException e) {
					downloadSuccess = false;
					_logger.Error(e.getMessage());
				} finally {
					answerIndex++;
				}
			}

			return String.valueOf(downloadSuccess);
		}

		@Override
		protected void onPostExecute(String result) {
			_logger.Debug("downloadSuccess: " + result);

			if (result.contains(String.valueOf(false))) {
				_logger.Error("Failed to download!");
				stopSelf();
				return;
			}

			// Hack for deactivating all sockets
			if (_name.contains("SHOW_NOTIFICATION_SOCKET")) {
				BroadcastController broadcastController = new BroadcastController(RESTService.this);
				broadcastController.SendSerializableBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
						new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
						new Object[] { MainServiceAction.DOWLOAD_SOCKETS });
			}
			// End hack
			else {
				if (_broadcast != null && _broadcast != "") {
					Intent broadcastIntent = new Intent(_broadcast);
					Bundle broadcastData = new Bundle();
					broadcastData.putStringArray(_name, _answer);
					broadcastData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, _lucaObject);
					broadcastIntent.putExtras(broadcastData);
					sendBroadcast(broadcastIntent);
				}
			}

			stopSelf();
		}
	}
}
