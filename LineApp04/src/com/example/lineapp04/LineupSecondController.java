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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LineupSecondController extends Activity {

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

		Intent intent1 = getIntent();
		final String lName = intent1.getStringExtra("lname");

		Log.d("This is debug", "Variables are:" + lName + "it.");

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
			mProgressDialog = new ProgressDialog(LineupSecondController.this);
			// Set progress dialog title
			mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
			// Set progress dialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progress dialog
			mProgressDialog.show();
		}

		protected Void doInBackground(Void... params) {

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Wallmart");
			query.whereEqualTo("LineIn", "Line1");
			query.countInBackground(new CountCallback() {
				public void done(int count, ParseException e) {
					if (count >= 1) {

						Log.d("Line 1 has", "people in it.");
					} else {

					}
				}
			});
			return null;
		}

		protected void onPostExecute(Void result) {
			// Locate the listView in listview_main.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into an ArrayAdapter
			adapter = new ArrayAdapter<String>(LineupSecondController.this,
					R.layout.listview_item);

			Intent intent1 = getIntent();
			final String lName = intent1.getStringExtra("lname");

			ParseQuery<ParseObject> query = ParseQuery.getQuery(lName);
			query.whereEqualTo("LineIn", "Line1");
			// query.whereStartsWith("LineIn", "Line1");
			query.countInBackground(new CountCallback() {
				public void done(int count, ParseException e) {
					if (count >= 1) {
						ParseQuery<ParseObject> query = ParseQuery
								.getQuery(lName);
						query.whereEqualTo("LineIn", "Line2");
						query.countInBackground(new CountCallback() {
							public void done(int count, ParseException e) {
								if (count >= 1) {
									ParseQuery<ParseObject> query = ParseQuery
											.getQuery(lName);
									query.whereEqualTo("LineIn", "Line3");
									query.countInBackground(new CountCallback() {
										public void done(int count,
												ParseException e) {
											if (count >= 1) {

												// The count request succeeded.
												// Log the count
												adapter.add("Line3");

												Log.d("Line 3 has",
														"people in it.");
												

											} else {
												// The request failed

											}
										}
									});

									// The count request succeeded. Log the
									// count
									adapter.add("Line2");

									Log.d("Line 2 has", "people in it.");

								} else {
									// The request failed

								}
							}
						});
						// The count request succeeded. Log the count
						adapter.add("Line1");

						Log.d("Line 1 has", "people in it.");

					} else {
						// The request failed
						Log.d("There are no people", "in any line");

					}
				}
			});

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
					Intent i2 = new Intent(LineupSecondController.this,
							LineupThirdController.class);

					String item3 = adapter.getItem(position).toString();
					i2.putExtra("name", adapter.getItem(position).toString());
					i2.putExtra("Storename", lName);

					Log.d("This is debug", "Variables are:" + item3 + "it.");

					boolean blah = false;
					if (!blah) {

					}

					startActivity(i2);
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

			blankmessage();
			
			//Make it a drop down list: with selections line1, line2, line3, ect; then on each selection
			//query the database for a count, and if count succeeds then attach a alert and dont allow user to add that one.
			/*
			if (!ob.contains("Line2")) {
				//adapter.add("Line2");
				Log.d("Line2", "was added");

			}
			
			
			else if (!ob.contains("Line3")) {
				adapter.add("Line3");
				Log.d("Line3", "was added");

			} else if (!ob.contains("Line4")) {
				adapter.add("Line4");
				Log.d("Line4", "was added");

			}
			*/

			// Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
			break;
		// action with ID action_settings was selected
		case R.id.action_settings:

			// could also work by quering the database for specific Line2, 3,
			// and Line4.

			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
					.show();
			break;
			
	//HERE IS WHERE I MUST COPY FOR LINE2, LINE3, ECT.....		
		case R.id.action_line1:
			//Toast.makeText(this, "Line1 added", Toast.LENGTH_SHORT).show();
			Intent intent1 = getIntent();
			final String lName = intent1.getStringExtra("lname");

			String goog = item.getTitle().toString();
//NOTE: when doing this for the others, you must use THE TITLE so it must BE CORRECT.
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery(lName);
			query.whereEqualTo("LineIn", goog);
			query.countInBackground(new CountCallback() {
				public void done(int count, ParseException e) {
					if (count >= 1) {
						alertforAddingLines();
						Log.d("Line 1 has", "already added.");
					} else {
						adapter.add("Line1");
					}
				}
			});
			
			
			break;

		default:
		}
		return true;

	}
	
	public void blankmessage(){
		Log.d("Line2" + ob + adapter, "was added");
		String bling = adapter.getItem(0);
		
		// if count != Line 2, add Line 2;
		//else if  count != Line3, add Line 3
		//else if count != Line4, add Line 4
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Wallmart");
		query.whereEqualTo("LineIn", "Line1");
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (count >= 1) {

					Log.d("Line 1 has", "people in it.");
				} else {

				}
			}
		});
		


		Log.d("Line2" +  bling , "was added" );
		Log.d("bbbbbb" , "was added");


	}
	
	public void alertforAddingLines(){
		new AlertDialog.Builder(LineupSecondController.this)
		.setTitle("Line Already Created")
		.setMessage(
				"This Line already exists. Please choose another.")
		.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int which) {
						// continue with delete

					}
				})
				/*
		.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog,
							int which) {
						// do nothing
					}
				})
				*/
		.setIcon(android.R.drawable.ic_dialog_alert).show();
	}

}
