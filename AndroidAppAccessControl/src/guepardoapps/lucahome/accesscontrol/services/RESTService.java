package guepardoapps.lucahome.accesscontrol.services;

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
import android.widget.Toast;

import guepardoapps.lucahome.accesscontrol.common.Constants;

import guepardoapps.toolset.common.Logger;

public class RESTService extends Service {

	private static final String TAG = RESTService.class.getName();
	private Logger _logger;

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		if (Constants.SERVER_URLs.length == 0) {
			_logger.Error("You did not enter server ips!");
			Toast.makeText(this, "You did not enter server ips!", Toast.LENGTH_LONG).show();
			return -1;
		}

		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			_logger.Warn("Bundle is null!");
			stopSelf();
			return -1;
		}

		String action = bundle.getString(Constants.BUNDLE_REST_ACTION);
		if (action == null) {
			_logger.Warn("Action is null!");
			stopSelf();
			return -1;
		}
		_logger.Debug("Action: " + action);

		String[] actions = new String[Constants.SERVER_URLs.length];
		for (int index = 0; index < Constants.SERVER_URLs.length; index++) {
			actions[index] = Constants.SERVER_URLs[index] + Constants.ACTION_PATH + Constants.USER_NAME + "&password="
					+ Constants.PASS_PHRASE + "&action=" + action;
			_logger.Debug("index " + String.valueOf(index) + ": " + actions[index]);
		}

		RestCommunicationTask task = new RestCommunicationTask();
		task.execute(actions);

		return 0;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class RestCommunicationTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... actions) {
			String response = "";
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
					_logger.Debug(response);

				} catch (IOException e) {
					_logger.Error(e.getMessage());
				}
			}
			return "FINISHED";
		}

		@Override
		protected void onPostExecute(String result) {
			_logger.Debug(result);
			stopSelf();
		}
	}
}
