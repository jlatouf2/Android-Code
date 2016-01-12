package com.example.mysqltest3;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Listview1 extends ListActivity {

	/*
	 * To use Parse:
	 * 1) Add bolts, jar, jar.properties to libs
	 * 2) Add      <uses-permission android:name="android.permission.INTERNET" />
    	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
		to manifest file.
	   3) impart all parse files to jave file that will use Parse
	   4) Add  Parse.initialize(this, "jdmcP67zhEK5NYqxzEk0PCwAIc9khqL4K9LhkSyC", "YWonSDK6FpgcYvTw0QPFZ4eVe1qVVofg6PPWgWNF");


	 */		

	String classes[] = { "Lineupworks01", "PassIntent01", "LineupBeginController", "Splash", "Lineup04", "ListParse02",
	"example6" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(Listview1.this, android.R.layout.simple_list_item_1, classes));
		Intent i = getIntent();

		// Get the name
		String name2 = i.getStringExtra("lname");

		Log.d("This is name in listview:", "" + name2);
/*
		ArrayList<String> mylist = new ArrayList<String>();
		mylist.add(mystring); //this adds an element to the list.
		
		String[] str = new String[10];
		str[0] = "value1"
				str[1] = "value2"
				str[2] = "value3"
				str[3] = "value4"
				str[4] = "value5"
				str[5] = "value6"
				str[6] = "value7"
				str[7] = "value8"
				str[8] = "value9"
				str[9] = "value10"
				str.add("Value1");
*/
		
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
		Class ourClass = Class.forName("com.example.MySQLTest3." + cheese);
		Intent ourIntent = new Intent(Listview1.this, ourClass);
	    startActivity(ourIntent);
		}catch(ClassNotFoundException e){
			e.printStackTrace();

		}

	}
	
}