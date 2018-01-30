package com.bandary.dcnet.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.ui.ConstantHelper;

/**
 * 空调列表信息ListView Adapter定义 1.填充数据源 2.写给ListView Item
 * */
public class KTInfoListViewAdapter extends BaseAdapter {

	//private static String tag = "KTInfoListViewAdapter";
	private Context context;
	private List<EntityKTInfo> kTList;
	//以下为临时变量	
	
	
	
	public KTInfoListViewAdapter(Context context, List<EntityKTInfo> kTList) {
		this.context = context;
		this.kTList = kTList;
	}

	@Override
	public int getCount() {
		return kTList.size();
	}

	@Override
	public Object getItem(int position) {
		return kTList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.i(tag, "getView() begin");
		RelativeLayout relativeLayout = null;	
		if (convertView == null) {
			relativeLayout = (RelativeLayout) View.inflate(context,
					R.layout.listview_kt_item, null);
		} else {
			relativeLayout = (RelativeLayout) convertView;
		}
		if (kTList != null) {			
			String txt_roomtemp=ConstantHelper.offLine_display;
			String txt_model=ConstantHelper.offLine_display;
			String txt_speed=ConstantHelper.offLine_display;
			//String txt_state=ConstantHelper.offLine_display;
			EntityKTInfo dataItem = kTList.get(position);
			if (ConstantHelper.ZN_Array_KT_fstate[1].equals(kTList.get(position).getfState())) {				
				txt_roomtemp=dataItem.getFroomtemp()+ConstantHelper.ZN_TEMP_UNIT;
				txt_model=dataItem.getfModel();
				txt_speed=dataItem.getFspeed();				
			}			
			((TextView) relativeLayout.findViewById(R.id.kt_listview_text_name))
					.setText(dataItem.getFname());
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_fCommstate))
					.setText(dataItem.getfCommstate());
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_fmac))
					.setText(dataItem.getfMac());			
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_froomtemp))
					.setText(txt_roomtemp);
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_fModel))
					.setText(txt_model);
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_fspeed))
					.setText(txt_speed);		
			((TextView) relativeLayout
					.findViewById(R.id.kt_listview_text_fstate))
					.setText(dataItem.getfState());			
		}
		//Log.i(tag, "getView() end");
		return relativeLayout;
	}

	public void setKTLists(List<EntityKTInfo> list) {
		//Log.i(tag, "setKTLists() begin");
		this.kTList = list;
		//Log.i(tag, "setKTLists() end");
	}

}
