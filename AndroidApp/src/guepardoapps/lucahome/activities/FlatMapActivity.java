package guepardoapps.lucahome.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import guepardoapps.common.BaseActivity;
import guepardoapps.common.Constants;
import guepardoapps.common.Logger;
import guepardoapps.lucahome.R;

public class FlatMapActivity extends BaseActivity {

	private static String TAG = "FlatMapActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_logger = new Logger(TAG);
		_logger.Debug("onCreate");

		_context = this;

		setContentView(R.layout.view_flat_map);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Constants.ACTION_BAR_COLOR));
	}
}
