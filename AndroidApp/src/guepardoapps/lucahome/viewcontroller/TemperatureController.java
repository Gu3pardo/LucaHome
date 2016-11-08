package guepardoapps.lucahome.viewcontroller;

import android.content.Context;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.controller.*;
import guepardoapps.lucahome.common.enums.MainServiceAction;
import guepardoapps.lucahome.dto.TemperatureDto;

public class TemperatureController {

	private static String TAG = TemperatureController.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;
	private BroadcastController _broadcastController;

	public TemperatureController(Context context) {
		_context = context;

		_logger = new LucaHomeLogger(TAG);
		_broadcastController = new BroadcastController(_context);
	}

	public void ReloadTemperature(TemperatureDto temperature) {
		_logger.Debug("Trying to reload temperature: " + temperature.toString());

		switch (temperature.GetTemperatureType()) {
		case RASPBERRY:
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
					new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
					new Object[] { MainServiceAction.DOWNLOAD_TEMPERATURE });
			break;
		case CITY:
			_broadcastController.SendSerializableBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
					new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
					new Object[] { MainServiceAction.DOWNLOAD_WEATHER_CURRENT });
			break;
		default:
			_logger.Warn(temperature.GetTemperatureType().toString() + " is not supported!");
			break;
		}
	}
}
