package com.example.lineapp04;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LineupBeginController extends Activity implements OnClickListener {

	EditText etLName;
	Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lineupbegin);

		etLName = (EditText) findViewById(R.id.editText1);

		btnSubmit = (Button) findViewById(R.id.button1);
		btnSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		String text2 = etLName.getText().toString();
		Log.d("This example" + text2, "is text log!");

		// Add a string that says
		// "This code has lines available with Store name".

		ParseQuery<ParseObject> query2 = ParseQuery.getQuery(text2);
		// query2.whereEqualTo("playerEmail", "dstemkoski@example.com");
		query2.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> object, ParseException e) {
				if (object == null) {
					Log.d("score", "The class does not exist.");

					new AlertDialog.Builder(LineupBeginController.this)
							.setTitle("Invalid Code Name")
							.setMessage(
									"No lines avaible with that code name. Would you like to create your own lines?")
							.setPositiveButton(android.R.string.yes,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with delete

										}
									})
							.setNegativeButton(android.R.string.no,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// do nothing
										}
									})
							.setIcon(android.R.drawable.ic_dialog_alert).show();

				} else {
					Intent intent1 = new Intent(LineupBeginController.this,
							LineupSecondController.class);
					intent1.putExtra("lname", etLName.getText().toString());
					// Log.d("This is debug", "Variables are:" + lname + "it.");

					startActivity(intent1);

					Log.d("score", "The class exists.");
				}
			}
		});

	}

}
