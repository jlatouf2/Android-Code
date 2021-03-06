package com.example.facebooktest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Nextpage extends Activity implements OnClickListener{
	/*
	 * TO CREATE LOGIN ACCESS:
	 * 1)Store variable with Sharedpreferences
	 * 2) place function before setContentView
	 * 3).make sure that finish() is in the function after the intent, otherwise loop will be created.
	 * ALSO: USE  A PROTECTED PAGE SO THAT IT TELLS USER TO PLEASE LOG IN...
	 */
	private EditText title;
	private Button  mSubmit;
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/linebegin.php";


    private static final String TAG_SUCCESS = "success";
   private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		first_time_check();
		setContentView(R.layout.add_comment2);
		
		title = (EditText)findViewById(R.id.title);
		//message = (EditText)findViewById(R.id.message);
		
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    protected void onStart() {
        super.onResume();
		//first_time_check();

    }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
			
	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Nextpage.this);
			sp.edit().remove("username").commit();
			/*
			SharedPreferences settings = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
			settings.edit().clear().commit();
			
			SharedPreferences preferences = getSharedPreferences("Mypref", 0);
			preferences.edit().remove("text").commit();
		*/

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

	private boolean first_time_check() {    
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Nextpage.this);
		String first = sp.getString("username", null);
		Log.d("This is ", "username:" + first );

	        if((first == null)){
	            Intent i = new Intent(Nextpage.this, MainActivity.class);
	             startActivity(i);
	             finish();
	             //COULD USE PROTECTED PAGE TO TELL USER TO PLEASE LOG IN......
	        }
	        return false;
	    }

	
	

	@Override
	public void onClick(View v) {
				new PostComment().execute();
	}
	
	
	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Nextpage.this);
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
            String post_title = title.getText().toString();
            Log.d("request!", post_title);

        //    String post_message = message.getText().toString();
            
            //Retrieving Saved Username Data:
          //  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LineupBegin.this);
          //  String post_username = sp.getString("username", "anon");
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
              //  params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("table", post_title));
               // params.add(new BasicNameValuePair("message", post_message));
 
                Log.d("request!", "starting");
                Log.d("request!", post_title);

                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());   
                	
                
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
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
            	Toast.makeText(Nextpage.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 
	

	

}
