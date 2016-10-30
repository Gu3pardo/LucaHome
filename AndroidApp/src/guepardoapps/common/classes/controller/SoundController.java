package guepardoapps.common.classes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.Sound;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.toolset.controller.SharedPrefController;

public class SoundController {
	private static String TAG = "SoundController";

	private Logger _logger;
	private Context _context;
	private ServiceController _serviceController;
	private SharedPrefController _sharedPrefController;

	public SoundController(Context context) {
		_logger = new Logger(TAG);
		_context = context;
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);
	}

	public void CheckPlaying(RaspberrySelection raspberrySelection) {
		_serviceController.StartRestService(TAG, Constants.ACTION_IS_SOUND_PLAYING,
				Constants.BROADCAST_IS_SOUND_PLAYING, LucaObject.SOUND, raspberrySelection);
	}

	public void StartSound(Sound sound, RaspberrySelection raspberrySelection) {
		_logger.Debug("Trying to start sound: " + sound.GetFileName());
		_serviceController.StartRestService(TAG, sound.GetCommandStart(), Constants.BROADCAST_START_SOUND,
				LucaObject.SOUND, raspberrySelection);
	}

	public void StopSound(RaspberrySelection raspberrySelection) {
		_logger.Debug("Trying to stop sound");
		_serviceController.StartRestService(TAG, Constants.ACTION_STOP_SOUND, Constants.BROADCAST_STOP_SOUND,
				LucaObject.SOUND, raspberrySelection);
	}

	public void IncreaseVolume(RaspberrySelection raspberrySelection) {
		_logger.Debug("Increase volume");
		_serviceController.StartRestService(TAG, Constants.ACTION_INCREASE_VOLUME, Constants.BROADCAST_GET_VOLUME,
				LucaObject.SOUND, raspberrySelection);
	}

	public void DecreaseVolume(RaspberrySelection raspberrySelection) {
		_logger.Debug("Decrease volume");
		_serviceController.StartRestService(TAG, Constants.ACTION_DECREASE_VOLUME, Constants.BROADCAST_GET_VOLUME,
				LucaObject.SOUND, raspberrySelection);
	}

	public void SelectRaspberry(RaspberrySelection previousSelection, RaspberrySelection newSelection, Sound sound) {
		_logger.Debug("Selecting raspberry: " + newSelection.toString());

		if (previousSelection == newSelection) {
			_logger.Warn("RaspberrySelection has to be different!");
			return;
		}

		if (newSelection == RaspberrySelection.BOTH || newSelection == RaspberrySelection.DUMMY
				|| newSelection == null) {
			_logger.Warn("RaspberrySelection not possible for new selection!");
			return;
		}

		StopSound(previousSelection);

		if (sound != null) {
			StartSound(sound, newSelection);
		}

		Intent broadcastIntent = new Intent(Constants.BROADCAST_SET_RASPBERRY);
		Bundle broadcastData = new Bundle();
		broadcastData.putSerializable(Constants.BUNDLE_RASPBERRY_SELETION, newSelection);
		broadcastIntent.putExtras(broadcastData);
		_context.sendBroadcast(broadcastIntent);

		_sharedPrefController.SaveIntegerValue(Constants.SOUND_RASPBERRY_SELECTION, newSelection.GetInt());
	}
}
