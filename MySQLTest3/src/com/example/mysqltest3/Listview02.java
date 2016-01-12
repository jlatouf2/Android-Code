package com.example.mysqltest3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Listview02 extends Activity {
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
		Intent i = getIntent();

		// Get the name
		String name2 = i.getStringExtra("lname");

		Log.d("This is name in listview:", "" + name2);
		adapter.add(name2);
        listView.setAdapter(adapter);



    }
}
