package com.example.applineup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Inbox extends ListActivity {

	ListView listview;

	SimpleAdapter adapter;

	// Progress Dialog
	private ProgressDialog pDialog;

	// private static final String READ_COMMENTS_URL =
	// "http://192.168.1.110:8888/LWebservice/res/comments.php";
	private static final String READ_COMMENTS_URL = "http://192.168.1.148:8888/AndroidPHP/querylocation.php";

	// JSON IDS:
	// private static final String TAG_SUCCESS = "success";
	private static final String TAG_Longitude = "Longitude";
	private static final String TAG_Latitude = "Latitude";

	private static final String TAG_POSTS = "posts";

	private LocationManager locationManager;
	private String provider;

	// An array of all of our comments
	JSONArray inbox = null;
	// manages all of our comments in a list.
	// ArrayList<HashMap<String, String>> mCommentList;
	ArrayList<HashMap<String, String>> inboxList;

	// ArrayList<HashMap<String, String>> inboxList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_list);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			// onLocationChanged(location);

			int lat2 = (int) (location.getLatitude());
			int lng2 = (int) (location.getLongitude());
			// latituteField.setText(String.valueOf(lat));
			// / longitudeField.setText(String.valueOf(lng));

			String book = String.valueOf(lat2);
			String book2 = String.valueOf(lng2);
			Log.d("Black!", "number" + book);
			Log.d("White!", "number" + book2);

		} else {
			// latituteField.setText("Location not available");
			// longitudeField.setText("Location not available");

		}

		// Hashmap for ListView
		inboxList = new ArrayList<HashMap<String, String>>();

		// Loading INBOX in Background Thread
		new LoadInbox().execute();
	}

	/**
	 * Background Async Task to Load all INBOX messages by making HTTP Request
	 * */
	class LoadInbox extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Inbox.this);
			pDialog.setMessage("Loading Inbox ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 * */
		protected String doInBackground(String... args) {
			// mCommentList = new ArrayList<HashMap<String, String>>();

			JSONParser jsonParser = new JSONParser();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(READ_COMMENTS_URL,
					"GET", params);

			// Check your log cat for JSON reponse
			Log.d("Inbox JSON: ", json.toString());

			try {

				inbox = json.getJSONArray(TAG_POSTS);

				for (int i = 0; i < inbox.length(); i++) {
					JSONObject c = inbox.getJSONObject(i);

					String title = c.getString(TAG_Longitude);
					String title2 = c.getString(TAG_Latitude);

					/*
					 * Location location1=new Location("locationA");
					 * location1.setLatitude(45.47193);
					 * location1.setLongitude(-70.33654); Location location2=new
					 * Location("locationA"); location2.setLatitude(45.70617);
					 * location2.setLongitude(-68.13666); double
					 * distance=location1.distanceTo(location2);
					 * 
					 * String s="25.6666"; Double d = Double.parseDouble(s);
					 * String s2="14.444"; Double d2 = Double.parseDouble(s2);
					 */
					// This is longitude and latitude returned from database in
					// table Location
					String s = title;
					Double d = Double.parseDouble(s);
					String s2 = title2;
					Double d2 = Double.parseDouble(s2);

					Log.d("Beforedouble : ", s);
					Log.d("Beforedouble : ", s2);

					Log.d("AfterDouble : ", "" + d);
					Log.d("AfterDouble : ", "" + d2);

					// This is the saved long and latitude of line:
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Inbox.this);
					String first = sp.getString("Postal", null);
					String second = sp.getString("latitude", null);
					String third = sp.getString("longitude", null);

					Log.d("second : ", "" + second);
					Log.d("third : ", "" + third);

					// Even though its a double, its saved as a string so have
					// to set to double again:
					Double doublefirst = Double.parseDouble(second);
					Double doublesecond = Double.parseDouble(third);
					Log.d("AfterDouble : ", "" + doublefirst);
					Log.d("AfterDouble : ", "" + doublesecond);

					// ALL ANSWERS ARE IN METERS!!!!!!!!!!
					// DIVIDE ANSWER ON ANDROID BY 1000 TO GET KILOMETERS.....
					Location location1 = new Location("locationA");
					location1.setLatitude(d2);
					location1.setLongitude(d);
					Location location2 = new Location("locationB");
					location2.setLatitude(doublefirst);
					location2.setLongitude(doublesecond);
					double distance = location1.distanceTo(location2);
					/*
					 * Location location3=new Location("locationA");
					 * location3.setLatitude(42.1102930);
					 * location3.setLongitude(-82.038495); Location
					 * location4=new Location("locationB");
					 * location4.setLatitude(44.039485);
					 * location4.setLongitude(-84.343434); double
					 * distance2=location3.distanceTo(location4); Log.d("Dis",
					 * "Stuf" + distance2);
					 * 
					 * The answer to this is: 284992.25 meters. (the answer on
					 * web is: 284.777 km) This means ALL ANSWERS ARE IN
					 * METERS!!!!!!!!!!
					 */
					Log.d("InboxBobby : ","" + distance);

					String yourDoubleString = String.valueOf(distance);
					Log.d("InboxBobby : ", yourDoubleString);
					Log.d("InboxBobby : ", title2);

					// String math1 = (title + 50);

					// Log.d("Inbox : ", title);
					// Log.d("Inbox : ", math1);

					// String content = c.getString(TAG_MESSAGE);
					// String username = c.getString(TAG_USERNAME);

					HashMap<String, String> map = new HashMap<String, String>();
					Log.d("Text", "returned" + title + title2);

					map.put(TAG_Longitude, yourDoubleString);
					// map.put(TAG_Longitude, title);
					map.put(TAG_Latitude, title2);

					Log.d("Inbox : ", TAG_Longitude);
					Log.d("Inbox : ", TAG_Latitude);

					// map.put(TAG_MESSAGE, content);
					// map.put(TAG_USERNAME, username);

					inboxList.add(map);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			/*
			 * // Building Parameters List<NameValuePair> params = new
			 * ArrayList<NameValuePair>();
			 * 
			 * // getting JSON string from URL JSONObject json =
			 * jsonParser.makeHttpRequest(READ_COMMENTS_URL, "GET", params);
			 * 
			 * // Check your log cat for JSON reponse Log.d("Inbox JSON: ",
			 * json.toString());
			 * 
			 * try { inbox = json.getJSONArray(TAG_MESSAGES); // looping through
			 * All messages for (int i = 0; i < inbox.length(); i++) {
			 * JSONObject c = inbox.getJSONObject(i);
			 * 
			 * // Storing each json item in variable String id =
			 * c.getString(TAG_ID); String from = c.getString(TAG_FROM); String
			 * subject = c.getString(TAG_SUBJECT); String date =
			 * c.getString(TAG_DATE);
			 * 
			 * // creating new HashMap HashMap<String, String> map = new
			 * HashMap<String, String>();
			 * 
			 * // adding each child node to HashMap key => value map.put(TAG_ID,
			 * id); map.put(TAG_FROM, from); map.put(TAG_SUBJECT, subject);
			 * map.put(TAG_DATE, date);
			 * 
			 * // adding HashList to ArrayList inboxList.add(map); }
			 * 
			 * } catch (JSONException e) { e.printStackTrace(); }
			 * 
			 * return null;
			 */
			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */

					// listview = (ListView) findViewById(R.id.listview);
					// Pass the results into an ArrayAdapter
					Log.d("InboxPost : ", TAG_Longitude);
					Log.d("InboxPost : ", TAG_Latitude);

					/*
					 * adapter = new
					 * ArrayAdapter<String>(ReadLineComments2.this, mCommentList
					 * R.layout.listview_item, new String[] { TAG_TITLE,
					 * TAG_MESSAGE, TAG_USERNAME }, new int[] { R.id.title,
					 * R.id.message, R.id.username });
					 * 
					 * 
					 * ListAdapter adapter = new
					 * SimpleAdapter(InboxActivity.this, inboxList,
					 * R.layout.inbox_list_item, new String[] { TAG_FROM,
					 * TAG_SUBJECT, TAG_DATE }, new int[] { R.id.from,
					 * R.id.subject, R.id.date });
					 */

					ListAdapter adapter = new SimpleAdapter(Inbox.this,
							inboxList, R.layout.inbox_list_item, new String[] {
									TAG_Longitude, TAG_Latitude }, new int[] {
									R.id.from, R.id.subject });

					// updating listview
					setListAdapter(adapter);

					// USE THIS ListView listView = getListView(); to make
					// reference to list and add click events
					ListView listView = getListView();

					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
									Inbox.this);

							// Setting Dialog Title
							alertDialog2.setTitle("Confirm Delete...");

							// Setting Dialog Message
							alertDialog2
									.setMessage("Are you sure you want delete this file?");

							// Setting Positive "Yes" Btn
							alertDialog2.setPositiveButton("YES",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// Write your code here to execute
											// after dialog
											Toast.makeText(
													getApplicationContext(),
													"You clicked on YES",
													Toast.LENGTH_SHORT).show();
										}
									});
							// Setting Negative "NO" Btn
							alertDialog2.setNegativeButton("NO",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// Write your code here to execute
											// after dialog
											Toast.makeText(
													getApplicationContext(),
													"You clicked on NO",
													Toast.LENGTH_SHORT).show();
											dialog.cancel();
										}
									});

							// Showing Alert Dialog
							alertDialog2.show();

						}

					});

				}
			});

		}

	}
}
