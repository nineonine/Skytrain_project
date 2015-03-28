package activities;

import java.util.ArrayList;

import com.douglas.skytrainproject.R;








import model.SkytrainOpenHelper;
import model.QueryAsyncTask;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TripRouteActivity extends Activity {
	
	public static final int WATERFRONT_ID = 0;
	public static final int BROADWAY_ID = 5;
	public static final int COLUMBIA_ID = 15;
	public static final int LOUGHEED_ID = 22;
	public static final int BRIDGEPORT_ID = 40;
	
	public enum Zone{
		ONE, TWO, THREE
	}
	
	private static final String lineQuery = "SELECT * FROM " + SkytrainOpenHelper.INDEX_TBL_NAME + " WHERE " + SkytrainOpenHelper.INDEX_STN_COL +
			" IN ( CAST( ? AS INTEGER), CAST( ? AS INTEGER) )";
	
	private int idStnA;
	private int idStnB;
	private int posStnA;
	private int posStnB;
	private int legCount;
	private int legsDone;
	private int tripTime;
	private boolean unsetTime;
	private boolean crosses1_2, crosses2_3;
	private Zone startZone;
	private ProgressDialog pdlg;
	private FragmentManager fragman;
	private SkytrainOpenHelper dbHelp;
	private Resources res;
	
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
				posStnA = stnAPosns.get(0);
				posStnB = stnBPosns.get(0);
				
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
			
			//This loops through the indices, returning if there's a match between lines
			//for A and B:
			for(int iA = 0;iA < lineCountA;++iA){
				int lineA = stnALines.get(iA);
				for(int iB = 0;iB < lineCountB;++iB){
					int lineB = stnBLines.get(iB);
					if(lineA == lineB){
						int posA = stnAPosns.get(iA);
						int posB = stnBPosns.get(iB);
						posStnA = posA;
						posStnB = posB;
						//the Millennium loop makes things awkward.
						if(lineA == 1){
							if(posA == 5 && posB > 16){
								legCount = 1;
								posStnA = 27;
								return new int[]{idStnA, 1, idStnB};
							}else if(posB == 5 && posA > 16){
								legCount = 1;
								posStnB = 27;
								return new int[]{idStnA, 1, idStnB};
							}else if((posA < 5 && posB > 16)){
								legCount = 2;
								return new int[]{idStnA, 0, BROADWAY_ID, 1, idStnB};
							}else if(posB < 5 && posA > 16){
								legCount = 2;
								return new int[]{idStnA, 1, BROADWAY_ID, 0, idStnB};
							}else if(Math.abs(posB - posA) < 14){
								legCount = 1;
								return new int[]{idStnA, 1, idStnB};
							}else{
								legCount = 2;
								return new int[]{idStnA, 1, BROADWAY_ID, 1, idStnB};
							}
						}
						legCount = 1;
						return new int[]{idStnA, lineA, idStnB};
					}
				}
			}
			//if the program reaches here, the two sets of lines are completely disjoint,
			//and at least one of them has more than one member.
			
			//if A is on the Airport Canada Line:
			if(stnALines.contains(2)){
				posStnA = stnAPosns.get(stnALines.indexOf(2));
				//if B is on the Expo Line:
				if(stnBLines.contains(0)){
					legCount = 2;
					posStnB = stnBPosns.get(stnBPosns.indexOf(0));
					return new int[]{idStnA, 2, WATERFRONT_ID, 0, idStnB};
				}
				//if B is on the Millennium Line:
				if(stnBLines.contains(1)){
					legCount = 3;
					posStnB = stnBPosns.get(stnBPosns.indexOf(1));
					return new int[]{idStnA, 2, WATERFRONT_ID, 0, BROADWAY_ID, 1, idStnB};
				}
				//if B is on the Evergreen Line:
				if(stnBLines.contains(4)){
					legCount = 4;
					posStnB = stnBPosns.get(stnBPosns.indexOf(4));
					return new int[]{idStnA, 2, WATERFRONT_ID, 0, BROADWAY_ID, 1, LOUGHEED_ID, 4, idStnB};
				}
			}
			//if A is on the Brighouse Canada Line:
			if(stnALines.contains(3)){
				posStnA = stnAPosns.get(stnALines.indexOf(3));
				//if B is on the Expo Line:
				if(stnBLines.contains(0)){
					legCount = 2;
					posStnB = stnBPosns.get(stnBPosns.indexOf(0));
					return new int[]{idStnA, 3, WATERFRONT_ID, 0, idStnB};
				}
				//if B is on the Millennium Line:
				if(stnBLines.contains(1)){
					legCount = 3;
					posStnB = stnBPosns.get(stnBPosns.indexOf(1));
					return new int[]{idStnA, 3, WATERFRONT_ID, 0, BROADWAY_ID, 1, idStnB};
				}
				//if B is on the Evergreen Line:
				if(stnBLines.contains(4)){
					legCount = 4;
					posStnB = stnBPosns.get(stnBPosns.indexOf(4));
					return new int[]{idStnA, 3, WATERFRONT_ID, 0, BROADWAY_ID, 1, LOUGHEED_ID, 4, idStnB};
				}
			}
			//if B is on the Airport Canada Line:
			if(stnBLines.contains(2)){
				posStnB = stnBPosns.get(stnBLines.indexOf(2));
				//if A is on the Expo Line:
				if(stnALines.contains(0)){
					legCount = 2;
					posStnA = stnAPosns.get(stnAPosns.indexOf(0));
					return new int[]{idStnA, 0, WATERFRONT_ID, 2, idStnB};
				}
				//if A is on the Millennium Line:
				if(stnALines.contains(1)){
					legCount = 3;
					posStnA = stnAPosns.get(stnAPosns.indexOf(1));
					return new int[]{idStnA, 1, BROADWAY_ID, 0, WATERFRONT_ID, 2, idStnB};
				}
				//if A is on the Evergreen Line:
				if(stnALines.contains(4)){
					legCount = 4;
					posStnA = stnAPosns.get(stnAPosns.indexOf(4));
					return new int[]{idStnA, 4, LOUGHEED_ID, 1, BROADWAY_ID, 0, WATERFRONT_ID, 2, idStnB};
				}
			}
			//if B is on the Brighouse Canada Line:
			if(stnBLines.contains(3)){
				posStnB = stnBPosns.get(stnBLines.indexOf(3));
				//if A is on the Expo Line:
				if(stnALines.contains(0)){
					legCount = 2;
					posStnA = stnAPosns.get(stnAPosns.indexOf(0));
					return new int[]{idStnA, 0, WATERFRONT_ID, 3, idStnB};
				}
				//if A is on the Millennium Line:
				if(stnALines.contains(1)){
					legCount = 3;
					posStnA = stnAPosns.get(stnAPosns.indexOf(1));
					return new int[]{idStnA, 1, BROADWAY_ID, 0, WATERFRONT_ID, 3, idStnB};
				}
				//if A is on the Evergreen Line:
				if(stnALines.contains(4)){
					legCount = 4;
					posStnA = stnAPosns.get(stnAPosns.indexOf(4));
					return new int[]{idStnA, 4, LOUGHEED_ID, 1, BROADWAY_ID, 0, WATERFRONT_ID, 3, idStnB};
				}
			}
			//SPECIAL CASE: Lougheed Town Centre is on two lines.
			if((idStnA == LOUGHEED_ID && stnBLines.contains(0))){
				posStnA = stnAPosns.get(stnAPosns.indexOf(1));
				posStnB = stnBPosns.get(stnBPosns.indexOf(0));
				legCount = 2;
				return new int[]{LOUGHEED_ID, 1, COLUMBIA_ID, 0, idStnB};
			}
			if(idStnB == LOUGHEED_ID && stnALines.contains(0)){
				posStnB = stnBPosns.get(stnBPosns.indexOf(1));
				posStnA = stnAPosns.get(stnAPosns.indexOf(0));
				legCount = 2;
				return new int[]{idStnA, 0, COLUMBIA_ID, 1, LOUGHEED_ID};
			}
			//the only remaining possibility is that one station is on the Expo/Millennium
			//shared track, and the other is on the Evergreen Line.
			if(stnALines.contains(0)){
				posStnA = stnAPosns.get(stnAPosns.indexOf(0));
				posStnB = stnBPosns.get(stnBPosns.indexOf(4));
				if(posStnA < 5){
					legCount = 3;
					return new int[]{idStnA, 0, BROADWAY_ID, 1, LOUGHEED_ID, 4, idStnB};
				}
				if(posStnA == 5){
					posStnA = 27;
					legCount = 2;
					return new int[]{idStnA, 1, LOUGHEED_ID, 4, idStnB};
				}
				if(Math.abs(22 - posStnA) > 14){
					legCount = 3;
					return new int[]{idStnA, 0, BROADWAY_ID, 1, LOUGHEED_ID, 4, idStnB};
				}
				legCount = 2;
				return new int[]{idStnA, 1, LOUGHEED_ID, 4, idStnB};
			}
			posStnA = stnAPosns.get(stnAPosns.indexOf(4));
			posStnB = stnBPosns.get(stnBPosns.indexOf(0));
			if(posStnB < 5){
				legCount = 3;
				return new int[]{idStnA, 4, LOUGHEED_ID, 1, BROADWAY_ID, 0, idStnB};
			}
			if(posStnB == 5){
				posStnB = 27;
				legCount = 2;
				return new int[]{idStnA, 4, LOUGHEED_ID, 1, idStnB};
			}
			if((22 - posStnB) > 14){
				legCount = 3;
				return new int[]{idStnA, 4, LOUGHEED_ID, 1, BROADWAY_ID, 0, idStnB};
			}
			legCount = 2;
			return new int[]{idStnA, 4, LOUGHEED_ID, 1, idStnB};
		}


		@Override
		protected void onPostExecute(int[] result) {
			FragmentTransaction trx = fragman.beginTransaction();
			for(int i = 1;i < result.length - 1;i += 2){
				switch(result[i]){
				case 0:
					trx.add(R.id.container, new ExpoLegFragment(result[i - 1], result[i + 1]));
					break;
				case 1:
					trx.add(R.id.container, new MillenFragment(result[i - 1], result[i + 1]));
					break;
				case 2:
					trx.add(R.id.container, new AirportFragment(result[i - 1], result[i + 1]));
					break;
				case 3:
					trx.add(R.id.container, new BrighouseFragment(result[i - 1], result[i + 1]));
					break;
				case 4:
					trx.add(R.id.container, new EvergreenFragment(result[i - 1], result[i + 1]));
				}
			}
			trx.commit();
		}
	}
	
	abstract class LegListFragment extends ListFragment{
		
		protected TextView beginText;
		protected boolean outbound;
		private int line, startPos, endPos;
		private String[] fromCols = {SkytrainOpenHelper.STN_NAME_COL};
		private int[] toCols = {android.R.id.text1};
		
		private class LegQueryTask extends AsyncTask<Void, Void, Cursor>{

			@Override
			protected Cursor doInBackground(Void... params) {
				Cursor stnQ = dbHelp.queryStationsOfLine(line, startPos, endPos);
				stnQ.moveToFirst();
				int idA = stnQ.getInt(stnQ.getColumnIndexOrThrow(SkytrainOpenHelper.STN_ID_COL));
				int znA = stnQ.getInt(stnQ.getColumnIndexOrThrow(SkytrainOpenHelper.STN_ZONE_COL));
				if(startZone == null){
					switch(znA){
					case 1:
						startZone = Zone.ONE;
						break;
					case 2:
						startZone = Zone.TWO;
						break;
					case 3:
						startZone = Zone.THREE;
						break;
					default:
						startZone = null;
						break;
					}
				}
				stnQ.moveToLast();
				int idB = stnQ.getInt(stnQ.getColumnIndexOrThrow(SkytrainOpenHelper.STN_ID_COL));
				int znB = stnQ.getInt(stnQ.getColumnIndexOrThrow(SkytrainOpenHelper.STN_ZONE_COL));
				if((znA == 1 && znB == 2)||(znA == 2 && znB == 1))crosses1_2 = true;
				if((znA == 2 && znB == 3)||(znA == 3 && znB == 2))crosses2_3 = true;
				if((znA == 1 && znB == 3)||(znA == 3 && znB == 1)){
					crosses1_2 = true;
					crosses2_3 = true;
				}
				try{
					if(line == 1 && idA < 15 && idB > 15){
						tripTime += dbHelp.queryTravelTime(idA, COLUMBIA_ID);
						tripTime += dbHelp.queryTravelTime(COLUMBIA_ID, idB);
					}else if(line == 1 && idA > 15 && idB < 15){
						tripTime += dbHelp.queryTravelTime(idB, COLUMBIA_ID);
						tripTime += dbHelp.queryTravelTime(COLUMBIA_ID, idA);
					}else{
						int id1 = (idA < idB)?idA:idB;
						int id2 = (idA < idB)?idB:idA;
						tripTime += dbHelp.queryTravelTime(id1, id2);
					}
				}catch(IllegalArgumentException ill){
					unsetTime = true;
				}
				return stnQ;
			}

			@Override
			protected void onPostExecute(Cursor result) {
				SimpleCursorAdapter sca = new SimpleCursorAdapter(TripRouteActivity.this, android.R.layout.simple_list_item_1, result, fromCols, toCols, 0);
				LegListFragment.this.setListAdapter(sca);
				++legsDone;
				LegListFragment.this.setBeginText();
				if(legsDone == legCount){
					TextView txt = (TextView)TripRouteActivity.this.findViewById(R.id.timeTxt);
					String val = unsetTime?"an unknown number of":String.valueOf(tripTime);
					txt.setText(getString(R.string.time_msg, val));
					float fare = 0.0f;
					int zones = 0;
					boolean yvr = false;
					float yvrFare = 0.0f;
					TypedArray fares = res.obtainTypedArray(R.array.fares);
					switch(startZone){
					case ONE:
						if(crosses2_3){
							fare += fares.getFloat(2, 5.5f);
							zones = 3;
							break;
						}
						if(crosses1_2){
							fare += fares.getFloat(1, 4.0f);
							zones = 2;
							break;
						}
						fare += fares.getFloat(0, 2.75f);
						zones = 1;
						break;
					case TWO:
						if(crosses1_2||crosses2_3){
							fare += fares.getFloat(1, 4.0f);
							zones = 2;
						}else{
							fare += fares.getFloat(0, 2.75f);
							zones = 1;
						}
						if(idStnA > BRIDGEPORT_ID && (idStnA - BRIDGEPORT_ID) < 4 && (idStnB > (BRIDGEPORT_ID + 3) || idStnB <= BRIDGEPORT_ID)){
							yvrFare = fares.getFloat(3, 5.0f);
							fare += yvrFare;
							yvr = true;
						}
						break;
					case THREE:
						if(crosses1_2){
							fare += fares.getFloat(2, 5.5f);
							zones = 3;
							break;
						}
						if(crosses2_3){
							fare += fares.getFloat(1, 4.0f);
							zones = 2;
							break;
						}
						fare += fares.getFloat(0, 2.75f);
						zones = 1;
						break;
					}
					fares.recycle();
					txt = (TextView)findViewById(R.id.timeTxt);
					txt.setText(getString(yvr?R.string.yvr_fare_msg:R.string.fare_msg, zones, fare, yvrFare));
					pdlg.dismiss();
				}
			}
			
			
		}
		
		LegListFragment(int line, int idStart, int idEnd){
			this.line = line;
			this.startPos = idStart;
			this.endPos = idEnd;
			outbound = (idStart < idEnd);
			if(idEnd == BROADWAY_ID && (27 - idStart) < 14 && idStart != 27)outbound = true;
		}
		
		protected abstract void setBeginText();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.fragment_trip_route, container, false);
			beginText = (TextView)root.findViewById(R.id.legBeginTxt);
			LegQueryTask lqt = new LegQueryTask();
			lqt.execute();
			return root;
		}
	}

	private class ExpoLegFragment extends LegListFragment{
		
		ExpoLegFragment(int start, int end){
			super(0, start, end);
		}

		@Override
		protected void setBeginText() {
			String line = "Expo";
			String dest = (super.outbound)?"King George":"Waterfront";
			super.beginText.setText(getString(R.string.boarding_instr_msg, line, dest));
		}
	}

	private class MillenFragment extends LegListFragment{
		
		MillenFragment(int start, int end){
			super(1, start, end);
		}
		
		@Override
		protected void setBeginText(){
			String line = "Millennium";
			String dest = (super.outbound)?"VCC-Clark":"Waterfront";
			super.beginText.setText(getString(R.string.boarding_instr_msg, line, dest));
		}
	}
	
	private class AirportFragment extends LegListFragment{
		
		AirportFragment(int start, int end){
			super(2, start, end);
		}
		
		protected void setBeginText(){
			String line = "Canada";
			String dest = super.outbound?"YVR-Airport":"Waterfront";
			super.beginText.setText(getString(R.string.boarding_instr_msg, line, dest));
		}
	}
	
	private class BrighouseFragment extends LegListFragment{
		
		BrighouseFragment(int start, int end){
			super(3, start, end);
		}
		
		protected void setBeginText(){
			String line = "Canada";
			String dest = super.outbound?"Richmond-Brighouse":"Waterfront";
			super.beginText.setText(getString(R.string.boarding_instr_msg, line, dest));
		}
	}
	
	private class EvergreenFragment extends LegListFragment{
		
		EvergreenFragment(int start, int end){
			super(4, start, end);
		}
		
		protected void setBeginText(){
			String line = "Evergreen";
			String dest = super.outbound?"Lafarge Lake-Douglas":"Lougheed Town Centre";
			super.beginText.setText(getString(R.string.boarding_instr_msg, line, dest));
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
		legsDone = 0;
		crosses1_2 = false;
		crosses2_3 = false;
		res = getResources();
		setContentView(R.layout.activity_trip_route);
		pdlg.show();
		fragman = getFragmentManager();
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
