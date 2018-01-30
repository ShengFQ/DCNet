package com.bandary.dcnet.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bandary.dcnet.R;

@InjectLayer(value = R.layout.activity_about_us, parent = R.id.LLO_centerframe)
public class AboutUsActivity extends BaseActivity {
	private Button mbtn_back;
	private Button mbtn_upgrade;	
	@InjectInit
	public void init() {
		initialView();
	}
	
	
	public void initialView(){
		mbtn_back=(Button)findViewById(R.id.btn_about_back);
		mbtn_upgrade=(Button)findViewById(R.id.btn_about_checkUpgrade);
		mbtn_back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.finish();
			}
		});
		mbtn_upgrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				Intent intent=new Intent(AboutUsActivity.this,UpgradeActivity.class);		
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_us, menu);
		return true;
	}
}
