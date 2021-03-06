package com.example.fragmenttabstutorial;

import android.app.Fragment;
import android.app.FragmentTransaction;
//Needed to import these two libraries:
import android.app.ActionBar;
import android.app.ActionBar.Tab;


public class TabListener implements ActionBar.TabListener {
	 
	Fragment fragment;
 
	public TabListener(Fragment fragment) {
		// TODO Auto-generated constructor stub
		this.fragment = fragment;
	}
 
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.replace(R.id.fragment_container, fragment);
	}
 
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.remove(fragment);
	}
 
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
 
	}
}
