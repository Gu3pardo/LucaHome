package guepardoapps.lucahome.wearcontrol.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ListAdapter;
import android.widget.ListView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.watchface.common.Constants;
import guepardoapps.lucahome.watchface.common.helper.MessageSendHelper;

import guepardoapps.lucahome.wearcontrol.views.customadapter.BirthdayListViewAdapter;
import guepardoapps.lucahome.wearcontrol.views.listitem.BirthdayListViewItem;
import guepardoapps.test.ConverterTest;
import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.ReceiverController;

public class BirthdayView extends Activity {

	private static final String TAG = BirthdayView.class.getName();
	private Logger _logger;

	private static final String COMMAND = "ACTION:GET:BIRTHDAYS";

	private Context _context;
	private MessageSendHelper _messageSendHelper;
	private ReceiverController _receiverController;

	private boolean _isInitialized;
	private List<BirthdayListViewItem> _itemList = new ArrayList<>();

	private ListAdapter _listAdapter;
	private ListView _listView;

	private ConverterTest _converterTest;

	private BroadcastReceiver _updateReceiver = new BroadcastReceiver() {
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_updateReceiver onReceive");
			List<BirthdayListViewItem> itemList = (List<BirthdayListViewItem>) intent
					.getSerializableExtra(Constants.BUNDLE_BIRTHDAY_LIST);
			if (itemList != null) {
				_itemList = itemList;
				_listAdapter = new BirthdayListViewAdapter(_context, _itemList);
				_listView.setAdapter(_listAdapter);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_basic);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;
		_messageSendHelper = new MessageSendHelper(_context);
		_receiverController = new ReceiverController(_context);

		_itemList.add(new BirthdayListViewItem(R.drawable.circle_yellow, "Loading...", Calendar.getInstance()));

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.basicWatchViewStub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				_listView = (ListView) stub.findViewById(R.id.basicListView);
				_listAdapter = new BirthdayListViewAdapter(_context, _itemList);
				_listView.setAdapter(_listAdapter);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		_logger.Debug("onResume");
		if (!_isInitialized) {
			_receiverController.RegisterReceiver(_updateReceiver,
					new String[] { Constants.BROADCAST_UPDATE_BIRTHDAY_LIST });
			_messageSendHelper.SendMessage(COMMAND);
			_isInitialized = true;

			if (Constants.TESTING_ENABLED) {
				_converterTest = new ConverterTest(_context);
				_converterTest.PerformTests();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		_logger.Debug("onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");
		_receiverController.UnregisterReceiver(_updateReceiver);
	}
}