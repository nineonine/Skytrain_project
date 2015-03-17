package activities;

import model.Station;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.douglas.skytrainproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StationActivity extends Activity {
	private GoogleMap googleMap;
	static final LatLng TutorialsPoint = new LatLng(21 , 57);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station);
		// getting stuff from the view
		TextView stationName = (TextView) findViewById(R.id.stationName);
		TextView stationDescription = (TextView) findViewById(R.id.stationDescription);
		TextView stationLocation = (TextView) findViewById(R.id.stationLocation);
		ImageView stationImage = (ImageView) findViewById(R.id.stationImage);

		// Getting passed object
		Intent intent = this.getIntent();
		Station station = intent.getParcelableExtra("station");

		// Seeding textviews with data from object
		stationName.setText(station.getName());
		stationDescription.setText(station.getDescription());
		stationLocation.setText(station.getLocation());

		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().
			               findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			Marker TP = googleMap.addMarker(new MarkerOptions().position(
					TutorialsPoint).title("TutorialsPoint"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
