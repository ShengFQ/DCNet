package com.bandary.dcnet.ui;

import java.util.List;

import org.w3c.dom.ls.LSInput;

import android.app.ListActivity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bandary.dcnet.R;
import com.bandary.dcnet.utils.WifiConfHelper;

public class WifiScanListActivity extends ListActivity {

	public static final int RESULT_CODE_TARGET_SSID=1;
	public static final String KEY_INDEX_OF_LISTVIEW="key";
	private SimpleAdapter simpleAdapter;
	WifiConfHelper wifiConfHelper=WifiConfHelper.getInstance(getApplicationContext());
	//刷新按钮事件
	Button btn_refresh_wifilist;
	List<ScanResult> list_scan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi_scan_list);		
		list_scan=wifiConfHelper.getScanResults();
		simpleAdapter = new SimpleAdapter(WifiScanListActivity.this,
				wifiConfHelper.getAllNetWorkList(),
				R.layout.listview_wifi_item, new String[] { "ItemTitle",
						"ItemImage" }, new int[] { R.id.wifiTextView,
						R.id.wifiImageView });
		this.setListAdapter(simpleAdapter);
		btn_refresh_wifilist=(Button)findViewById(R.id.btn_refresh_wifilist);
		btn_refresh_wifilist.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				wifiConfHelper.reFreshScanResult();
				wifiConfHelper.getAllNetWorkList();
				simpleAdapter.notifyDataSetChanged();
			}
			
		});		
		}
	
	/**
	 * ListActivity特征方法，响应列表点击的回调函数
	 * */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent data=new Intent();	
		 ScanResult sc_result=list_scan.get(position);
		data.putExtra(KEY_INDEX_OF_LISTVIEW, sc_result.SSID);
		setResult(RESULT_CODE_TARGET_SSID, data);
		WifiScanListActivity.this.finish();
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi_scan_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
