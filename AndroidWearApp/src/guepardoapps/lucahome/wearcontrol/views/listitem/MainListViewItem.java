package guepardoapps.lucahome.wearcontrol.views.listitem;

public class MainListViewItem {

	@SuppressWarnings("unused")
	private static final String TAG = MainListViewItem.class.getName();

	private int _imageResource;
	private String _text;

	public MainListViewItem(int imageResource, String text) {
		_imageResource = imageResource;
		_text = text;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetText() {
		return _text;
	}
}
