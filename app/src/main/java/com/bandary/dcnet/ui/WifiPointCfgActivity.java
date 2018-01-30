package com.bandary.dcnet.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.bandary.dcnet.R;
/**
 * Step 2
 *设备通过wifi热点联网配置(快连技术)-配置获取
 *将当前连接的SSID显示在下拉框，密码由用户输入，做好配置点击下一步 ，可以保存配置到文件
 * 开始别做复杂了，就将当前连接点SSID显示在文本框
 * 
 *
 * */
@InjectLayer(value = R.layout.activity_wifi_point_cfg, parent = R.id.LLO_centerframe)
public class WifiPointCfgActivity extends BaseActivity {

	private static final int REQUEST_CODE_SCAN_WIFI_LIST=1;
	
	@InjectView(value = R.id.txt_wifi_point_ssid, binders = { @InjectBinder(method = "ssid_Click", listeners = { OnClick.class }) })
	private EditText mTxt_wifi_point_ssid;//当前wifi连接点的ssid显示
	@InjectView(R.id.txt_wifi_point_ssidpwd)
	private EditText mTxt_wifi_point_ssidpwd;
	@InjectView(value = R.id.btn_wifi_cfg_next, binders = { @InjectBinder(method = "next_Click", listeners = { OnClick.class }) })
	private Button mBtn_wifi_cfg_next;
	//private String mCurrent_SSID;
	
	@InjectInit
	public void init() {
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_wifi_point_cfg);
		//mTxt_wifi_point_ssid=(EditText)findViewById(R.id.txt_wifi_point_ssid);
		
		//mTxt_wifi_point_ssidpwd=(EditText)findViewById(R.id.txt_wifi_point_ssidpwd);
	}
	
	protected void ssid_Click(View view){
		Intent intent=new Intent(WifiPointCfgActivity.this,WifiScanListActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SCAN_WIFI_LIST);
	}
	
	protected void next_Click(View view){
		Intent intent=new Intent(WifiPointCfgActivity.this,WifiPointCommuActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 目标Activity返回到本实例时回传的数据
	 * */
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		/*if(data==null){
			return;
		}*/
		if(requestCode==REQUEST_CODE_SCAN_WIFI_LIST){
			if(resultCode==WifiScanListActivity.RESULT_CODE_TARGET_SSID){
				//如果是wifi扫描列表返回
			String	mCurrent_SSID=data.getStringExtra(WifiScanListActivity.KEY_INDEX_OF_LISTVIEW);
			this.mTxt_wifi_point_ssid.setText(mCurrent_SSID);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi_point_cfg, menu);
		return true;
	}

}


