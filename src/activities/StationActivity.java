package activities;

import model.Station;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.douglas.skytrainproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StationActivity extends Activity implements View.OnClickListener {
	
	public static final String EXTRA_STATION_OBJECT = "com.douglas.skytrainproject.STATION";
	
	private GoogleMap googleMap;
//	static LatLng StationLatLng = new LatLng(21 , 57);

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
		stationImage.setImageResource(Integer.valueOf(station.getImage()));
		//vars for map (we should get values in the constructor from object ) 
		LatLng StationLatLng = new LatLng(Double.parseDouble(station.getLatX()), Double.parseDouble(station.getLongY()));
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().
			               findFragmentById(R.id.map)).getMap();
				
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			/*Marker TP = */googleMap.addMarker(new MarkerOptions().position(
					StationLatLng).title(station.getName()));
			CameraPosition cameraPosition = new CameraPosition.Builder().target(StationLatLng).zoom(14).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		View btnRouteTo = findViewById(R.id.goToRoute);
		btnRouteTo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.goToRoute:
			Intent in = new Intent(this, TripFormActivity.class);
			in.putExtra(EXTRA_STATION_OBJECT, getIntent().getParcelableExtra("station"));
			startActivity(in);
			break;
		}
	}
}
