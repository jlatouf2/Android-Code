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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationExample2 extends Activity  {
	private TextView latituteField;
	private TextView longitudeField;
	private TextView TextView05;
    private static final long POINT_RADIUS = 100; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1; // It will never expire
    private static final String PROX_ALERT_INTENT = "com.androidmyway.demo.ProximityAlert";

	private LocationManager locationManager;
	private String provider;
	private Button BTTN1;

	 public String latitude2;
	 public String longitude2;


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		latituteField = (TextView) findViewById(R.id.TextView02);
		longitudeField = (TextView) findViewById(R.id.TextView04);
		TextView05 = (TextView) findViewById(R.id.TextView05);

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		

		
		BTTN1 = (Button) findViewById(R.id.button1);
		BTTN1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
			}
		});

		
		 locationManager.requestSingleUpdate(criteria, new LocationListener(){

		        @Override
		        public void onLocationChanged(Location location) {
		            // reverse geo-code location
		        	
		        	
		    		// Initialize the location fields
		    		if (location != null) {
		    			System.out.println("Provider " + provider + " has been selected.");
		    		//	onLocationChanged(location);
		    			
		    			try {
		    				//THIS WORKS B/C THE SHAREDPREF ONLY GOES IF LOCATION SERVICES ARE ENABLED!!!!!
		    				SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    				boolean music = getPrefs.getBoolean("checkbox", true);
		    				if (music == true){
		    					//ourSong.start();
		    					addProximityAlert();
		    				}

		    		      // DB operation
		    		    } catch (Exception e){

		    		      throw new RuntimeException(e);
		    		    }
		    			
		    		} else {
		    			latituteField.setText("Location not available");
		    			longitudeField.setText("Location not available");
		    		}

		        	

		        }

		        @Override
		        public void onProviderDisabled(String provider) {
		            // TODO Auto-generated method stub

		        }

		        @Override
		        public void onProviderEnabled(String provider) {
		            // TODO Auto-generated method stub

		        }

		        @Override
		        public void onStatusChanged(String provider, int status,
		                Bundle extras) {
		            // TODO Auto-generated method stub

		        }

		    }, null);

		

		
		
		

		
	}

	
	
	
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates((LocationListener) this);
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
/*
 * This is the example to add singleupdate to button
 * locationManager.requestSingleUpdate(criteria, new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            // reverse geo-code location

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status,
                Bundle extras) {
            // TODO Auto-generated method stub

        }

    }, null);

 */


}
