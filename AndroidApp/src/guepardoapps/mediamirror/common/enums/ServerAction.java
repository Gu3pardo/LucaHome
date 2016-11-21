package guepardoapps.mediamirror.common.enums;

import java.io.Serializable;

public enum ServerAction implements Serializable {

	NULL(0, ""), 
	SHOW_YOUTUBE_VIDEO(1, "Show_YouTube_Video"), 
	PLAY_YOUTUBE_VIDEO(2, "Play_YouTube_Video"), 
	STOP_YOUTUBE_VIDEO(3, "Stop_YouTube_Video"), 
	SHOW_WEBVIEW(4, "Show_Webview"), 
	SHOW_CENTER_TEXT(5, "Show_Center_Text"),
	SET_RSS_FEED(6, "Set_Rss_Feed"), 
	RESET_RSS_FEED(7, "Reset_Rss_Feed"), 
	UPDATE_CURRENT_WEATHER(8, "Update_Current_Weather"), 
	UPDATE_FORECAST_WEATHER(9, "Update_Forecast_Weather"), 
	UPDATE_RASPBERRY_TEMPERATURE(10, "Update_Raspberry_Temperature"), 
	UPDATE_IP_ADDRESS(11, "Update_Ip_Address"), 
	UPDATE_BIRTHDAY_ALARM(12, "Update_Birthday_Alarm"),
	INCREASE_VOLUME(13, "Increase_Volume"),
	DECREASE_VOLUME(14, "Decrease_Volume"),
	MUTE_VOLUME(15, "Mute_Volume"),
	UNMUTE_VOLUME(16, "Unmute_Volume"),
	INCREASE_SCREEN_BRIGHTNESS(17, "Increase_Screen_Brightness"),
	DECREASE_SCREEN_BRIGHTNESS(18, "Decrease_Screen_Brightness"),
	PLAY_ALARM(19, "PLAY_ALARM"),
	STOP_ALARM(20, "STOP_ALARM"),
	GAME_COMMAND(21, "GAME_COMMAND"),
	GAME_SNAKE_START(22, "GAME_SNAKE_START"),
	GAME_SNAKE_STOP(23, "GAME_SNAKE_STOP"),
	GAME_TETRIS_START(24, "GAME_TETRIS_START"),
	GAME_TETRIS_STOP(25, "GAME_TETRIS_STOP");

	private int _id;
	private String _action;

	private ServerAction(int id, String action) {
		_id = id;
		_action = action;
	}

	public int GetId() {
		return _id;
	}

	@Override
	public String toString() {
		return _action;
	}

	public static ServerAction GetById(int id) {
		for (ServerAction e : values()) {
			if (e._id == id) {
				return e;
			}
		}
		return null;
	}

	public static ServerAction GetByString(String action) {
		for (ServerAction e : values()) {
			if (e._action.contains(action)) {
				return e;
			}
		}
		return null;
	}
}
