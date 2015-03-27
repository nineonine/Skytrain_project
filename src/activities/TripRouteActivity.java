package activities;

import java.util.ArrayList;

import com.douglas.skytrainproject.R;






import model.SkytrainOpenHelper;
import model.QueryAsyncTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class TripRouteActivity extends Activity {
	
	public static final int WATERFRONT_ID = 0;
	public static final int BROADWAY_ID = 5;
	public static final int COLUMBIA_ID = 15;
	public static final int LOUGHEED_ID = 22;
	public static final int BRIDGEPORT_ID = 40;
	
	private static final String lineQuery = "SELECT * FROM " + SkytrainOpenHelper.INDEX_TBL_NAME + " WHERE " + SkytrainOpenHelper.INDEX_STN_COL +
			" IN ( CAST( ? AS INTEGER), CAST( ? AS INTEGER) )";
	
	private int idStnA;
	private int idStnB;
	private int legCount;
	private int legsDone;
	private ProgressDialog pdlg;
	private ViewGroup fragtainer;
	private SkytrainOpenHelper dbHelp;
	
	private class LineQueryTask extends QueryAsyncTask{
		
		LineQueryTask(){
			super(TripRouteActivity.this);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			StationLookupTask slt = new StationLookupTask();
			slt.execute(result);
		}
	}
	
	private class StationLookupTask extends AsyncTask<Cursor, Void, int[]>{

		//The return value is of the form {stnID(, lineID, stnID)+}
		@Override
		protected int[] doInBackground(Cursor... params) {
			Cursor in = params[0];
			ArrayList<Integer> stnALines = new ArrayList<Integer>();
			ArrayList<Integer> stnAPosns = new ArrayList<Integer>();
			ArrayList<Integer> stnBLines = new ArrayList<Integer>();
			ArrayList<Integer> stnBPosns = new ArrayList<Integer>();
			int lineCol = in.getColumnIndexOrThrow(SkytrainOpenHelper.INDEX_LINE_COL);
			int posCol = in.getColumnIndexOrThrow(SkytrainOpenHelper.INDEX_POS_COL);
			int stnCol = in.getColumnIndexOrThrow(SkytrainOpenHelper.INDEX_STN_COL);
			in.moveToFirst();
			while(!in.isAfterLast()){
				int rowStn = in.getInt(stnCol);
				if(rowStn == idStnA){
					stnALines.add(in.getInt(lineCol));
					stnAPosns.add(in.getInt(posCol));
				}else{
					stnBLines.add(in.getInt(lineCol));
					stnBPosns.add(in.getInt(posCol));
				}
				in.moveToNext();
			}
			int lineCountA = stnAPosns.size();
			int lineCountB = stnBPosns.size();
			if(lineCountA == 0 || lineCountB == 0)throw new IllegalArgumentException("Useless Cursor!");
			//if both lineCountA and lineCountB == 1, there are 8 possible general routes.
			if(lineCountA == 1 && lineCountB == 1){
				int lineA = stnALines.get(0);
				int lineB = stnBLines.get(0);
				
				//if A and B are on the same line:
				if(lineA == lineB){
					legCount = 1;
					return new int[]{idStnA, lineA, idStnB};
				}
				
				//if A is in Surrey and B is on the Millennium Line, or vice versa:
				if((lineA == 0 && lineB == 1)||(lineA == 1 && lineB == 0)){
					legCount = 2;
					return new int[]{idStnA, lineA, COLUMBIA_ID, lineB, idStnB};
				}
				
				//if A is on the Evergreen Line and B is on the Millennium Line, or vice versa:
				if((lineA == 4 && lineB == 1)||(lineA == 1 && lineB == 4)){
					legCount = 2;
					return new int[]{idStnA, lineA, LOUGHEED_ID, lineB, idStnB};
				}
				
				//if A is in Surrey and B is on the Evergreen Line:
				if(lineA == 0 && lineB == 4){
					legCount = 3;
					return new int[]{idStnA, 0, COLUMBIA_ID, 1, LOUGHEED_ID, 4, idStnB};
				}
				//or vice versa:
				if(lineA == 4 && lineB == 0){
					legCount = 3;
					return new int[]{idStnA, 4, LOUGHEED_ID, 1, COLUMBIA_ID, 0, idStnB};
				}
				
				//if A is on Sea Island and B is elsewhere in Richmond, or vice versa:
				if((lineA == 2 && lineB == 3)||(lineA == 3 && lineB == 2)){
					legCount = 2;
					return new int[]{idStnA, lineA, BRIDGEPORT_ID, lineB, idStnB};
				}
				
				//if A is in Richmond and B is in Surrey, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 0)||(lineA == 0 && (lineB == 2 || lineB == 3))){
					legCount = 2;
					return new int[]{idStnA, lineA, WATERFRONT_ID, lineB, idStnB};
				}
				
				//if A is in Richmond and B is on the Millennium Line, or vice versa:
				if((lineA == 2 || lineA == 3) && lineB == 1){
					legCount = 3;
					return new int[]{idStnA, lineA, WATERFRONT_ID, 0, BROADWAY_ID, lineB, idStnB};
				}
				if(lineA == 1 && (lineB == 2 || lineB == 3)){
					legCount = 3;
					return new int[]{idStnA, lineA, BROADWAY_ID, 0, WATERFRONT_ID, lineB, idStnB};
				}
				
				//if A is in Richmond and B is on the Evergreen Line, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 4)){
					legCount = 4;
					return new int[]{idStnA, lineA, WATERFRONT_ID, 0, BROADWAY_ID, 1, LOUGHEED_ID, lineB, idStnB};
				}
				if(lineA == 4 && (lineB == 2 || lineB == 3)){
					legCount = 4;
					return new int[]{idStnA, lineA, LOUGHEED_ID, 1, BROADWAY_ID, 0, WATERFRONT_ID, lineB, idStnB};
				}
			}
			return null;
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelp = new SkytrainOpenHelper(this);
		Intent in = getIntent();
		idStnA = in.getIntExtra(TripFormActivity.EXTRA_STNA_ID, -1);
		idStnB = in.getIntExtra(TripFormActivity.EXTRA_STNB_ID, -1);
		if(idStnA == -1 || idStnB == -1){
			//TODO gracefully fail
		}
		pdlg = new ProgressDialog(this);
		pdlg.setTitle(R.string.progress_title);
		pdlg.setMessage(getText(R.string.progress_route_msg));
		setContentView(R.layout.activity_trip_route);
		pdlg.show();
		fragtainer = (ViewGroup)findViewById(R.id.container);
		LineQueryTask lqt = new LineQueryTask();
		lqt.execute(lineQuery, String.valueOf(idStnA), String.valueOf(idStnB));
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
}
