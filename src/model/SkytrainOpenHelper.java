package model;

import java.util.ArrayList;

import com.douglas.skytrainproject.R;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SkytrainOpenHelper extends SQLiteOpenHelper {
	
	public static String DBNAME = "SkytrainDB";
	public static int DBVER = 1;
	
	public static String STN_TBL_NAME = "Stations";
	public static String STN_ID_COL = "_id";
	public static String STN_NAME_COL = "Name";
	public static String STN_DESC_COL = "Description";
	public static String STN_LOC_COL = "Location";
	public static String STN_LAT_COL = "Latitude";
	public static String STN_LON_COL = "Longitude";
	public static String STN_ZONE_COL = "Zone";
	public static String STN_IMG_COL = "Image";
	private static String STN_TBL_CREATE = "CREATE TABLE " + STN_TBL_NAME +
			" (" + STN_ID_COL + " INTEGER PRIMARY KEY, " +
			STN_NAME_COL + " CHAR NOT NULL ON CONFLICT FAIL, " +
			STN_DESC_COL + " CHAR, " +
			STN_LOC_COL + " CHAR, " +
			STN_LAT_COL + " FLOAT, " +
			STN_LON_COL + " FLOAT, " +
			STN_ZONE_COL + " INT NOT NULL ON CONFLICT FAIL, " +
			STN_IMG_COL + " INT DEFAULT " + String.valueOf(R.drawable.placeholder) + ")";
	private static String[] stnnames = {"Waterfront", "Burrard", "Granville",
		"Stadium-Chinatown", "Main Street-Science World", "Commercial-Broadway",
		"Nanaimo", "29th Avenue", "Joyce-Collingwood", "Patterson", "Metrotown",
		"Royal Oak", "Edmonds", "22nd Street", "New Westminster", "Columbia",
		"Scott Road", "Gateway", "Surrey Central", "King George", "Sapperton",
		"Braid", "Lougheed Town Centre", "Production Way-University",
		"Lake City Way", "Sperling-Burnaby Lake", "Holdom", "Brentwood Town Centre",
		"Gilmore", "Rupert", "Renfrew", "VCC-Clark", "Vancouver City Centre",
		"Yaletown-Roundhouse", "Olympic Village", "Broadway-City Hall",
		"King Edward", "Oakridge-41st Avenue", "Langara-49th Avenue", "Marine Drive",
		"Bridgeport", "Templeton", "Sea Island Centre", "YVR-Airport", "Aberdeen",
		"Lansdowne", "Richmond-Brighouse", "Burquitlam", "Moody Centre",
		"Inlet Centre", "Coquitlam Central", "Lincoln", "Lafarge Lake-Douglas"};
	private static ContentValues[] stnValues = buildStationsValues();
	
	public static String LINE_TBL_NAME = "Lines";
	public static String LINE_ID_COL = "_id";
	public static String LINE_NAME_COL = "Name";
	private static String LINE_TBL_CREATE = "CREATE TABLE " + LINE_TBL_NAME +
			" (" + LINE_ID_COL + " INTEGER PRIMARY KEY, " +
			LINE_NAME_COL + " CHAR NOT NULL ON CONFLICT FAIL)";
	private static String[] linenames = {"Expo", "Millennium",
		"Airport", "Brighouse", "Evergreen"};
	private static ContentValues[] lineValues = buildLinesValues();
	
	public static String INDEX_TBL_NAME = "StationIndices";
	public static String INDEX_LINE_COL = "line_id";
	public static String INDEX_POS_COL = "index_number";
	public static String INDEX_STN_COL = "station_id";
	private static String INDEX_TBL_CREATE = "CREATE TABLE " + INDEX_TBL_NAME +
			" (" + INDEX_LINE_COL + " INTEGER NOT NULL REFERENCES " + LINE_TBL_NAME + "(" + LINE_ID_COL + "), "+
			INDEX_STN_COL + " INTEGER NOT NULL REFERENCES " + STN_TBL_NAME + "(" + STN_ID_COL + "), " +
			INDEX_POS_COL + " INTEGER NOT NULL ON CONFLICT FAIL)";
	private static ContentValues[] indexValues = makeIndexValues();
	
	public static String TIMES_TBL_NAME = "TravelTimes";
	public static String TIMES_STN_A_COL = "station_idA";
	public static String TIMES_STN_B_COL = "station_idB";
	public static String TIMES_DURATION_COL = "duration";
	private static String TIMES_TBL_CREATE = "CREATE TABLE " + TIMES_TBL_NAME + " (" +
			TIMES_STN_A_COL + " INTEGER NOT NULL REFERENCES " + STN_TBL_NAME + "(" +
			STN_ID_COL + "), " + TIMES_STN_B_COL + " INTEGER NOT NULL REFERENCES " +
			STN_TBL_NAME + "(" + STN_ID_COL + "), " + TIMES_DURATION_COL +
			" INTEGER NOT NULL, PRIMARY KEY (" + TIMES_STN_A_COL + ", " + TIMES_STN_B_COL +
			"), CHECK (" + TIMES_STN_B_COL + " != " + TIMES_STN_A_COL + ") )";
	private static int[][] expoTimes = {
			{2, 3, 4, 6, 9,12,13,15,17,18,20,23,25,29,30,33,36,37,39},
			   {1, 2, 4, 7,10,11,13,15,16,18,21,23,27,28,31,34,35,37},
			      {1, 3, 6, 9,10,12,14,15,17,20,22,26,27,30,33,34,36},
			         {2, 5, 8, 9,11,13,14,16,19,21,25,26,29,32,33,35},
			            {3, 6, 7, 9,11,12,14,17,19,23,24,27,30,31,33},
			               {3, 4, 6, 8, 9,11,14,16,20,21,24,27,28,30},
			                  {1, 3, 5, 6, 8,11,13,17,18,21,24,25,27},
			                     {2, 4, 5, 7,10,12,16,17,20,23,24,26},
			                        {2, 3, 5, 8,10,14,15,18,21,22,24},
			                           {1, 3, 6, 8,12,13,16,19,20,22},
			                              {2, 5, 7,11,12,15,18,19,21},
			                                 {3, 5, 9,10,13,16,17,19},
			                                    {2, 6, 7,10,13,14,16},
			                                       {4, 5, 8,11,12,14},
			                                          {1, 4, 7, 8,10},
			                                             {3, 6, 7, 9},
			                                                {3, 4, 6},
			                                                   {1, 3},
			                                                      {2}};
	private static int[][] millenTimes = {
			{3, 5, 8,10,12,14,16,18,19,22,23,26,27},
			   {2, 5, 7, 9,11,13,15,16,19,20,23,24},
			      {3, 5, 7, 9,11,13,14,17,18,21,22},
			         {2, 4, 6, 8,10,11,14,15,18,19},
			            {2, 4, 6, 8, 9,12,13,16,17},
			               {2, 4, 6, 7,10,11,14,15},
			                  {2, 4, 5, 8, 9,12,13},
			                     {2, 3, 6, 7,10,11},
			                        {1, 4, 5, 8, 9},
			                           {3, 4, 7, 8},
			                              {1, 4, 5},
			                                 {3, 4},
			                                    {1}};
	private static int[][] brighouseTimes = {
			{2, 4, 6, 7, 9,12,14,17,19,21,23,25},
			   {2, 4, 5, 7,10,12,15,17,19,21,23},
			      {2, 3, 5, 8,10,13,15,17,19,21},
			         {1, 3, 6, 8,11,13,15,17,19},
			            {2, 5, 7,10,12,14,16,18},
			               {3, 5, 8,10,12,14,16},
			                  {2, 5, 7, 9,11,13},
			                     {3, 5, 7, 9,11},
			                        {2, 4, 6, 8},
			                           {2, 4, 6},
			                              {2, 4},
			                                 {2}};
	private static int[][] timesToSeaIsland = {
			{22,20,18,16,15,13,10, 8, 5, 3},
			{24,22,20,18,17,15,12,10, 7, 5, 2},
			{26,24,22,20,19,17,14,12, 9, 7, 4, 2}};
	private static ContentValues[] timeValues = buildTimeValues();
	
	private static String fkPragma = "PRAGMA foreign_keys = ON";
	
	private SQLiteDatabase db;
	
	public SkytrainOpenHelper(Context context){
		super(context, DBNAME, null, DBVER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(fkPragma);
		db.beginTransaction();
		try{
			db.execSQL(LINE_TBL_CREATE);
			db.execSQL(STN_TBL_CREATE);
			db.execSQL(INDEX_TBL_CREATE);
			db.execSQL(TIMES_TBL_CREATE);
			for(ContentValues line : lineValues){
				db.insert(LINE_TBL_NAME, null, line);
			}
			for(ContentValues stn : stnValues){
				db.insert(STN_TBL_NAME, null, stn);
			}
			for(ContentValues index : indexValues){
				db.insert(INDEX_TBL_NAME, null, index);
			}
			for(ContentValues time : timeValues){
				db.insert(TIMES_TBL_NAME, null, time);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		//That first transaction creates the basic data that my (Aidan's) logic
		//will need to work. PLEASE DO NOT ALTER IT WITHOUT MY INPUT.
		//This second transaction is a place to put "extra" data.
		//First, I'll create a sample piece of data:
		ContentValues wfDesc = new ContentValues();
		wfDesc.put(STN_LOC_COL, "Waterfront Station is located in the former CPR station at the west end of Gastown.");
		//now, begin:
		db.beginTransaction();
		try{
			//Example:
			db.update(STN_TBL_NAME, wfDesc, STN_NAME_COL + " = 'Waterfront'", null);
			
			//insert database update code here:
			
			
			
			//NO DATABASE WORK BELOW THIS LINE
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @param where
	 * @param binding
	 * @return
	 */
	public Station[] queryForStationArray(String where, String... binding){
		db = (db == null)?getReadableDatabase():db;
		Cursor stnQuery = db.query(STN_TBL_NAME, null, where, binding, null, null, null);
		int nameX = stnQuery.getColumnIndex(STN_NAME_COL);
		int descX = stnQuery.getColumnIndex(STN_DESC_COL);
		int latX = stnQuery.getColumnIndex(STN_LAT_COL);
		int lonX = stnQuery.getColumnIndex(STN_LON_COL);
		int locX = stnQuery.getColumnIndex(STN_LOC_COL);
		int zoneX = stnQuery.getColumnIndex(STN_ZONE_COL);
		int imgX = stnQuery.getColumnIndex(STN_IMG_COL);
		Station[] result = new Station[stnQuery.getCount()];
		int i = 0;
		stnQuery.moveToFirst();
		do{
			String name = stnQuery.getString(nameX);
			String desc = stnQuery.getString(descX);
			String lat = String.valueOf(stnQuery.getDouble(latX));
			String lon = String.valueOf(stnQuery.getDouble(lonX));
			String loc = stnQuery.getString(locX);
			String zone = String.valueOf(stnQuery.getInt(zoneX));
			result[i] = new Station(name, zone, desc, loc, lat, lon);
			result[i].setImage(String.valueOf(stnQuery.getInt(imgX)));
			++i;
			stnQuery.moveToNext();
		}while(!stnQuery.isAfterLast());
		stnQuery.close();
		return result;
	}
	
	private static ContentValues[] buildLinesValues(){
		ContentValues[] lines = new ContentValues[5];
		for(int i = 0; i < 5; ++i){
			lines[i] = new ContentValues();
			lines[i].put(LINE_ID_COL, i);
			lines[i].put(LINE_NAME_COL, linenames[i]);
		}
		return lines;
	}
	
	public int findLineWithStations(int stnA, int stnB){
		db = (db == null)?getReadableDatabase():db;
		String[] cols = {INDEX_LINE_COL,INDEX_STN_COL};
		String where = INDEX_STN_COL + " = CAST(? AS INTEGER)";
		String[] whereArgs = {String.valueOf(stnA)};
		Cursor crs = db.query(true, INDEX_TBL_NAME, cols, where, whereArgs, null, null, null, null);
		int[] lines = new int[crs.getCount()];
		crs.moveToFirst();
		for(int i = 0;i < lines.length;++i){
			lines[i] = crs.getInt(crs.getColumnIndex(INDEX_LINE_COL));
		}
		crs.close();
		where = INDEX_STN_COL + " = CAST(? AS INTEGER)";
		whereArgs[0] = String.valueOf(stnB);
		crs = db.query(true, INDEX_TBL_NAME, cols, where, whereArgs, null, null, null, null);
		for(int i = 0;i < crs.getCount();++i){
			crs.moveToPosition(i);
			int lineB = crs.getInt(crs.getColumnIndex(INDEX_LINE_COL));
			for(int j = 0;j < lines.length;++j){
				if(lines[j] == lineB)return lineB;
			}
		}
		crs.close();
		return -1;
	}
	
	public int findLineFromTransfer(int xferId, int otherId){
		switch(xferId){
		case activities.TripRouteActivity.BROADWAY_ID:
			if(otherId < 20)return 0;
			if(otherId < 32)return 1;
			return -1;
		case activities.TripRouteActivity.WATERFRONT_ID:
			if(otherId < 20)return 0;
			if(otherId < 32)return 1;
			if(otherId < 44)return 2;
			if(otherId < 47)return 3;
			return -1;
		case activities.TripRouteActivity.BRIDGEPORT_ID:
			if(otherId == 0)return 2;
			if(otherId < 32)return -1;
			if(otherId < 44)return 2;
			if(otherId < 47)return 3;
			return -1;
		case activities.TripRouteActivity.COLUMBIA_ID:
			if(otherId < 20)return 0;
			if(otherId < 32)return 1;
			return -1;
		case activities.TripRouteActivity.LOUGHEED_ID:
			if(otherId < 20){
				if(otherId > 15)return -1;
				return 1;
			}
			if(otherId < 32)return 1;
			if(otherId < 47)return -1;
			return 4;
		default:
			return -1;
		}
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs,
			String groupBy, String having, String orderBy){
		db = (db == null)?getReadableDatabase():db;
		return db.query(table, columns, where, whereArgs, groupBy, having, orderBy);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs){
		return query(table, columns, where, whereArgs, null, null, null);
	}
	
	public Cursor queryStationsOfLine(int lineID, Integer from, Integer to){
		String qstr = "SELECT " + STN_ID_COL + " , " + INDEX_STN_COL + " , " + STN_NAME_COL + " , " + STN_ZONE_COL + " , " +
						INDEX_LINE_COL + " , " + INDEX_POS_COL + " FROM " + STN_TBL_NAME + " JOIN " + INDEX_TBL_NAME + " ON " +
						STN_ID_COL + "=" + INDEX_STN_COL + " WHERE " + INDEX_LINE_COL + " = CAST( ? AS INTEGER)";
		if(from != null && to != null){
			qstr = qstr + " AND " + INDEX_POS_COL + " BETWEEN CAST( ? AS INTEGER) AND CAST( ? AS INTEGER)";
			if(from < to){
				qstr = qstr + " ORDER BY " + INDEX_POS_COL + " ASC";
			}else{
				qstr = qstr + " ORDER BY " + INDEX_POS_COL + " DESC";
			}
		}
		String[] bindings = {String.valueOf(lineID), String.valueOf((from<to)?from:to), String.valueOf((from<to)?to:from)};
		return rawQuery(qstr, bindings);
	}
	
	public Cursor queryLineByStationId(int lineID, int stnA, int stnB){
		String posAq = "SELECT " + INDEX_POS_COL + " , " + INDEX_LINE_COL + " , " + INDEX_STN_COL + " FROM " + INDEX_TBL_NAME +
				" WHERE " + INDEX_LINE_COL + " = " + lineID + " AND " + INDEX_STN_COL + " = " + stnA;
		Cursor posAc = rawQuery(posAq, null);
		if(posAc.getCount() == 0)throw new NullPointerException("The line doesn't contain stnA!");//this shouldn't happen, but you never know.
		int[] posnsA = new int[posAc.getCount()];
		posAc.moveToFirst();
		for(int i = 0;i < posnsA.length;++i){
			posnsA[i] = posAc.getInt(posAc.getColumnIndex(INDEX_POS_COL));
			posAc.moveToNext();
		}
		posAc.close();
		String posBq = "SELECT " + INDEX_POS_COL + " , " + INDEX_LINE_COL + " , " + INDEX_STN_COL + " FROM " + INDEX_TBL_NAME +
				" WHERE " + INDEX_LINE_COL + " = " + lineID + " AND " + INDEX_STN_COL + " = " + stnB;
		Cursor posBc = rawQuery(posBq, null);
		if(posBc.getCount() == 0)throw new NullPointerException("The line doesn't contain stnB!");//can't happen
		int[] posnsB = new int[posBc.getCount()];
		posBc.moveToFirst();
		for(int i = 0;i < posnsB.length;++i){
			posnsB[i] = posBc.getInt(posBc.getColumnIndex(INDEX_POS_COL));
			posBc.moveToNext();
		}
		posBc.close();
		if(posnsA.length == 1 && posnsB.length == 1)return queryStationsOfLine(lineID, posnsA[0], posnsB[0]);
		int dA = 0;
		int dB = 0;
		int diff = Integer.MAX_VALUE;
		for(int iA = 0;iA < posnsA.length;++iA){
			for(int iB = 0;iB < posnsB.length;++iB){
				int del = Math.abs(posnsA[iA] - posnsB[iB]);
				diff = (del < diff)?del:diff;
				if(diff == del){
					dA = iA;
					dB = iB;
				}
			}
		}
		return queryStationsOfLine(lineID, posnsA[dA], posnsB[dB]);
	}
	
	public Cursor rawQuery(String query, String[] bindings){
		db = (db == null)?getReadableDatabase():db;
		return db.rawQuery(query, bindings);
	}
	
	private static ContentValues[] buildStationsValues(){
		ContentValues[] stns = new ContentValues[53];
		for(int i = 0;i < 53;++i){
			stns[i] = new ContentValues();
			stns[i].put(STN_ID_COL, i);
			stns[i].put(STN_NAME_COL, stnnames[i]);
			int zone;
			if(i < 9 || (i > 29 && i < 40)){
				zone = 1;
			}else if(i < 16 || (i > 19 && i < 47)){
				zone = 2;
			}else{
				zone = 3;
			}
			stns[i].put(STN_ZONE_COL, zone);
		}
		return stns;
	}
	
	private static ContentValues valuesOfPosnTuple(int line, int stn, int index){
		ContentValues res = new ContentValues();
		res.put(INDEX_LINE_COL, line);
		res.put(INDEX_STN_COL, stn);
		res.put(INDEX_POS_COL, index);
		return res;
	}
	
	//This is a more systematic version of buildIndexValues(). I was getting weird bugs in the logic,
	//and I couldn't figure out where they were coming from, so I decided to reduce the number of places
	//where things can go wrong.
	private static ContentValues[] makeIndexValues(){
		ArrayList<ContentValues> accum = new ArrayList<ContentValues>();
		int i = 0;
		//Expo-Millennium shared track:
		while(i < 16){
			accum.add(valuesOfPosnTuple(0, i, i));
			accum.add(valuesOfPosnTuple(1, i, i));
			++i;
		}
		//Expo track:
		while(i < 20){
			accum.add(valuesOfPosnTuple(0, i, i));
			++i;
		}
		//Millennium track up to Renfrew:
		int posOffset = 4;
		while(i < 31){
			accum.add(valuesOfPosnTuple(1, i, i - posOffset));
			++i;
		}
		//Commercial-Broadway Millenium platforms:
		accum.add(valuesOfPosnTuple(1, 5, i - posOffset));
		--posOffset;
		//VCC-Clark:
		accum.add(valuesOfPosnTuple(1, i, i - posOffset));
		//Waterfront Canada platforms:
		accum.add(valuesOfPosnTuple(2, 0, 0));
		accum.add(valuesOfPosnTuple(3, 0, 0));
		//Canada Line shared track:
		posOffset = i;
		++i;
		while(i < 41){
			accum.add(valuesOfPosnTuple(2, i, i - posOffset));
			accum.add(valuesOfPosnTuple(3, i, i - posOffset));
			++i;
		}
		//Canada Line Airport:
		while(i < 44){
			accum.add(valuesOfPosnTuple(2, i, i - posOffset));
			++i;
		}
		//Canada Line Richmond:
		posOffset += 3;
		while(i < 47){
			accum.add(valuesOfPosnTuple(3, i, i - posOffset));
			++i;
		}
		//Lougheed Evergreen platforms:
		accum.add(valuesOfPosnTuple(4, 22, 0));
		//Evergreen Line track:
		posOffset = i - 1;
		while(i < 53){
			accum.add(valuesOfPosnTuple(4, i, i - posOffset));
			++i;
		}
		ContentValues[] result = new ContentValues[accum.size()];
		return accum.toArray(result);
	}
	
	private static ContentValues valuesOfTimeTuple(int stnA, int stnB, int time){
		ContentValues res = new ContentValues();
		res.put(TIMES_STN_A_COL, stnA);
		res.put(TIMES_STN_B_COL, stnB);
		res.put(TIMES_DURATION_COL, time);
		return res;
	}
	
	private static ContentValues[] buildTimeValues(){
		ArrayList<ContentValues> accum = new ArrayList<ContentValues>();
		
		//Expo Times:
		for(int i = 0;i < 19;++i){
			for(int j = i;j < 19; ++j){
				accum.add(valuesOfTimeTuple(i, j + 1, expoTimes[i][j - i]));
			}
		}
		int idOffset = 20;
		//Millennium times from Columbia:
		int columID = activities.TripRouteActivity.COLUMBIA_ID;
		int bdwyID = activities.TripRouteActivity.BROADWAY_ID;
		columLoop:for(int i = 0;i < 13;++i){
			if(i == 11){
				accum.add(valuesOfTimeTuple(columID,bdwyID,millenTimes[0][11]));
				continue;
			}
			if(i == 12){
				accum.add(valuesOfTimeTuple(columID, i + idOffset, millenTimes[0][i]));
				break columLoop;
			}
			accum.add(valuesOfTimeTuple(columID, i + 1 + idOffset, millenTimes[0][i]));
		}
		//other Millennium times up to from Renfrew:
		millenLoop:for(int i = 0;i < 11;++i){
			for(int j = i;j < 12;++j){
				if(j == 10){
					accum.add(valuesOfTimeTuple(idOffset + i, bdwyID, millenTimes[i + 1][j - i]));
					continue;
				}
				if(j == 11){
					accum.add(valuesOfTimeTuple(idOffset + i, idOffset + j, millenTimes[i + 1][j - i]));
					continue millenLoop;
				}
				accum.add(valuesOfTimeTuple(idOffset + i, 1 + idOffset + j, millenTimes[i + 1][j - i]));
			}
		}
		//time from Commercial-Broadway to VCC-Clark:
		accum.add(valuesOfTimeTuple(bdwyID, 31, millenTimes[12][0]));
		idOffset = 31;
		//Canada line times between Waterfront and Bridgeport:
		for(int i = 0;i < 9;++i){
			for(int j = i;j < 9;++j){
				accum.add(valuesOfTimeTuple((i == 0)?i:i + idOffset,j + 1 + idOffset,brighouseTimes[i][j - i]));
			}
		}
		//Canada Line times from Waterfront to Templeton:
		for(int i = 0;i < 10;++i){
			accum.add(valuesOfTimeTuple((i == 0)?i:i + idOffset,idOffset + 10,timesToSeaIsland[0][i]));
		}
		//to Sea Island Centre:
		for(int i = 0;i < 11;++i){
			accum.add(valuesOfTimeTuple((i == 0)?i:i + idOffset,idOffset + 11,timesToSeaIsland[1][i]));
		}
		//to YVR-Airport:
		for(int i = 0;i < 12;++i){
			accum.add(valuesOfTimeTuple((i == 0)?i:i + idOffset,idOffset + 12,timesToSeaIsland[2][i]));
		}
		//Canada Line times Bridgeport to Brighouse:
		int bridgeID = activities.TripRouteActivity.BRIDGEPORT_ID;
		int arrayOffset = 9;
		idOffset = bridgeID + 3;
		for(int i = 0;i < 3;++i){
			accum.add(valuesOfTimeTuple(bridgeID, idOffset + i, brighouseTimes[arrayOffset][i]));
		}
		accum.add(valuesOfTimeTuple(idOffset, idOffset + 1, brighouseTimes[arrayOffset + 1][0]));
		accum.add(valuesOfTimeTuple(idOffset, idOffset + 2, brighouseTimes[arrayOffset + 1][1]));
		accum.add(valuesOfTimeTuple(idOffset + 1, idOffset + 2, brighouseTimes[arrayOffset + 2][0]));
		ContentValues[] result = new ContentValues[accum.size()];
		return accum.toArray(result);
	}
}
