package activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.DataProvider;
import listview.SchedAdapter;
import com.douglas.skytrainproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;


public class ScheduleActivity extends Activity {
	HashMap<String, List<String>> Train_category;
	List<String> Train_list;
	ExpandableListView Exp_list;
	SchedAdapter tAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_train);
        Exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        Train_category = DataProvider.getInfo();
        Train_list = new ArrayList<String>(Train_category.keySet());
        tAdapter = new SchedAdapter(this, Train_category, Train_list);
        Exp_list.setAdapter(tAdapter);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		boolean isHandled = MenuHandler.handleMenuItemClick(this, item);
		if(isHandled)return true;
		return super.onOptionsItemSelected(item);
	}
}
