package com.example.mysqltest2;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

	/*
	 * To use Parse:
	 * 1) Add bolts, jar, jar.properties to libs
	 * 2) Add      <uses-permission android:name="android.permission.INTERNET" />
    	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
		to manifest file.
	   3) impart all parse files to jave file that will use Parse
	   4) Add  Parse.initialize(this, "jdmcP67zhEK5NYqxzEk0PCwAIc9khqL4K9LhkSyC", "YWonSDK6FpgcYvTw0QPFZ4eVe1qVVofg6PPWgWNF");

	 */
	
	
	String classes[] = { "Login", "Register", "AddComment", "ReadComments", "Lines", "ReadLineComments",
	"ReadLineComments2", "ReadLineup01", "AddLineup01", "ReadLineStuff", "LineupBegin" };

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
			/*
			 Class method example
			Class ourClass = Class.forName("com.example.bucky.Bucky");
			
			THIS IS OUR CLASS NAME, not our ACTION name that is defined in manifest; thats why
			it has NO Capitals
			 */
		@SuppressWarnings("rawtypes")
		Class ourClass = Class.forName("com.example.mysqltest2." + cheese);
		Intent ourIntent = new Intent(MainActivity.this, ourClass);
	    startActivity(ourIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}

	}
	
}