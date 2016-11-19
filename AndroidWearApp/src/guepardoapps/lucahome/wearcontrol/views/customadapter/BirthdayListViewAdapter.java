package guepardoapps.lucahome.wearcontrol.views.customadapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import guepardoapps.lucahome.R;

import guepardoapps.lucahome.wearcontrol.views.listitem.BirthdayListViewItem;

public class BirthdayListViewAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = BirthdayListViewAdapter.class.getName();

	private List<BirthdayListViewItem> _list;
	private Context _context;
	private static LayoutInflater _inflater = null;

	public BirthdayListViewAdapter(Context context, List<BirthdayListViewItem> _viewItemList) {
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
		private RelativeLayout _backgrond;
		private TextView _name;
		private TextView _date;
		private TextView _age;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_item_birthday, null);

		holder._backgrond = (RelativeLayout) rowView.findViewById(R.id.list_birthday_item_layout);
		if (_list.get(index).HasBirthday()) {
			holder._backgrond.setBackgroundColor(0xFFF44336);
		}

		Drawable drawable = _context.getResources().getDrawable(_list.get(index).GetImageResource());
		drawable.setBounds(0, 0, 20, 20);

		holder._name = (TextView) rowView.findViewById(R.id.list_birthday_item_name);
		holder._name.setText(_list.get(index).GetName());
		holder._name.setCompoundDrawablesRelative(drawable, null, null, null);

		holder._date = (TextView) rowView.findViewById(R.id.list_birthday_item_date);
		holder._date.setText(_list.get(index).GetBirthday());

		holder._age = (TextView) rowView.findViewById(R.id.list_birthday_item_age);
		holder._age.setText(String.valueOf(_list.get(index).GetAge()));

		return rowView;
	}
}