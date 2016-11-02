package guepardoapps.common.classes.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
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
		sendBroadCast(Constants.BROADCAST_ACTIVATE_SOUND_SOCKET, raspberrySelection);
		_serviceController.StartRestService(TAG, sound.GetCommandStart(), Constants.BROADCAST_START_SOUND,
				LucaObject.SOUND, raspberrySelection);
	}

	public void StopSound(RaspberrySelection raspberrySelection) {
		sendBroadCast(Constants.BROADCAST_DEACTIVATE_SOUND_SOCKET, raspberrySelection);
		_serviceController.StartRestService(TAG, Constants.ACTION_STOP_SOUND, Constants.BROADCAST_STOP_SOUND,
				LucaObject.SOUND, raspberrySelection);
	}

	public void IncreaseVolume(RaspberrySelection raspberrySelection) {
		_serviceController.StartRestService(TAG, Constants.ACTION_INCREASE_VOLUME, Constants.BROADCAST_GET_VOLUME,
				LucaObject.SOUND, raspberrySelection);
	}

	public void DecreaseVolume(RaspberrySelection raspberrySelection) {
		_serviceController.StartRestService(TAG, Constants.ACTION_DECREASE_VOLUME, Constants.BROADCAST_GET_VOLUME,
				LucaObject.SOUND, raspberrySelection);
	}

	public void SelectRaspberry(RaspberrySelection previousSelection, RaspberrySelection newSelection, Sound sound) {
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

		sendBroadCast(Constants.BROADCAST_SET_RASPBERRY, newSelection);
		_sharedPrefController.SaveIntegerValue(Constants.SOUND_RASPBERRY_SELECTION, newSelection.GetInt());
	}

	private void sendBroadCast(String broadcast, RaspberrySelection selection) {
		Intent broadcastIntent = new Intent(broadcast);
		Bundle broadcastData = new Bundle();
		broadcastData.putSerializable(Constants.BUNDLE_RASPBERRY_SELECTION, selection);
		broadcastIntent.putExtras(broadcastData);
		_context.sendBroadcast(broadcastIntent);
	}
}
