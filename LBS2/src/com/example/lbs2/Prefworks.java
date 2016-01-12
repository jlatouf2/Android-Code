package com.example.lbs2;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Prefworks extends Activity  {
    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1; // It will never expire
    private static final String PROX_ALERT_INTENT = "com.androidmyway.demo.ProximityAlert";
    private LocationManager locationManager;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button addAlertButton;
	private String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.savescreen);
    		
    		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	    
    		//New Stuff
    		Criteria criteria = new Criteria();
    		provider = locationManager.getBestProvider(criteria, false);
    		Location location = locationManager.getLastKnownLocation(provider);

    		
    		double lat2 = (double) (location.getLatitude());

    		double lng2 = (double) (location.getLongitude());

    		Log.d("Latitude22222!", "number" + lat2);
    		Log.d("Longitude!", "number" + lng2);
    		//End of New Stuff

    		
    		
    		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    		boolean music = getPrefs.getBoolean("checkbox", true);
    		if (music == true){
    			//ourSong.start();
    			addProximityAlert();
    			
    		}

    		
    		
    }
    
    
    
    
    
    private void addProximityAlert() {
    	
    	//New Stuff
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		
		Location location = locationManager.getLastKnownLocation(provider);
		//Log.d("location:", "" + location);

		double lat2 = (double) (location.getLatitude());

		double lng2 = (double) (location.getLongitude());
/*
		String book = String.valueOf(lat2);
		String book2 = String.valueOf(lng2);
		Log.d("latitude1", "number" + book);
		Log.d("longitude1", "number" + book2);
*/

      //     double latitude = Double.parseDouble(latitudeEditText.getText().toString());
      //     double longitude = Double.parseDouble(longitudeEditText.getText().toString());
           Intent intent = new Intent(PROX_ALERT_INTENT);
           PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
           locationManager.addProximityAlert(
                  lat2, // the latitude of the central point of the alert region
                  lng2, // the longitude of the central point of the alert region
                  POINT_RADIUS, // the radius of the central point of the alert region, in meters
                  PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no  expiration
                  proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
           );

           IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
           registerReceiver(new ProximityIntentReceiver(), filter);
           Toast.makeText(getApplicationContext(),"Alert Added",Toast.LENGTH_SHORT).show();
    }




}
