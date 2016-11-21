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
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.wearcontrol.controller.NavigationController;
import guepardoapps.lucahome.wearcontrol.enums.TargetActivity;
import guepardoapps.lucahome.wearcontrol.views.customadapter.MainListViewAdapter;
import guepardoapps.lucahome.wearcontrol.views.listitem.MainListViewItem;

import guepardoapps.test.*;

import guepardoapps.toolset.common.Logger;

public class MainView extends Activity {

	private static final String TAG = MainView.class.getName();
	private Logger _logger;

	private Context _context;
	private NavigationController _navigationController;

	private List<MainListViewItem> _viewItemList = new ArrayList<>();

	private ListAdapter _listAdapter;
	private ListView _mainListView;

	private ConverterTest _converterTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_basic_list);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug(MainView.class.getName() + " onCreate");

		_context = this;
		_navigationController = new NavigationController(_context);

		if (Constants.TESTING_ENABLED) {
			_converterTest = new ConverterTest(_context);
			_converterTest.PerformTests();
		}

		_viewItemList.add(new MainListViewItem(R.drawable.birthday, TargetActivity.BIRTHDAYS.GetName()));
		_viewItemList.add(new MainListViewItem(R.drawable.scheduler, TargetActivity.SCHEDULE.GetName()));
		_viewItemList.add(new MainListViewItem(R.drawable.socket, TargetActivity.SOCKET.GetName()));
		_viewItemList.add(new MainListViewItem(R.drawable.circle_blue, TargetActivity.GRAVITY.GetName()));

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.basicListWatchViewStub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				_mainListView = (ListView) stub.findViewById(R.id.basicListView);
				_listAdapter = new MainListViewAdapter(_context, _viewItemList, _navigationController);
				_mainListView.setAdapter(_listAdapter);
			}
		});
	}
}