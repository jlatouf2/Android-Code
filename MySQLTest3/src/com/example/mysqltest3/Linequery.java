package com.example.mysqltest3;

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


public class Linequery extends Activity {

	ListView listview;
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



	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progress dialog
			mProgressDialog = new ProgressDialog(Linequery.this);
			// Set progress dialog title
			mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
			// Set progress dialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progress dialog
			mProgressDialog.show();
		}

		protected Void doInBackground(Void... params) {


			return null;
		}

		protected void onPostExecute(Void result) {
			Intent i2 = getIntent();

			// Get the name
			String name2 = i2.getStringExtra("lname");

			// Locate the listView in listview_main.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into an ArrayAdapter
			adapter = new ArrayAdapter<String>(Linequery.this,
					R.layout.single_post2);
			
			

			adapter.add(name2);
			
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
/*
					Intent i = new Intent(Linequery.this,
							SingleItemView.class);
					String item3 = adapter.getItem(position).toString();
					i.putExtra("name", adapter.getItem(position).toString());

					Log.d("This is debug", "Variables are:" + item3 + "it.");

					startActivity(i);
					*/
				}
			});

			listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View v,
						final int index, long arg3) {
					// TODO Auto-generated method stub
					// Toast.makeText(LineupThirdController.this,
					// "Your Message", Toast.LENGTH_LONG).show();


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
