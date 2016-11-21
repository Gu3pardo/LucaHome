package guepardoapps.lucahome.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.services.helper.NavigationService;
import guepardoapps.lucahome.viewcontroller.MediaMirrorController;

public class GameView extends Activity {

	private static final String TAG = GameView.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;
	private MediaMirrorController _mediaMirrorController;
	private NavigationService _navigationService;

	private Spinner _selectServerSpinner;

	private Button _buttonSnakePlay;
	private Button _buttonSnakeStop;

	private Button _buttonTetrisPlay;
	private Button _buttonTetrisStop;

	private Button _buttonUp;
	private Button _buttonLeft;
	private Button _buttonRight;
	private Button _buttonDown;
	private Button _buttonRotate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_games);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_logger = new LucaHomeLogger(TAG);
		_logger.Debug("onCreate");

		_context = this;
		_mediaMirrorController = new MediaMirrorController(_context);
		_navigationService = new NavigationService(_context);

		initializeSpinner();
		initializeButtonSnake();
		initializeButtonTetris();
		initializeButtonControl();
	}

	@Override
	public void onResume() {
		super.onResume();
		_logger.Debug("onResume");
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
		_mediaMirrorController.Dispose();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			_navigationService.NavigateTo(HomeView.class, true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initializeSpinner() {
		_logger.Debug("initializeSpinner");

		_selectServerSpinner = (Spinner) findViewById(R.id.selectServerSpinner);
		ArrayAdapter<String> serverDataAdapter = new ArrayAdapter<String>(_context,
				android.R.layout.simple_spinner_item, MediaMirrorController.SERVER_IPS);
		serverDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_selectServerSpinner.setAdapter(serverDataAdapter);
		_selectServerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				_mediaMirrorController.SelectServer(parent.getItemAtPosition(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
	}

	private void initializeButtonSnake() {
		_logger.Debug("initializeButtonSnake");

		_buttonSnakePlay = (Button) findViewById(R.id.buttonSnakePlay);
		_buttonSnakePlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendSnakeCommandStart();
			}
		});
		_buttonSnakeStop = (Button) findViewById(R.id.buttonSnakeStop);
		_buttonSnakeStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendSnakeCommandStop();
			}
		});
	}

	private void initializeButtonTetris() {
		_logger.Debug("initializeButtonTetris");

		_buttonTetrisPlay = (Button) findViewById(R.id.buttonTetrisPlay);
		_buttonTetrisPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendTetrisCommandStart();
			}
		});
		_buttonTetrisStop = (Button) findViewById(R.id.buttonTetrisStop);
		_buttonTetrisStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendTetrisCommandStop();
			}
		});
	}

	private void initializeButtonControl() {
		_logger.Debug("initializeButtonControl");

		_buttonUp = (Button) findViewById(R.id.buttonUp);
		_buttonUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendGameCommandUp();
			}
		});
		_buttonLeft = (Button) findViewById(R.id.buttonLeft);
		_buttonLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendGameCommandLeft();
			}
		});
		_buttonRight = (Button) findViewById(R.id.buttonRight);
		_buttonRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendGameCommandRight();
			}
		});
		_buttonDown = (Button) findViewById(R.id.buttonDown);
		_buttonDown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendGameCommandDown();
			}
		});
		_buttonRotate = (Button) findViewById(R.id.buttonRotate);
		_buttonRotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_mediaMirrorController.SendGameCommandRotate();
			}
		});
	}
}