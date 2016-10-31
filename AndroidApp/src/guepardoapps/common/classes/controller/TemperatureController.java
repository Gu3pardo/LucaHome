package guepardoapps.common.classes.controller;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.Temperature;
import guepardoapps.common.controller.*;
import guepardoapps.common.converter.json.JsonDataToTemperatureConverter;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.common.enums.TemperatureType;

import guepardoapps.toolset.openweather.OpenWeatherConstants;
import guepardoapps.toolset.openweather.OpenWeatherController;
import guepardoapps.toolset.openweather.WeatherModel;

public class TemperatureController {

	private static String TAG = "TemperatureController";

	private Logger _logger;
	private ReceiverController _receiverController;
	private ServiceController _serviceController;

	private OpenWeatherController _openWeatherController;

	private Context _context;

	private TemperatureType _temperatureType;
	private int _id;

	private BroadcastReceiver _downloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("Received broadcast...");

			switch (_temperatureType) {
			case RASPBERRY:
				String[] temperatureStringArray = intent.getStringArrayExtra(Constants.TEMPERATURE_DOWNLOAD);
				if (temperatureStringArray != null) {
					SerializableList<Temperature> temperatureList = JsonDataToTemperatureConverter
							.GetList(temperatureStringArray);
					Temperature updatedTemperature = temperatureList.getValue(_id);
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

		_logger = new Logger(TAG);
		_receiverController = new ReceiverController(_context);
		_serviceController = new ServiceController(_context);

		_openWeatherController = new OpenWeatherController(_context, Constants.CITY);

		_temperatureType = TemperatureType.DUMMY;
		_id = -1;
	}

	public void ReloadTemperature(Temperature temperature, int id) {
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
					new String[] { OpenWeatherConstants.GET_CURRENT_WEATHER_JSON_FINISHED });
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
