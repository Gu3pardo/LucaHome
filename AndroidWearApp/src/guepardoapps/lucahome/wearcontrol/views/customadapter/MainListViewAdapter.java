package guepardoapps.lucahome.wearcontrol.views.customadapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.wearcontrol.controller.NavigationController;
import guepardoapps.lucahome.wearcontrol.views.listitem.MainListViewItem;
import guepardoapps.toolset.common.Logger;

public class MainListViewAdapter extends BaseAdapter {

	private static final String TAG = MainListViewAdapter.class.getName();
	@SuppressWarnings("unused")
	private Logger _logger;

	private List<MainListViewItem> _list;
	private Context _context;
	private NavigationController _navigationController;
	private static LayoutInflater _inflater = null;

	public MainListViewAdapter(Context context, List<MainListViewItem> _viewItemList,
			NavigationController navigationController) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_list = _viewItemList;
		_context = context;
		_navigationController = navigationController;
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
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_item_main, null);

		Drawable drawable = _context.getResources().getDrawable(_list.get(index).GetImageResource());
		drawable.setBounds(0, 0, 20, 20);

		holder._item = (TextView) rowView.findViewById(R.id.list_main_item_text);
		holder._item.setText(_list.get(index).GetText());
		holder._item.setCompoundDrawablesRelative(drawable, null, null, null);
		holder._item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(_context, _list.get(index).GetText(), Toast.LENGTH_SHORT).show();
				_navigationController.NavigateTo(_list.get(index).GetText());
			}
		});

		return rowView;
	}
}