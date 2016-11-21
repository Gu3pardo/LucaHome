package guepardoapps.lucahome.wearcontrol.sensor.helper;

public class GravityDto {

	@SuppressWarnings("unused")
	private static final String TAG = GravityDto.class.getName();

	private float _x;
	private float _y;
	private float _z;

	private String _handDirection;

	public GravityDto(float x, float y, float z) {
		_x = x;
		_y = y;
		_z = z;
		_handDirection = generateHandDirection();
	}

	public float GetX() {
		return _x;
	}

	public float GetY() {
		return _y;
	}

	public float GetZ() {
		return _z;
	}

	public String GetHandDirection() {
		if (_handDirection == null) {
			return "not yet calculated!";
		}
		return _handDirection;
	}

	private String generateHandDirection() {
		if (_x > 0) {
			// points down
		}
		if (_y > 0) {
			// wrist turned leftside (left hand!)
		}
		if (_z > 0) {
			// wether hand lay normal down or is rotated 180° (TEST!)
		}
		// TODO
		return null;
	}

	@Override
	public String toString() {
		return "{" + GravityDto.class.getName() + ":{X:" + String.valueOf(_x) + ",Y:" + String.valueOf(_y) + ",Z:"
				+ String.valueOf(_z) + "}}";
	}
}