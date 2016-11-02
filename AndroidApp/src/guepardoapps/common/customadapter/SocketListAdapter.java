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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.WirelessSocket;
import guepardoapps.common.classes.controller.SocketController;
import guepardoapps.common.service.DialogService;
import guepardoapps.lucahome.R;

public class SocketListAdapter extends BaseAdapter {

	private static String TAG = "SocketListAdapter";

	private Logger _logger;

	private Context _context;

	private SocketController _socketController;
	private DialogService _dialogService;

	private static LayoutInflater _inflater = null;

	private SerializableList<WirelessSocket> _socketList;

	public SocketListAdapter(Context context, SerializableList<WirelessSocket> socketList) {
		_logger = new Logger(TAG);

		_context = context;

		_socketController = new SocketController(_context);
		_dialogService = new DialogService(_context);

		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		_socketList = socketList;
	}

	@Override
	public int getCount() {
		return _socketList.getSize();
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
		private Button _name;
		private TextView _code;
		private TextView _area;
		private Switch _state;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_socket_item, null);

		holder._name = (Button) rowView.findViewById(R.id.socket_item_name);
		holder._name.setText(_socketList.getValue(index).GetName());
		holder._name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("onClick _name button: " + _socketList.getValue(index).GetName());
			}
		});
		holder._name.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				_logger.Debug("onLongClick _name button: " + _socketList.getValue(index).GetName());
				_dialogService.ShowUpdateSocketDialog(_socketList.getValue(index));
				return true;
			}
		});

		holder._code = (TextView) rowView.findViewById(R.id.socket_item_code);
		holder._code.setText(_socketList.getValue(index).GetCode());

		holder._area = (TextView) rowView.findViewById(R.id.socket_item_area);
		holder._area.setText(_socketList.getValue(index).GetArea());

		holder._state = (Switch) rowView.findViewById(R.id.socket_item_switch);
		holder._state.setChecked(_socketList.getValue(index).GetIsActivated());
		holder._state.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				_socketController.SetSocket(_socketList.getValue(index), isChecked);
				_socketController.CheckMedia(_socketList.getValue(index));
			}
		});

		return rowView;
	}
}