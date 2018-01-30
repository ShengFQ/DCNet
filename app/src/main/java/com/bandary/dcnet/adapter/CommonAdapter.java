package com.bandary.dcnet.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter extends BaseAdapter {

	public ArrayList<String> data;
	public Context activity;

	public CommonAdapter() {
    }
	
	public CommonAdapter(Activity activity, ArrayList<String> data) {
	    this.activity = activity;
	    this.data = data;
    }
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return view(position, convertView, parent);
	}

	public abstract View view(int position, View convertView, ViewGroup parent);
}
