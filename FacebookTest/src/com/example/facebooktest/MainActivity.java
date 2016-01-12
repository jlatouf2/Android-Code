package com.example.facebooktest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class MainActivity extends Activity {
	// to find the facebook has: use: cd ~/.android
	// then: keytool -exportcert -alias androiddebugkey -keystore debug.keystore
	// | openssl sha1 -binary | openssl base64
	/*
	 * To implement facebook login: 
	 * 1)Make sure to LINK to facebookSDK for each project 
	 * 2)Add facebook id to string; then to manifest 
	 * 3)Add internet permission.
	 * 
	 * function logged_in() {
	return (isset($_SESSION['user_id'])) ? true : false;
		}
		
		//if user does not store in variable, you can pass it in intent then use autoposting to add to database

	 * 	TO PASS FACEBOOK LOGIN BETWEEN PAGES:
	 * 1) Pass an intent with the 
	 * 2) user_id (or email, or username) with a check for one on top of each page
	 * (use the function above for the format)
	 * 3) MAKE SURE THAT TO POST USER_ID YOU ONLY NEED CHANGE QUERY IN PHP TO INSERT INTO DATABASE, NOTHING ELSE...
	 */

	JSONParser jsonParser = new JSONParser();

	private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/linebegin.php";

	private ProgressDialog pDialog;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	private String TAG = "MainActivity";
	private TextView lblEmail;
	private TextView lblPassword;
	private TextView lblUsername;
	
	//facebook login part 1
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lblEmail = (TextView) findViewById(R.id.lblEmail);
		lblPassword = (TextView) findViewById(R.id.lblPassword);
		lblUsername = (TextView) findViewById(R.id.lblUsername);

		
		
		
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());
			}
		});
		// set permission list, Don't foeget to add email
		authButton.setReadPermissions(Arrays.asList("basic_info", "email"));
		// session state call back event
		authButton.setSessionStatusCallback(new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				if (session.isOpened()) {
					Log.i(TAG, "Access Token" + session.getAccessToken());
						Request.newMeRequest(session, new Request.GraphUserCallback() {
				//	Request.executeMeRequestAsync(session,	new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								// TODO Auto-generated method stub
								if (user != null) {

									//new PostComment2().execute();

									Log.i(TAG, "User ID " + user.getId());
									Log.i(TAG,
											"Email "
													+ user.asMap().get(
															"email"));

									Log.d(TAG, "UserID : " + user.getId());
									Log.d(TAG,
											"User FirstName : "
													+ user.getFirstName());

									lblEmail.setText(user.asMap()
											.get("email").toString());
									lblPassword.setText(user.getId()
											.toString());
									lblUsername.setText(user.getFirstName()
											.toString());
									
									String username = user.getFirstName();
				                	// save user data
									SharedPreferences sp = PreferenceManager
											.getDefaultSharedPreferences(MainActivity.this);
									Editor edit = sp.edit();
									edit.putString("username", username);
									edit.commit();
									

								}
							}
							
						
					}).executeAsync();
						
					// This is the change in the program:
				} else if (session.isClosed()) {
					Log.d(TAG, "Logged out...");
					lblEmail.setText("Blackout");
					lblPassword.setText("");
					lblUsername.setText("");
			        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
					sp.edit().remove("username").commit();

					// lblUsername.setVisibility(View.GONE);
				}

			}
		});
	}
		
		//facebook login part 2
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();		
			 Intent i = new Intent(MainActivity.this, Nextpage.class);
             startActivity(i);
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

	class PostComment2 extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
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
			String post_title = "lineup1";
			Log.d("request!", post_title);

			// String post_message = message.getText().toString();

			// Retrieving Saved Username Data:
			// SharedPreferences sp =
			// PreferenceManager.getDefaultSharedPreferences(LineupBegin.this);
			// String post_username = sp.getString("username", "anon");

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("username",
				// post_username));
				params.add(new BasicNameValuePair("table", post_title));
				// params.add(new BasicNameValuePair("message", post_message));

				Log.d("request!", "starting");
				Log.d("request!", post_title);

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL,
						"POST", params);

				// full json response
				Log.d("Post Comment attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Comment Added!", json.toString());

					return json.getString(TAG_MESSAGE);
				} else {
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
			if (file_url != null) {
				Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}

}
