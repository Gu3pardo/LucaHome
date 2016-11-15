package guepardoapps.lucahome.accesscontrol.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;

import guepardoapps.lucahome.accesscontrol.common.Constants;

import guepardoapps.toolset.common.Logger;
import guepardoapps.toolset.controller.NetworkController;

public class ServerThread {

	private static final String TAG = ServerThread.class.getName();
	private Logger _logger;

	private int _socketServerPort;
	private ServerSocket _serverSocket;

	private Context _context;
	private NetworkController _networkController;

	private DataHandler _dataHandler;

	public ServerThread(int port, Context context) {
		_socketServerPort = port;

		_context = context;
		_networkController = new NetworkController(_context, null);

		_dataHandler = new DataHandler(_context);

		_logger = new Logger(TAG, Constants.DEBUGGING_ENABLED);
		_logger.Debug("IpAddress: " + _networkController.GetIpAddress());
		_logger.Debug("SocketServerPort: " + String.valueOf(_socketServerPort));
	}

	public void Start() {
		_logger.Debug("Start");

		SocketServerThread socketServerThread = new SocketServerThread();
		Thread thread = new Thread(socketServerThread);
		thread.start();
	}

	public void Dispose() {
		_logger.Debug("Dispose");

		if (_serverSocket != null) {
			try {
				_serverSocket.close();
			} catch (IOException e) {
				_logger.Error(e.toString());
			}
		}
	}

	private class SocketServerThread extends Thread {

		@Override
		public void run() {
			try {
				_serverSocket = new ServerSocket(_socketServerPort);
				_logger.Debug("I'm waiting here: " + _serverSocket.getLocalPort());

				while (true) {
					Socket socket = _serverSocket.accept();

					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket);
					socketServerReplyThread.run();
				}
			} catch (IOException e) {
				_logger.Error(e.toString());
			}
		}

	}

	private class SocketServerReplyThread extends Thread {

		private Socket _hostThreadSocket;
		private BufferedReader _inputReader;

		SocketServerReplyThread(Socket socket) {
			_logger.Debug("New SocketServerReplyThread");
			_hostThreadSocket = socket;
			_logger.Debug("_hostThreadSocket: " + _hostThreadSocket.toString());
		}

		@Override
		public void run() {
			OutputStream outputStream;
			String response;
			boolean fail = false;

			try {
				InputStreamReader inputStreamReader = new InputStreamReader(_hostThreadSocket.getInputStream());
				_logger.Debug("inputStreamReader: " + inputStreamReader.toString());
				_inputReader = new BufferedReader(inputStreamReader);
				_logger.Debug("inputReader: " + _inputReader.toString());
				response = "OK";
			} catch (IOException e) {
				_logger.Error(e.toString());
				response = "Fail! " + e.toString();
				fail = true;
			}

			if (!fail) {
				try {
					_logger.Debug("trying to read");
					String read = _inputReader.readLine();
					_logger.Info("read: " + read);
					_dataHandler.PerformAction(read);
					response = "OK";
				} catch (IOException e) {
					_logger.Error(e.toString());
					response = "Fail! " + e.toString();
					fail = true;
				}
			}

			try {
				outputStream = _hostThreadSocket.getOutputStream();
				_logger.Debug("outputStream: " + outputStream.toString());

				PrintStream printStream = new PrintStream(outputStream);
				_logger.Debug("printStream: " + printStream.toString());
				printStream.print(response);
				printStream.close();
			} catch (IOException e) {
				_logger.Error(e.toString());
			} finally {
				_logger.Debug("response: " + response);
			}
		}
	}
}