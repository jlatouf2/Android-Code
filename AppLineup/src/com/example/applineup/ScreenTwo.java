package com.example.applineup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ScreenTwo extends Activity implements OnClickListener {

	private EditText title;
	private Button mSubmit;
	private TextView TView1, TView2;
	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/linebegin.php";
	private static final String POST_COMMENT_URL2 = "http://192.168.1.148:8888/AndroidPHP/Areacode.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	private LocationManager locationManager;
	private String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		first_time_check();
		setContentView(R.layout.add_comment2);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		Log.d("location:", "" + location);

		String d = getZipCodeFromLocation(location);
		Address a = getAddressFromLocation(location);
		Log.d("Zipcode:", "" + d);
		Log.d("Adress:", "" + a);

		double lat2 = (double) (location.getLatitude());

		double lng2 = (double) (location.getLongitude());

	//	int lat3 = (int) (location.getLatitude());

	//	int lng3 = (int) (location.getLongitude());
		
		String book = String.valueOf(lat2);
		String book2 = String.valueOf(lng2);

		Log.d("Black!", "number" + book);
		Log.d("White!", "number" + book2);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ScreenTwo.this);
		Editor edit = sp.edit();
		edit.putString("Postal", d);
		edit.putString("latitude", book);
		edit.putString("longitude", book2);

		edit.commit();
		/*
		 * SharedPreferences sp =
		 * PreferenceManager.getDefaultSharedPreferences(ScreenTwo.this); String
		 * post_username = sp.getString("latitude", null); String post_username2
		 * = sp.getString("longitude", null);
		 */

		Log.d("latitude:", "" + lat2);
		Log.d("longitude:", "" + lng2);

		new AreaCode().execute();

		// getZipCodeFromLocation(location);
		// getAddressFromLocation(location);

		title = (EditText) findViewById(R.id.title);

		TView1 = (TextView) findViewById(R.id.textView3);
		TView2 = (TextView) findViewById(R.id.TextView01);

		mSubmit = (Button) findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onResume();
		// first_time_check();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ScreenTwo.this);
			sp.edit().remove("username").commit();
			/*
			 * SharedPreferences settings =
			 * getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
			 * settings.edit().clear().commit();
			 * 
			 * SharedPreferences preferences = getSharedPreferences("Mypref",
			 * 0); preferences.edit().remove("text").commit();
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

	private boolean first_time_check() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(ScreenTwo.this);
		String first = sp.getString("username", null);
		Log.d("This is ", "username:" + first);

		if ((first == null)) {
			// Intent i = new Intent(ScreenTwo.this, Login3.class);
			// startActivity(i);
			// finish();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		new PostComment().execute();
	}

	class PostComment extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ScreenTwo.this);
			pDialog.setMessage("Posting Comment...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String post_title = title.getText().toString();
			Log.d("request!", post_title);

			// String post_message = message.getText().toString();

			// Retrieving Saved Username Data:
			// SharedPreferences sp =
			// PreferenceManager.getDefaultSharedPreferences(LineupBegin.this);
			// String post_username = sp.getString("username", "anon");

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("username",
				// post_username));
				params.add(new BasicNameValuePair("table", post_title));
				// params.add(new BasicNameValuePair("message", post_message));

				Log.d("request!", "starting");
				Log.d("request!", post_title);

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL,
						"POST", params);

				// full json response
				Log.d("Post Comment attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Comment Added!", json.toString());

					// if success you add an intent in here to go to page that
					// shows
					// lines with people in them......
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(ScreenTwo.this);
					String post_username = sp.getString("Postal", null);
					String post_username2 = sp.getString("latitude", null);
					String post_username3 = sp.getString("longitude", null);
					
					Log.d("Postal_Code!", post_username);
					Log.d("latitude!", post_username2);
					Log.d("longitude!", post_username3);

					Intent i = new Intent(ScreenTwo.this, ScreenThree.class);

					String text2 = title.getText().toString();

					i.putExtra("lname", text2);
					Log.d("This is the text entered:", "" + text2);
					startActivity(i);


					

					
					// Finish actually screwed up the application.....
					// finish();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(ScreenTwo.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	class AreaCode extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ScreenTwo.this);
			pDialog.setMessage("Posting Comment...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ScreenTwo.this);
			String post_username = sp.getString("Postal", null);
			String post_username2 = sp.getString("latitude", null);
			String post_username3 = sp.getString("longitude", null);

			Log.d("request!", post_username);
			Log.d("request!", post_username2);
			Log.d("request!", post_username3);

			String field = post_username.replaceAll("\\s+", "");
			Log.d("Trimmed", "" + field);

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Postal", field));

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL2,
						"POST", params);

				// full json response
				Log.d("Post Comment attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					Log.d("Comment Added!", json.toString());
					Log.d("Comment Added!", json.getString("posts"));

					// THIS IS PART THAT

					JSONArray mComments = json.getJSONArray("posts");

					for (int i = 0; i < mComments.length(); i++) {
						JSONObject c = mComments.getJSONObject(i);

						final String title = c.getString("Store");
						// String content = c.getString(TAG_MESSAGE);
						// String username = c.getString(TAG_USERNAME);
						// Log.d("Inbox : ","" + mComments);
						Log.d("Storename : ", "" + title);
						// YOU TAKE TO GET POSTS AND GET SPECIFIC DATA FROM THEM

						SharedPreferences sp2 = PreferenceManager
								.getDefaultSharedPreferences(ScreenTwo.this);
						Editor edit = sp2.edit();
						edit.putString("Splash", title);

						edit.commit();

					}

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {

				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(ScreenTwo.this);
				String post_username = sp.getString("Splash", null);

				Toast.makeText(ScreenTwo.this, file_url, Toast.LENGTH_LONG)
						.show();
				TView1.setText(post_username);
				
			}

		}

	}

	private String getZipCodeFromLocation(Location location) {
		Address addr = getAddressFromLocation(location);
		return addr.getPostalCode() == null ? "" : addr.getPostalCode();
	}

	private Address getAddressFromLocation(Location location) {
		Geocoder geocoder = new Geocoder(this);
		Address address = new Address(Locale.getDefault());
		try {
			List<Address> addr = geocoder.getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);
			if (addr.size() > 0) {
				address = addr.get(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}

}
