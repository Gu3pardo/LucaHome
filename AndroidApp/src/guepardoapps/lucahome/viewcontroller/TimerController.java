package guepardoapps.lucahome.viewcontroller;

import android.content.Context;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.controller.*;
import guepardoapps.lucahome.common.enums.LucaObject;
import guepardoapps.lucahome.common.enums.RaspberrySelection;
import guepardoapps.lucahome.dto.TimerDto;

public class TimerController {

	private Context _context;
	private ServiceController _serviceController;

	public TimerController(Context context) {
		_context = context;
		_serviceController = new ServiceController(_context);
	}

	public void LoadTimer() {
		_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, Constants.ACTION_GET_SCHEDULES,
				Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED, LucaObject.SCHEDULE, RaspberrySelection.BOTH);
	}

	public void Delete(TimerDto timer) {
		_serviceController.StartRestService(timer.GetName(), timer.GetCommandDelete(), Constants.BROADCAST_RELOAD_TIMER,
				LucaObject.TIMER, RaspberrySelection.BOTH);
	}
}
