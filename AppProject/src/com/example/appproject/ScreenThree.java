package com.example.appproject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScreenThree extends Activity {

	// ArrayAdapter<String> adapter;
	ListView listview;

	SimpleAdapter adapter;

	// Progress Dialog
	private ProgressDialog mProgressDialog;

	// private static final String READ_COMMENTS_URL =
	// "http://192.168.1.110:8888/LWebservice/res/comments.php";
	private static final String READ_COMMENTS_URL = "http://192.168.1.148:8888/AndroidPHP/querylocation.php";

	// JSON IDS:
	// private static final String TAG_SUCCESS = "success";
	private static final String TAG_Longitude = "Longitude";
	private static final String TAG_Latitude = "Latitude";

	private static final String TAG_POSTS = "posts";

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
		
		
		
		/*
		 If want to hide the corrdinates:
		 1)Use sharedpref at beginneg in on create
		 	SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean hideText = getPrefs.getBoolean("checkbox", true);
		2)Then use 		if (hideText == true) {
				new RemoteDataTask().execute();
				(updateJSONdata() and updatelist())
				
		} else {
				new RemoteDataTask2().execute();
				(updateJSONdata2() and updatelist2())
		3)THIS WILL DETERMINE WHAT KIND OF PAGE WILL BE LOADED BASED ON THE PREFERNCE THAT IS CHECKED
		4)Then you can change the updateJSONdata() and updatelist() methods so that only that particular list can be loaded
		 
		
		
		OTHER CRAP****
				SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String et = getData.getString("name", "Blank Space");
		String values = getData.getString("list", "4");
		if(values.contentEquals("1")){
			question.setText(et);
		}
		
			//TextView titleone = (TextView) findViewById(R.id.title);
	//	TextView titletwo = (TextView) findViewById(R.id.title2);

			//lblPassword.setText("");
		*/

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(ScreenThree.this);
			// Set progress dialog title
			mProgressDialog.setTitle("Data Load");
			// Set progress dialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progress dialog
			mProgressDialog.show();
		}

		protected Void doInBackground(Void... params) {
			updateJSONdata();
			return null;
		}

		protected void onPostExecute(Void result) {
			// Locate the listView in listview_main.xml
			mProgressDialog.dismiss();
			updateList();
			/*
			 * This is where the parse queries go.
			 */

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
			break;
		// action with ID action_settings was selected
		case R.id.action_settings:

			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
		}
		return true;

	}

	public void updateJSONdata() {

		mCommentList = new ArrayList<HashMap<String, String>>();

		JSONParser jParser = new JSONParser();

		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		try {

			mComments = json.getJSONArray(TAG_POSTS);

			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				String title = c.getString(TAG_Longitude);
				String title2 = c.getString(TAG_Latitude);

				// String content = c.getString(TAG_MESSAGE);
				// String username = c.getString(TAG_USERNAME);

				HashMap<String, String> map = new HashMap<String, String>();
				Log.d("Text", "returned" + title + title2);
				map.put(TAG_Longitude, title);
				map.put(TAG_Latitude, title2);

				// map.put(TAG_MESSAGE, content);
				// map.put(TAG_USERNAME, username);
				
				mCommentList.add(map);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateList() {

		listview = (ListView) findViewById(R.id.listview);
		// Pass the results into an ArrayAdapter

		/*
		 * adapter = new ArrayAdapter<String>(ReadLineComments2.this,
		 * mCommentList R.layout.listview_item, new String[] { TAG_TITLE,
		 * TAG_MESSAGE, TAG_USERNAME }, new int[] { R.id.title, R.id.message,
		 * R.id.username });
		 */

		adapter = new SimpleAdapter(this, mCommentList, R.layout.singlestuff,
				new String[] { TAG_Longitude, TAG_Latitude }, new int[] { R.id.title, R.id.title2});

		// new String[] { TAG_TITLE, TAG_MESSAGE, TAG_USERNAME },
		// new int[] { R.id.title, R.id.message, R.id.username });
		
		
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				//Getting Previous Intent info and setting to to new variable
				Intent i2 = getIntent();
				final String lName = i2.getStringExtra("lname");


			//	Intent i3 = new Intent(ScreenThree.this, Third.class);

				// String item2 = lv.getItemAtPosition(position).toString();
				
				// To get it to pass the right Line I needed trim the excess
				// off:
				
				//can use for loop and make it so that 
				/*
				String item3 = adapter.getItem(position).toString();
				String item4 = item3.replace("{linein=", "");
				String item5 = item4.replace("}", "");

				Log.d("This is debug", "This grabs the info at the position:"
						+ item3 + "it." + item4 + item5);

				// i2.putExtra("name", adapter.getItem(position).toString());
				i3.putExtra("linename", item5);
				i3.putExtra("Storename", lName);
				
				Log.d("I selected:", "" + item5 + lName);
				*/
			//	startActivity(i3);

			}
		});
	}
	
	
	
	

}