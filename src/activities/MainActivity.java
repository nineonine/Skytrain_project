package activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.douglas.skytrainproject.R;

import listview.Item;
import listview.Items;
import listview.ItemsSections;
import listview.NamesAdapter;
import model.Station;
import model.StationDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements TextWatcher,
		OnItemClickListener {
	
	ListView listView;
	List<Items> items;
	List<Items> filterArray;
	StationDAO stationDao;

	ArrayList<Item> itemsSection = new ArrayList<Item>();

	NamesAdapter objAdapter = null;

	EditText mySearch;
	String searchString;
	AlertDialog alert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		filterArray = new ArrayList<Items>();
		stationDao = new StationDAO(this);
		setContentView(R.layout.main);
		filterArray = new ArrayList<Items>();
		stationDao = new StationDAO();
		
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		mySearch = (EditText) findViewById(R.id.input_search_query);
		mySearch.addTextChangedListener(this);

		// Parsing Using AsyncTask...
		if (isNetworkAvailable()) {
			new MyTask().execute();
		} else {
			showToast("No DB Connection");
			this.finish();
		}
	}

	class MyTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			//GETTING STATIONS FROM DB HERE..
			items = stationDao.getStationsForAdapter();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			 if (null == items || items.size() == 0) {
				 showToast("No data available in DB!!!");
			 MainActivity.this.finish();
			 } else {
				 setAdapterToListview(items);
			 }

			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		//code for testing
		TextView tv = (TextView)view.findViewById(R.id.tvname);
		String stationName = (String) tv.getText();
		
		Intent intent = new Intent(this, StationActivity.class);
		intent.putExtra("station", new Station(stationName,
				"zone",
				"Lorem Ipsum is simply dummy text of the printing and "
				+ "typesetting industry. Lorem Ipsum has been the industry's "
				+ "standard dummy text ever since the 1500s, when an unknown "
				+ "printer took a galley of type and scrambled it to make a type "
				+ "specimen book. It has survived not only five centuries, but also "
				+ "the leap into electronic typesetting, remaining essentially unchanged. "
				+ "It was popularised in the 1960s with the release of Letraset "
				+ "sheets containing Lorem Ipsum passages, and more recently "
				+ "with desktop publishing software like Aldus PageMaker including "
				+ "versions of Lorem Ipsum.",
				"Some useful information about location of the station. Here you might also find"
				+ " this random text !",
				"lat",
				"lon"));
		this.startActivityForResult(intent,0);

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	//FILTERING HERE
	public void afterTextChanged(Editable s) {
		
		filterArray.clear();
		searchString = mySearch.getText().toString().trim()
				.replaceAll("\\s", "");

		if (items.size() > 0 && searchString.length() > 0) {
			for (Items name : items) {
				if (name.getName().toLowerCase(Locale.CANADA)
						.startsWith(searchString.toLowerCase(Locale.CANADA))) {

					filterArray.add(name);
				}
			}
			setAdapterToListview(filterArray);
		} else {
			filterArray.clear();
			setAdapterToListview(items);
		}

	}
	
	// Setting adapter here....
	public void setAdapterToListview(List<Items> listForAdapter) {

		itemsSection.clear();

		if (null != listForAdapter && listForAdapter.size() != 0) {

			Collections.sort(listForAdapter);

			char checkChar = ' ';

			for (int index = 0; index < listForAdapter.size(); index++) {

				Items objItem = (Items) listForAdapter.get(index);

				char firstChar = objItem.getName().charAt(0);

				if (' ' != checkChar) {
					if (checkChar != firstChar) {
						ItemsSections objSectionItem = new ItemsSections();
						objSectionItem.setSectionLetter(firstChar);
						itemsSection.add(objSectionItem);
					}
				} else {
					ItemsSections objSectionItem = new ItemsSections();
					objSectionItem.setSectionLetter(firstChar);
					itemsSection.add(objSectionItem);
				}

				checkChar = firstChar;

				itemsSection.add(objItem);
			}
		} else {
			showAlertView();
		}

		if (null == objAdapter) {
			objAdapter = new NamesAdapter(MainActivity.this, itemsSection);
			listView.setAdapter(objAdapter);
		} else {
			objAdapter.notifyDataSetChanged();
		}

	}
	
	//some crash evasion - when one taps at alphabetical section panel
	public void evade(View v) {
		
	}


	// Check Internet Connection!!!
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void showAlertView() {

		if (null == alert)
			alert = new AlertDialog.Builder(this).create();

		if (alert.isShowing()) {
			return;
		}

		alert.setTitle("Not Found!!!");
		alert.setMessage("Can not find name Like '" + searchString + "'");
		alert.setButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

}
