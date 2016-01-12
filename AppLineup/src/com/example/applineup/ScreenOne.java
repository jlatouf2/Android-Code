package com.example.applineup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class ScreenOne extends Activity {
	// to find the facebook has: use: cd ~/.android
	// then: keytool -exportcert -alias androiddebugkey -keystore debug.keystore
	// | openssl sha1 -binary | openssl base64
	/*
	 * To implement facebook login: 1)Make sure to LINK to facebookSDK for each
	 * project 2)Add facebook id to string; then to manifest 3)Add internet
	 * permission.
	 * 
	 * function logged_in() { return (isset($_SESSION['user_id'])) ? true :
	 * false; }
	 * 
	 * //if user does not store in variable, you can pass it in intent then use
	 * autoposting to add to database
	 * 
	 * TO PASS FACEBOOK LOGIN BETWEEN PAGES: 1) Pass an intent with the 2)
	 * user_id (or email, or username) with a check for one on top of each page
	 * (use the function above for the format) 3) MAKE SURE THAT TO POST USER_ID
	 * YOU ONLY NEED CHANGE QUERY IN PHP TO INSERT INTO DATABASE, NOTHING
	 * ELSE...
	 */

	JSONParser jsonParser = new JSONParser();

	private static final String POST_COMMENT_URL = "http://192.168.1.148:8888/AndroidPHP/login.php";

	private ProgressDialog pDialog;

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	private String TAG = "MainActivity";
	private TextView lblEmail;
	private TextView lblPassword;
	private TextView lblUsername;
	private Button BTTN;
	private Button BTTN2;

	// facebook login part 1
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.btn1);
		lblEmail = (TextView) findViewById(R.id.lblEmail);
		lblPassword = (TextView) findViewById(R.id.lblPassword);
		lblUsername = (TextView) findViewById(R.id.lblUsername);
		
		BTTN2 = (Button) findViewById(R.id.Button01);
		BTTN2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				
				Intent i = new Intent(ScreenOne.this, Prefactivity.class);
				startActivity(i);
				
				/*
				 * TO REFERENCE SOMETHING IN PREFERNCES AND THEN GET START STUFF
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean music = getPrefs.getBoolean("checkbox", true);
		if (music == true){
			ourSong.start();
						}
		
					}

				 */

				//Intent i = new Intent(ScreenOne.this, LineupController.class);
				//startActivity(i);
			}
		});


		BTTN = (Button) findViewById(R.id.button1);
		BTTN.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Do something in response to button click
				loginDialog();

			}
		});
		

		
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
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {
								// Request.executeMeRequestAsync(session, new
								// Request.GraphUserCallback() {

								/*******
								 * To Sign up with facebook: Use this process
								 * and then store All data (name, email,
								 * password [not access token]) in db; then
								 * check with if{ } statement if all these are
								 * valid, AND if access token is valid
								 * 
								 * To Log in: Get users name, email, password
								 * and access token, then check against db if
								 * there; if they are there then allow them to
								 * log in AND store their access token IN A
								 * SESSION so that it allows them to view the
								 * next page.... SO EACH PAGE NOW REQUIRES A
								 * REAL ACCESS TOKEN TO VIEW IT: ie if {access
								 * token == false) { Intent i = new
								 * Intent(Login); startActivity(i); }
								 * 
								 * each page will also required a email and
								 * password check for reg users.....
								 * 
								 * (please read if have any troble:
								 * http://programmerguru
								 * .com/android-tutorial/android
								 * -facebook-login-implementation-with-fragment/
								 * AND
								 * https://developers.facebook.com/docs/facebook
								 * -login/android/v2.3
								 * *************/
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									// TODO Auto-generated method stub
									if (user != null) {

										// new PostComment2().execute();

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
												.getDefaultSharedPreferences(ScreenOne.this);
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
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(ScreenOne.this);
					sp.edit().remove("username").commit();

					// lblUsername.setVisibility(View.GONE);
				}

			}
		});
	}

	// facebook login part 2
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
			//Intent i = new Intent(ScreenOne.this, ScreenTwo.class);
			//startActivity(i);
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

	public void loginDialog() {

		// Create Object of Dialog class
		final Dialog login = new Dialog(this);
		// Set GUI of login screen
		login.setContentView(R.layout.dialog_signin);
		login.setTitle("Login to Pulse 7");

		// Init button of login GUI
		Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
		Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
		final EditText txtUsername = (EditText) login
				.findViewById(R.id.txtUsername);
		final EditText txtPassword = (EditText) login
				.findViewById(R.id.txtPassword);

		// Attached listener for login GUI button
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (txtUsername.getText().toString().trim().length() > 0
						&& txtPassword.getText().toString().trim().length() > 0) {
					// Validate Your login credential here than display message
					Toast.makeText(ScreenOne.this, "Login Sucessfull",
							Toast.LENGTH_LONG).show();

					final String uname = txtUsername.getText().toString();
					final String pword = txtPassword.getText().toString();
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(ScreenOne.this);
					Editor edit = sp.edit();
					edit.putString("username", uname);
					edit.putString("password", pword);

					edit.commit();
					Log.d("request!", uname);

					new AttemptLogin().execute();

					// Redirect to dashboard / home screen.
					login.dismiss();
				} else {
					Toast.makeText(ScreenOne.this,
							"Please enter Username and Password",
							Toast.LENGTH_LONG).show();

				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login.dismiss();
			}
		});

		// Make dialog box visible.
		login.show();

	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ScreenOne.this);
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

			// This passes the text from alert dialog so that i can log in with
			// it:
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(ScreenOne.this);
			String username = sp.getString("username", null);
			String password = sp.getString("password", null);
			Log.d("request!", username);

			// String username = user.getText().toString();
			// String password = pass.getText().toString();

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				
				Log.d("request!", password);

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL,
						"POST", params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());

					// save user data
					/*
					 * SharedPreferences sp = PreferenceManager
					 * .getDefaultSharedPreferences(Login3.this); Editor edit =
					 * sp.edit(); edit.putString("username", username);
					 * edit.commit();
					 */

					Intent i = new Intent(ScreenOne.this, ScreenTwo.class);
					finish();
					startActivity(i);
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
				Toast.makeText(ScreenOne.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}

}
