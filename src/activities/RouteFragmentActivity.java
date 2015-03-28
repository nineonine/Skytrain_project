package activities;

import com.douglas.skytrainproject.R;

import model.QueryAsyncTask;
import model.SkytrainOpenHelper;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RouteFragmentActivity extends Activity {
	
	public static final int WATERFRONT_ID = 0;
	public static final int BROADWAY_ID = 5;
	public static final int COLUMBIA_ID = 15;
	public static final int LOUGHEED_ID = 22;
	public static final int BRIDGEPORT_ID = 40;
	
	private static final String lineQuery = "SELECT * FROM " + SkytrainOpenHelper.INDEX_TBL_NAME + " WHERE " + SkytrainOpenHelper.INDEX_STN_COL +
			" IN ( CAST( ? AS INTEGER), CAST( ? AS INTEGER) )";
	private static final String[] legTags = {"leg1", "leg2", "leg3", "leg4"};

	private FragmentManager fragman;
	private int idA;
	private int idB;
	private int legCount;
	private int legsDone;
//	private int fareZone; //1-3 standard fare of that many zones; 4=Sea Island to Richmond ($5 + 1 zone); 5=Sea Island to elsewhere ($5 + 2 zones)
	private ProgressDialog pdlg;
	private int[] xferIds;
	private SkytrainOpenHelper dbHelp;
	
	private class LineQueryTask extends QueryAsyncTask{
		
		LineQueryTask(){
			super(RouteFragmentActivity.this);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			TransferQueryTask tqt = new TransferQueryTask();
			tqt.execute(result);
		}
	}
	
	private class TransferQueryTask extends AsyncTask<Cursor, Void, Cursor>{
		
		public TransferQueryTask() {
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
					xferIds = new int[] {idA,COLUMBIA_ID,idB};
					return null;
				}
				
				//if stnA is on Millennium and stnB is on Evergreen or vice versa:
				if((lineA == 1 && lineB == 4)||(lineB == 1 && lineA == 4)){
					legCount = 2;
					xferIds = new int[] {idA,LOUGHEED_ID,idB};
					return null;
				}
				
				//if stnA is in Surrey and stnB is on Evergreen or vice versa:
				if(lineA == 0 && lineB == 4){
					legCount = 3;
					xferIds = new int[]{idA,COLUMBIA_ID,LOUGHEED_ID,idB};
					return null;
				}
				if(lineB == 0 && lineA == 4){
					legCount = 3;
					xferIds = new int[]{idA,LOUGHEED_ID,COLUMBIA_ID,idB};
					return null;
				}
				
				//if stnA is on Sea Island and stnB is elsewhere in Richmond, or vice versa:
				if((lineA == 2 && lineB == 3)||(lineB == 2 && lineA == 3)){
					legCount = 2;
					xferIds = new int[]{idA,BRIDGEPORT_ID,idB};
					return null;
				}
				
				//if stnA is in Richmond and stnB is in Surrey, or vice versa:
				if(((lineA == 2 || lineA == 3) && lineB == 0)||((lineB == 2 || lineB == 3) && lineA == 0)){
					legCount = 2;
					xferIds = new int[]{idA,WATERFRONT_ID,idB};
					return null;
				}
				
				//if stnA is in Richmond and stnB is on the Millennium Line, or vice versa:
				if((lineA == 2 || lineA == 3) && lineB == 1){
					legCount = 3;
					xferIds = new int[]{idA,WATERFRONT_ID,BROADWAY_ID,idB};
					return null;
				}
				if((lineB == 2 || lineB == 3) && lineA == 1){
					legCount = 3;
					xferIds = new int[]{idA,BROADWAY_ID,WATERFRONT_ID,idB};
					return null;
				}
				
				//if stnA is in Richmond and stnB is on the Evergreen Line, or vice versa:
				if((lineA == 2 || lineA == 3) && lineB == 4){
					legCount = 4;
					xferIds = new int[]{idA,WATERFRONT_ID,BROADWAY_ID,LOUGHEED_ID,idB};
					return null;
				}
				if((lineB == 2 || lineB == 3) && lineA == 4){
					legCount = 4;
					xferIds = new int[]{idA,LOUGHEED_ID,BROADWAY_ID,WATERFRONT_ID,idB};
					return null;
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
								xferIds = new int[]{idA,BROADWAY_ID,idB};
								return null;
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
					xferIds = new int[]{idA,WATERFRONT_ID,idB};
					return null;
				}
				
				//if stnB is on the Millennium Line:
				if(stnBLines.contains(1)){
					legCount = 3;
					xferIds = new int[]{idA,WATERFRONT_ID,BROADWAY_ID,idB};
					return null;
				}
				
				//if stnB is on the Evergreen Line:
				if(stnBLines.contains(4)){
					legCount = 4;
					xferIds = new int[]{idA,WATERFRONT_ID,BROADWAY_ID,LOUGHEED_ID,idB};
					return null;
				}
			}
			
			//if stnB is on the Canada Line:
			if(stnBLines.contains(2)||stnBLines.contains(3)){
				
				//if stnA is on the Expo Line:
				if(stnALines.contains(0)){
					legCount = 2;
					xferIds = new int[]{idA,WATERFRONT_ID,idB};
					return null;
				}
				
				//if stnA is on the Millennium Line:
				if(stnALines.contains(1)){
					legCount = 3;
					xferIds = new int[]{idA,BROADWAY_ID,WATERFRONT_ID,idB};
					return null;
				}
				
				//if stnA is on the Evergreen Line:
				if(stnALines.contains(4)){
					legCount = 4;
					xferIds = new int[]{idA,LOUGHEED_ID,BROADWAY_ID,WATERFRONT_ID,idB};
					return null;
				}
			}
			
			//The only possible way to reach here is if one station is on the Evergreen
			//Line and the other is Expo/Millennium.
			
			//if stnA is Expo/Millennium:
			if(stnALines.contains(0)){
				
				//if stnA is near or past Commercial-Broadway:
				if(idA < 8){
					legCount = 3;
					xferIds = new int[]{idA,BROADWAY_ID,LOUGHEED_ID,idB};
					return null;
				}
				
				//otherwise:
				legCount = 2;
				xferIds = new int[]{idA,LOUGHEED_ID,idB};
				return null;
			}
			
			if(idB < 8){
				legCount = 3;
				xferIds = new int[]{idA,LOUGHEED_ID,BROADWAY_ID,idB};
				return null;
			}
			
			legCount = 2;
			xferIds = new int[]{idA,LOUGHEED_ID,idB};
			return null;
		}
		
		protected void onPostExecute(Cursor result){
			if(legCount == 1){
				fragman.beginTransaction().add(R.id.container, new TripLegFragment(result)).commit();
				return;
			}
			TripLegFragment[] legs = new TripLegFragment[legCount];
			int xferId = xferIds[1];
			int line = dbHelp.findLineFromTransfer(xferId, idA);
			legs[0] = new TripLegFragment(line, idA, xferId);
			if(legCount == 2){
				line = dbHelp.findLineFromTransfer(xferId, idB);
				legs[1] = new TripLegFragment(line, xferId, idB);
				FragmentTransaction fragtxn = fragman.beginTransaction();
				for(int i = 0;i < legCount;++i){
					fragtxn.add(R.id.container, legs[i], legTags[i]);
				}
				fragtxn.commit();
				return;
			}
			int xferYd = xferIds[2];
			line = dbHelp.findLineFromTransfer(xferId, xferYd);
			legs[1] = new TripLegFragment(line, xferId, xferYd);
			if(legCount == 3){
				line = dbHelp.findLineFromTransfer(xferYd, idB);
				legs[2] = new TripLegFragment(line, xferYd, idB);
				FragmentTransaction fragtxn = fragman.beginTransaction();
				for(int i = 0;i < legCount;++i){
					fragtxn.add(R.id.container, legs[i], legTags[i]);
				}
				fragtxn.commit();
				return;
			}
			xferId = xferIds[3];
			line = dbHelp.findLineFromTransfer(xferYd, xferId);
			legs[2] = new TripLegFragment(line, xferYd, xferId);
			//we'll never get more than four legs in a single trip.
			line = dbHelp.findLineFromTransfer(xferId, idB);
			legs[3] = new TripLegFragment(line, xferId, idB);
			FragmentTransaction fragtxn = fragman.beginTransaction();
			for(int i = 0;i < legCount;++i){
				fragtxn.add(R.id.container, legs[i], legTags[i]);
			}
			fragtxn.commit();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbHelp = new SkytrainOpenHelper(this);
		idA = getIntent().getIntExtra(TripFormActivity.EXTRA_STNA_ID, -1);
		idB = getIntent().getIntExtra(TripFormActivity.EXTRA_STNB_ID, -1);
		legsDone = 0;
		if(idA == -1 || idB == -1){
			// TODO handle this in some graceful manner, even though it shouldn't ever happen.
		}
		
		super.onCreate(savedInstanceState);
		fragman = getFragmentManager();
		LineQueryTask lqt = new LineQueryTask();
		pdlg = new ProgressDialog(this);
		pdlg.setTitle(R.string.progress_title);
		pdlg.setMessage(getResources().getText(R.string.progress_route_msg));
		
		setContentView(R.layout.activity_trip_fragment);
		pdlg.show();
		lqt.execute(lineQuery, String.valueOf(idA), String.valueOf(idB));
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
	public class TripLegFragment extends ListFragment {

		private boolean skipQuery;
		private Cursor data;
		private int line, stnA, stnB;
		private TextView boardingInstr;
		
		final String[] from = {SkytrainOpenHelper.STN_NAME_COL};
		final int[] to = {android.R.id.text1};
		
		private class LegQueryTask extends AsyncTask<Void, Void, Cursor>{

			@Override
			protected Cursor doInBackground(Void... params) {
				return dbHelp.queryLineByStationId(line, stnA, stnB);
			}

			@Override
			protected void onPostExecute(Cursor result) {
				data = result;
				setBoardingInstr();
				SimpleCursorAdapter sca = new SimpleCursorAdapter(RouteFragmentActivity.this, android.R.layout.activity_list_item, result, from, to, 0);
				setListAdapter(sca);
				legsDone++;
				if(legsDone == legCount) pdlg.dismiss();
			}
			
		}
		
		public TripLegFragment(int line, int stnA, int stnB) {
			this.line = line;
			this.stnA = stnA;
			this.stnB = stnB;
			skipQuery = false;
		}
		
		public TripLegFragment(Cursor data){
			this.data = data;
			skipQuery = true;
		}
		
		private void setBoardingInstr(){
			int lineCX = data.getColumnIndex(SkytrainOpenHelper.INDEX_LINE_COL);
			int posCX = data.getColumnIndex(SkytrainOpenHelper.INDEX_POS_COL);
			data.moveToFirst();
			int myline = data.getInt(lineCX);
			int posbegin = data.getInt(posCX);
			data.moveToLast();
			int posfinal = data.getInt(posCX);
			String lineName;
			String terminus;
			if(posbegin < posfinal){
				switch(myline){
				case 0:
					if(posfinal < 16){
						lineName = "Expo Line to King George or Millennium";
						terminus = "VCC-Clark";
					}else{
						lineName = "Expo";
						terminus = "King George";
					}
					break;
				case 1:
					if(posfinal < 16){
						lineName = "Expo Line to King George or Millennium";
						terminus = "VCC-Clark";
					}else{
						lineName = "Millennium";
						terminus = "VCC-Clark";
					}
					break;
				case 2:
					lineName = "Canada";
					if(posfinal < 10){
						terminus = "YVR-Airport or Richmond-Brighouse";
					}else{
						terminus = "YVR-Airport";
					}
					break;
				case 3:
					lineName = "Canada";
					if(posfinal < 10){
						terminus = "YVR-Airport or Richmond-Brighouse";
					}else{
						terminus = "Richmond-Brighouse";
					}
					break;
				case 4:
					lineName = "Evergreen";
					terminus = "Lafarge Lake-Douglas";
					break;
				default:
					lineName = "Most Convenient";
					terminus = "your destination";
					break;
				}
			}else{
				switch(myline){
				case 0:
					if(posbegin < 16){
						lineName = "Expo or Millennium";
					}else{
						lineName = "Expo";
					}
					terminus = "Waterfront";
					break;
				case 1:
					if(posbegin < 16){
						lineName = "Expo or Millennium";
					}else{
						lineName = "Millennium";
					}
					terminus = "Waterfront";
					break;
				case 2:
				case 3:
					lineName = "Canada";
					terminus = "Waterfront";
					break;
				case 4:
					lineName = "Evergreen";
					terminus = "Lougheed Town Centre";
					break;
				default:
					lineName = "Most Convenient";
					terminus = "your destination";
					break;
				}
			}
			boardingInstr.setText(getResources().getString(R.string.boarding_instr_msg, lineName, terminus));
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View rootView = inflater.inflate(R.layout.fragment_trip_route, container, false);
			boardingInstr = (TextView)rootView.findViewById(R.id.legBeginTxt);
			if(skipQuery){
				setBoardingInstr();
				SimpleCursorAdapter sca = new SimpleCursorAdapter(RouteFragmentActivity.this, android.R.layout.activity_list_item, data, from, to, 0);
				setListAdapter(sca);
				pdlg.dismiss();
			}else{
				LegQueryTask lqt = new LegQueryTask();
				lqt.execute();
			}
			return rootView;
		}
	}
}
