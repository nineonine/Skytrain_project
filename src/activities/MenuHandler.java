package activities;

import android.content.Intent;
import android.content.Context;
import android.view.MenuItem;
import com.douglas.skytrainproject.R;

public class MenuHandler {

	public static boolean handleMenuItemClick(Context context, MenuItem menuItem){
		Intent in;
		switch(menuItem.getItemId()){
		case R.id.tripPlanner:
			in = new Intent(context, TripFormActivity.class);
			context.startActivity(in);
			return true;
		case R.id.stationList:
			in = new Intent(context, MainActivity.class);
			context.startActivity(in);
			return true;
		case R.id.fares:
			in = new Intent(context, FaresActivity.class);
			context.startActivity(in);
			return true;
		case R.id.schedule:
			in = new Intent(context, ScheduleActivity.class);
			context.startActivity(in);
			return true;
		}
		return false;
	}
}
