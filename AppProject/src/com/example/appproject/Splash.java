package com.example.appproject;


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
					sleep(1000);
				} catch (InterruptedException e){
					
				} finally{

					Intent openStartingPoint = new Intent(Splash.this, ScreenOne.class);
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}

}
