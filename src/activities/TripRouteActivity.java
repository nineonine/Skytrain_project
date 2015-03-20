package activities;

import com.douglas.skytrainproject.R;

import model.QueryAsyncTask;
import model.SkytrainOpenHelper;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class TripRouteActivity extends Activity {
	
	private static final String lineQuery = "SELECT " + SkytrainOpenHelper.INDEX_LINE_COL + ", " + SkytrainOpenHelper.INDEX_STN_COL +
			", " + SkytrainOpenHelper.INDEX_POS_COL + " FROM " + SkytrainOpenHelper.INDEX_TBL_NAME + " WHERE " + SkytrainOpenHelper.INDEX_STN_COL +
			" IS IN { CAST( ? AS INTEGER), CAST( ? AS INTEGER) }";

	private FragmentManager fragman;
	private int idA;
	private int idB;
	private int legCount;
	private int legsDone;
	
	private class LineQueryTask extends QueryAsyncTask{
		
		LineQueryTask(){
			super(TripRouteActivity.this);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			TransferQueryTask tqt = new TransferQueryTask();
			tqt.execute(result);
		}
	}
	
	private class TransferQueryTask extends AsyncTask<Cursor, Void, Cursor>{
		
		private SkytrainOpenHelper dbHelp;
		
		public TransferQueryTask() {
			dbHelp = new SkytrainOpenHelper(TripRouteActivity.this);
		}

		@Override
		protected Cursor doInBackground(Cursor... params) {
			ArrayList<Integer> stnALines = new ArrayList<Integer>();
			ArrayList<Integer> stnAPosns = new ArrayList<Integer>();
			ArrayList<Integer> stnBLines = new ArrayList<Integer>();
			ArrayList<Integer> stnBPosns = new ArrayList<Integer>();
			Cursor in = params[0];
			int stnCol = in.getColumnIndex(SkytrainOpenHelper.INDEX_STN_COL);
			int lineCol = in.getColumnIndex(SkytrainOpenHelper.INDEX_LINE_COL);
			int posCol = in.getColumnIndex(SkytrainOpenHelper.INDEX_POS_COL);
			in.moveToFirst();
			do{
				int stnId = in.getInt(stnCol);
				int lineId = in.getInt(lineCol);
				int positn = in.getInt(posCol);
				if(stnId == idA){
					stnALines.add(lineId);
					stnAPosns.add(positn);
				}else{
					stnBLines.add(lineId);
					stnBPosns.add(positn);
				}
				in.moveToNext();
			}while(!in.isAfterLast());
			in.close();
			for(int i = 0;i<stnALines.size();++i){
				int lineA = stnALines.get(i);
				//int posA = stnAPosns.get(i);
				for(int j = 0;j<stnBLines.size();++j){
					int lineB = stnBLines.get(j);
					if(lineA == lineB){
						int posA = stnAPosns.get(i);
						int posB = stnBPosns.get(j);
						if(lineA == 1){
							//The way the Millennium Line loops makes it annoying to deal with.
						}else{
							legCount = 1;
							return dbHelp.queryStationsOfLine(lineB, posA, posB);
						}
					}
				}
			}
			return null;
		}
		
		protected void onPostExecute(Cursor result){
			
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		idA = getIntent().getIntExtra(TripFormActivity.EXTRA_STNA_ID, -1);
		idB = getIntent().getIntExtra(TripFormActivity.EXTRA_STNB_ID, -1);
		if(idA == -1 || idB == -1){
			// TODO handle this in some graceful manner, even though it shouldn't ever happen.
		}
		super.onCreate(savedInstanceState);
		fragman = getFragmentManager();
		LineQueryTask lqt = new LineQueryTask();
		setContentView(R.layout.activity_trip_route);
		lqt.execute(lineQuery);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip_route, menu);
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

	/**
	 * A fragment that calculates and displays a single leg of a trip.
	 */
	public class TripLegFragment extends Fragment {

		public TripLegFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_trip_route,
					container, false);
			return rootView;
		}
	}
}
