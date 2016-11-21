package guepardoapps.lucahome.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.Display;
import android.view.WindowManager;

import guepardoapps.toolset.common.Constants;
import guepardoapps.toolset.common.Logger;

public class Tools {

	private static final String TAG = Tools.class.getName();
	private Logger _logger;

	private Context _context;

	public Tools(Context context) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_context = context;
	}

	public Paint CreateDefaultPaint(Canvas canvas, int textSize) {
		_logger.Debug("createDefaultPaint");

		Paint paint = new Paint();
		paint.setColor(0x00000000);
		paint.setStyle(Style.FILL);

		canvas.drawPaint(paint);

		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize);

		return paint;
	}

	public Display GetDisplayDimension() {
		WindowManager windowManager = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		return display;
	}

	public String CheckLength(String entry) {
		while (entry.length() < 2) {
			entry = "0" + entry;
		}
		return entry;
	}
}
