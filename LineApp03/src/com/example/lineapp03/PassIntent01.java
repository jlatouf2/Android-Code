package com.example.lineapp03;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class PassIntent01 extends Activity {

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
  
   //Add a timer to delay loading of tableView
   final Handler handler = new Handler();
   handler.postDelayed(new Runnable() {
     @Override
     public void run() {
   	//  adapter.add("Line3");
       //Do something after 100ms
   	   // adapter.notifyDataSetChanged();
    	 //listview.invalidateViews();

     }
   }, 200);
   
   
	}
	
	  // RemoteDataTask AsyncTask
private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
   @Override
   protected void onPreExecute() {
       super.onPreExecute();
       // Create a progress dialog
       mProgressDialog = new ProgressDialog(PassIntent01.this);
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
	      // The request failed
	    	//Log.d("There are no people","in any line");

	    }
	  }
	});
		return null;
	}
	
protected void onPostExecute(Void result) {
   // Locate the listView in listview_main.xml
   listview = (ListView) findViewById(R.id.listview);
   // Pass the results into an ArrayAdapter
   adapter = new ArrayAdapter<String>(PassIntent01.this,
           R.layout.listview_item);
   

   ParseQuery<ParseObject> query = ParseQuery.getQuery("Wallmart");
	query.whereEqualTo("LineIn", "Line1");
	//query.whereStartsWith("LineIn", "Line1");
	query.countInBackground(new CountCallback() {
	  public void done(int count, ParseException e) {
	    if (count >= 1) {
	        ParseQuery<ParseObject> query = ParseQuery.getQuery("Wallmart");
	    	query.whereEqualTo("LineIn", "Line2");
	    	query.countInBackground(new CountCallback() {
	    	  public void done(int count, ParseException e) {
	    	    if (count >= 1) {
	    	        ParseQuery<ParseObject> query = ParseQuery.getQuery("Wallmart");
	    	    	query.whereEqualTo("LineIn", "Line3");
	    	    	query.countInBackground(new CountCallback() {
	    	    	  public void done(int count, ParseException e) {
	    	    	    if (count >= 1) {

	    	    	
	    	    	    	// The count request succeeded. Log the count
	    	               adapter.add("Line3");

	    	    	    	Log.d("Line 3 has", "people in it.");

	    	    	    } else {
	    	    	      // The request failed

	    	    	    }
	    	    	  }
	    	    	});
	    	    	
	    	    	// The count request succeeded. Log the count
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
	    	Log.d("There are no people","in any line");

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
           Intent i = new Intent(PassIntent01.this,
                   SingleItemView.class);
        //   int item = listview.getPositionForView(view);
         //  String blah = (String) listview.getSelectedItem();
          // String item2 = listview.toString();
          String item3 = adapter.getItem(position).toString();
            i.putExtra("name", adapter.getItem(position).toString());

        // String item2 = ob.toString();
	    Log.d("This is debug", "Variables are:" + item3 + "it.");
          // i.putExtra(name, value)
           //i.putExtra("name", ob.toString());
           // Pass data "name" followed by the position
         //  i.putExtra("LineIn", ob.get(position).getString("Line")
           //        .toString());
           // Open SingleItemView.java Activity
           startActivity(i); 
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
	        	 Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
	             .show();
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
