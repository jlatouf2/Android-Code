package com.example.lineapp03;


import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LineupBeginController extends Activity implements OnClickListener  {

	  EditText etLName;
	  Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lineupbegin);

        etLName = (EditText) findViewById(R.id.editText1);
        
        btnSubmit = (Button) findViewById(R.id.button1);
        btnSubmit.setOnClickListener(this);

	}
	
	 @Override
	  public void onClick(View v) {
		 /*
		  * To make this add lines as well as see then ad an if statement
		  * if {query for the parse class that you add to the edittext field; if there then alert user and stop intent.
		  * } else if{if query not there, then start intent. }
		  * 
		  * 
		  * //To drop use: 	adapter.remove(object);

		  */
		 
		 
		 
		/*
		To DELETE OBJECT MUST USE THIS AND IMPORT THE PARSE DELETE CALLBACK
			 ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Wallmart");
	            query3.whereEqualTo("username","Joey");
	            query3.getFirstInBackground(new GetCallback<ParseObject>() {

	                @Override
	                public void done(ParseObject object, ParseException e) {
	                    // TODO Auto-generated method stub
	                    if (object == null) {
	                        Toast.makeText(LineupBeginController.this,"  not found",
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            object.deleteInBackground(new DeleteCallback() {
	                                           public void done(ParseException e) {
	                                                 if (e == null) {
	                                                     Toast.makeText(LineupBeginController.this,"  deleted",
	                                                                Toast.LENGTH_SHORT).show();
	                                                 } else {
	                                                     Toast.makeText(LineupBeginController.this,"  not deleted",
	                                                                Toast.LENGTH_SHORT).show();
	                                                     e.printStackTrace();

	                                                 }
	                                               }
	                                             });

	                        }
	                    }

	            });
	
			*/
		 
		 String text2 = etLName.getText().toString();
		 Log.d("This example" + text2, "is text log!");

	      	
	     	ParseQuery<ParseObject> query2 = ParseQuery.getQuery(text2);
	      //	query2.whereEqualTo("playerEmail", "dstemkoski@example.com");
	      	query2.findInBackground(new FindCallback<ParseObject>() {
	      	    public void done(List<ParseObject> object, ParseException e) {
	      	    if (object == null) {
	      	      Log.d("score", "The class does not exist.");
	      	      
	      	      
	      	    new AlertDialog.Builder(LineupBeginController.this)
	      	    .setTitle("Invalid Store Name")
	      	    .setMessage("Please type the correct name of the Store lineup you are attempting to find.")
	      	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	      	        public void onClick(DialogInterface dialog, int which) { 
	      	            // continue with delete
	      	        }
	      	     })
	      	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	      	        public void onClick(DialogInterface dialog, int which) { 
	      	            // do nothing
	      	        }
	      	     })
	      	    .setIcon(android.R.drawable.ic_dialog_alert)
	      	     .show();

	      	      
	      	      
    	      // The request failed
    	    	//Log.d("There are no people","in any line");
	      	    } else {
		      	    Intent intent1 = new Intent(LineupBeginController.this, LineupSecondController.class); 
	    	 	    intent1.putExtra("lname", etLName.getText().toString());
	    	 	//	Log.d("This is debug", "Variables are:" + lname + "it.");

	    	 	    startActivity(intent1);

	      	      Log.d("score", "The class exists.");
	      	    }
	      	  }
	      	});
		 
	      /*	
	    	ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
	    	//query.whereEqualTo("LineIn", "Line1");
	    	query.countInBackground(new CountCallback() {
	    	  public void done(int count, ParseException e) {
	    	    if (count >= 1) {
	    	
	    	    	Log.d("Line 1 has", "people in it.");
	    	    } else {

	    	    	
	    	    	 Intent intent1 = new Intent(LineupBeginController.this, LineupSecondController.class); 
	    	 	    intent1.putExtra("lname", etLName.getText().toString());
	    	 	//	Log.d("This is debug", "Variables are:" + lname + "it.");

	    	 	    startActivity(intent1);
	    	      // The request failed
	    	    	//Log.d("There are no people","in any line");

	    	    }
	    	  }
	    	});
		*/
	 //    Intent intent1 = new Intent(LineupBeginController.this, LineupSecondController.class); 
	 	//   intent1.putExtra("lname", etLName.getText().toString());

	 	  //  startActivity(intent1);


	      	
		 
	   
	  } 

}
