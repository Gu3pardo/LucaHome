package guepardoapps.common.customadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import guepardoapps.common.classes.Timer;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.classes.controller.TimerController;
import guepardoapps.common.service.DialogService;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.lucahome.R;

public class TimerListAdapter extends BaseAdapter {
	private static String TAG = "TimerListAdapter";

	private Logger _logger;

	private Context _context;

	private TimerController _timerController;
	private DialogService _dialogService;

	private static LayoutInflater _inflater = null;

	private SerializableList<Timer> _timerList;

	public TimerListAdapter(Context context, SerializableList<Timer> timerList,
			SerializableList<WirelessSocket> socketList) {
		_logger = new Logger(TAG);

		_context = context;
		_timerList = timerList;

		_timerController = new TimerController(_context);
		_dialogService = new DialogService(_context);
		_dialogService.InitializeSocketList(socketList);

		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _timerList.getSize();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		private ImageView _image;
		private Button _name;
		private TextView _time;
		private TextView _weekday;
		private TextView _socket;
		private TextView _action;
		private TextView _playsound;
		private Button _delete;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_timer_item, null);

		holder._image = (ImageView) rowView.findViewById(R.id.timer_item_image);
		holder._image.setImageResource(R.drawable.timer);

		holder._name = (Button) rowView.findViewById(R.id.timer_item_name);
		holder._name.setText(_timerList.getValue(index).GetName());
		holder._name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("onClick _name button: " + _timerList.getValue(index).GetName());
			}
		});
		holder._name.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				_logger.Debug("onLongClick _name button: " + _timerList.getValue(index).GetName());
				_dialogService.ShowUpdateTimerDialog(_timerList.getValue(index));
				return true;
			}
		});

		holder._time = (TextView) rowView.findViewById(R.id.timer_item_time);
		holder._time.setText(_timerList.getValue(index).GetTime().toString());

		holder._weekday = (TextView) rowView.findViewById(R.id.timer_item_weekday);
		holder._weekday.setText(_timerList.getValue(index).GetWeekday().toString());

		holder._socket = (TextView) rowView.findViewById(R.id.timer_item_socket);
		holder._socket.setText(_timerList.getValue(index).GetSocket().GetName());

		holder._action = (TextView) rowView.findViewById(R.id.timer_item_action);
		if (_timerList.getValue(index).GetAction()) {
			holder._action.setText(String.valueOf("Activate"));
		} else {
			holder._action.setText(String.valueOf("Deactivate"));
		}

		holder._playsound = (TextView) rowView.findViewById(R.id.timer_item_playsound);
		if (_timerList.getValue(index).GetPlaySound()) {
			holder._playsound.setText(String.valueOf("Sound"));
		} else {
			holder._playsound.setText(String.valueOf("-/-"));
		}

		holder._delete = (Button) rowView.findViewById(R.id.timer_item_delete);
		holder._delete.setText(_timerList.getValue(index).GetIsActiveString());
		holder._delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_timerController.Delete(_timerList.getValue(index));
			}
		});

		return rowView;
	}
}