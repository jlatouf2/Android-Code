package com.example.alphaapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Profile extends ListActivity {

	ListView listview;
	private EditText user, pass;

	SimpleAdapter adapter;

	// Progress Dialog
	private ProgressDialog pDialog;

	private static final String READ_COMMENTS_URL = "http://192.168.1.148:8888/AlphaApp01/querylocation.php";

	JSONParser jsonParser = new JSONParser();

	private static final String LOGIN_URL = "http://192.168.1.148:8888/AlphaApp01/addlocation.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	// JSON IDS:
	// private static final String TAG_SUCCESS = "success";
	private static final String TAG_Longitude = "Longitude";
	private static final String TAG_Latitude = "Latitude";

	private static final String TAG_POSTS = "posts";

	private LocationManager locationManager;
	private String provider;
	
	private  String book;
	private String book2;

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

			 book = String.valueOf(lat2);
			 book2 = String.valueOf(lng2);
			Log.d("Black!", "number" + book);
			Log.d("White!", "number" + book2);
			
			// save user data
			
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(Profile.this);
			Editor edit = sp.edit();
			edit.putString("latitude", book);
			edit.putString("longitude", book2);

			edit.commit();


			
			new PostComment().execute();


		} else {
			// latituteField.setText("Location not available");
			// longitudeField.setText("Location not available");

		}
		new LoadInbox().execute();

		//new PassLocation().execute();
		// Hashmap for ListView
		inboxList = new ArrayList<HashMap<String, String>>();

		// Loading INBOX in Background Thread
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
			pDialog = new ProgressDialog(Profile.this);
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
					 */

					// String math1 = (title + 50);

					// Log.d("Inbox : ", title);
					// Log.d("Inbox : ", math1);

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

					
					ListAdapter adapter = new SimpleAdapter(Profile.this,
							inboxList, R.layout.inbox_list_item, new String[] {
									TAG_Longitude, TAG_Latitude }, new int[] {
									R.id.from, R.id.subject });

					// updating listview
					setListAdapter(adapter);

					ListView listView = getListView();

					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub

							AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
									Profile.this);

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

	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
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
       //     String post_title = title.getText().toString();
       //     String post_message = message.getText().toString();
            
          // Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Profile.this);
            String post_username = sp.getString("latitude", null);
            String post_username2 = sp.getString("longitude", null);

            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("latitude", "Bob"));
                params.add(new BasicNameValuePair("longitude", "Dan"));
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                		LOGIN_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());    
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
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
            if (file_url != null){
            	Toast.makeText(Profile.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
	


}
