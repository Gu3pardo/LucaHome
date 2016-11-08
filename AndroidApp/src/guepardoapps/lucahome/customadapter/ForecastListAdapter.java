package guepardoapps.lucahome.customadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import guepardoapps.lucahome.R;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.classes.SerializableList;

import guepardoapps.toolset.openweather.model.ForecastWeatherModel;

public class ForecastListAdapter extends BaseAdapter {

	private static String TAG = ForecastListAdapter.class.getName();
	private LucaHomeLogger _logger;

	private SerializableList<ForecastWeatherModel> _forecastList;
	private static LayoutInflater _inflater = null;

	public ForecastListAdapter(Context context, SerializableList<ForecastWeatherModel> forecastList) {
		_logger = new LucaHomeLogger(TAG);

		_forecastList = forecastList;
		for (int index = 0; index < _forecastList.getSize(); index++) {
			_logger.Debug(_forecastList.getValue(index).toString());
		}

		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return _forecastList.getSize();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		private ImageView _image;
		private TextView _description;
		private TextView _temperature;
		private TextView _humidity;
		private TextView _pressure;
		private TextView _dateTime;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int index, View convertView, ViewGroup parent) {
		final Holder holder = new Holder();
		View rowView = _inflater.inflate(R.layout.list_forecast_item, null);

		holder._image = (ImageView) rowView.findViewById(R.id.weather_item_image);
		holder._image.setImageResource(_forecastList.getValue(index).GetIcon());

		holder._description = (TextView) rowView.findViewById(R.id.weather_item_description);
		holder._description.setText(_forecastList.getValue(index).GetWeatherDescription());

		holder._temperature = (TextView) rowView.findViewById(R.id.weather_item_temperatures);
		holder._temperature
				.setText(_forecastList.getValue(index).GetTempMin() + "-" + _forecastList.getValue(index).GetTempMax());

		holder._humidity = (TextView) rowView.findViewById(R.id.weather_item_humidity);
		holder._humidity.setText(_forecastList.getValue(index).GetHumidity());

		holder._pressure = (TextView) rowView.findViewById(R.id.weather_item_pressure);
		holder._pressure.setText(_forecastList.getValue(index).GetPressure());

		holder._dateTime = (TextView) rowView.findViewById(R.id.weather_item_datetime);
		holder._dateTime
				.setText(_forecastList.getValue(index).GetDate() + "/" + _forecastList.getValue(index).GetTime());

		return rowView;
	}
}