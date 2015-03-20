package activities;

import com.douglas.skytrainproject.R;

import model.QueryAsyncTask;
import model.SkytrainOpenHelper;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
	
	public static final int WATERFRONT_ID = 0;
	public static final int BROADWAY_ID = 5;
	public static final int COLUMBIA_ID = 15;
	public static final int LOUGHEED_ID = 22;
	public static final int BRIDGEPORT_ID = 40;
	
	private static final String lineQuery = "SELECT " + SkytrainOpenHelper.INDEX_LINE_COL + ", " + SkytrainOpenHelper.INDEX_STN_COL +
			", " + SkytrainOpenHelper.INDEX_POS_COL + " FROM " + SkytrainOpenHelper.INDEX_TBL_NAME + " WHERE " + SkytrainOpenHelper.INDEX_STN_COL +
			" IS IN { CAST( ? AS INTEGER), CAST( ? AS INTEGER) }";

	private FragmentManager fragman;
	private int idA;
	private int idB;
	private int legCount;
	private int legsDone;
	private ProgressDialog pdlg;
	
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
		
		private final String xferBase = "SELECT " + SkytrainOpenHelper.STN_ID_COL + " , " +
				SkytrainOpenHelper.STN_NAME_COL + " , " + SkytrainOpenHelper.STN_ZONE_COL +
				" FROM " + SkytrainOpenHelper.STN_TBL_NAME + " WHERE " +
				SkytrainOpenHelper.STN_ID_COL + " IN ( ";
		
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
			int lineCountA = stnALines.size();
			int lineCountB = stnBLines.size();
			
			//if both lineCountA and lineCountB == 1, there are 8 possible general routes.
			if(lineCountA == 1 && lineCountB == 1){
				int lineA = stnALines.get(0);
				int lineB = stnBLines.get(0);
				int posA = stnAPosns.get(0);
				int posB = stnBPosns.get(0);
				
				//if stnA and stnB are on the same line, it's simple:
				if(lineA == lineB){
					legCount = 1;
					return dbHelp.queryStationsOfLine(lineA, posA, posB);
				}
				
				//if stnA is on the Millennium Line and stnB is in Surrey, or vice versa:
				if((lineA == 0 && lineB == 1)||(lineB == 0 && lineA == 1)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + COLUMBIA_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is on Millennium and stnB is on Evergreen or vice versa:
				if((lineA == 1 && lineB == 4)||(lineB == 1 && lineA == 4)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is in Surrey and stnB is on Evergreen or vice versa:
				if((lineA == 0 && lineB == 4)||(lineB == 0 && lineA == 4)){
					legCount = 3;
					String xferQuery = xferBase + idA + " , " + COLUMBIA_ID + " , " + LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is on Sea Island and stnB is elsewhere in Richmond, or vice versa:
				if((lineA == 2 && lineB == 3)||(lineB == 2 && lineA == 3)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + BRIDGEPORT_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is in Richmond and stnB is in Surrey, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 0)||((lineB == 2 || lineB == 3) && lineA == 0)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is in Richmond and stnB is on the Millennium Line, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 1)||((lineB == 2 || lineB == 3) && lineA == 1)){
					legCount = 3;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is in Richmond and stnB is on the Evergreen Line, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 4)||((lineB == 2 || lineB == 3) && lineA == 4)){
					legCount = 4;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " +
											LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
			}
			
			//this nested pair of loops finds any line which has both stations on it.
			for(int i = 0;i < lineCountA;++i){
				int lineA = stnALines.get(i);
				//int posA = stnAPosns.get(i);
				for(int j = 0;j < lineCountB;++j){
					int lineB = stnBLines.get(j);
					if(lineA == lineB){
						int posA = stnAPosns.get(i);
						int posB = stnBPosns.get(j);
						if(lineA == 1){
							//The way the Millennium Line loops makes it annoying to deal with.
							if(Math.abs(posA - posB) < 16){
								//slightly favour riding around the loop over transferring
								legCount = 1;
								return dbHelp.queryStationsOfLine(lineA, posA, posB);
							}else{
								legCount = 2;
								String xferQuery = xferBase + idA + " , " + BROADWAY_ID + " , " + idB + " )";
								return dbHelp.rawQuery(xferQuery, null);
							}
						}else{
							legCount = 1;
							return dbHelp.queryStationsOfLine(lineB, posA, posB);
						}
					}
				}
			}
			//If the code reaches here, stnALines and stnBLines are completely disjoint,
			//and at least one of them has more than one element.
			
			//if stnA is on the Canada Line:
			if(stnALines.contains(2)||stnALines.contains(3)){
				
				//if stnB is on the Expo Line:
				if(stnBLines.contains(0)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnB is on the Millennium Line:
				if(stnBLines.contains(1)){
					legCount = 3;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnB is on the Evergreen Line:
				if(stnBLines.contains(4)){
					legCount = 4;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " + LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
			}
			
			//if stnB is on the Canada Line:
			if(stnBLines.contains(2)||stnBLines.contains(3)){
				
				//if stnA is on the Expo Line:
				if(stnALines.contains(0)){
					legCount = 2;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is on the Millennium Line:
				if(stnALines.contains(1)){
					legCount = 3;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//if stnA is on the Evergreen Line:
				if(stnALines.contains(4)){
					legCount = 4;
					String xferQuery = xferBase + idA + " , " + WATERFRONT_ID + " , " + BROADWAY_ID + " , " + LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
			}
			
			//The only possible way to reach here is if one station is on the Evergreen
			//Line and the other is Expo/Millennium.
			
			//if stnA is Expo/Millennium:
			if(stnALines.contains(0)){
				
				//if stnA is near or past Commercial-Broadway:
				if(idA < 7){
					legCount = 3;
					String xferQuery = xferBase + idA + " , " + BROADWAY_ID + " , " + LOUGHEED_ID + " , " + idB + " )";
					return dbHelp.rawQuery(xferQuery, null);
				}
				
				//otherwise:
				legCount = 2;
				String xferQuery = xferBase + idA + " , " + LOUGHEED_ID + " , " + idB + " )";
				return dbHelp.rawQuery(xferQuery, null);
			}
			
			if(idB < 7){
				legCount = 3;
				String xferQuery = xferBase + idA + " , " + LOUGHEED_ID + " , " + BROADWAY_ID + " , " + idB + " )";
				return dbHelp.rawQuery(xferQuery, null);
			}
			
			legCount = 2;
			String xferQuery = xferBase + idA + " , " + LOUGHEED_ID + " , " + idB + " )";
			return dbHelp.rawQuery(xferQuery, null);
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
	 * A fragment that queries and displays a single leg of a trip.
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
