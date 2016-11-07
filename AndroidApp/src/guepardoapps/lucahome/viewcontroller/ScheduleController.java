package guepardoapps.lucahome.viewcontroller;

import android.content.Context;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.controller.*;
import guepardoapps.lucahome.common.enums.LucaObject;
import guepardoapps.lucahome.common.enums.RaspberrySelection;
import guepardoapps.lucahome.dto.ScheduleDto;

public class ScheduleController {

	private Context _context;
	private ServiceController _serviceController;

	public ScheduleController(Context context) {
		_context = context;
		_serviceController = new ServiceController(_context);
	}

	public void LoadSchedules() {
		_serviceController.StartRestService(Constants.SCHEDULE_DOWNLOAD, Constants.ACTION_GET_SCHEDULES,
				Constants.BROADCAST_DOWNLOAD_SCHEDULE_FINISHED, LucaObject.SCHEDULE, RaspberrySelection.BOTH);
	}

	public void SetSchedule(ScheduleDto schedule, boolean newState) {
		_serviceController.StartRestService(schedule.GetName(), schedule.GetCommandSet(newState),
				Constants.BROADCAST_RELOAD_SCHEDULE, LucaObject.SCHEDULE, RaspberrySelection.BOTH);
	}

	public void DeleteSchedule(ScheduleDto schedule) {
		_serviceController.StartRestService(schedule.GetName(), schedule.GetCommandDelete(),
				Constants.BROADCAST_RELOAD_SCHEDULE, LucaObject.SCHEDULE, RaspberrySelection.BOTH);
	}
}
