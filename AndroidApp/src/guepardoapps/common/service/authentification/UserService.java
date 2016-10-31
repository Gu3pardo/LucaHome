package guepardoapps.common.service.authentification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.User;
import guepardoapps.common.controller.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.service.RESTService;
import guepardoapps.toolset.controller.SharedPrefController;

public class UserService {

	private static String TAG = "UserService";

	private boolean _userValidated;
	private Runnable _storedCallback = null;

	private Logger _logger;

	private Context _context;

	private ReceiverController _receiverController;

	private SharedPrefController _sharedPrefController;

	private BroadcastReceiver _receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received data...");

			String[] answerArray = intent.getStringArrayExtra(Constants.VALIDATE_USER);

			for (String answer : answerArray) {
				if (answer != null) {
					_logger.Debug(answer);
					if (answer.contains("Error")) {
						_logger.Warn(answer);
						_userValidated = false;
						break;
					} else {
						answer = answer.replace(Constants.ACTION_VALIDATE_USER, "").replace(":", "");
						if (answer.length() == 1 && answer.contains("1")) {
							_userValidated &= true;
						} else {
							_logger.Warn(answer);
						}
					}
				}
			}

			_receiverController.UnregisterReceiver(_receiver);

			_storedCallback.run();
			_storedCallback = null;
		}
	};

	public UserService(Context context) {
		_userValidated = true;

		_logger = new Logger(TAG);

		_context = context;

		_receiverController = new ReceiverController(_context);

		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);
	}

	public User LoadUser() {
		String userName = _sharedPrefController.LoadStringValueFromSharedPreferences(Constants.USER_NAME);
		String userPassword = _sharedPrefController.LoadStringValueFromSharedPreferences(Constants.USER_PASSPHRASE);
		if (userName != null && userPassword != null) {
			User user = new User(userName, userPassword);
			return user;
		}
		return null;
	}

	public void ValidateUser(User user, Runnable callback) {
		_receiverController.RegisterReceiver(_receiver, new String[] { Constants.BROADCAST_VALIDATE_USER });

		if (callback != null) {
			_storedCallback = callback;
		}

		Intent serviceIntent = new Intent(_context, RESTService.class);
		Bundle serviceData = new Bundle();

		serviceData.putString(Constants.BUNDLE_USER, user.GetUserName());
		serviceData.putString(Constants.BUNDLE_PASSPHRASE, user.GetPassword());

		serviceData.putString(Constants.BUNDLE_ACTION, Constants.ACTION_VALIDATE_USER);
		serviceData.putString(Constants.BUNDLE_NAME, Constants.VALIDATE_USER);
		serviceData.putString(Constants.BUNDLE_BROADCAST, Constants.BROADCAST_VALIDATE_USER);
		serviceData.putSerializable(Constants.BUNDLE_LUCA_OBJECT, LucaObject.USER);

		serviceIntent.putExtras(serviceData);

		_context.startService(serviceIntent);
	}

	public boolean GetValidationResult() {
		return _userValidated;
	}
}
