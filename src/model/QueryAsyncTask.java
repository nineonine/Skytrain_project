package model;

import java.util.Arrays;

import android.os.AsyncTask;
import android.content.Context;
import android.database.Cursor;

/**
 * It's all well and good for POJOs to access the database directly,
 * but the user experience could suffer if we put too many queries
 * in the UI thread. Note that this requires further subclassing to
 * implement the onPostExecute(Cursor) callback, which is generally
 * where you want to put the results to use. See TripFormActivity for
 * an example.
 * @author aidan
 *
 */
public class QueryAsyncTask extends AsyncTask<String, Void, Cursor> {
	
	private SkytrainOpenHelper dbHelp;
	
	public QueryAsyncTask(Context context){
		super();
		dbHelp = new SkytrainOpenHelper(context);
	}

	@Override
	protected Cursor doInBackground(String... arg0) {
		String[] subArgs = null;
		try{
			subArgs = Arrays.copyOfRange(arg0, 1, arg0.length);
		}catch(Exception e){
			// TODO log the exception. Don't abort or anything, though.
		}
		return dbHelp.rawQuery(arg0[0], subArgs);
	}

}
