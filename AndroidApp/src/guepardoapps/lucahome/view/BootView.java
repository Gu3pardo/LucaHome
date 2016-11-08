package guepardoapps.lucahome.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.controller.ReceiverController;
import guepardoapps.lucahome.common.enums.Command;
import guepardoapps.lucahome.common.enums.MainServiceAction;
import guepardoapps.lucahome.common.enums.NavigateData;
import guepardoapps.lucahome.services.MainService;
import guepardoapps.lucahome.services.NavigationService;

public class BootView extends Activity {

	private static final String TAG = BootView.class.getName();
	private LucaHomeLogger _logger;

	private int _progressBarMax = 100;
	private int _progressBarSteps = Constants.DOWNLOAD_STEPS;

	private ProgressBar _percentProgressBar;
	private TextView _progressTextView;

	private Context _context;
	private ReceiverController _receiverController;
	private NavigationService _navigationService;

	private BroadcastReceiver _commandReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_commandReceiver");
			
			Command command = (Command) intent.getSerializableExtra(Constants.BUNDLE_COMMAND);
			if (command != null) {
				_logger.Debug("Command: " + command.toString());
				
				switch (command) {
				case NAVIGATE:
					NavigateData navigateData = (NavigateData) intent
							.getSerializableExtra(Constants.BUNDLE_NAVIGATE_DATA);
					
					if (navigateData != null) {
						switch (navigateData) {
						case MAIN:
							_navigationService.NavigateTo(MainView.class, null, true);
							break;
						case FINISH:
							finish();
							break;
						default:
							_logger.Warn("NavigateData is not supported: " + navigateData.toString());
							break;
						}
					} else {
						_logger.Warn("NavigateData is null!");
					}
					break;
				default:
					_logger.Warn("Command is not supported: " + command.toString());
					break;
				}
			} else {
				_logger.Warn("Command is null!");
			}
		}
	};

	private BroadcastReceiver _updateProgressBarReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_updateProgressBarReceiver");
			int progress = intent.getIntExtra(Constants.BUNDLE_VIEW_PROGRESS, -1);
			_logger.Debug("Progress is: " + String.valueOf(progress));

			_percentProgressBar.setProgress((int) (progress * _progressBarMax / _progressBarSteps));
			_progressTextView.setText(String.valueOf((int) (progress * _progressBarMax / _progressBarSteps)) + " %");
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_boot);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_percentProgressBar = (ProgressBar) findViewById(R.id.percentProgressBar);
		_percentProgressBar.setMax(_progressBarMax);
		_percentProgressBar.setProgress(0);

		_progressTextView = (TextView) findViewById(R.id.percentProgressTextView);
		_progressTextView.setText("0 %");

		_logger = new LucaHomeLogger(TAG);
		_logger.Debug("onCreate");

		_context = this;
		_receiverController = new ReceiverController(_context);
		_navigationService = new NavigationService(_context);

		Intent startMainService = new Intent(_context, MainService.class);
		Bundle mainServiceBundle = new Bundle();
		mainServiceBundle.putSerializable(Constants.BUNDLE_MAIN_SERVICE_ACTION, MainServiceAction.BOOT);
		startMainService.putExtras(mainServiceBundle);
		_context.startService(startMainService);
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");

		_receiverController.RegisterReceiver(_commandReceiver, 
				new String[] { Constants.BROADCAST_COMMAND });
		_receiverController.RegisterReceiver(_updateProgressBarReceiver,
				new String[] { Constants.BROADCAST_UPDATE_PROGRESSBAR });
	}

	@Override
	public void onPause() {
		super.onPause();
		_logger.Debug("onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		_logger.Debug("onDestroy");

		_receiverController.UnregisterReceiver(_commandReceiver);
		_receiverController.UnregisterReceiver(_updateProgressBarReceiver);
	}
}