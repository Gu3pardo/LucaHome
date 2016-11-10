package guepardoapps.lucahome.viewcontroller;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.services.DialogService;

import guepardoapps.mediamirror.client.ClientTask;
import guepardoapps.mediamirror.common.dto.YoutubeVideoDto;
import guepardoapps.mediamirror.common.enums.ServerAction;

import guepardoapps.toolset.controller.ReceiverController;

public class MediaMirrorController {

	private static final String TAG = MediaMirrorController.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;

	private DialogService _dialogService;
	private ReceiverController _receiverController;

	private ProgressDialog _loadingVideosDialog;

	private ClientTask _clientTask;

	private static final int SERVERPORT = 8080;
	private static final String SERVER_IP = "192.168.178.147";

	private BroadcastReceiver _youtubeIdReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			_logger.Debug("_youtubeIdReceiver onReceive");
			String youtubeId = intent.getStringExtra(Constants.BUNDLE_YOUTUBE_ID);
			if (youtubeId != null) {
				SendYoutubeId(youtubeId);
				_dialogService.CloseDialogCallback.run();
			}
		}
	};

	public MediaMirrorController(Context context) {
		_logger = new LucaHomeLogger(TAG);

		_context = context;

		_dialogService = new DialogService(_context);
		_receiverController = new ReceiverController(_context);
		_receiverController.RegisterReceiver(_youtubeIdReceiver, new String[] { Constants.BROADCAST_YOUTUBE_ID });
	}

	public void Dispose() {
		_logger.Debug("Dispose");
		_receiverController.UnregisterReceiver(_youtubeIdReceiver);
	}

	public void SendCenterText(String data) {
		_logger.Debug("SendCenterText");
		if (data.length() < 5) {
			_logger.Warn("Data is too short!");
			Toast.makeText(_context, "Data is too short!", Toast.LENGTH_SHORT).show();
			return;
		}
		sendServerCommand(ServerAction.SHOW_CENTER_TEXT.toString(), data);
	}

	public void SendYoutubeId(String id) {
		_logger.Debug("SendYoutubeId");
		if (id.length() < 0 || id.length() > 3 && id.length() < 11 || id.length() > 11) {
			_logger.Warn("ID is invalid! Length: " + String.valueOf(id.length()));
			Toast.makeText(_context, "ID is invalid! Length: " + String.valueOf(id.length()), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		sendServerCommand(ServerAction.SHOW_YOUTUBE_VIDEO.toString(), String.valueOf(id));
	}

	public void SendPlayYoutube() {
		_logger.Debug("SendPlayYoutube");
		sendServerCommand(ServerAction.PLAY_YOUTUBE_VIDEO.toString(), "");
	}

	public void SendStopYoutube() {
		_logger.Debug("SendStopYoutube");
		sendServerCommand(ServerAction.STOP_YOUTUBE_VIDEO.toString(), "");
	}

	public void SendRssFeedId(int id) {
		_logger.Debug("SendRssFeedId");
		if (id < 0) {
			_logger.Warn("Id is invalid!");
			Toast.makeText(_context, "ID is invalid!", Toast.LENGTH_SHORT).show();
			return;
		}
		sendServerCommand(ServerAction.SET_RSS_FEED.toString(), String.valueOf(id));
	}

	public void SendWebviewUrl(String data) {
		_logger.Debug("SendWebviewUrl");
		if (data.length() < 5) {
			_logger.Warn("Data is too short!");
			Toast.makeText(_context, "Data is too short!", Toast.LENGTH_SHORT).show();
			return;
		}
		sendServerCommand(ServerAction.SHOW_WEBVIEW.toString(), data);
	}

	private void sendServerCommand(String command, String data) {
		_logger.Debug("sendServerCommand: " + command + " with data " + data);

		String communication = "ACTION:" + command + "&DATA:" + data;
		_logger.Debug("Communication is: " + communication);

		_clientTask = new ClientTask(SERVER_IP, SERVERPORT);
		_clientTask.SetCommunication(communication);
		_clientTask.execute();
	}

	public void LoadVideos(String searchValue, int results) {
		searchValue = searchValue.replace(" ", "+");

		String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=" + String.valueOf(results)
				+ "&q=" + searchValue + "&key=" + Constants.YOUTUBE_API_KEY;

		_loadingVideosDialog = null;
		_loadingVideosDialog = ProgressDialog.show(_context, "Loading Videos...", "");
		_loadingVideosDialog.setCancelable(false);

		DownloadYoutubeVideoTask task = new DownloadYoutubeVideoTask();
		task.execute(new String[] { url });
	}

	private class DownloadYoutubeVideoTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			if (urls.length > 1) {
				_logger.Warn("Entered too many urls!");
				return "Error:Entered too many urls!";
			}

			Document document = null;
			try {
				String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17";
				document = Jsoup.connect(urls[0]).ignoreContentType(true).timeout(60 * 1000).userAgent(userAgent).get();
			} catch (IOException e) {
				_logger.Error(e.toString());
			}

			if (document != null) {
				String getJson = document.text();

				JSONObject jsonObject = null;
				try {
					jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
				} catch (JSONException e) {
					_logger.Error(e.toString());
				}

				if (jsonObject != null) {
					try {
						final ArrayList<YoutubeVideoDto> youtubeVideoList = new ArrayList<YoutubeVideoDto>();

						JSONArray items = jsonObject.getJSONArray("items");
						for (int index = 0; index < items.length(); index++) {
							JSONObject object = items.getJSONObject(index);

							try {
								JSONObject id = object.getJSONObject("id");
								String videoId = id.getString("videoId");

								JSONObject snippet = object.getJSONObject("snippet");
								String title = snippet.getString("title");
								String description = snippet.getString("description");

								if (videoId != null && title != null && description != null) {
									YoutubeVideoDto modelDto = new YoutubeVideoDto(videoId, title, description);
									_logger.Debug("New Dto: " + modelDto.toString());
									youtubeVideoList.add(modelDto);
								} else {
									_logger.Warn("Error in parsing data!");
								}
							} catch (Exception e) {
								_logger.Error(e.toString());
							}
						}

						((Activity) _context).runOnUiThread(new Runnable() {
							public void run() {
								_loadingVideosDialog.dismiss();
							}
						});
						if (youtubeVideoList.size() > 0) {
							((Activity) _context).runOnUiThread(new Runnable() {
								public void run() {
									_dialogService.ShowSelectYoutubeIdDialog(youtubeVideoList);
								}
							});
						}
					} catch (JSONException e) {
						_logger.Error(e.toString());
					}
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}
}
