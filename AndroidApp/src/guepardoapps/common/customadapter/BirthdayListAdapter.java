package guepardoapps.common.customadapter;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Birthday;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.SerializableList;
import guepardoapps.common.classes.controller.BirthdayController;
import guepardoapps.common.service.DialogService;
import guepardoapps.lucahome.R;

import guepardoapps.particles.ParticleSystem;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class BirthdayListAdapter extends BaseAdapter {

	private static String TAG = "BirthdayListAdapter";

	private SerializableList<Birthday> _birthdayList;

	private Logger _logger;
	private Context _context;

	private BirthdayController _birthdayController;
	private DialogService _dialogService;

	private static LayoutInflater _inflater = null;

	public BirthdayListAdapter(Context context, SerializableList<Birthday> birthdayList) {
		_logger = new Logger(TAG);
		_context = context;

		_birthdayController = new BirthdayController();
		_dialogService = new DialogService(_context);

		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		_birthdayList = birthdayList;
	}

	@Override
	public int getCount() {
		return _birthdayList.getSize();
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
		private TextView _age;
		private TextView _date;
		private Button _name;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_birthday_item, null);

		if (_birthdayController.HasBirthday(_birthdayList.getValue(index))) {
			holder._image = (ImageView) rowView.findViewById(R.id.birthday_item_image);
			holder._image.setImageResource(R.drawable.birthday_hd);

			rowView.setBackgroundColor(Constants.BIRTHDAY_BACKGROUND_COLOR);
			new ParticleSystem((Activity) _context, 150, R.drawable.particle, 1250).setSpeedRange(0.2f, 0.5f)
					.oneShot(rowView, 150);
		}

		holder._age = (TextView) rowView.findViewById(R.id.birthday_item_age);
		holder._age.setText(String.valueOf(_birthdayController.GetAge(_birthdayList.getValue(index))));

		holder._date = (TextView) rowView.findViewById(R.id.birthday_item_date);
		holder._date.setText(_birthdayList.getValue(index).GetBirthday().toLocaleString().replace(" 00:00:00", ""));

		holder._name = (Button) rowView.findViewById(R.id.birthday_item_name);
		holder._name.setText(_birthdayList.getValue(index).GetName());
		holder._name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_logger.Debug("onClick _name button: " + _birthdayList.getValue(index).GetName());
			}
		});
		holder._name.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				_logger.Debug("onLongClick _name button: " + _birthdayList.getValue(index).GetName());
				//_dialogService.ShowUpdateBirthdayDialog(_birthdayList.getValue(index));
				return true;
			}
		});

		return rowView;
	}
}