package activities;

import com.douglas.skytrainproject.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class StationActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station);
		//getting stuff from the view
		TextView stationName =(TextView)findViewById(R.id.stationName);
		
		Intent intent = this.getIntent();
		//Station station = intent.getParcelableExtra("station");
		
		//stationName.setText(station.getName());

	}
}
