package guepardoapps.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import guepardoapps.common.Logger;

public class ReceiverController implements Serializable {

	private static final long serialVersionUID = -8309693750379542161L;

	private static String TAG = "ReceiverController";

	private Logger _logger;
	private Context _context;
	private List<BroadcastReceiver> _registeredReceiver;

	public ReceiverController(Context context) {
		_logger = new Logger(TAG);
		_context = context;
		_registeredReceiver = new ArrayList<BroadcastReceiver>();
	}

	public void RegisterReceiver(BroadcastReceiver receiver, String[] actions) {
		_logger.Debug("Registering new receiver! " + receiver.toString());

		IntentFilter downloadStateFilter = new IntentFilter();
		for (String action : actions) {
			downloadStateFilter.addAction(action);
		}

		_context.registerReceiver(receiver, downloadStateFilter);
		_registeredReceiver.add(receiver);
	}

	public void UnregisterReceiver(BroadcastReceiver receiver) {
		_logger.Debug("Trying to unregister receiver " + receiver.toString());

		for (int index = 0; index < _registeredReceiver.size(); index++) {
			if (_registeredReceiver.get(index) == receiver) {
				try {
					_context.unregisterReceiver(receiver);
					_registeredReceiver.remove(index);
				} catch (Exception e) {
					_logger.Error(e.toString());
				}
				break;
			}
		}
	}
}
