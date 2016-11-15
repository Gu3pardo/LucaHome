package guepardoapps.lucahome.accesscontrol.server;

import android.content.Context;

import guepardoapps.lucahome.accesscontrol.common.Constants;
import guepardoapps.lucahome.accesscontrol.common.enums.AlarmState;
import guepardoapps.lucahome.accesscontrol.common.enums.ServerAction;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.BroadcastController;

public class DataHandler {

	private static final String TAG = DataHandler.class.getName();
	private Logger _logger;

	private Context _context;
	private BroadcastController _broadcastController;

	public DataHandler(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
		_broadcastController = new BroadcastController(_context);
	}

	public void PerformAction(String command) {
		if (command == null) {
			_logger.Warn("Command is null!");
			return;
		}

		_logger.Debug("PerformAction with data: " + command);
		if (command.startsWith("ACTION:")) {
			ServerAction action = ServerAction.GetByString(command.replace("ACTION:", ""));

			if (action != null) {
				_logger.Debug("action: " + action.toString());

				switch (action) {
				case REQUEST_CODE:
					_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_ALARM_STATE,
							Constants.BUNDLE_ALARM_STATE, AlarmState.REQUEST_CODE);
					break;
				case LOGIN_FAILED:
					_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_ALARM_STATE,
							Constants.BUNDLE_ALARM_STATE, AlarmState.ACCESS_FAILED);
					break;
				case LOGIN_SUCCESS:
					_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_ALARM_STATE,
							Constants.BUNDLE_ALARM_STATE, AlarmState.ACCESS_SUCCESSFUL);
					break;
				case ALARM_ACTIVE:
					_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_ALARM_STATE,
							Constants.BUNDLE_ALARM_STATE, AlarmState.ALARM_ACTIVE);
					break;
				case ACTIVATE_ACCESS_CONTROL:
					_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_ALARM_STATE,
							Constants.BUNDLE_ALARM_STATE, AlarmState.ACCESS_CONTROL_ACTIVE);
					break;
				case NULL:
				default:
					_logger.Warn("Action not handled!\n" + action.toString());
					break;
				}
			} else {
				_logger.Warn("Action failed to be converted! Is null!\n" + command);
			}
		} else {
			_logger.Warn("Command has wrong format!\n" + command);
		}
	}
}
