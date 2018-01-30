package com.bandary.dcnet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerAdpter extends CommonAdapter {

	public SpinnerAdpter(Activity activity, ArrayList<String> data) {
	    this.activity = activity;
	    this.data = data;
    }
	
	@Override
	public View view(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(activity);
		textView.setPadding(10, 10, 10, 10);
		textView.setText(data.get(position));
		textView.setTextColor(Color.BLACK);
		return textView;
	}
	
}