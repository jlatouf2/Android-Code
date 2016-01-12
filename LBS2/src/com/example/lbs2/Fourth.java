package com.example.lbs2;


import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fourth extends Activity {
    Context context;
    List<String> tasks;
    ArrayAdapter<String> adapter;

    /**
     * Called when the activity is first created.
     */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        
        context = this;
        tasks = new ArrayList<String>();

        // instances all your variables on initial only
     //   Button add = (Button) findViewById(R.id.button);
      //  final EditText editText = (EditText) findViewById(R.id.editText);

        //WORKS B/C THIS ARRAY HAS A ARRAYLIST ADDED TO IT.
        // second parameter is row layout, 
        /*
         * 		
		setListAdapter(new ArrayAdapter<String>(Listview1.this, android.R.layout.simple_list_item_1, classes));
		Intent i = getIntent();

         */
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,tasks);
        ListView listView = (ListView) findViewById(R.id.listview);
		Intent i6 = getIntent();

		// Get the name
		String name2 = i6.getStringExtra("lname");

		Log.d("This is name in listview:", "" + name2);
		
		adapter.add("Blahal");
		
		// lblUsername.setVisibility(View.GONE);

        listView.setAdapter(adapter);
        
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//Getting Previous Intent info and setting to to new variable



			}
		});



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
			
			/*
			
	        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,tasks);
	        ListView listView = (ListView) findViewById(R.id.listview);
			Intent i6 = getIntent();

			// Get the name
			String name2 = i6.getStringExtra("lname");

			Log.d("This is name in listview:", "" + name2);
			*/
			adapter.add("This is it");
			adapter.notifyDataSetChanged();
			// lblUsername.setVisibility(View.GONE);
			
	     //   listView.setAdapter(adapter);

	        break;
			
			//myListView.invalidateViews();
			//notifyDataSetChanged().
			
			
			//adapter.add("New Item");

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
