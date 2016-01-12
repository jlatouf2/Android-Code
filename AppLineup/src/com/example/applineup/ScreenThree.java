package com.example.applineup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.applineup.ScreenOne.AttemptLogin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ScreenThree extends Activity {

	// ArrayAdapter<String> adapter;
	ListView listview;

	SimpleAdapter adapter;

	// Progress Dialog
	private ProgressDialog mProgressDialog;

	private static final String READ_COMMENTS_URL = "http://192.168.1.148:8888/AndroidPHP/querylines3.php";
	private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/AddLineName.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	private static final String TAG_TITLE = "linein";
	private static final String TAG_POSTS = "posts";

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;

	JSONParser jsonParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ScreenThree.this);
		String first = sp.getString("Postal", null);
		String second = sp.getString("latitude", null);
		String third = sp.getString("longitude", null);

		getActionBar().setTitle(" Lat:" + second + "Long:" + third);

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
			// Toast.makeText(this, "Refresh selected",
			// Toast.LENGTH_SHORT).show();

			loginDialog();
			/*
			 * //ALL THIS WILL ADD ANOTHER SCREEN WITH ANOTHER LIST AND THE WORD
			 * "Back" in it: mCommentList = new ArrayList<HashMap<String,
			 * String>>();
			 * 
			 * HashMap<String, String> map = new HashMap<String, String>();
			 * 
			 * map.put(TAG_TITLE, "Back"); mCommentList.add(map);
			 * 
			 * listview = (ListView) findViewById(R.id.listview);
			 * map.putAll(map); Log.d("Map list:", "" + mCommentList); adapter =
			 * new SimpleAdapter(this, mCommentList, R.layout.single_post2, new
			 * String[] { TAG_TITLE, "Postal_Code" }, new int[] { R.id.title,
			 * R.id.coordinates });
			 * 
			 * adapter.notifyDataSetChanged(); listview.setAdapter(adapter); //
			 * listview.setAdapter(adapter);
			 */
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

				String title = c.getString(TAG_TITLE);
				// String content = c.getString(TAG_MESSAGE);
				// String username = c.getString(TAG_USERNAME);
				Log.d("Inbox : ", "" + mComments);

				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(ScreenThree.this);
				String first = sp.getString("Postal", null);
				String second = sp.getString("latitude", null);
				String third = sp.getString("longitude", null);

				Log.d("Postal", "Code:" + first);
				/*
				 * String post_username = sp.getString("Postal", null); String
				 * post_username2 = sp.getString("latitude", null); String
				 * post_username3 = sp.getString("longitude", null);
				 */

				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_TITLE, title);
				map.put("Postal_Code", first);
				// map.put(TAG_USERNAME, username);
				map.put("blood", "Back");
				mCommentList.add(map);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateList() {

		listview = (ListView) findViewById(R.id.listview);

		adapter = new SimpleAdapter(this, mCommentList, R.layout.single_post2,
				new String[] { TAG_TITLE, "Postal_Code" }, new int[] {
						R.id.title, R.id.coordinates });

		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i2 = getIntent();
				final String lName = i2.getStringExtra("lname");

				Intent i3 = new Intent(ScreenThree.this,
						AndroidTabAndListView.class);

				String item3 = adapter.getItem(position).toString();
				String item4 = item3.replace("{linein=", "");
				String item5 = item4.replace("}", "");

				Log.d("This is debug", "This grabs the info at the position:"
						+ item3 + "it." + item4 + item5);

				i3.putExtra("linename", item5);
				i3.putExtra("Storename", lName);

				Log.d("I selected:", "" + item5 + lName);
				startActivity(i3);

			}
		});
	}

	public void loginDialog() {

		// Create Object of Dialog class
		final Dialog login = new Dialog(this);
		// Set GUI of login screen
		login.setContentView(R.layout.addline);
		login.setTitle("Add New Line");

		// Init button of login GUI
		Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
		Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
		final EditText txtLinename = (EditText) login
				.findViewById(R.id.txtLineName);
		// final EditText txtPassword = (EditText)
		// login.findViewById(R.id.txtPassword);

		// Attached listener for login GUI button
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (txtLinename.getText().toString().trim().length() > 0) {
					// Validate Your login credential here than display message

					final String linenumber = txtLinename.getText().toString();
					// final String pword = txtPassword.getText().toString();

					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(ScreenThree.this);
					Editor edit = sp.edit();
					edit.putString("linename", linenumber);
					// edit.putString("password", pword);

					edit.commit();
					Log.d("request!", linenumber);

					new AttemptLogin().execute();

					// Redirect to dashboard / home screen.
					login.dismiss();
				} else {
					Toast.makeText(ScreenThree.this,
							"Please enter a Line Name", Toast.LENGTH_LONG)
							.show();

				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login.dismiss();
			}
		});

		// Make dialog box visible.
		login.show();

	}

	/*
	 * Problem stolved: 1)make a new php script that searches the database for
	 * the line name that you pass..... BUT make when it comes to the part where
	 * it sends a 1 for success which means you found that line name, make it
	 * send a 0 on the php script... then change the part with a 0 to a 1 AND
	 * add an INSERT INTO statemnt their to insert the new line name into the
	 * DB.
	 * 
	 * -ALSO: for this page i need to change attemptlogin and loginDialog
	 * 2)change the POST_COMMENT_URL for attemptlogin to the new php script.
	 * 3)change the uname and psword strings that you pass and save.
	 */

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(ScreenThree.this);
			mProgressDialog.setMessage("Attempting to add line...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			// This passes the text from alert dialog so that i can log in with
			// it:
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ScreenThree.this);
			String linenumber = sp.getString("linename", null);
			// String password = sp.getString("password", null);
			Log.d("request!", linenumber);

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("linename", linenumber));
				// params.add(new BasicNameValuePair("password", password));

				Log.d("request!", linenumber);

				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL,
						"POST", params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());

					// save user data
					/*
					 * SharedPreferences sp = PreferenceManager
					 * .getDefaultSharedPreferences(Login3.this); Editor edit =
					 * sp.edit(); edit.putString("username", username);
					 * edit.commit();
					 */

					Intent i = new Intent(ScreenThree.this, Inbox.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);

				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			mProgressDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(ScreenThree.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

}