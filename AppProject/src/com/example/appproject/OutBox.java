package com.example.appproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class OutBox extends ListActivity {
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> outboxList;

	// products JSONArray
	JSONArray outbox = null;

	// Outbox JSON url
	private static final String OUTBOX_URL = "http://192.168.1.148:8888/AndroidPHP/Name_Position.php";
	
	// ALL JSON node names
	/*
	private static final String TAG_MESSAGES = "messages";
	private static final String TAG_ID = "id";
	private static final String TAG_TO = "to";
	private static final String TAG_SUBJECT = "subject";
	private static final String TAG_DATE = "date";
	*/
	private static final String TAG_MESSAGES = "posts";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_POST_ID = "post_id";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox_list);
		
		// Hashmap for ListView
        outboxList = new ArrayList<HashMap<String, String>>();
 
        // Loading OUTBOX in Background Thread
        new LoadOutbox().execute();
	}

	/**
	 * Background Async Task to Load all OUTBOX messages by making HTTP Request
	 * */
	class LoadOutbox extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OutBox.this);
			pDialog.setMessage("Loading Outbox ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Outbox JSON
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(OUTBOX_URL, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("Outbox JSON: ", json.toString());

			try {
				outbox = json.getJSONArray(TAG_MESSAGES);
				// looping through All messages
				for (int i = 0; i < outbox.length(); i++) {
					JSONObject c = outbox.getJSONObject(i);

					
					
					String title = c.getString(TAG_USERNAME);
					String title2 = c.getString(TAG_MESSAGE);
					String title3 = c.getString(TAG_POST_ID);

					// String content = c.getString(TAG_MESSAGE);
					// String username = c.getString(TAG_USERNAME);


					// map.put(TAG_MESSAGE, content);
					// map.put(TAG_USERNAME, username);
					Log.d("Text", "returned" + title + title2 + title3);

					/*
					// Storing each json item in variable
					String id = c.getString(TAG_ID);
					String to = c.getString(TAG_TO);
					String subject = c.getString(TAG_SUBJECT);
					String date = c.getString(TAG_DATE);
					
					
					// subject taking only first 23 chars
					// to fit into screen
					if(subject.length() > 23){
						subject = subject.substring(0, 22) + "..";
					}
					*/
					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_USERNAME, title);
					map.put(TAG_MESSAGE, title2);
					map.put(TAG_POST_ID, title3);

					// adding HashList to ArrayList
					outboxList.add(map);
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
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							OutBox.this, outboxList,
							R.layout.outbox_list_item, new String[] { TAG_USERNAME, TAG_MESSAGE, TAG_POST_ID },
							new int[] { R.id.subject, R.id.to, R.id.date });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}