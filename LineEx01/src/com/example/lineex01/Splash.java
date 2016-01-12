package com.example.lineex01;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(5000);
				} catch (InterruptedException e){
					
				} finally{

					Intent openStartingPoint = new Intent(Splash.this, Login.class);
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

}
