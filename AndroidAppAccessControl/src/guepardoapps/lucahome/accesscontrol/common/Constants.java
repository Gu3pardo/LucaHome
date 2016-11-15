package guepardoapps.lucahome.accesscontrol.common;

public class Constants {
	// DEBUGGING
	public static final boolean DEBUGGING_ENABLED = true;
	// IP ADRESS VIEW
	public static final int IP_ADRESS_UPDATE_TIMEOUT = 4 * 60 * 60 * 1000;
	public static final String BUNDLE_IP_ADRESS = "BUNDLE_IP_ADRESS";
	public static final String BROADCAST_UPDATE_IP_ADRESS = "guepardoapps.lucahome.accesscontrol.broadcast.UPDATE_IP_ADRESS";
	public static final String BROADCAST_PERFORM_UPDATE_IP_ADRESS = "guepardoapps.lucahome.accesscontrol.broadcast.PERFORM_UPDATE_IP_ADRESS";
	// AlARM STATE VIEW
	public static final String BUNDLE_ALARM_STATE = "BUNDLE_ALARM_STATE";
	public static final String BROADCAST_ALARM_STATE = "guepardoapps.lucahome.accesscontrol.broadcast.ALARM_STATE";
	// SERVER
	public static final int SERVERPORT = 8080;
	// DATA FOR RASPBERRY
	public static final String USER_NAME = "AccessControl";
	public static final String PASS_PHRASE = "518716";
	public static final String[] SERVER_URLs = new String[] { /*TODO add ip addresses*/ };
	public static final String ACTION_PATH = "/lib/lucahome.php?user=";
	public static final String BUNDLE_REST_ACTION = "BUNDLE_REST_ACTION";
	// ACTIONS FOR RASPBERRY
	public static final String ACTION_ACTIVATE_ALARM = "activatealarm";
	public static final String ACTION_SEND_CODE = "sendcode";
}
