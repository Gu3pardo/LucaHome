package guepardoapps.test;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;
import guepardoapps.lucahome.common.Constants;

import guepardoapps.lucahome.wearcontrol.helper.MessageSendHelper;
import guepardoapps.lucahome.wearcontrol.sensor.helper.GravityDto;

import guepardoapps.toolset.common.Logger;

public class SensorDataLogger {

	private static final String TAG = SensorDataLogger.class.getName();
	private Logger _logger;

	private Context _context;
	private MessageSendHelper _messageSendHelper;

	private static final int MAX_LIST_SIZE = 500;
	private ArrayList<GravityDto> _data = new ArrayList<GravityDto>();

	public SensorDataLogger(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_messageSendHelper = new MessageSendHelper(_context);
	}

	public void AddGravityDto(GravityDto data) {
		_logger.Debug("AddGravityDto");
		_data.add(data);
		if (_data.size() >= MAX_LIST_SIZE) {
			String message = "EVALUATION:GRAVITY:DATA:";
			for (GravityDto entry : _data) {
				message += entry.toString() + "&";
			}
			_messageSendHelper.SendMessage(message);
			_data.clear();
			Toast.makeText(_context, "Cleared data after send!", Toast.LENGTH_SHORT).show();
		}
	}
}
