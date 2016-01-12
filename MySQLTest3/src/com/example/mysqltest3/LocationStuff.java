package com.example.mysqltest3;

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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationStuff extends Activity implements LocationListener {
  private TextView latituteField;
  private TextView longitudeField;
  private LocationManager locationManager;
  private String provider;

	
	private EditText title;
	private Button  mSubmit;
	
	 // Progress Dialog
  private ProgressDialog pDialog;

  // JSON parser class
  JSONParser jsonParser = new JSONParser();
  
  private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/addlocation.php";


  private static final String TAG_SUCCESS = "success";
 private static final String TAG_MESSAGE = "message";

  
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.location_main);
    latituteField = (TextView) findViewById(R.id.TextView02);
    longitudeField = (TextView) findViewById(R.id.TextView04);

    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the locatioin provider -> use
    // default
    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    Location location = locationManager.getLastKnownLocation(provider);

    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      onLocationChanged(location);
    } else {
      latituteField.setText("Location not available");
      longitudeField.setText("Location not available");
    }
    
    
  }
  
  public void ClickButton1(View view) {
	  //getting the 
        String post_title = longitudeField.getText().toString();
        String post_title2 = latituteField.getText().toString();
        		
	    Log.d("request!", "starting" + post_title);
	    Log.d("request!", "starting" + post_title2);
		new PostComment().execute();

	     // Kabloey
	 }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(provider, 400, 1, this);
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    locationManager.removeUpdates(this);
  }

  @Override
  public void onLocationChanged(Location location) {
    int lat = (int) (location.getLatitude());
    int lng = (int) (location.getLongitude());
    latituteField.setText(String.valueOf(lat));
    longitudeField.setText(String.valueOf(lng));
    Log.d("Longitude!", "number" + String.valueOf(lng));
    Log.d("Latitude!", "number" + String.valueOf(lat));

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onProviderEnabled(String provider) {
    Toast.makeText(this, "Enabled new provider " + provider,
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(this, "Disabled provider " + provider,
        Toast.LENGTH_SHORT).show();
  }
  
  
  
  
  class PostComment extends AsyncTask<String, String, String> {
	

		
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(LocationStuff.this);
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
	        
	        String post_title = longitudeField.getText().toString();
            String post_title2 = latituteField.getText().toString();
            		
    	    Log.d("request!", "starting" + post_title);
    	    Log.d("request!", "starting" + post_title2);

	        
	        //Retrieving Saved Username Data:
	      //  SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LineupBegin.this);
	      //  String post_username = sp.getString("username", "anon");
	        
	        try {
	            // Building Parameters
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	          //  params.add(new BasicNameValuePair("username", post_username));
	            params.add(new BasicNameValuePair("Longitude", post_title));
	            params.add(new BasicNameValuePair("Latitude", post_title2));

	           // params.add(new BasicNameValuePair("message", post_message));

	          
	            //Posting user data to script 
	            JSONObject json = jsonParser.makeHttpRequest(
	            		POST_COMMENT_URL, "POST", params);

	            // full json response
	            Log.d("Post Comment attempt", json.toString());

	            // json success element
	            success = json.getInt(TAG_SUCCESS);
	            if (success == 1) {
	            	Log.d("Comment Added!", json.toString());   
	            	
	            	//if success you add an intent in here to go to page that shows
	            	//lines with people in them......
	            	/*
	                Intent i = new Intent(LocationStuff.this, Second.class);
	                
					String text2 = title.getText().toString();

					i.putExtra("lname", text2);
					Log.d("This is the text entered:", "" + text2);
	                startActivity(i);

	            	//Finish actually screwed up the application.....
	            	//finish();
	            	return json.getString(TAG_MESSAGE);
	            	*/
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
	        	Toast.makeText(LocationStuff.this, file_url, Toast.LENGTH_LONG).show();
	        }

	    }
		
	}

  
} 



