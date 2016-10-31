package guepardoapps.common;

import java.io.Serializable;

import android.util.Log;

public class Logger implements Serializable {

	private static final long serialVersionUID = -1816799824182252692L;

	private static boolean DEBUG_ENABLED = false;

	private String _tag;

	public Logger(String tag) {
		_tag = tag;
	}

	public void Verbose(String message) {
		if (DEBUG_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.v(_tag, message);
				}
			}
		}
	}

	public void Debug(String message) {
		if (DEBUG_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.d(_tag, message);
				}
			}
		}
	}

	public void Info(String message) {
		if (DEBUG_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.i(_tag, message);
				}
			}
		}
	}

	public void Warn(String message) {
		if (DEBUG_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.w(_tag, message);
				}
			}
		}
	}

	public void Error(String message) {
		if (DEBUG_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.e(_tag, message);
				}
			}
		}
	}
}
