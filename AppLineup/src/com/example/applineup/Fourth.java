package com.example.applineup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Fourth extends Activity {
	Context context;
	List<String> tasks;
	ArrayAdapter<String> adapter;
	ListView listview;

	private ProgressDialog mProgressDialog;

	private static final String READ_COMMENTS_URL = "http://192.168.1.148:8888/AndroidPHP/querylines3.php";

	// JSON IDS:
	// private static final String TAG_SUCCESS = "success";
	private static final String TAG_TITLE = "linein";
	private static final String TAG_POSTS = "posts";

	// An array of all of our comments
	private JSONArray mComments = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);

		new RemoteDataTask().execute();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(Fourth.this);
		String first = sp.getString("Postal", null);
		String second = sp.getString("latitude", null);
		String third = sp.getString("longitude", null);

		getActionBar().setTitle(" Lat:" + second + "Long:" + third);

		context = this;
		tasks = new ArrayList<String>();

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(Fourth.this);
			// Set progress dialog title
			mProgressDialog.setTitle("Data Load");
			// Set progress dialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progress dialog
			mProgressDialog.show();
		}

		protected Void doInBackground(Void... params) {

			// mCommentList = new ArrayList<HashMap<String, String>>();

			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into an ArrayAdapter

			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, tasks);
			// ListView listView = (ListView) findViewById(R.id.listview);

			adapter.add("Blahal");

			listview.setAdapter(adapter);

			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

			try {

				mComments = json.getJSONArray(TAG_POSTS);

				for (int i = 0; i < mComments.length(); i++) {
					JSONObject c = mComments.getJSONObject(i);

					String title = c.getString(TAG_TITLE);
					Log.d("Title", "" + title);

					// String content = c.getString(TAG_MESSAGE);
					// String username = c.getString(TAG_USERNAME);
					Log.d("Inbox : ", "" + mComments);

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Fourth.this);
					String first = sp.getString("Postal", null);
					String second = sp.getString("latitude", null);
					String third = sp.getString("longitude", null);

					Log.d("Postal", "Code:" + first);

					//mComments = null;

				//	tasks.add(title);
				//	tasks.add(first);
					Log.d("", "" + title);
					Log.d("", "" + title);
					

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			// Locate the listView in listview_main.xml

			mProgressDialog.dismiss();

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

			adapter.add("This is it");
			adapter.notifyDataSetChanged();

			break;

		case R.id.action_settings:

			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
		}
		return true;

	}

}
