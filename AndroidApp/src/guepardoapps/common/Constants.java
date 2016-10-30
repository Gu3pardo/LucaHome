package guepardoapps.common;

/**********************************************************
 * Class holding variables used all over the application *
 **********************************************************/
public class Constants {

	/***************** IDs for notifications ****************/

	public static int ID_NOTIFICATION_WEAR = 120288;
	public static int ID_NOTIFICATION_BIRTHDAY = 219000;
	public static int ID_NOTIFICATION_TEMPERATURE = 111293;

	/********************* Package names ********************/

	public static String PACKAGE_BUBBLE_UPNP = "com.bubblesoft.android.bubbleupnp";
	public static String PACKAGE_KORE = "org.xbmc.kore";
	public static String PACKAGE_YATSE = "org.leetzone.android.yatsewidgetfree";

	/**************** Intent bundle constants ***************/

	// ________________SEND_ACTION_______________

	public static String BUNDLE_ACTION = "ACTION";
	public static String BUNDLE_USER = "USER";
	public static String BUNDLE_PASSPHRASE = "PASSPHRASE";

	public static String BUNDLE_BROADCAST = "BROADCAST";
	public static String BUNDLE_NAME = "NAME";
	public static String BUNDLE_LUCA_OBJECT = "LUCA_OBJECT";
	public static String BUNDLE_RASPBERRY_SELETION = "RASPBERRY_SELETION";

	public static String BUNDLE_SEND_ACTION = "SEND_ACTION";
	public static String BUNDLE_SOCKET_DATA = "SOCKET_DATA";

	// ________________NOTIFICATION______________

	public static String BUNDLE_NOTIFICATION_TITLE = "BUNDLE_NOTIFICATION_TITLE";
	public static String BUNDLE_NOTIFICATION_BODY = "BUNDLE_NOTIFICATION_BODY";
	public static String BUNDLE_NOTIFICATION_ID = "BUNDLE_NOTIFICATION_ID";
	public static String BUNDLE_NOTIFICATION_ICON = "BUNDLE_NOTIFICATION_ICON";

	/************ Constants for sockets and stuff ***********/

	public static String SERVER_URL_RPI1 = "ENTER_HERE_YOUR_IP";
	public static String SERVER_URL_RPI2 = "ENTER_HERE_YOUR_SECOND_IP"; //I have to raspberrys running the server

	public static String REST_URL_RPI1 = SERVER_URL_RPI1 + "/lib/lucahome.php?user=";
	public static String REST_URL_RPI2 = SERVER_URL_RPI2 + "/lib/lucahome.php?user=";

	public static String REST_PASSWORD = "&password=";
	public static String REST_ACTION = "&action=";

	public static String STATE_ON = "&state=1";
	public static String STATE_OFF = "&state=0";

	// ________________SOCKET________________

	public static String BUNDLE_SOCKET_LIST = "SOCKET_LIST";
	public static String BUNDLE_SOCKET_SINGLE = "SOCKET_SINGLE";

	public static String ACTION_GET_SOCKETS = "getsockets";
	public static String ACTION_ADD_SOCKET = "addsocket&name=";
	public static String ACTION_SET_SOCKET = "setsocket&socket=";
	public static String ACTION_DELETE_SOCKET = "deletesocket&socket=";

	public static String ACTION_ACTIVATE_ALL_SOCKETS = "activateAllSockets";
	public static String ACTION_DEACTIVATE_ALL_SOCKETS = "deactivateAllSockets";

	public static String SOCKET_DOWNLOAD = "SOCKET_DOWNLOAD";

	public static String BROADCAST_SET_SOCKET = "guepardoapps.lucahome.broadcast.set.wirelesssocket.";
	public static String BROADCAST_RELOAD_SOCKET = "guepardoapps.lucahome.broadcast.reload.wirelesssocket";
	public static String BROADCAST_NOTIFICATION_SOCKET = "guepardoapps.lucahome.broadcast.notification.wirelesssocket.";
	public static String BROADCAST_DELETE_SOCKET = "guepardoapps.lucahome.broadcast.delete.wirelesssocket.";
	public static String BROADCAST_ADD_SOCKET = "guepardoapps.lucahome.broadcast.add.wirelesssocket";
	public static String BROADCAST_RELOAD_SOCKETS = "guepardoapps.lucahome.broadcast.reload.wirelesssockets";

	// _______________SCHEDULE_______________

	public static String BUNDLE_SCHEDULE_LIST = "SCHEDULE_LIST";
	public static String BUNDLE_SCHEDULE_SINGLE = "SCHEDULE_SINGLE";

	public static String ACTION_GET_SCHEDULES = "getschedules";
	public static String ACTION_ADD_SCHEDULE = "addschedule&name=";
	public static String ACTION_SET_SCHEDULE = "setschedule&schedule=";
	public static String ACTION_DELETE_SCHEDULE = "deleteschedule&schedule=";

	public static String ACTION_ACTIVATE_ALL_SCHEDULES = "activateAllSchedules";
	public static String ACTION_DEACTIVATE_ALL_SCHEDULES = "deactivateAllSchedules";

	public static String SCHEDULE_ACTIVE = "Active";
	public static String SCHEDULE_INACTIVE = "Inactive";

	public static String SCHEDULE_DOWNLOAD = "SCHEDULE_DOWNLOAD";

	public static String BROADCAST_SET_SCHEDULE = "guepardoapps.lucahome.broadcast.set.schedule.";
	public static String BROADCAST_RELOAD_SCHEDULE = "guepardoapps.lucahome.broadcast.reload.schedule";
	public static String BROADCAST_DELETE_SCHEDULE = "guepardoapps.lucahome.broadcast.delete.schedule.";
	public static String BROADCAST_ADD_SCHEDULE = "guepardoapps.lucahome.broadcast.add.schedule";

	// ________________TIMER_________________

	public static String BUNDLE_TIMER_LIST = "TIMER_LIST";
	public static String BUNDLE_TIMER_SINGLE = "TIMER_SINGLE";

	public static String BROADCAST_RELOAD_TIMER = "guepardoapps.lucahome.broadcast.reload.timer";

	// ________________BIRTHDAY______________

	public static String BUNDLE_BIRTHDAY_LIST = "BIRTHDAY_LIST";
	public static String BUNDLE_BIRTHDAY_SINGLE = "BIRTHDAY_SINGLE";

	public static String ACTION_GET_BIRTHDAYS = "getbirthdays";
	public static String ACTION_ADD_BIRTHDAY = "addbirthday&id=";
	public static String ACTION_UPDATE_BIRTHDAY = "updatebirthday&id=";
	public static String ACTION_DELETE_BIRTHDAY = "deletebirthday&id=";

	public static String BIRTHDAY_DOWNLOAD = "BIRTHDAY_DOWNLOAD";

	public static String BROADCAST_DELETE_BIRTHDAY = "guepardoapps.lucahome.broadcast.delete.birthday.";
	public static String BROADCAST_RELOAD_BIRTHDAY = "guepardoapps.lucahome.broadcast.reload.birthday";
	public static String BROADCAST_HAS_BIRTHDAY = "guepardoapps.lucahome.broadcast.has.birthday.";
	public static String BROADCAST_ADD_BIRTHDAY = "guepardoapps.lucahome.broadcast.add.birthday";

	// __________________MOVIE_______________

	public static String BUNDLE_MOVIE_LIST = "MOVIE_LIST";
	public static String BUNDLE_MOVIE_SINGLE = "MOVIE_SINGLE";

	public static String ACTION_GET_MOVIES = "getmovies";
	public static String ACTION_ADD_MOVIE = "addmovie&title=";
	public static String ACTION_UPDATE_MOVIE = "updatemovie&title=";
	public static String ACTION_DELETE_MOVIE = "deletemovie&title=";
	public static String ACTION_START_MOVIE = "startmovie&title=";

	public static String MOVIE_SOCKET_TV = "TV";
	public static String MOVIE_SOCKET_STORAGE = "Storage";

	public static String MOVIE_DOWNLOAD = "MOVIE_DOWNLOAD";

	public static String BROADCAST_DELETE_MOVIE = "guepardoapps.lucahome.broadcast.delete.movie.";
	public static String BROADCAST_RELOAD_MOVIE = "guepardoapps.lucahome.broadcast.reload.movie";
	public static String BROADCAST_UPDATE_MOVIE = "guepardoapps.lucahome.broadcast.update.movie.";
	public static String BROADCAST_ADD_MOVIE = "guepardoapps.lucahome.broadcast.add.movie";
	public static String BROADCAST_ACTIVATE_MOVIE = "guepardoapps.lucahome.broadcast.activate.movie";

	// _______________TEMPERATURE____________

	public static String BUNDLE_TEMPERATURE_LIST = "TEMPERATURE_LIST";
	public static String BUNDLE_TEMPERATURE_SINGLE = "TEMPERATURE_SINGLE";
	public static String BUNDLE_TEMPERATURE_ID = "TEMPERATURE_ID";
	public static String BUNDLE_TEMPERATURE_TYPE = "TEMPERATURE_TYPE";

	public static String ACTION_GET_TEMPERATURES = "getcurrenttemperaturerest";

	public static String TEMPERATURE_DOWNLOAD = "TEMPERATURE_DOWNLOAD";

	public static String BROADCAST_UPDATE_TEMPERATURE = "guepardoapps.lucahome.broadcast.UPDATE_TEMPERATURE";

	// _______________INFORMATION____________

	public static String BUNDLE_INFORMATION_LIST = "INFORMATION_LIST";
	public static String BUNDLE_INFORMATION_SINGLE = "INFORMATION_SINGLE";

	public static String ACTION_GET_INFORMATIONS = "getinformationsrest";

	public static String INFORMATION_DOWNLOAD = "INFORMATION_DOWNLOAD";

	// _________________CHANGE_______________

	public static String BUNDLE_CHANGE_LIST = "CHANGE_LIST";
	public static String BUNDLE_CHANGE_SINGLE = "CHANGE_SINGLE";

	public static String ACTION_GET_CHANGES = "getchangesrest";

	public static String CHANGE_DOWNLOAD = "CHANGE_DOWNLOAD";

	// _______________OPEN_WEATHER___________

	public static String BUNDLE_WEATHER_CURRENT = "WEATHER_CURRENT";
	public static String BUNDLE_WEATHER_FORECAST = "WEATHER_FORECAST";

	// ___________________USER_______________

	public static String BUNDLE_LOGGED_IN_USER = "LOGGED_IN_USER";

	public static String VALIDATE_USER = "VALIDATE_USER";

	public static String ACTION_VALIDATE_USER = "validateuser";

	public static String BROADCAST_VALIDATE_USER = "guepardoapps.lucahome.broadcast.validate.user";

	// __________________SOUND_______________

	public static String ACTION_PLAY_SOUND = "startplaying&song=";
	public static String ACTION_STOP_SOUND = "stopplaying";
	public static String ACTION_INCREASE_VOLUME = "increasevolume";
	public static String ACTION_DECREASE_VOLUME = "decreasevolume";
	public static String ACTION_GET_VOLUME = "getvolume";
	public static String ACTION_GET_SOUNDS = "getsounds";
	public static String ACTION_IS_SOUND_PLAYING = "issoundplaying";
	public static String ACTION_GET_PLAYING_FILE = "getplayingfile";

	public static String BROADCAST_IS_SOUND_PLAYING = "guepardoapps.lucahome.broadcast.get.isplaying";
	public static String BROADCAST_PLAYING_FILE = "guepardoapps.lucahome.broadcast.get.playingfile";
	public static String BROADCAST_GET_SOUNDS = "guepardoapps.lucahome.broadcast.get.sounds";
	public static String BROADCAST_GET_VOLUME = "guepardoapps.lucahome.broadcast.get.volume";
	public static String BROADCAST_START_SOUND = "guepardoapps.lucahome.broadcast.start.sound";
	public static String BROADCAST_STOP_SOUND = "guepardoapps.lucahome.broadcast.stop.sound";
	public static String BROADCAST_SET_RASPBERRY = "guepardoapps.lucahome.broadcast.set.raspberry";

	/********************** SharedPref *********************/

	public static String SHARED_PREF_NAME = "LUCA_HOME";
	public static String SHARED_PREF_INSTALLED = "APP_VERSION_0.9.1.161030_INSTALLED";

	public static String DISPLAY_SOCKET_NOTIFICATION = "DISPLAY_SOCKET_NOTIFICATION";
	public static String DISPLAY_WEATHER_NOTIFICATION = "DISPLAY_WEATHER_NOTIFICATION";
	public static String DISPLAY_TEMPERATURE_NOTIFICATION = "DISPLAY_TEMPERATURE_NOTIFICATION";

	public static String START_AUDIO_APP = "Start_Audio_App";
	public static String START_OSMC_APP = "Start_OSMC_App";

	public static String USER_DATA_ENTERED = "USER_DATA_ENTERED";
	public static String USER_NAME = "USER_NAME";
	public static String USER_PASSPHRASE = "USER_PASSPHRASE";
	
	public static String SOUND_RASPBERRY_SELECTION = "SOUND_RASPBERRY_SELECTION";

	/************************ Custom Broadcast ***********************/

	public static String BROADCAST_DOWNLOAD_BIRTHDAY_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_BIRTHDAY_FINISHED";
	public static String BROADCAST_DOWNLOAD_CHANGE_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_CHANGE_FINISHED";
	public static String BROADCAST_DOWNLOAD_INFORMATION_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_INFORMATION_FINISHED";
	public static String BROADCAST_DOWNLOAD_MOVIE_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_MOVIE_FINISHED";
	public static String BROADCAST_DOWNLOAD_SCHEDULE_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_SCHEDULE_FINISHED";
	public static String BROADCAST_DOWNLOAD_SOCKET_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_SOCKET_FINISHED";
	public static String BROADCAST_DOWNLOAD_TEMPERATURE_FINISHED = "guepardoapps.lucahome.broadcast.DOWNLOAD_TEMPERATURE_FINISHED";

	/************************ Further Constants **********************/

	public static int ACTION_BAR_COLOR = 0xff0097A7;
	public static int BIRTHDAY_BACKGROUND_COLOR = 0xFFD32F2F;

	public static int SYSTEM_PERMISSION_CODE = 345532149;

	public static String LUCAHOME_SSID = "ENTER_HERE_YOUR_SSID";
	public static String NO_LUCA_HOME = "No LucaHome network!";

	public static String ACTIVATED = "Activated";
	public static String DEACTIVATED = "Deactivated";

	public static String ACTIVE = "Active";
	public static String INACTIVE = "Inactive";

	public static String YES = "Yes";
	public static String NO = "No";

	public static String HIGH = "High";
	public static String LOW = "Low";

	public static String NO_STRING_DATA = "No Data available!";
	public static Boolean NO_BOOLEAN_DATA = false;
	public static Integer NO_INTEGER_DATA = 0;

	public static String NO_DATA_TO_LOAD = "No data to load!";
	public static String NO_DATA_TO_DELETE = "No data to delete!";

	public static String ERROR = "Error!";
	public static String VISIBLE = "VISIBLE";

	public static String CITY = "ENTER_HERE_YOUR_CITY";

	public static String BUNDLE_DOWNLOAD_SUCCESS = "DOWNLOAD_SUCCESS";
	public static String BUNDLE_DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS";
}