package guepardoapps.lucahome.accesscontrol.view;

import com.google.android.youtube.player.YouTubeBaseActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import guepardoapps.lucahome.accesscontrol.R;
import guepardoapps.lucahome.accesscontrol.common.Constants;
import guepardoapps.lucahome.accesscontrol.services.*;
import guepardoapps.lucahome.accesscontrol.viewcontroller.*;

import guepardoapps.toolset.common.Logger;

public class Main extends YouTubeBaseActivity {

	private static final String TAG = Main.class.getName();
	private Logger _logger;

	private Context _context;

	private AlarmStateViewController _alarmStateViewController;
	private BatteryViewController _batteryViewController;
	private CenterViewController _centerViewController;
	private CountdownViewController _countdownViewController;
	private IpAdressViewController _ipAdressViewController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("onCreate");

		_context = this;

		initializeController();

		_alarmStateViewController.onCreate();
		_batteryViewController.onCreate();
		_centerViewController.onCreate();
		_countdownViewController.onCreate();
		_ipAdressViewController.onCreate();

		startServices();
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");

		_alarmStateViewController.onResume();
		_batteryViewController.onResume();
		_centerViewController.onResume();
		_countdownViewController.onResume();
		_ipAdressViewController.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		_logger.Debug("onPause");

		_alarmStateViewController.onPause();
		_batteryViewController.onPause();
		_centerViewController.onPause();
		_countdownViewController.onPause();
		_ipAdressViewController.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");

		_alarmStateViewController.onDestroy();
		_batteryViewController.onDestroy();
		_centerViewController.onDestroy();
		_countdownViewController.onDestroy();
		_ipAdressViewController.onDestroy();
	}

	private void initializeController() {
		_alarmStateViewController = new AlarmStateViewController(_context);
		_batteryViewController = new BatteryViewController(_context);
		_centerViewController = new CenterViewController(_context);
		_countdownViewController = new CountdownViewController(_context);
		_ipAdressViewController = new IpAdressViewController(_context);
	}

	private void startServices() {
		startService(new Intent(_context, MainService.class));
		startService(new Intent(_context, ControlServiceStateService.class));
	}
}