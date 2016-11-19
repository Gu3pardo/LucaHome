package guepardoapps.lucahome.wearcontrol.views.listitem;

import java.io.Serializable;

public class SocketListViewItem implements Serializable {

	private static final long serialVersionUID = 2001018740352470878L;

	@SuppressWarnings("unused")
	private static final String TAG = SocketListViewItem.class.getName();

	private static final String COMMAND = "ACTION:SET:SOCKET:";

	private int _imageResource;
	private String _name;
	private boolean _isActivated;

	public SocketListViewItem(int imageResource, String name, boolean isActivated) {
		_imageResource = imageResource;
		_name = name;
		_isActivated = isActivated;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetName() {
		return _name;
	}

	public boolean GetIsActivated() {
		return _isActivated;
	}

	public void SetIsActivated(boolean isActivated) {
		_isActivated = isActivated;
	}

	public String GetCommandText() {
		return COMMAND + _name + (_isActivated ? ":0" : ":1");
	}

	@Override
	public String toString() {
		return "_imageResource: " + String.valueOf(_imageResource) + " _name: " + String.valueOf(_name)
				+ " _isActivated: " + String.valueOf(_isActivated);
	}
}
