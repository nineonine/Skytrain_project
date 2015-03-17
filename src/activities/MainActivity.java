package activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.douglas.skytrainproject.R;

import listview.Item;
import listview.Items;
import listview.ItemsSections;
import listview.NamesAdapter;

import model.StationDAO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements TextWatcher,
		OnItemClickListener {
	
	ListView listView;
	List<Items> items;
	List<Items> filterArray = new ArrayList<Items>();
	StationDAO stationDao = new StationDAO();

	ArrayList<Item> itemsSection = new ArrayList<Item>();

	NamesAdapter objAdapter = null;

	EditText mySearch;
	String searchString;
	AlertDialog alert = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		mySearch = (EditText) findViewById(R.id.input_search_query);
		mySearch.addTextChangedListener(this);

		// XML Parsing Using AsyncTask...
		if (isNetworkAvailable()) {
			new MyTask().execute();
		} else {
			showToast("No Internet Connection");
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

		//Item item = itemsSection.get(position);

//		 if
//		 (view.getTag().getClass().getSimpleName().equals("ViewHolderName")) {
//		 Items objSchoolname = (Items) item;
//		 showToast(objSchoolname.getName());
//		 } else {
//		 ItemsSections objSectionsName = (ItemsSections) item;
//		 showToast("Section :: "+String.valueOf(objSectionsName.getSectionLetter()));
//		 }

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
				if (name.getName().toLowerCase()
						.startsWith(searchString.toLowerCase())) {

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
		alert.setButton(0, "Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}

}
