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

public class Posting1 extends Activity implements OnClickListener{
	
	private EditText title, message;
	private Button  mSubmit;
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    /*
     * DIFFERENCES BETWEEN THE TWO DIFFERENT THINGS:
     * 1)Posting page allows you to add content to php (which means you can either, depending on if query is Select from
     * or INSERT INTO, insert or edit out content)
     * 2)The other sheet allows you to get conent from php query and automatically parse the conent out
     * WITHOUT having to replace (CUT) pieces of json string.
     */
    
    
    
//This checks if posting to form actually works:
    /*
     * 1) use Begin
     * 2)Use Second which selects Line1, or Line2 and holds both tablename and linename in intents
     * 3)Use Third which uses the AddComment script to post (if ok: then add intent to go to 4)
     * 4)Use linename and if statemnts to see which query will be run for datanames (in php backend)
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
		setContentView(R.layout.add_comment);
		
		title = (EditText)findViewById(R.id.title);
		message = (EditText)findViewById(R.id.message);
		
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
				new PostComment().execute();
	}
	
	
	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Posting1.this);
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
            String post_message = message.getText().toString();
            
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

                	//USE json.toString() to get whole message... BUT json.getString("message, success, or posts)
                	//GETS YOU SPECIFIC PARTS OF THE RESPONCE TO DO WITH WHAT YOU PLEASE.....
    				
                	 
                	
                	String item3 = json.getString("posts");
                	Log.d("This is username:",  item3);    
           	
                	String item4 = item3.replace("[{\"username\":\"", "");
    				//String item4 = item3.replace("{linein=", "");

    				String item5 = item4.replace("\"}]", "");

    				//String item5 = item4.replace("}", "");
    				Log.d("This is the text entered:", "" + item5);

                	
                   Intent i = new Intent(Posting1.this, Listview02.class);
                    

    			
    				
    				
    				i.putExtra("lname", item5);
    				Log.d("This is the text entered:", "" + item5);
                    startActivity(i);
                	
                	
                	
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
            	Toast.makeText(Posting1.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
