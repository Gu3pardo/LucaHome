package guepardoapps.lucahome.wearcontrol.views.customadapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.wearcontrol.views.listitem.SocketListViewItem;
import guepardoapps.toolset.common.Logger;

public class SocketListViewAdapter extends BaseAdapter {

	private static final String TAG = SocketListViewAdapter.class.getName();
	private Logger _logger;

	private List<SocketListViewItem> _list;
	private Context _context;
	private static LayoutInflater _inflater = null;

	public SocketListViewAdapter(Context context, List<SocketListViewItem> _viewItemList) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_list = _viewItemList;
		_context = context;
		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _list.size();
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
		private TextView _item;
		private Switch _switch;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_item_socket, null);

		Drawable drawable = _context.getResources().getDrawable(_list.get(index).GetImageResource());
		drawable.setBounds(0, 0, 20, 20);

		holder._item = (TextView) rowView.findViewById(R.id.list_socket_item_text);
		holder._item.setText(_list.get(index).GetText());
		holder._item.setCompoundDrawablesRelative(drawable, null, null, null);

		holder._switch = (Switch) rowView.findViewById(R.id.list_socket_item_switch);
		holder._switch.setChecked(_list.get(index).GetIsActivated());
		holder._switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
				// TODO Send message to phone
				_logger.Debug("Set state of " + _list.get(index).GetText() + " to " + String.valueOf(checked));
			}
		});

		return rowView;
	}
}