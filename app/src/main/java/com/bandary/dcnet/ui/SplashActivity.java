package com.bandary.dcnet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ProgressBar;

import com.bandary.dcnet.R;

public class SplashActivity extends Activity {

	ProgressBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		bar=(ProgressBar)findViewById(R.id.pro_loading);
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
					handler.sendMessage(handler.obtainMessage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 收到信息后改变布局
			Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
}