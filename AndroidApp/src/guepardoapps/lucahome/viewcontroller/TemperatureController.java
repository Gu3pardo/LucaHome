package guepardoapps.lucahome.viewcontroller;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.classes.SerializableList;
import guepardoapps.lucahome.common.controller.*;
import guepardoapps.lucahome.common.converter.json.JsonDataToTemperatureConverter;
import guepardoapps.lucahome.common.enums.LucaObject;
import guepardoapps.lucahome.common.enums.RaspberrySelection;
import guepardoapps.lucahome.common.enums.TemperatureType;
import guepardoapps.lucahome.dto.TemperatureDto;

import guepardoapps.toolset.openweather.OpenWeatherController;
import guepardoapps.toolset.openweather.common.OpenWeatherConstants;
import guepardoapps.toolset.openweather.model.WeatherModel;

public class TemperatureController {

	private static String TAG = TemperatureController.class.getName();
	private LucaHomeLogger _logger;

	private TemperatureType _temperatureType;
	private int _id;

	private Context _context;

	private OpenWeatherController _openWeatherController;
	private ReceiverController _receiverController;
	private ServiceController _serviceController;

	private BroadcastReceiver _downloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast...");

			switch (_temperatureType) {
			case RASPBERRY:
				String[] temperatureStringArray = intent.getStringArrayExtra(Constants.TEMPERATURE_DOWNLOAD);
				if (temperatureStringArray != null) {
					SerializableList<TemperatureDto> temperatureList = JsonDataToTemperatureConverter
							.GetList(temperatureStringArray);
					TemperatureDto updatedTemperature = temperatureList.getValue(_id);
					SendBroadcast(updatedTemperature);
				}
				break;
			case CITY:
				WeatherModel currentWeather = (WeatherModel) intent
						.getSerializableExtra(OpenWeatherConstants.BUNDLE_EXTRA_WEATHER_MODEL);
				SendBroadcast(currentWeather);
				break;
			default:
				_logger.Warn(_temperatureType.toString() + " is not supported!");
				break;
			}

			_receiverController.UnregisterReceiver(_downloadReceiver);

			_temperatureType = TemperatureType.DUMMY;
			_id = -1;
		}
	};

	public TemperatureController(Context context) {
		_context = context;

		_logger = new LucaHomeLogger(TAG);
		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);

		_openWeatherController = new OpenWeatherController(_context, Constants.CITY);

		_temperatureType = TemperatureType.DUMMY;
		_id = -1;
	}

	public void ReloadTemperature(TemperatureDto temperature, int id) {
		_logger.Debug("Trying to reload temperature: " + temperature.toString());

		_temperatureType = temperature.GetTemperatureType();
		_id = id;

		switch (_temperatureType) {
		case RASPBERRY:
			_receiverController.RegisterReceiver(_downloadReceiver,
					new String[] { Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED });
			_serviceController.StartRestService(Constants.TEMPERATURE_DOWNLOAD, Constants.ACTION_GET_TEMPERATURES,
					Constants.BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED, LucaObject.TEMPERATURE, RaspberrySelection.BOTH);
			break;
		case CITY:
			_receiverController.RegisterReceiver(_downloadReceiver,
					new String[] { OpenWeatherConstants.BROADCAST_GET_CURRENT_WEATHER_JSON_FINISHED });
			_openWeatherController.loadCurrentWeather();
			break;
		default:
			_logger.Warn(_temperatureType.toString() + " is not supported!");
			break;
		}
	}

	private void SendBroadcast(Serializable temperature) {
		Intent broadcastIntent = new Intent(Constants.BROADCAST_UPDATE_TEMPERATURE);

		Bundle broadcastData = new Bundle();

		broadcastData.putSerializable(Constants.BUNDLE_TEMPERATURE_TYPE, _temperatureType);
		broadcastData.putInt(Constants.BUNDLE_TEMPERATURE_ID, _id);
		broadcastData.putSerializable(Constants.BUNDLE_TEMPERATURE_SINGLE, temperature);

		broadcastIntent.putExtras(broadcastData);

		_context.sendBroadcast(broadcastIntent);
	}

	public void Dispose() {
		_logger.Info("Disposing...");
		_receiverController.UnregisterReceiver(_downloadReceiver);
	}
}
