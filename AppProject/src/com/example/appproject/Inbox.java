package com.example.appproject;

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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Inbox extends ListActivity {
	
	
	/**
	 * TO MAKE THE APP:
	 * 1)Needs to have 3 lists 
	 * 1.Position with numberical order (post_id)
	 * 2.Displacement 
	 * 3.This could be used to limit the number of people who are close to the cashier to only positions 
	 * 1 through 10, so that only those positions could exit with displacment being the factor instead of 
	 * lineup number. (ie. number 50 cannot just go to front of line and exit b/c he is very far
	 * down on numbered position).
	 * 3.Best of both position and displacement [dont really need this but could be useful]
	 * ie. the name with the smallest distance(closer to cashier) AND lowest number (1).
	 * 
	 * 
	 * NOTE : The lineups will work like this:
	 * 1. Person who starts a line will make it show up for other people to be able to select it on
	 * their devices.... 
	 * 2. WHEN THEY SELECT LINE IT WILL SEND THE COORDINATES FROM THAT APPS POSITION!!!!!
	 * [So these 2 coordinates will be used in calculation of distance for each person].
	 * */

	/*
	 * 
	 * TO MAKE THE APP:
	 * 1)Needs to have 3 lists 
	 * 1.Position with numberical order (post_id)
	 * 2.Displacement 
	 * 3.Best of both position and displacement
	 * ie. the name with the smallest distance(closer to cashier) AND lowest number (1).
	 * 
	 * 
	 * // Progress Dialog private ProgressDialog pDialog;
	 * 
	 * // Creating JSON Parser object JSONParser jsonParser = new JSONParser();
	 * 
	 * ArrayList<HashMap<String, String>> inboxList;
	 * 
	 * // products JSONArray JSONArray inbox = null;
	 * 
	 * // Inbox JSON url private static final String INBOX_URL =
	 * "http://api.androidhive.info/mail/inbox.json";
	 * 
	 * // ALL JSON node names private static final String TAG_MESSAGES =
	 * "messages"; private static final String TAG_ID = "id"; private static
	 * final String TAG_FROM = "from"; private static final String TAG_EMAIL =
	 * "email"; private static final String TAG_SUBJECT = "subject"; private
	 * static final String TAG_DATE = "date";
	 */
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
			//onLocationChanged(location);

			int lat2 = (int) (location.getLatitude());
			int lng2 = (int) (location.getLongitude());
			// latituteField.setText(String.valueOf(lat));
			// / longitudeField.setText(String.valueOf(lng));

			String book = String.valueOf(lat2);
			String book2 = String.valueOf(lng2);
			Log.d("Black!", "number" + book);
			Log.d("White!", "number" + book2);

		} else {
		//	latituteField.setText("Location not available");
		//	longitudeField.setText("Location not available");
			
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
				    Location location1=new Location("locationA");
				    location1.setLatitude(45.47193);
				    location1.setLongitude(-70.33654);
				Location location2=new Location("locationA");
				location2.setLatitude(45.70617);
				    location2.setLongitude(-68.13666);
				double distance=location1.distanceTo(location2);
*/

				//	String math1 = (title + 50);
					
				//	Log.d("Inbox : ", title);
				//	Log.d("Inbox : ", math1);

					// String content = c.getString(TAG_MESSAGE);
					// String username = c.getString(TAG_USERNAME);

					HashMap<String, String> map = new HashMap<String, String>();
					Log.d("Text", "returned" + title + title2);
					map.put(TAG_Longitude, title);
					map.put(TAG_Latitude, title2);

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
							alertDialog2.setMessage("Are you sure you want delete this file?");


							// Setting Positive "Yes" Btn
							alertDialog2.setPositiveButton("YES",
							        new DialogInterface.OnClickListener() {
							            public void onClick(DialogInterface dialog, int which) {
							                // Write your code here to execute after dialog
							                Toast.makeText(getApplicationContext(),
							                        "You clicked on YES", Toast.LENGTH_SHORT)
							                        .show();
							            }
							        });
							// Setting Negative "NO" Btn
							alertDialog2.setNegativeButton("NO",
							        new DialogInterface.OnClickListener() {
							            public void onClick(DialogInterface dialog, int which) {
							                // Write your code here to execute after dialog
							                Toast.makeText(getApplicationContext(),
							                        "You clicked on NO", Toast.LENGTH_SHORT)
							                        .show();
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
