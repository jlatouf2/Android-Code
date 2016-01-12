package com.example.lineex01;


import com.example.lineex01.R;
import com.example.lineex01.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FragmentButton extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.fragbutton);
	}
	
	 public void FragmentClass(View view) {
	     // Kabloey		
		 Intent ourIntent = new Intent(this, MainAct2.class );
		    startActivity(ourIntent);

	 }
}
