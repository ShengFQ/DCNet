package com.bandary.dcnet.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bandary.dcnet.R;

public class ComponetGridViewAdapter extends BaseAdapter{
	public View[] viewItems;
	//输入 主显示页面实例  
	
	//标签  图片索引
	
	public ComponetGridViewAdapter(Context context,List<String> title,List<Integer> imag){
		viewItems=new View[imag.size()];
		for (int i = 0; i < viewItems.length; i++) {
			viewItems[i]=makeItemView(context,  title.get(i), imag.get(i));
		}
	}

	@Override
	public int getCount() {
		return viewItems.length;
	}

	@Override
	public Object getItem(int position) {
		return viewItems[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			return viewItems[position];
		return convertView;
	}
	
	private View makeItemView(Context context, String strText, int resId) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 使用View的对象itemView与R.layout.item关联
		View itemView = inflater.inflate(R.layout.gridview_nav_item, null);

		// 通过findViewById()方法实例R.layout.item内各组件		
		TextView text = (TextView) itemView.findViewById(R.id.cgvi_ItemText);
		text.setText(strText);
		ImageView image = (ImageView) itemView.findViewById(R.id.cgvi_ItemImage);
		image.setImageResource(resId);		
		return itemView;
	}
}
