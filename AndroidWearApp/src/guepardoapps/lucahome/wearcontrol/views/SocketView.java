package guepardoapps.lucahome.wearcontrol.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ListAdapter;
import android.widget.ListView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.wearcontrol.views.customadapter.SocketListViewAdapter;
import guepardoapps.lucahome.wearcontrol.views.listitem.SocketListViewItem;
import guepardoapps.toolset.common.Logger;

public class SocketView extends Activity {

	private static final String TAG = SocketView.class.getName();
	private Logger _logger;

	private Context _context;

	private List<SocketListViewItem> _viewItemList = new ArrayList<>();

	private ListAdapter _listAdapter;
	private ListView _mainListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_basic);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("SocketView onCreate");

		_context = this;

		// TODO Call for sockets!

		_viewItemList.add(new SocketListViewItem(R.drawable.socket, "PC", true));
		_viewItemList.add(new SocketListViewItem(R.drawable.socket, "TV", false));
		_viewItemList.add(new SocketListViewItem(R.drawable.socket, "Storage", true));

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.basicWatchViewStub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				_mainListView = (ListView) stub.findViewById(R.id.basicListView);
				_listAdapter = new SocketListViewAdapter(_context, _viewItemList);
				_mainListView.setAdapter(_listAdapter);
			}
		});
	}
}