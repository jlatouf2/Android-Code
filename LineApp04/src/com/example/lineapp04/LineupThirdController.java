package com.example.lineapp04;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LineupThirdController extends Activity {

	ListView listview;
	List<ParseObject> ob;
	ProgressDialog mProgressDialog;
	ArrayAdapter<String> adapter;
	String classes[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_main);
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();

		// Retrieve data from MainActivity on item click event
		Intent i2 = getIntent();

		// Get the name
		String name2 = i2.getStringExtra("Storename");

		// String Linename = i2.getStringExtra("name");

		Log.d("This is Storename", ":" + name2 + ".");

		// Add a timer to delay loading of tableView
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// adapter.add("Line3");
				// Do something after 100ms
				// adapter.notifyDataSetChanged();
				// listview.invalidateViews();

			}
		}, 200);

	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(LineupThirdController.this);
			// Set progress dialog title
			mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
			// Set progress dialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progress dialog
			mProgressDialog.show();
		}

		protected Void doInBackground(Void... params) {

			Intent i2 = getIntent();
			String Linename = i2.getStringExtra("name");

			// Get the name
			String name2 = i2.getStringExtra("Storename");

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(name2);
			query.whereEqualTo("LineIn", Linename);

			query.orderByDescending("_created_at");
			try {
				ob = query.find();
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			// Locate the listView in listview_main.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into an ArrayAdapter
			adapter = new ArrayAdapter<String>(LineupThirdController.this,
					R.layout.listview_item);

			for (ParseObject country : ob) {
				adapter.add((String) country.get("username"));
			}

			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);

			// Close the progress dialog
			mProgressDialog.dismiss();
			// Capture button clicks on ListView items

			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Send single item click data to SingleItemView Class

					Intent i = new Intent(LineupThirdController.this,
							SingleItemView.class);
					String item3 = adapter.getItem(position).toString();
					i.putExtra("name", adapter.getItem(position).toString());

					Log.d("This is debug", "Variables are:" + item3 + "it.");

					startActivity(i);
				}
			});

			listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View v,
						final int index, long arg3) {
					// TODO Auto-generated method stub
					// Toast.makeText(LineupThirdController.this,
					// "Your Message", Toast.LENGTH_LONG).show();

					// String item3 = adapter.getItem(position).toString();

					Intent i2 = getIntent();
					// String Linename = i2.getStringExtra("name");

					// Get the name
					final String name2 = i2.getStringExtra("Storename");
					final String item5 = adapter.getItem(index).toString();

					ParseQuery<ParseObject> query3 = ParseQuery.getQuery(name2);
					query3.whereEqualTo("username", item5);
					query3.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							// TODO Auto-generated method stub
							if (object == null) {
								Toast.makeText(LineupThirdController.this,
										"  not found", Toast.LENGTH_SHORT)
										.show();
							} else {
								object.deleteInBackground(new DeleteCallback() {
									public void done(ParseException e) {
										if (e == null) {
											Toast.makeText(
													LineupThirdController.this,
													"  deleted",
													Toast.LENGTH_SHORT).show();
											adapter.remove(item5);

											Log.d("Name", item5
													+ "was deleted. " + name2);

										} else {
											Toast.makeText(
													LineupThirdController.this,
													"  not deleted",
													Toast.LENGTH_SHORT).show();
											e.printStackTrace();

										}
									}
								});

							}
						}

					});

					return true;
				}
			});

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

			Intent i2 = getIntent();
			// String Linename = i2.getStringExtra("name");

			// Get the name
			final String name2 = i2.getStringExtra("Storename");
			// final String item5 = adapter.getItem(index).toString();

			//THIS may not be the right one....:
			final String currentUser = ParseUser.getCurrentUser().getUsername();

			final String Linename = i2.getStringExtra("name");

			Log.d("This line has" + name2 + currentUser + Linename , "people in it with you.");

			
			
			ParseQuery<ParseObject> query2 = ParseQuery.getQuery(name2);
			query2.whereEqualTo("username", currentUser);
			query2.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (object == null) {
						//This checks if name is not in table; then you add your name as object.
						ParseQuery<ParseObject> query3 = ParseQuery
								.getQuery(name2);
						query3.whereEqualTo("LineIn", Linename);
						query3.countInBackground(new CountCallback() {
							public void done(int count, ParseException e) {
								if (e == null) {
																		
									Log.d("This line has" + count , "people in it.");
									Log.d("This line has" + (count+1) , "people in it with you.");
									
									//SEE if this int works in here:
									//int dog = count+1;
									
									//This places the username,Line number, and Line name in database:
									ParseObject gameScore = new ParseObject(name2);
									gameScore.put("username", currentUser);
									gameScore.put("LineIn", Linename);
									gameScore.put(Linename, String.valueOf(count+1));
									gameScore.saveInBackground();
									//So this works b/c the count is an int and the database saves as string so
									//I have to change it to a string value
									//adapter.add(currentUser); This adds the name to the beginning
									//Solved: so use refresh screen to add name to it
									Intent intent = getIntent();
								    finish();
								    startActivity(intent);

								} else {

								}
							}
						});

						Log.d("Currentuser", "your name has been added.");
					} else {
						
						new AlertDialog.Builder(LineupThirdController.this)
						.setTitle("Invalid Code Name")
						.setMessage(
								"You are already in line. Please delete your name first, then add it to this line.")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// continue with delete

									}
								})
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// do nothing
									}
								})
						.setIcon(android.R.drawable.ic_dialog_alert).show();
						
						Log.d("Current User", "alerady in table.");
					}
				}
			});

			/*
			 * ParseQuery<ParseObject> query = ParseQuery.getQuery(name2);
			 * query.whereEqualTo("username", currentUser);
			 * query.countInBackground(new CountCallback() { public void
			 * done(int count, ParseException e) { if (count >= 1) {
			 * 
			 * Log.d("Line 1 has", "people in it."); } else {
			 * 
			 * } } });
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

}
