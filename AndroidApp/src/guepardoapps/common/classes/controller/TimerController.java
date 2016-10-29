package guepardoapps.common.classes.controller;

import android.content.Context;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Timer;
import guepardoapps.common.controller.*;
import guepardoapps.common.enums.LucaObject;

public class TimerController {

	@SuppressWarnings("unused")
	private static String TAG = "TimerController";

	private Context _context;
	private ServiceController _serviceController;

	public TimerController(Context context) {
		_context = context;
		_serviceController = new ServiceController(_context);
	}

	public void SetSchedule(Timer timer, boolean newState) {
		_serviceController.StartRestService(timer.GetName(), timer.GetCommandSet(newState),
				Constants.BROADCAST_RELOAD_TIMER, LucaObject.TIMER);
	}
}
