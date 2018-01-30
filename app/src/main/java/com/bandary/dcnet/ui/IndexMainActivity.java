package com.bandary.dcnet.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.bandary.dcnet.R;
import com.bandary.dcnet.adapter.ComponetGridViewAdapter;
import com.bandary.dcnet.uihandler.BaseSharePreferenceHandler;
import com.bandary.dcnet.uihandler.ExitApplication;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;

@InjectLayer(value = R.layout.activity_index_main, parent = R.id.LLO_centerframe)
public class IndexMainActivity extends BaseActivity {
	public static String TAG = "IndexMainActivity";
	private String usid;// userid
	boolean[] customizingType;// local storage customizing data
	//private ArrayList<EntityAbsDeviceType> typelist;// need display
	GridView gridView;

	@InjectView(binders = { @InjectBinder(method = "click", listeners = { OnClick.class }) })
	Button btn_index_sysinfo, btn_index_addtype;

	public void click(View view) {
		switch (view.getId()) {
		case R.id.btn_index_sysinfo:
			openAboutUs();
			break;
		case R.id.btn_index_addtype:
		default:
			openAddType();
			break;
		}
	}

	// 关于我们
	public void openAboutUs() {
		Intent intent = new Intent(IndexMainActivity.this,
				AboutUsActivity.class);
		startActivity(intent);
		//finish();
	}

	// 打开类别设置
	public void openAddType() {
		Intent intent = new Intent(IndexMainActivity.this,
				TypeManagerActivity.class);
		startActivity(intent);
		//finish();
	}

	@Override
	@InjectInit
	public void init() {
		//for exit
		ExitApplication.getInstanse().addActivity(this);
		// get login usid
		usid = getusid();
		// translate share preference date to boolean array,for initial checked
		customizingType = initCustomizingTypes(IndexMainActivity.this, usid);
		// get listview items data
		// typelist = initTypes(customizingType);
		List<String> titles = initTypesName(customizingType);
		List<Integer> imgsource = initTypesImg(customizingType);

		gridView = (GridView) findViewById(R.id.index_main_componet_grid_view);

		// List<String> List<Integer>
		gridView.setAdapter(new ComponetGridViewAdapter(IndexMainActivity.this,
				titles, imgsource));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if (position == 0) {
					//TODO 每个图标都代表了设备类型，将设备类型传递到列表界面是这个任务的重点					
					Intent intent = new Intent(IndexMainActivity.this,
							KTListActivity.class);
					intent.putExtra("ftype", position);
					startActivity(intent);
				} else {
					MakeToast("正在建设中" + position);
				}
			}
		});
	}

	/**
	 * 获取用户id
	 * */
	private String getusid() {
		return UserSharePreferencesHandler.getUserIDFromLocal(getApplicationContext());

	}

	/**
	 * init listview item data
	 * */
	/*
	 * public ArrayList<EntityAbsDeviceType> initTypes(boolean[] displayvalue) {
	 * ArrayList<EntityAbsDeviceType> typelist = new
	 * ArrayList<EntityAbsDeviceType>(); for (int i = 0; i <displayvalue.length;
	 * i++) { if(displayvalue[i]){ typelist.add(new
	 * EntityAbsDeviceType(ConstantHelper.ZN_TYPES_NAME[i],
	 * ConstantHelper.ZN_TYPES_NUM[i], ConstantHelper.ZN_TYPES_PIC[i])); } }
	 * return typelist; }
	 */

	public ArrayList<String> initTypesName(boolean[] isDisplay) {
		ArrayList<String> typelist = new ArrayList<String>();
		for (int i = 0; i < isDisplay.length; i++) {
			if (isDisplay[i]) {
				typelist.add(ConstantHelper.ZN_TYPES_TAG[i]);
			}
		}
		return typelist;
	}

	public ArrayList<Integer> initTypesImg(boolean[] isDisplay) {
		ArrayList<Integer> typelist = new ArrayList<Integer>();
		for (int i = 0; i < isDisplay.length; i++) {
			if (isDisplay[i]) {
				typelist.add(ConstantHelper.ZN_TYPES_PIC[i]);
			}
		}
		return typelist;
	}

	/**
	 * 读取配置文件内的类型选配数据放到全局变量里面，注意 存储格式与显示需要转换，存储的是字符串，显示的是boolean数组
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
	
	private static Boolean isExit = false; // 按两次返回键退出
	private static Boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			//3s后喂狗
			isExit = false;
			hasTask = true;
		}
	};	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode==KeyEvent.KEYCODE_BACK){//按两次返回键退出
            	//默认是不想退出的
                    if(isExit==false){
                            isExit = true;//意图退出 
                            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                            if(!hasTask){
                                    tExit.schedule(task, 3000);//3s后还没按返回键就喂狗，不退出
                                    }
                            }else{
                                    //在需要结束所有Activity的时候调用exit方法
                                    ExitApplication.getInstanse().exit();
                                    }  
                    }                          
            return false;  
    }	
}
