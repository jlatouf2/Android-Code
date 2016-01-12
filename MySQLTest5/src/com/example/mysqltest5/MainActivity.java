package com.example.mysqltest5;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	String classes[] = { "Login", "Nextpage", "Second", "Third", "Fourth"  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, classes));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//cheese equals the position that is clicked on the activity[which is the string[] position]
		String cheese = classes[position];

		try{
		@SuppressWarnings("rawtypes")
		Class ourClass = Class.forName("com.example.mysqltest5." + cheese);
		Intent ourIntent = new Intent(MainActivity.this, ourClass);
	    startActivity(ourIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}

	}
	
}