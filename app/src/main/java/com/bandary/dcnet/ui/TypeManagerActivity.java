package com.bandary.dcnet.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bandary.dcnet.R;
import com.bandary.dcnet.adapter.TypeEditAdapter;
import com.bandary.dcnet.entity.EntityAbsDeviceType;
import com.bandary.dcnet.uihandler.BaseSharePreferenceHandler;
import com.bandary.dcnet.uihandler.ExitApplication;

/**
 * 功能介绍： 主要是提供给用户选择控制产品类型，把自己需要的产品类型添加到主界面 风格采用switchButton+ListView的方式实现
 * 数据源来自SharePreferences保存的HashMap中
 * 
 * */
@InjectLayer(value = R.layout.activity_type_manager, parent = R.id.LLO_centerframe, isTitle = false)
public class TypeManagerActivity extends BaseActivity {
	private String usid;
	boolean[] customizingType;
	ArrayList<EntityAbsDeviceType> typelist;
	HashMap<Integer, Boolean> hsSelectMap;
	TypeEditAdapter adapter;
	private ListView listview;
	private Button btn_all;
	private ImageButton ibtn_back;
	private Button btn_save;

	/**
	 * 获取用户id
	 * */
	private String getusid() {
		return BaseSharePreferenceHandler.getSharePreferences(TypeManagerActivity.this,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_USERID,
				ConstantHelper.DEFAULT_STRING_NULL);

	}

	@Override
	@InjectInit
	public void init() {
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_type_manager);
		ExitApplication.getInstanse().addActivity(this);
		// initial view
		btn_all = (Button) findViewById(R.id.btn_typemgr_all);
		
		btn_save = (Button) findViewById(R.id.btn_typemgr_save);
		ibtn_back = (ImageButton) findViewById(R.id.btn_typemgr_back);
		// get login usid
		usid = getusid();
		// get listview items data
		typelist = initTypes(ConstantHelper.ZN_TYPES_NUM.length);
		// translate share preference date to boolean array,for initial checked
		customizingType = initCustomizingTypes(TypeManagerActivity.this, usid);
		// if choised must be checked ,so,this is a boolean value
		hsSelectMap = arrayToHashMap(customizingType);
		// initial adapter view
		listview = (ListView) findViewById(R.id.listview_typeList);
		listview.setItemsCanFocus(false);
		listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		adapter = new TypeEditAdapter(TypeManagerActivity.this, typelist,
				hsSelectMap);
		listview.setAdapter(adapter);
		// 声明注册事件
		// select all button
		OnClickListener click_all_Listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < typelist.size(); i++) {
					hsSelectMap.put(i, true);
				}
				adapter.setHashMapChecked(hsSelectMap);
				adapter.notifyDataSetChanged();
			}
		};
		// select not all button
	/*	OnClickListener click_notall_Listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < typelist.size(); i++) {
					hsSelectMap.put(i, false);
				}
				adapter.setHashMapChecked(hsSelectMap);
				adapter.notifyDataSetChanged();
			}
		};*/
		// back button
		OnClickListener click_back_Listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(TypeManagerActivity.this,IndexMainActivity.class);
				startActivity(intent);
				//finish();
			}
		};
		// save 
		//an idea:we save it :0,0,0,0,0,0,0,0  the 0 delegate choose the 1 delegate not choose
		OnClickListener click_save_Listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<Integer, Boolean> hash = adapter.getHashMapChecked();
				StringBuffer s = new StringBuffer();
				for (int i = 0; i < hash.size(); i++) {
					if (hash.get(i)) {
						s.append("0");
					} else {
						s.append("1");
					}
					s.append(",");
				}
				s.delete(s.length() - 1, s.length());
				//save to share preference
				writeCustomizingTypes(getBaseContext(), usid, s.toString());
				Intent intent = new Intent(TypeManagerActivity.this,
						IndexMainActivity.class);
				startActivity(intent);
				//close
				finish();
			}
		};
		// 注册事件
		// listview.setOnItemClickListener(itemClickListener);
		btn_all.setOnClickListener(click_all_Listener);
		//btn_notall.setOnClickListener(click_notall_Listener);
		btn_save.setOnClickListener(click_save_Listener);
		ibtn_back.setOnClickListener(click_back_Listener);
	}

	/**
	 * init listview item data
	 * */
	public ArrayList<EntityAbsDeviceType> initTypes(int typeSum) {
		ArrayList<EntityAbsDeviceType> typelist = new ArrayList<EntityAbsDeviceType>();
		for (int i = 0; i < typeSum; i++) {
			typelist.add(new EntityAbsDeviceType(
					ConstantHelper.ZN_TYPES_NAME[i],
					ConstantHelper.ZN_TYPES_NUM[i],
					ConstantHelper.ZN_TYPES_PIC[i]));
		}
		return typelist;
		// ArrayList<EntityAbsDeviceType> typelist = new
		// ArrayList<EntityAbsDeviceType>();
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[0],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_kt));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[1],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_xf));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[2],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_dn));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[3],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_rs));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[4],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_zj));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[5],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_zm));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[6],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_cl));
		// typelist.add(new EntityAbsDeviceType(ConstantHelper.ZN_TYPES[7],
		// ConstantHelper.TYPE_KT_NUM, R.drawable.ic_nav_hj));
		// return typelist;
	}

	/**
	 * fill hashmap<integer,boolean>
	 * */
	public HashMap<Integer, Boolean> arrayToHashMap(boolean[] cus) {
		HashMap<Integer, Boolean> selectHashMap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < cus.length; i++) {
			boolean isSel = false;
			if (cus[i] == true) {
				isSel = cus[i];
			}
			selectHashMap.put(i, isSel);
		}
		return selectHashMap;
	}

	/**
	 * read from share preference ,
	 * 
	 * */
	public boolean[] initCustomizingTypes(Context context, String usid) {
		// 用一个字符串保存每个用户的选择类别
		/*
		 * 格式如下 "0","0","0","0","0","0","0","0" 位置很重要
		 */
		boolean customizingType[] = new boolean[8];
		String customerChoise = "0,0,0,0,0,0,0,0";
		String customerChoiseString = BaseSharePreferenceHandler
				.getSharePreferences(context, ConstantHelper.SHARE_LOGIN_TAG,
						usid + "." + ConstantHelper.SHARE_CUSTOMIZING_TYPE,
						customerChoise);
		String[] item = customerChoiseString.split(",");
		for (int i = 0; i < item.length; i++) {
			if ("0".equals(item[i])) {
				customizingType[i] = true;
			} else {
				customizingType[i] = false;
			}
		}
		return customizingType;
	}

	/**
	 * //save to share preference
	 * */
	public void writeCustomizingTypes(Context context, String usid, String value) {
		// String customerChoise = "0,0,0,0,0,0,0,0";
		BaseSharePreferenceHandler.saveSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG, usid + "."
						+ ConstantHelper.SHARE_CUSTOMIZING_TYPE, value);
	}
}
