package guepardoapps.lucahome.watchface.viewcontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.format.Time;
import android.view.Display;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.Tools;
import guepardoapps.toolset.common.Logger;

@SuppressWarnings("deprecation")
public class DateViewController {

	private static final String TAG = DateViewController.class.getName();
	private Logger _logger;

	private Context _context;
	private Tools _tools;

	private Display _display;

	public DateViewController(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_context = context;
		_tools = new Tools(_context);
		_display = _tools.GetDisplayDimension();
	}

	public void onDestroy() {
		_logger.Debug("onDestroy");
	}

	public void DrawDate(Canvas canvas, Time time) {
		_logger.Debug("drawDate");

		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_SMALL);

		String dayString = String.valueOf(time.monthDay);
		dayString = _tools.CheckLength(dayString);
		String monthString = String.valueOf(time.month + 1);
		monthString = _tools.CheckLength(monthString);
		String yearString = String.valueOf(time.year);

		String dateString = dayString + "." + monthString + "." + yearString;
		_logger.Info("dateString: " + dateString);

		canvas.drawText(dateString, _display.getWidth() - 130, 30, paint);
	}

	public void DrawWeekOfYear(Canvas canvas, Time time) {
		_logger.Debug("drawWeekOfYear");

		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_SMALL);

		String weekofYearString = String.valueOf(time.getWeekNumber());
		_logger.Info("weekofYearString: " + weekofYearString);

		canvas.drawText("Week", _display.getWidth() - 65, 60, paint);
		canvas.drawText(weekofYearString, _display.getWidth() - 37, 90, paint);
	}

	public void DrawTime(Canvas canvas, Time time) {
		_logger.Debug("drawTime");

		Paint paint = _tools.CreateDefaultPaint(canvas, Constants.TEXT_SIZE_MEDIUM);

		String hourString = String.valueOf(time.hour);
		hourString = _tools.CheckLength(hourString);
		String minuteString = String.valueOf(time.minute);
		minuteString = _tools.CheckLength(minuteString);

		String timeString = hourString + ":" + minuteString;
		_logger.Info("timeString: " + timeString);

		canvas.drawText(timeString, (_display.getWidth() / 3), (_display.getHeight() / 3) + 15, paint);
	}
}
