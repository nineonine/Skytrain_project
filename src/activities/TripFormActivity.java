package activities;

import com.douglas.skytrainproject.R;

import model.QueryAsyncTask;
import model.SkytrainOpenHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TripFormActivity extends Activity implements OnClickListener {

	private static String query = "SELECT " + SkytrainOpenHelper.STN_ID_COL +
			", " + SkytrainOpenHelper.STN_NAME_COL + " FROM " + SkytrainOpenHelper.STN_TBL_NAME;
	
	private View btnRoute;
	private Spinner spnStnA;
	private Spinner spnStnB;
	private ProgressDialog loadPD;
	private int idCol;
	
	public static final String EXTRA_STNA_ID = "com.douglas.skytrainproject.STNA";
	public static final String EXTRA_STNB_ID = "com.douglas.skytrainproject.STNB";
	
	private class PopulateSpinnerTask extends QueryAsyncTask{
		
		public String[] fromCols = { SkytrainOpenHelper.STN_NAME_COL };
		private int[] toViews = { android.R.id.text1 };
		
		@Override
		protected void onPostExecute(Cursor result) {
			idCol = result.getColumnIndex(SkytrainOpenHelper.STN_ID_COL);
			SimpleCursorAdapter sca = new SimpleCursorAdapter(TripFormActivity.this,
					android.R.layout.simple_spinner_item, result, fromCols, toViews, 0);
			sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnStnA.setAdapter(sca);
			spnStnB.setAdapter(sca);
			loadPD.dismiss();
		}

		PopulateSpinnerTask(){
			super(TripFormActivity.this);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PopulateSpinnerTask pst = new PopulateSpinnerTask();
		setContentView(R.layout.activity_trip_form);
		spnStnA = (Spinner)findViewById(R.id.spnStnA);
		spnStnB = (Spinner)findViewById(R.id.spnStnB);
		loadPD = ProgressDialog.show(this, getString(R.string.progress_title),
				getString(R.string.progress_form_msg), true);
		pst.execute(query);
		btnRoute = findViewById(R.id.btnFindRoute);
		btnRoute.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(btnRoute)){
			Cursor crs = (Cursor)spnStnA.getSelectedItem();
			int idA = crs.getInt(idCol);
			crs = (Cursor)spnStnB.getSelectedItem();
			int idB = crs.getInt(idCol);
			if(idA == idB){
				Toast.makeText(this, R.string.go_nowhere_msg, Toast.LENGTH_LONG).show();
				return;
			}
			Intent routeIntent = new Intent(this, TripRouteActivity.class);
			routeIntent.putExtra(EXTRA_STNA_ID, idA);
			routeIntent.putExtra(EXTRA_STNB_ID, idB);
			startActivity(routeIntent);
			return;
		}
	}
}