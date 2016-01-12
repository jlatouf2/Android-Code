package com.example.lineapp03;

import com.parse.Parse;
import com.parse.ParseACL;
 
import com.parse.ParseUser;
 
import android.app.Application;
 
public class ParseApplication extends Application {
 //This must be a private section b/c it extends Application, and it is placed in application section of Manifest 
	//file, not in its own activity.
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
		Parse.initialize(this, "0owBUUNUgTQA0mlUKby9U78EL0od89kmoxGKlxmq", "hAbV04hKolsUQak0tBbOM20iHqZFpWdo0A4hvFA6");
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}
