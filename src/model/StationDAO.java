package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import listview.Items;

public class StationDAO {

	// You might want to add another factoryClass which will create a DB
	// connection object
	// this db object might be handy here !
	private SkytrainOpenHelper dbHelp;

	//public Station station;

	public StationDAO(Context context) {
		dbHelp = new SkytrainOpenHelper(context);
	}

	public Station getStation(String stationName) {

		// HERE SHOULD BE IMPLEMENTED SOME LOGIC OF RETREIVING A STATION FROM A
		// DB TABLE
		// WE HAVE TO USE OUR DB CONNECTION OBJECT OR SMTH
		String where = SkytrainOpenHelper.STN_NAME_COL + " = ?";
		Station[] res = dbHelp.queryForStationArray(where, stationName);
		//Assume that there is exactly one station that matches
		return res[0];
	}

	public ArrayList<String> getListOfStations() {
		
		//getting all names of stations from the DB and returning ArrayList<String> of names
		
		//STUB FOR TESTING
		boolean hardcode = false;
		if (hardcode) {
			ArrayList<String> temporaryList = new ArrayList<String>(
					Arrays.asList("22nd Street", "29th Avenue", "Aberdeen",
							"Braid", "Brentwood Town Centre", "Bridgeport",
							"Broadway City Hall", "Burrard", "Columbia",
							"Commercial Broadway", "Edmonds", "Gateway",
							"Gilmore", "Granville", "Holdom",
							"Joyce Collingwood", "King Edward", "King George",
							"Lake City Way", "Langara 49th Avenue",
							"Lansdowne", "Lougheed Town Centre",
							"Main Street Science World", "Marine Drive",
							"Metrotown", "Nanaimo", "New Westminster",
							"Oakridge 41st Avenue", "Olympic Village",
							"Patterson", "Production Way University",
							"Renfrew", "Richmond Brighouse", "Royal Oak",
							"Rupert", "Sapperton", "Scott Road",
							"Sea Island Centre", "Sperling Burnaby Lake",
							"Stadium Chinatown", "Surrey Central", "Templeton",
							"Vancouver City Centre", "VCC Clark", "Waterfront",
							"Yaletown Roundhouse", "YVR Airport"));
			return temporaryList;
		}
		String[] cols = {SkytrainOpenHelper.STN_NAME_COL};
		Cursor stnNames = dbHelp.query(SkytrainOpenHelper.STN_TBL_NAME, cols, null, null);
		int nameX = stnNames.getColumnIndex(SkytrainOpenHelper.STN_NAME_COL);
		ArrayList<String> names = new ArrayList<String>();
		stnNames.moveToFirst();
		do{
			names.add(stnNames.getString(nameX));
			stnNames.moveToNext();
		}while(!stnNames.isAfterLast());
		stnNames.close();
		return names;
	}
	
	public List<Items> getStationsForAdapter() {

		Items objItem;
		List<Items> listArray = new ArrayList<Items>();
		//potentially-expensive database operations should be done as few
		//times as possible
		ArrayList<String> stnNames = getListOfStations();
		int len = stnNames.size();
		
		for(int i=0;i<len;i++) {
			objItem = new Items();
			objItem.setName(stnNames.get(i));
			Log.d("TEST", "added " + stnNames.get(i));
			listArray.add(objItem);
		}
		
		return listArray;
	}

}
