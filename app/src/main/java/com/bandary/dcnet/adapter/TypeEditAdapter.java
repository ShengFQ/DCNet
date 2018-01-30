package com.bandary.dcnet.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityAbsDeviceType;
/**
 * 实现listview+checkbox功能，虽然checkbox只是两个图标切换，但只要达到目的即可。
 * */
public class TypeEditAdapter extends BaseAdapter {
	private ArrayList<EntityAbsDeviceType> alltypes;
	HashMap<Integer, Boolean> isSelected;
	LayoutInflater mInflater;
	Context context;
	int viewItem_icon_noCheck=R.drawable.icon_check_off;
	int viewItem_icon_yesCheck=R.drawable.icon_check_on;
	@Override
	public int getCount() {
		return alltypes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return alltypes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public TypeEditAdapter(Context context,
			ArrayList<EntityAbsDeviceType> alltypes,
			HashMap<Integer, Boolean> isSelect) {
		this.alltypes = alltypes;
		this.context = context;
		this.isSelected = isSelect;
		mInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (this.isSelected.size() < alltypes.size()) {
			for (int i = 0; i < alltypes.size(); i++) {
				this.isSelected = new HashMap<Integer, Boolean>();
				isSelected.put(i, true);
			}
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RelativeLayout relativeLayout = null;
		if (convertView == null) {
			relativeLayout = (RelativeLayout) View.inflate(context,
					R.layout.listview_type_item, null);
		} else {
			relativeLayout = (RelativeLayout) convertView;
		}
		EntityAbsDeviceType type = alltypes.get(position);

		((ImageView) relativeLayout.findViewById(R.id.lvitem_types_iv_tag))
				.setImageResource(type.getResourceID());
		((TextView) relativeLayout.findViewById(R.id.lvitem_types_tv_text))
				.setText(type.getTypeName());
		if (isSelected.get(position)) {
			((ImageView) relativeLayout
					.findViewById(R.id.lvitem_types_iv_check))
					.setImageResource(viewItem_icon_yesCheck);
			relativeLayout.setBackgroundResource(R.drawable.list_item_selected);
		}else{
			((ImageView) relativeLayout
					.findViewById(R.id.lvitem_types_iv_check))
					.setImageResource(viewItem_icon_noCheck);
			relativeLayout.setBackgroundResource(R.drawable.list_item_unselected);
		}
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSelected.get(position)) {
					isSelected.put(position, false);
				} else {
					isSelected.put(position, true);
				}
				notifyDataSetChanged();
			}
		});
		return relativeLayout;
	}

	public void setHashMapChecked(HashMap<Integer, Boolean> hashMap){
		this.isSelected=hashMap;
	}
	
	public HashMap<Integer, Boolean> getHashMapChecked(){
		return this.isSelected;
	}
}
