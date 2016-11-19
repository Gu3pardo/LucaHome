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
import android.widget.Button;
import android.widget.TextView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.watchface.common.helper.MessageSendHelper;

import guepardoapps.lucahome.wearcontrol.views.listitem.MovieListViewItem;

import guepardoapps.toolset.common.Logger;

public class MovieListViewAdapter extends BaseAdapter {

	private static final String TAG = MovieListViewAdapter.class.getName();
	private Logger _logger;

	private List<MovieListViewItem> _list;

	private Context _context;
	private MessageSendHelper _messageSendHelper;

	private static LayoutInflater _inflater = null;

	public MovieListViewAdapter(Context context, MessageSendHelper messageSendHelper,
			List<MovieListViewItem> _viewItemList) {
		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);

		_list = _viewItemList;
		_context = context;
		_messageSendHelper = messageSendHelper;

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
		private TextView _title;
		private Button _play;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_item_movie, null);

		Drawable drawable = _context.getResources().getDrawable(_list.get(index).GetImageResource());
		drawable.setBounds(0, 0, 20, 20);

		holder._title = (TextView) rowView.findViewById(R.id.list_movie_item_title);
		holder._title.setText(_list.get(index).GetTitle());

		holder._play = (Button) rowView.findViewById(R.id.list_movie_item_play);
		holder._play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_logger.Debug("Play movie " + _list.get(index).GetTitle());
				if (_messageSendHelper != null) {
					_messageSendHelper.SendMessage(_list.get(index).GetCommandText());
				}
			}
		});

		return rowView;
	}
}