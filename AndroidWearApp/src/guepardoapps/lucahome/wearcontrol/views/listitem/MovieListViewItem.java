package guepardoapps.lucahome.wearcontrol.views.listitem;

import java.io.Serializable;

public class MovieListViewItem implements Serializable {

	private static final long serialVersionUID = -6512570226015212910L;

	@SuppressWarnings("unused")
	private static final String TAG = MovieListViewItem.class.getName();

	private static final String COMMAND = "ACTION:PLAY:MOVIE:";

	private int _imageResource;
	private String _title;

	public MovieListViewItem(int imageResource, String title) {
		_imageResource = imageResource;
		_title = title;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetTitle() {
		return _title;
	}

	public String GetCommandText() {
		return COMMAND + _title;
	}

	@Override
	public String toString() {
		return "_imageResource: " + String.valueOf(_imageResource) + " _title: " + String.valueOf(_title);
	}
}
