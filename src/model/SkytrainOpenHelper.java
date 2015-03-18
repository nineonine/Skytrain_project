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
	public static String STN_ID_ROW = "_id";
	public static String STN_NAME_ROW = "Name";
	public static String STN_DESC_ROW = "Description";
	public static String STN_LOC_ROW = "Location";
	public static String STN_LAT_ROW = "Latitude";
	public static String STN_LON_ROW = "Longitude";
	public static String STN_ZONE_ROW = "Zone";
	public static String STN_IMG_ROW = "Image";
	private static String STN_TBL_CREATE = "CREATE TABLE " + STN_TBL_NAME +
			" (" + STN_ID_ROW + " INTEGER PRIMARY KEY, " +
			STN_NAME_ROW + " CHAR NOT NULL ON CONFLICT FAIL, " +
			STN_DESC_ROW + " CHAR, " +
			STN_LOC_ROW + " CHAR, " +
			STN_LAT_ROW + " FLOAT, " +
			STN_LON_ROW + " FLOAT, " +
			STN_ZONE_ROW + " INT NOT NULL ON CONFLICT FAIL, " +
			STN_IMG_ROW + " INT DEFAULT " + String.valueOf(R.drawable.placeholder) + ")";
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
	public static String LINE_ID_ROW = "_id";
	public static String LINE_NAME_ROW = "Name";
	private static String LINE_TBL_CREATE = "CREATE TABLE " + LINE_TBL_NAME +
			" (" + LINE_ID_ROW + " INTEGER PRIMARY KEY, " +
			LINE_NAME_ROW + " CHAR NOT NULL ON CONFLICT FAIL)";
	private static String[] linenames = {"Expo", "Millennium",
		"Airport", "Brighouse", "Evergreen"};
	private static ContentValues[] lineValues = buildLinesValues();
	
	public static String INDEX_TBL_NAME = "StationIndices";
	public static String INDEX_LINE_ROW = "line_id";
	public static String INDEX_POS_ROW = "index";
	public static String INDEX_STN_ROW = "station_id";
	private static String INDEX_TBL_CREATE = "CREATE TABLE " + INDEX_TBL_NAME +
			" (" + INDEX_LINE_ROW + " INTEGER NOT NULL REFERENCES " + LINE_TBL_NAME + "(" + LINE_ID_ROW + "), "+
			INDEX_STN_ROW + " INTEGER NOT NULL REFERENCES " + STN_TBL_NAME + "(" + STN_ID_ROW + "), " +
			INDEX_POS_ROW + " INTEGER NOT NULL ON CONFLICT FAIL)";
	private static ContentValues[] indexValues = buildIndexValues();
	
	private static String fkPragma = "PRAGMA foreign_keys = ON";
	
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
			for(ContentValues line : lineValues){
				db.insert(LINE_TBL_NAME, null, line);
			}
			for(ContentValues stn : stnValues){
				db.insert(STN_TBL_NAME, null, stn);
			}
			for(ContentValues index : indexValues){
				db.insert(INDEX_TBL_NAME, null, index);
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		//That first transaction creates the basic data that my (Aidan's) logic
		//will need to work.
		//This second transaction is a place to put "extra" data.
		//First, I'll create a sample piece of data:
		ContentValues wfDesc = new ContentValues();
		wfDesc.put(STN_LOC_ROW, "Waterfront Station is located in the former CPR station at the west end of Gastown.");
		//now, begin:
		db.beginTransaction();
		try{
			//Example:
			db.update(STN_TBL_NAME, wfDesc, STN_NAME_ROW + " = 'Waterfront'", null);
			
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
		SQLiteDatabase db = getReadableDatabase();
		Cursor stnQuery = db.query(STN_TBL_NAME, null, where, binding, null, null, null);
		int nameX = stnQuery.getColumnIndex(STN_NAME_ROW);
		int descX = stnQuery.getColumnIndex(STN_DESC_ROW);
		int latX = stnQuery.getColumnIndex(STN_LAT_ROW);
		int lonX = stnQuery.getColumnIndex(STN_LON_ROW);
		int locX = stnQuery.getColumnIndex(STN_LOC_ROW);
		int zoneX = stnQuery.getColumnIndex(STN_ZONE_ROW);
		int imgX = stnQuery.getColumnIndex(STN_IMG_ROW);
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
			lines[i].put(LINE_ID_ROW, i);
			lines[i].put(LINE_NAME_ROW, linenames[i]);
		}
		return lines;
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs,
			String groupBy, String having, String orderBy){
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, where, whereArgs, groupBy, having, orderBy);
	}
	
	public Cursor query(String table, String[] columns, String where, String[] whereArgs){
		return query(table, columns, where, whereArgs, null, null, null);
	}
	
	private static ContentValues[] buildStationsValues(){
		ContentValues[] stns = new ContentValues[53];
		for(int i = 0;i < 53;++i){
			stns[i] = new ContentValues();
			stns[i].put(STN_ID_ROW, i);
			stns[i].put(STN_NAME_ROW, stnnames[i]);
			int zone;
			if(i < 9 || (i > 29 && i < 40)){
				zone = 1;
			}else if(i < 16 || (i > 19 && i < 47)){
				zone = 2;
			}else{
				zone = 3;
			}
			stns[i].put(STN_ZONE_ROW, zone);
		}
		return stns;
	}
	
	private static ContentValues valuesOfTuple(int line, int stn, int index){
		ContentValues res = new ContentValues();
		res.put(INDEX_LINE_ROW, line);
		res.put(INDEX_STN_ROW, stn);
		res.put(INDEX_POS_ROW, index);
		return res;
	}
	
	private static ContentValues[] buildIndexValues(){
		ArrayList<ContentValues> accum = new ArrayList<ContentValues>();
		//Let's do the Evergreen Line first
		int lineIx = 4;
		//Inbound terminus is Lougheed, which should have ID 22
		accum.add(valuesOfTuple(lineIx, 22, 0));
		//The rest of the Evergreen line should have IDs 47-52 in order
		int idOffset = 46;
		for(int i = 1;i < 7;++i){
			accum.add(valuesOfTuple(lineIx, idOffset + i, i));
		}
		//The joined section of the Expo/Millennium Line:
		//This'll be easy, because here ID==index
		//up to Columbia, ID 15:
		for(int i = 0;i < 16;++i){
			//Expo:
			accum.add(valuesOfTuple(0, i, i));
			//Millennium:
			accum.add(valuesOfTuple(1, i, i));
		}
		lineIx = 0;
		//the rest of the Expo line is also ID==index
		for(int i = 16;i < 20;++i){
			accum.add(valuesOfTuple(lineIx, i, i));
		}
		idOffset = 20;
		int posOffset = 16;
		lineIx = 1;
		//the rest of the Millennium Line:
		for(int i = 0;i < 11;++i){
			accum.add(valuesOfTuple(lineIx, i + idOffset, i + posOffset));
		}
		//Commercial-Broadway appears twice in the Millennium Line.
		//Its ID is 5.
		accum.add(valuesOfTuple(lineIx, 5, posOffset + 11));
		accum.add(valuesOfTuple(lineIx, idOffset + 11, posOffset + 12));
		//The two branches of the Canada Line have the same kind of
		//arrangement as the Expo and Millennium Lines.
		//The Canada Line's sole inbound terminus is Waterfront, ID 0:
		accum.add(valuesOfTuple(2, 0, 0));
		accum.add(valuesOfTuple(3, 0, 0));
		idOffset = idOffset + 11;
		for(int i = 1;i < 10;++i){
			accum.add(valuesOfTuple(2, idOffset + i, i));
			accum.add(valuesOfTuple(3, idOffset + i, i));
		}
		idOffset += 10;
		posOffset = 10;
		//Airport branch:
		for(int i = 0;i < 3;++i){
			accum.add(valuesOfTuple(2, idOffset + i, posOffset + i));
		}
		idOffset += 3;
		//Brighouse branch:
		for(int i = 0;i < 3;++i){
			accum.add(valuesOfTuple(3, idOffset + i, posOffset + i));
		}
		ContentValues[] result = new ContentValues[accum.size()];
		return accum.toArray(result);
	}

}
