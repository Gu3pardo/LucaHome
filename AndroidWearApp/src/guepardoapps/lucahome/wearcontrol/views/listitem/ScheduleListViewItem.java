package guepardoapps.lucahome.wearcontrol.views.listitem;

import java.io.Serializable;

public class ScheduleListViewItem implements Serializable {

	private static final long serialVersionUID = -1895726413284365227L;

	@SuppressWarnings("unused")
	private static final String TAG = ScheduleListViewItem.class.getName();

	private static final String COMMAND = "ACTION:SET:SCHEDULE:";

	private int _imageResource;
	private String _name;
	private String _information;
	private boolean _isActive;

	public ScheduleListViewItem(int imageResource, String name, String information, boolean isActive) {
		_imageResource = imageResource;
		_name = name;
		_information = information;
		_isActive = isActive;
	}

	public int GetImageResource() {
		return _imageResource;
	}

	public String GetName() {
		return _name;
	}

	public String GetInformation() {
		return _information;
	}

	public boolean GetIsActive() {
		return _isActive;
	}

	public void SetIsActive(boolean isActive) {
		_isActive = isActive;
	}

	public String GetCommandText() {
		return COMMAND + _name + (_isActive ? ":1" : ":0");
	}

	@Override
	public String toString() {
		return "_imageResource: " + String.valueOf(_imageResource) + " _name: " + String.valueOf(_name)
				+ " _information: " + _information + " _isActive: " + String.valueOf(_isActive);
	}
}
