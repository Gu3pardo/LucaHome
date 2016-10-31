package guepardoapps.common.customadapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import guepardoapps.common.Logger;
import guepardoapps.common.classes.Sound;
import guepardoapps.common.classes.controller.SoundController;
import guepardoapps.common.enums.RaspberrySelection;
import guepardoapps.lucahome.R;

public class SoundListAdapter extends BaseAdapter {

	private static String TAG = "SoundListAdapter";
	private static LayoutInflater _inflater = null;

	private RaspberrySelection _raspberrySelection;

	private Logger _logger;
	private Context _context;
	private ArrayList<Sound> _soundList;

	private SoundController _soundController;

	public SoundListAdapter(Context context, ArrayList<Sound> soundList, RaspberrySelection raspberrySelection) {
		_logger = new Logger(TAG);
		_context = context;
		_soundList = soundList;
		_raspberrySelection = raspberrySelection;

		_soundController = new SoundController(_context);

		_inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _soundList.size();
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
		private TextView _name;
		private ImageView _playing;
		private ImageButton _start;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_sound_item, null);

		holder._name = (TextView) rowView.findViewById(R.id.sound_item_name);
		holder._name.setText(_soundList.get(index).GetFileName());

		holder._playing = (ImageView) rowView.findViewById(R.id.sound_item_playing);
		if (_soundList.get(index).GetIsPlaying()) {
			holder._playing.setVisibility(View.VISIBLE);
		} else {
			holder._playing.setVisibility(View.INVISIBLE);
		}

		holder._start = (ImageButton) rowView.findViewById(R.id.sound_item_play);
		holder._start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				_logger.Debug("onClick _start: " + _soundList.get(index).toString());
				_soundController.StartSound(_soundList.get(index), _raspberrySelection);
			}
		});

		return rowView;
	}
}