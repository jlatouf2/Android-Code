package com.example.lineex01;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login2 extends Activity implements OnClickListener{

//	private EditText user, pass;
//	private Button mSubmit, mRegister;

	private Button button1;
	
 

	 // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();



	public TextView txtUsername;
	public TextView txtPassword;

	private static final String LOGIN_URL = "http://192.168.1.148:8888/AndroidPHP/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginlayout);
/*
		//setup input fields
		user = (EditText)findViewById(R.id.username);
		pass = (EditText)findViewById(R.id.password);

		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
		mRegister = (Button)findViewById(R.id.register);

		//register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);
*/
		button1 = (Button)findViewById(R.id.login);
		button1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			loginDialog();
			
			//new AttemptLogin().execute();
			break;
	

		default:
			break;
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_refresh:
			Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
			break;
		// action with ID action_settings was selected
		case R.id.action_settings:

			Intent i = new Intent(this, Prefs.class);
			startActivity(i);
			
			Toast.makeText(this, "Preferences Selected", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
		}
		return true;

	}

	

	

	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login2.this);
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
       
            //String username = user.getText().toString();
            //String password = pass.getText().toString();
            
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            
            Log.d("This is your username!", username);
            Log.d("This is your password!", password);

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Login Successful!", json.toString());
                	
                	// save user data
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Login2.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.commit();
					
					Intent i = new Intent(Login2.this, Nextpage.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);
					
                }else{
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
            if (file_url != null){
            	Toast.makeText(Login2.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

	}
	
	
	
	public void loginDialog(){
		
		 // Create Object of Dialog class
        final Dialog login = new Dialog(this);
        // Set GUI of login screen
        login.setContentView(R.layout.dialog_signin);
        login.setTitle("Login to Pulse 7");

        // Init button of login GUI
        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
        final EditText txtUsername = (EditText)login.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText)login.findViewById(R.id.txtPassword);

        // Attached listener for login GUI button
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().trim().length() > 0 && txtPassword.getText().toString().trim().length() > 0)
                {
                // Validate Your login credential here than display message
                Toast.makeText(Login2.this,
                        "Login Sucessfull", Toast.LENGTH_LONG).show();
                
            //  final  String uname = txtUsername.getText().toString();
            //  final  String pword = txtPassword.getText().toString();

                
    			new AttemptLogin().execute();

                // Redirect to dashboard / home screen.
                login.dismiss();
                }
                else
                {
                    Toast.makeText(Login2.this,
                            "Please enter Username and Password", Toast.LENGTH_LONG).show();

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
	

	
	
	
}
