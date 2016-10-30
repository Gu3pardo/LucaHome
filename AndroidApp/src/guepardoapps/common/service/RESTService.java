package guepardoapps.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;

public class RESTService extends Service {

	private static String TAG = "RESTService";

	private String _action;
	private String[] _actions;
	private String _name;
	private String _broadcast;
	private LucaObject _lucaObject;
	private RaspberrySelection _raspberrySelection;

	private String[] _answer;

	private Logger _logger;

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_logger = new Logger(TAG);

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

		_raspberrySelection = (RaspberrySelection) data.getSerializable(Constants.BUNDLE_RASPBERRY_SELETION);

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

		_name = data.getString(Constants.BUNDLE_NAME);
		_broadcast = data.getString(Constants.BUNDLE_BROADCAST);
		_lucaObject = (LucaObject) data.getSerializable(Constants.BUNDLE_LUCA_OBJECT);

		_answer = new String[_actions.length];

		SendActionTask task = new SendActionTask();
		task.execute(_actions);

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class SendActionTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... actions) {
			String response = "";
			int answerIndex = 0;
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

					_answer[answerIndex] = response;
					_logger.Debug(response);

				} catch (IOException e) {
					_logger.Error(e.getMessage());
				} finally {
					answerIndex++;
				}
			}

			return "FINISHED";
		}

		@Override
		protected void onPostExecute(String result) {
			_logger.Debug(result);

			// Hack for deactivating all sockets
			if (_name.contains("SHOW_NOTIFICATION_SOCKET")) {
				ServiceController serviceController = new ServiceController(RESTService.this);
				serviceController.StartWifiDownload();
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
