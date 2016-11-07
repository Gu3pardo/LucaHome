package guepardoapps.lucahome.view;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.Logger;
import guepardoapps.lucahome.common.enums.ServerAction;

public class SmartMirrorView extends Activity {

	private static final String TAG = SmartMirrorView.class.getName();
	private Logger _logger;

	private Context _context;

	private EditText _informationTextInput;
	private EditText _webviewInput;
	private EditText _youtubeIdInput;

	private Button _sendInformationTextButton;
	private Button _sendWebviewButton;
	private Button _sendYoutubeIdButton;
	private Button _sendYoutubePlayButton;
	private Button _sendYoutubeStopButton;

	private Socket _socket;

	// TODO CHECK IF VALID!
	private static final int SERVERPORT = 5000;
	private static final String SERVER_IP = "10.0.2.2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_smartmirror);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));

		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;

		_informationTextInput = (EditText) findViewById(R.id.informationTextInput);
		_webviewInput = (EditText) findViewById(R.id.webviewInput);
		_youtubeIdInput = (EditText) findViewById(R.id.youtubeIdInput);

		_sendInformationTextButton = (Button) findViewById(R.id.informationTextSendButton);
		_sendInformationTextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _informationTextInput.getText().toString();
				if (data.length() < 5) {
					_logger.Warn("Data is too short!");
					Toast.makeText(_context, "Data is too short!", Toast.LENGTH_SHORT).show();
					return;
				}
				sendServerCommand(ServerAction.SHOW_INFORMATION_TEXT.toString(), data);
			}
		});
		_sendWebviewButton = (Button) findViewById(R.id.webviewSendButton);
		_sendWebviewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _webviewInput.getText().toString();
				if (data.length() < 5) {
					_logger.Warn("Data is too short!");
					Toast.makeText(_context, "Data is too short!", Toast.LENGTH_SHORT).show();
					return;
				}
				sendServerCommand(ServerAction.SHOW_WEBVIEW.toString(), data);
			}
		});
		_sendYoutubeIdButton = (Button) findViewById(R.id.youtubeSendButton);
		_sendYoutubeIdButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String data = _youtubeIdInput.getText().toString();
				if (data.length() < 5) {
					_logger.Warn("Data is too short!");
					Toast.makeText(_context, "Data is too short!", Toast.LENGTH_SHORT).show();
					return;
				}
				sendServerCommand(ServerAction.SHOW_YOUTUBE_VIDEO.toString(), data);
			}
		});
		_sendYoutubePlayButton = (Button) findViewById(R.id.youtubePlayButton);
		_sendYoutubePlayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendServerCommand(ServerAction.PLAY_YOUTUBE_VIDEO.toString(), "");
			}
		});
		_sendYoutubeStopButton = (Button) findViewById(R.id.youtubeStopButton);
		_sendYoutubeStopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendServerCommand(ServerAction.STOP_YOUTUBE_VIDEO.toString(), "");
			}
		});

		ClientThread clientThread = new ClientThread(SERVERPORT, SERVER_IP);
		new Thread(clientThread).start();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (_logger != null) {
			_logger.Debug("onResume");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (_logger != null) {
			_logger.Debug("onPause");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (_logger != null) {
			_logger.Debug("onDestroy");
		}
	}

	private void sendServerCommand(String command, String data) {
		_logger.Debug("sendServerCommand");
		try {
			String communication = "ACTION:" + command + "&DATA:" + data;
			_logger.Debug("Communication is: " + communication);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_socket.getOutputStream());
			_logger.Debug("outputStreamWriter is: " + outputStreamWriter.toString());
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			_logger.Debug("bufferedWriter is: " + bufferedWriter.toString());
			PrintWriter printWriter = new PrintWriter(bufferedWriter);
			_logger.Debug("printWriter is: " + printWriter.toString());
			printWriter.println(communication);
		} catch (UnknownHostException e) {
			_logger.Error(e.toString());
		} catch (IOException e) {
			_logger.Error(e.toString());
		} catch (Exception e) {
			_logger.Error(e.toString());
		}
	}

	private class ClientThread implements Runnable {

		private final String TAG = ClientThread.class.getName();
		private Logger _logger;

		private int _serverPort;
		private String _serverIP;

		public ClientThread(int serverPort, String serverIP) {
			_logger = new Logger(TAG);

			_serverPort = serverPort;
			_serverIP = serverIP;
		}

		@Override
		public void run() {
			_logger.Debug("run");
			try {
				InetAddress serverAddr = InetAddress.getByName(_serverIP);
				_logger.Debug("serverAddr is: " + serverAddr.toString());
				_socket = new Socket(serverAddr, _serverPort);
				_logger.Debug("_socket is: " + _socket.toString());
			} catch (UnknownHostException e) {
				_logger.Error(e.toString());
			} catch (IOException e) {
				_logger.Error(e.toString());
			}
		}
	}
}
