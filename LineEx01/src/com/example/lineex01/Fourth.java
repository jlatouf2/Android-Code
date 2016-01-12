package com.example.lineex01;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
		
		adapter.add(name2);
		
		// lblUsername.setVisibility(View.GONE);

        listView.setAdapter(adapter);
        
        
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//Getting Previous Intent info and setting to to new variable
				Intent i2 = getIntent();
				final String lName = i2.getStringExtra("lname");


				Intent i3 = new Intent(Fourth.this, Third.class);

				// String item2 = lv.getItemAtPosition(position).toString();
				
				// To get it to pass the right Line I needed trim the excess
				// off:
				
				//can use for loop and make it so that 
				String item3 = adapter.getItem(position).toString();
				String item4 = item3.replace("{linein=", "");
				String item5 = item4.replace("}", "");

				Log.d("This is debug", "This grabs the info at the position:"
						+ item3 + "it." + item4 + item5);

				// i2.putExtra("name", adapter.getItem(position).toString());
				i3.putExtra("linename", item5);
				i3.putExtra("Storename", lName);
				
				Log.d("I selected:", "" + item5 + lName);
				startActivity(i3);

			}
		});



    }
}
