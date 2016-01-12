package com.example.lineex01;

import com.example.lineex01.R;
import com.example.lineex01.R.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
 
public class FragmentTab1 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        return rootView;
    }
 
}