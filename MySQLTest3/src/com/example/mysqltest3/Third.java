package com.example.mysqltest3;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Third extends Activity implements OnClickListener{
	
	private EditText title, message;
	private Button  mSubmit;
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
//This checks if posting to form actually works:
    /*
     * 1) use Begin
     * 2)Use Second which selects Line1, or Line2 and holds both tablename and linename in intents
     * 3)Use Third which uses the AddComment script to post (if ok: then add intent to go to 4)
     * 4)Use linename and if statemnts to see which query will be run for datanames (in php backend)
     * 
     * -use begin: which grabs intent
     * -then second: grabs Line intent
     * -autopost: to see usernames are there and intents are correct:
     * could: have autopost get back jsondata on success and then go to 4th page with a linequery4.php that simply
     * uses a query that goes for Line1 (This means that for each posible Line query i need an activity and php script)
     * which would be a lot....
     * DOES NOT WORK B/C NEED THE STORENAME FOR THE QUERY TO PROCESS CORRECTLY...
     * -Could get jasonData back from autopost, then have a REGULAR ArrayList (like the ones used in LineApp) that shows the data on the 4th page....
     
     ----WORKS:::::BY::::
     1)USING ONLY Addcomment activity (which POSTS to php and database)
     2) Use json.getString("posts") to get responce data
     3) Add response data in intents then use REGULAR ARRAYLIST page
     4) take the responce that the user selected and either:
     		1. Add it to autopost page with intents OR
     		2. make that page its A Arraylist page WITH a Addcomment built in it (so when selected it will post)
     		(NOT SURE IF 2 WILL WORK... B/C NOT SURE IF POSTING CAN BE DONE THAT WAY.....)
     		
     		---THIS MEANS DETERMINATE FOR IF YOU POST DATA TO DATABASE OR QUERY DATA FROM DATABASE
     		IS ACTUALLY THE QUERY IN THE PHP ITSELF: 
     		
     		(EX: posting if query is INSERT INTO
     			querying if its SELECT * FROM table WHERE ...)
     		
     *   	//USE json.toString() to get whole message... BUT json.getString("message, success, or posts)
       //GETS YOU SPECIFIC PARTS OF THE RESPONCE TO DO WITH WHAT YOU PLEASE.....

     *
     */
    
    // private static final String POST_COMMENT_URL = "http://192.168.1.110:8888/LWebservice/res/addcomment.php";
    private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/linethird.php";


  //testing from a real server:
    //private static final String POST_COMMENT_URL = "http://www.mybringback.com/webservice/addcomment.php";
    
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_comment3);
		
		title = (EditText)findViewById(R.id.title);
		message = (EditText)findViewById(R.id.message);
		
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
		new PostComment().execute();

	}

	@Override
	public void onClick(View v) {

	}
	
	
	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Third.this);
            pDialog.setMessage("Posting Comment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            //Change this to auto post:
         //   String post_title = title.getText().toString();
         //   String post_message = message.getText().toString();
            
			Intent i = getIntent();
			final String Storename = i.getStringExtra("Storename");
			final String linename = i.getStringExtra("linename");

            String post_title = Storename;
            String post_message = linename;

            //Retrieving Saved Username Data:
        //    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Posting1.this);
        //    String post_username = sp.getString("username", "anon");
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
          //      params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("table", post_title));
                params.add(new BasicNameValuePair("linename", post_message));
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());    
                	finish();
                	
                	//To: refresh the page: (this could also carry intents to itself theoretically:
                	/*
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    
               for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);
				}
					for (int i = 0; i < posts.length(); i++ {
					
					}
                    
					*/
                	
                	//if success you add an intent in here to go to page that shows
                	//lines with people in them......

                	String item3 = json.getString("posts");
                	Log.d("This is username:",  item3);    
           	
                	String item4 = item3.replace("[{\"username\":\"", "");
    				//String item4 = item3.replace("{linein=", "");

    				String item5 = item4.replace("\"}]", "");

    				//String item5 = item4.replace("}", "");
    				Log.d("This is the text entered:", "" + item5);

                	
                   Intent i5 = new Intent(Third.this, Fourth.class);
                    
    				i5.putExtra("lname", item5);
    				Log.d("This is the text entered:", "" + item5);
                    startActivity(i5);
                	
                	
                	
                	/*
                    Intent i = new Intent(Third.this, Linequery.class);
                    
    				String text2 = title.getText().toString();

    				i.putExtra("lname", text2);
    				Log.d("This is the text entered:", "" + text2);
                    startActivity(i);
*/
                	
                	
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                	finish();
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Third.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
