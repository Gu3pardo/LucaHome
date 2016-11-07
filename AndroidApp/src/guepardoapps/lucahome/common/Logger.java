package guepardoapps.lucahome.common;

import java.io.Serializable;

import android.util.Log;

public class Logger implements Serializable {

	private static final long serialVersionUID = -6838045207724144177L;

	private String _tag;

	public Logger(String tag) {
		_tag = tag;
	}

	public void Verbose(String message) {
		if (Constants.DEBUGGING_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.v(_tag, message);
				}
			}
		}
	}

	public void Debug(String message) {
		if (Constants.DEBUGGING_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.d(_tag, message);
				}
			}
		}
	}

	public void Info(String message) {
		if (Constants.DEBUGGING_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.i(_tag, message);
				}
			}
		}
	}

	public void Warn(String message) {
		if (Constants.DEBUGGING_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.w(_tag, message);
				}
			}
		}
	}

	public void Error(String message) {
		if (Constants.DEBUGGING_ENABLED) {
			if (message != null) {
				if (message.length() > 0) {
					Log.e(_tag, message);
				}
			}
		}
	}
}
