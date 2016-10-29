package guepardoapps.common.customadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import guepardoapps.common.classes.Information;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.service.DialogService;
import guepardoapps.lucahome.R;

public class InformationListAdapter extends BaseAdapter {

	private static String TAG = "InformationListAdapter";

	private Logger _logger;

	private Context _context;

	private DialogService _dialogService;

	private Information _information;

	private static LayoutInflater _inflater = null;

	public InformationListAdapter(Context context, Information information) {
		_logger = new Logger(TAG);

		_context = context;

		_dialogService = new DialogService(_context);

		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		_information = information;
		_logger.Debug(information.toString());
	}

	@Override
	public int getCount() {
		return _information.GetInformationList().size();
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
		private TextView _key;
		private TextView _value;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_information_item, null);

		holder._key = (TextView) rowView.findViewById(R.id.information_item_key);
		holder._key.setText(_information.GetKey(index));
		if (_information.GetKey(index).contains("contact")) {
			holder._key.setLongClickable(true);
			holder._key.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					_dialogService.ShowSendInformationMailDialog();
					return true;
				}
			});
		}

		holder._value = (TextView) rowView.findViewById(R.id.information_item_value);
		holder._value.setText(_information.GetValue(index));
		if (_information.GetKey(index).contains("contact")) {
			holder._value.setLongClickable(true);
			holder._value.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {
					_dialogService.ShowSendInformationMailDialog();
					return true;
				}
			});
		}

		return rowView;
	}
}