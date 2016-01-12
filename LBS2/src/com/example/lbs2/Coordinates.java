package com.example.lbs2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Coordinates extends Activity implements LocationListener {
	private TextView latituteField;
	private TextView longitudeField;
	private TextView TextView05;

	private LocationManager locationManager;
	private String provider;
	private Button BTTN1;

	private static String ILatitude;
	private static String ILongitude;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	private static final String LOGIN_URL = "http://192.168.1.148:8888/AndroidPHP/addlocation2.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

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

		// BTTN1 = (Button)findViewById(R.id.button1);

		// register listeners

		BTTN1 = (Button) findViewById(R.id.button1);
		BTTN1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				new AttemptLogin().execute();
			}
		});

		/*
		 * // longitudeField.setText(String.valueOf(lng)); Location
		 * location1=new Location("locationA");
		 * location1.setLatitude(17.372102); location1.setLongitude(78.484196);
		 * Location location2=new Location("locationA");
		 * location2.setLatitude(17.375775); location2.setLongitude(78.469218);
		 * double distance=location1.distanceTo(location2);
		 * Log.d("Location calulation", "" + distance);
		 * 
		 * TextView05.setText(String.valueOf(distance));
		 */
		/*
		 * Location loc1 = new Location(""); loc1.setLatitude(lat1);
		 * loc1.setLongitude(lon1);
		 * 
		 * Location loc2 = new Location(""); loc2.setLatitude(lat2);
		 * loc2.setLongitude(lon2);
		 * 
		 * float distanceInMeters = loc1.distanceTo(loc2);
		 */

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		} else {
			latituteField.setText("Location not available");
			longitudeField.setText("Location not available");
		}
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

		Log.d("Location lat", "" + lat);
		Log.d("Location lat", "" + lng);
//
		int latitude = (int) (location.getLatitude());
		int longitude = (int) (location.getLongitude());

		ILatitude = Integer.toString(latitude);
		ILongitude = Integer.toString(longitude);
		Log.d("Location lat", "" + lat);
		Log.d("Location lat", "" + lng);

//
		/*
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(Coordinates.this);
		Editor edit = sp.edit();
		edit.putInt("lati", lat);
		edit.putInt("long", lng);
		// edit.putString("lati", lat);

		edit.commit();
*/
		Location location1 = new Location("locationA");
		location1.setLatitude(45.47193);
		location1.setLongitude(-70.33654);
		Location location2 = new Location("locationA");
		location2.setLatitude(45.70617);
		location2.setLongitude(-68.13666);
		double distance = location1.distanceTo(location2);
		TextView05.setText(String.valueOf(distance));
		Log.d("Location calulation", "" + distance);
		// THIS IS IN METERS
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

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Coordinates.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			// String username = user.getText().toString();

			try {

				String strLatitude = ILatitude;
				String strLongitude = ILongitude;
				// Building parameters
				List<NameValuePair> params1 = new ArrayList<NameValuePair>();
				params1.add(new BasicNameValuePair("latitude", strLatitude));
				params1.add(new BasicNameValuePair("longitude", strLongitude));

				/*
				 * SharedPreferences sp = PreferenceManager
				 * .getDefaultSharedPreferences(Coordinates.this); //int lat1 =
				 * sp.getInt("lati", 1); //int long1 = sp.getInt("long", 1);
				 * 
				 * // int i = 5; String Latitude = String.valueOf(lat1); String
				 * Longitude = String.valueOf(long1);
				 * 
				 * // Building Parameters List<NameValuePair> params = new
				 * ArrayList<NameValuePair>(); params.add(new
				 * BasicNameValuePair("latitude", Latitude)); params.add(new
				 * BasicNameValuePair("longitude", Longitude));
				 * Log.d("request!", Latitude); Log.d("request!", Latitude);
				 */
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
						params1);
				// int block = json.getInt(Latitude);
				// Log.d("Login attempt", "" +block);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());

					/*
					 * // save user data SharedPreferences sp =
					 * PreferenceManager
					 * .getDefaultSharedPreferences(Login.this); Editor edit =
					 * sp.edit(); edit.putString("username", username);
					 * edit.commit();
					 * 
					 * Intent i = new Intent(Coordinates.this, Nextpage.class);
					 * finish(); startActivity(i);
					 */
					return json.getString(TAG_MESSAGE);

				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(Coordinates.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

}
