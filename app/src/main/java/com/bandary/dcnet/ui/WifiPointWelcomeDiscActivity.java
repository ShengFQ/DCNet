package com.bandary.dcnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bandary.dcnet.R;
/**
 * Step 1
 * 设备通过wifi热点联网配置-快连技术-发现欢迎
 * 欢迎发现联网温控器-告知用户怎样进入快连模式
 * **/
public class WifiPointWelcomeDiscActivity extends BaseActivity {
	private static final String TAG = "WelcomeDiscActivity";// LOG
	private Button btn_welcomeDisc_next;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_disc);
		btn_welcomeDisc_next = (Button) findViewById(R.id.btn_welcomeDisc_next);
		btn_welcomeDisc_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent wifipoint = new Intent(getApplicationContext(),
						WifiPointCfgActivity.class);
				startActivity(wifipoint);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.welcome_disc, menu);// comment in
		// 2014-12-27 by shengfq
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
