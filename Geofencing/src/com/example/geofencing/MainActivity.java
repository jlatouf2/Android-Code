package com.example.geofencing;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private LocationManager lm;
	private String provider;
	private Button mSubmit, Distance02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		// setup buttons
		mSubmit = (Button) findViewById(R.id.button2223434);
		Distance02 = (Button) findViewById(R.id.DistanceBTTN);

		// register listeners
		mSubmit.setOnClickListener(this);
		Distance02.setOnClickListener(this);

		// ---use the LocationManager class to obtain locations data---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		

		
		// ---PendingIntent to launch activity if the user is within // some
		// locations---
		// NOTE: this is a 5 meter radius from the coordinates presented
		/*
		 * PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, new
		 * Intent(android.content.Intent.ACTION_VIEW, Uri
		 * .parse("http://www.amazon.com")), 0);
		 * 
		 * 
		 * // lm.addProximityAlert(37.422006, -122.084095, 5, -1,
		 * pendingIntent); // lm.addProximityAlert(42.22813637, -83.09967105, 5,
		 * -1, // pendingIntent); lm.addProximityAlert(42.22813637,
		 * -83.09967105, 5, -1, pendingIntent); // lm.addProximityAlert(lat2,
		 * lng2, 5, -1, pendingIntent);
		 * 
		 * if (pendingIntent == true) { Toast.makeText(this, "Refresh selected",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * }
		 */

		Criteria criteria = new Criteria();
		provider = lm.getBestProvider(criteria, false);
		Location location = lm.getLastKnownLocation(provider);
		Log.d("location:", "" + location);

		double lat2 = (double) (location.getLatitude());

		double lng2 = (double) (location.getLongitude());

		Log.d("Latitude22222!", "number" + lat2);
		Log.d("Longitude!", "number" + lng2);

		// Location Data this is used to determine the actual Coordinates....
		// 42.22813637
		// -83.09967105
	}

	public void passingStuff() {
		Toast.makeText(this, "PASSED DATA", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button2223434:

			Criteria criteria = new Criteria();
			provider = lm.getBestProvider(criteria, false);
			
			Location location = lm.getLastKnownLocation(provider);
			//Log.d("location:", "" + location);

			double lat2 = (double) (location.getLatitude());

			double lng2 = (double) (location.getLongitude());

			String book = String.valueOf(lat2);
			String book2 = String.valueOf(lng2);

			Log.d("latitude1", "number" + book);
			Log.d("longitude1", "number" + book2);

			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(MainActivity.this);
			Editor edit = sp.edit();
			edit.putString("latitude", book);
			edit.putString("longitude", book2);

			edit.commit();
			

			PendingIntent pendingIntent = PendingIntent.getActivity(
					this,
					0,
					new Intent(android.content.Intent.ACTION_VIEW, Uri
							.parse("http://www.amazon.com")), 0);
			
			lm.addProximityAlert(lat2, lng2, 5, -1, pendingIntent);
			//lm.requestSingleUpdate(criteria, pendingIntent);
			//lm.requestSingleUpdate(book2, intent);
		//	lm.requestSingleUpdate(criteria, listener, looper);
		//	lm.requestSingleUpdate(book2, listener, looper);
			passingStuff();
			break;

		case R.id.DistanceBTTN:

			SharedPreferences sp2 = PreferenceManager
					.getDefaultSharedPreferences(MainActivity.this);
			String proximitylatitude = sp2.getString("latitude", null);
			String proximitylongitude = sp2.getString("longitude", null);

			Log.d("latitude2!", proximitylatitude);
			Log.d("longitude2!", proximitylongitude);

			Double doublefirst = Double.parseDouble(proximitylatitude);
			Double doublesecond = Double.parseDouble(proximitylongitude);

			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			Location location2 = lm.getLastKnownLocation(provider);
			
			double lat3 = (double) (location2.getLatitude());

			double lng3 = (double) (location2.getLongitude());

			// double latitude=lat;
			// double longitude=lng;
			float distance = 0;
			Location crntLocation = new Location("crntlocation");
			crntLocation.setLatitude(lat3);
			crntLocation.setLongitude(lng3);

			Location newLocation = new Location("newlocation");
			newLocation.setLatitude(doublefirst);
			newLocation.setLongitude(doublesecond);

			// float distance = crntLocation.distanceTo(newLocation); in meters
			distance = crntLocation.distanceTo(newLocation) / 1000; // in km
			Log.d("Distance:", "" + distance);
			Log.d("crntLocation", "" + crntLocation);
			Log.d("newLocation", "" + newLocation);

			//NOTE: PRESS TOP BUTTON TO GET CURRENT LOCATION
			//NEWLOCATION WILL STAY THE SAME ON DEVICE.....
			//ADD ALERT DIALOG TO PENDING INTENT
			
			// float distance = location.distanceTo(lat2, lng2);

			Toast.makeText(this, "Preferences Selected" + distance,
					Toast.LENGTH_SHORT).show();
			break;
		default:

		}

	}

}
