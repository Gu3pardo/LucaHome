package guepardoapps.common.customadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import guepardoapps.common.classes.Change;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.lucahome.R;

public class ChangeListAdapter extends BaseAdapter {

	private static String TAG = "ChangeListAdapter";

	private Logger _logger;

	private Context _context;
	private static LayoutInflater _inflater = null;

	private SerializableList<Change> _changeList;

	public ChangeListAdapter(Context context, SerializableList<Change> changeList) {
		_logger = new Logger(TAG);

		_context = context;
		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		_changeList = changeList;

		for (int index = 0; index < _changeList.getSize(); index++) {
			_logger.Debug(_changeList.getValue(index).toString());
		}
	}

	@Override
	public int getCount() {
		return _changeList.getSize();
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
		private TextView _type;
		private TextView _date;
		private TextView _time;
		private TextView _user;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_change_item, null);

		holder._type = (TextView) rowView.findViewById(R.id.change_item_type);
		holder._type.setText(_changeList.getValue(index).GetType());

		holder._date = (TextView) rowView.findViewById(R.id.change_item_date);
		holder._date.setText(_changeList.getValue(index).GetDate().toString());

		holder._time = (TextView) rowView.findViewById(R.id.change_item_time);
		holder._time.setText(_changeList.getValue(index).GetTime().toString());

		holder._user = (TextView) rowView.findViewById(R.id.change_item_user);
		holder._user.setText(_changeList.getValue(index).GetUser());

		return rowView;
	}
}