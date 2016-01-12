package com.example.mysqltest2;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReadLineComments2 extends Activity {

	// ArrayAdapter<String> adapter;
	ListView listview;

	SimpleAdapter adapter;

	// Progress Dialog
	private ProgressDialog mProgressDialog;

	// private static final String READ_COMMENTS_URL =
	// "http://192.168.1.110:8888/LWebservice/res/comments.php";
	private static final String READ_COMMENTS_URL = "http://192.168.1.110:8888/AndroidPHP/linecomments.php";

	// JSON IDS:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_TITLE = "title";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_POST_ID = "post_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_MESSAGE = "message";

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

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(ReadLineComments2.this);
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

		// Instantiate the arraylist to contain all the JSON data.
		// we are going to use a bunch of key-value pairs, referring
		// to the json element name, and the content, for example,
		// message it the tag, and "I'm awesome" as the content..

		mCommentList = new ArrayList<HashMap<String, String>>();

		// Bro, it's time to power up the J parser
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String title = c.getString(TAG_TITLE);
				String content = c.getString(TAG_MESSAGE);
				String username = c.getString(TAG_USERNAME);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_TITLE, title);
				map.put(TAG_MESSAGE, content);
				map.put(TAG_USERNAME, username);

				// adding HashList to ArrayList
				mCommentList.add(map);

				// annndddd, our JSON data is up to date same with our array
				// list
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateList() {
		// For a ListActivity we need to set the List Adapter, and in order to
		// do
		// that, we need to create a ListAdapter. This SimpleAdapter,
		// will utilize our updated Hashmapped ArrayList,
		// use our single_post xml template for each item in our list,
		// and place the appropriate info from the list to the
		// correct GUI id. Order is important here.
		// Locate the listView in listview_main.xml
		listview = (ListView) findViewById(R.id.listview);
		// Pass the results into an ArrayAdapter

		/*
		 * adapter = new ArrayAdapter<String>(ReadLineComments2.this,
		 * mCommentList R.layout.listview_item, new String[] { TAG_TITLE,
		 * TAG_MESSAGE, TAG_USERNAME }, new int[] { R.id.title, R.id.message,
		 * R.id.username });
		 */

		adapter = new SimpleAdapter(this, mCommentList, R.layout.single_post,
				new String[] { TAG_TITLE, TAG_MESSAGE, TAG_USERNAME },
				new int[] { R.id.title, R.id.message, R.id.username });

		// Binds the Adapter to the ListView
		listview.setAdapter(adapter);

		// Optional: when the user clicks a list item we
		// could do something. However, we will choose
		// to do nothing...
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i2 = new Intent(ReadLineComments2.this, Lines.class);

				// String item2 = lv.getItemAtPosition(position).toString();

				String item3 = adapter.getItem(position).toString();
				i2.putExtra("name", adapter.getItem(position).toString());

				Log.d("This is debug", "This grabs the info at the position:"
						+ item3 + "it.");
				startActivity(i2);

				// This method is triggered if an item is click within our
				// list. For our example we won't be using this, but
				// it is useful to know in real life applications.

			}
		});
	}

}