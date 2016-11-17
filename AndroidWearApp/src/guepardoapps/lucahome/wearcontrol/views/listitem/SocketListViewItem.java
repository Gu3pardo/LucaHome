package guepardoapps.lucahome.wearcontrol.views.listitem;

public class SocketListViewItem {

	@SuppressWarnings("unused")
	private static final String TAG = SocketListViewItem.class.getName();

	private int _imageResource;
	private String _text;
	private boolean _isActivated;

	public SocketListViewItem(int imageResource, String text, boolean isActivated) {
		_imageResource = imageResource;
		_text = text;
		_isActivated = isActivated;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetText() {
		return _text;
	}

	public boolean GetIsActivated() {
		return _isActivated;
	}
}
