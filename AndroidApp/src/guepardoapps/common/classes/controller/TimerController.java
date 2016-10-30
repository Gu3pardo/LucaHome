package guepardoapps.common.classes.controller;

import android.content.Context;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Timer;
import guepardoapps.common.controller.*;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;

public class TimerController {

	@SuppressWarnings("unused")
	private static String TAG = "TimerController";

	private Context _context;
	private ServiceController _serviceController;

	public TimerController(Context context) {
		_context = context;
		_serviceController = new ServiceController(_context);
	}

	public void Delete(Timer timer) {
		_serviceController.StartRestService(timer.GetName(), timer.GetCommandDelete(), Constants.BROADCAST_RELOAD_TIMER,
				LucaObject.TIMER, RaspberrySelection.BOTH);
	}
}
