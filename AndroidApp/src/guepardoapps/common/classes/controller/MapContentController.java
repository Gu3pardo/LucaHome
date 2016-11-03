package guepardoapps.common.classes.controller;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.widget.RelativeLayout;
import android.widget.TextView;
import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.*;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.lucahome.R;

public class MapContentController {
	private static String TAG = "MapContentController";
	private Logger _logger;

	private Context _context;
	private ServiceController _serviceController;

	public MapContentController(Context context) {
		_logger = new Logger(TAG);
		_context = context;
		_serviceController = new ServiceController(_context);
	}

	public void LoadMapContents() {
		_logger.Debug("GetMapContents");
		_serviceController.StartRestService(TAG, Constants.ACTION_GET_MAP_CONTENTS,
				Constants.BROADCAST_UPDATE_MAP_CONTENT_VIEW, LucaObject.MAP_CONTENT, RaspberrySelection.BOTH);
	}

	public void AddMapContent(MapContent mapContent) {
		_logger.Debug("AddMapContent");
		_serviceController.StartRestService(TAG, mapContent.GetCommandAdd(), Constants.BROADCAST_RELOAD_MAP_CONTENT,
				LucaObject.MAP_CONTENT, RaspberrySelection.BOTH);
	}

	public void UpdateMapContent(MapContent mapContent) {
		_logger.Debug("UpdateMapContent");
		_serviceController.StartRestService(TAG, mapContent.GetCommandUpdate(), Constants.BROADCAST_RELOAD_MAP_CONTENT,
				LucaObject.MAP_CONTENT, RaspberrySelection.BOTH);
	}

	public void DeleteMapContent(MapContent mapContent) {
		_logger.Debug("DeleteMapContent");
		_serviceController.StartRestService(TAG, mapContent.GetCommandDelete(), Constants.BROADCAST_RELOAD_MAP_CONTENT,
				LucaObject.MAP_CONTENT, RaspberrySelection.BOTH);
	}

	public TextView CreateEntry(final MapContent newMapContent, Point clickPosition,
			SerializableList<WirelessSocket> wirelessSocketList) {
		final TextView newTextView = new TextView(_context);

		switch (newMapContent.GetDrawingType()) {
		case RASPBERRY:
			newTextView.setBackgroundResource(R.drawable.drawing_raspberry);
			break;
		case ARDUINO:
			newTextView.setBackgroundResource(R.drawable.drawing_arduino);
			break;
		case SOCKET:
			ArrayList<String> socketList = newMapContent.GetSockets();
			WirelessSocket socket = null;

			if (socketList.size() == 1) {
				if (wirelessSocketList != null) {
					for (int index = 0; index < wirelessSocketList.getSize(); index++) {
						if (wirelessSocketList.getValue(index).GetName().contains(socketList.get(0))) {
							socket = wirelessSocketList.getValue(index);
							break;
						}
					}
				}
			}

			if (socket != null) {
				if (socket.GetIsActivated()) {
					newTextView.setBackgroundResource(R.drawable.drawing_socket_on);
				} else {
					newTextView.setBackgroundResource(R.drawable.drawing_socket_off);
				}
			} else {
				_logger.Warn("No socket found!");
				newTextView.setBackgroundResource(R.drawable.drawing_socket_off);
			}

			break;
		case TEMPERATURE:
			newTextView.setBackgroundResource(R.drawable.drawing_temperature);
			break;
		default:
			_logger.Warn("drawingType: " + newMapContent.toString() + " is not supported!");
			return null;
		}

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(clickPosition.x - 15, clickPosition.y - 15, 0, 0);

		newTextView.setLayoutParams(layoutParams);

		return newTextView;
	}
}