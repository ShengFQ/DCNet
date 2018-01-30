package com.bandary.dcnet.ui;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityAbsDevice;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.service.KTService;

/**
 * 正在建设中
 * 2014-6-24 mac type name 需要修改
 * */
@InjectLayer(value = R.layout.activity_edit_dc, parent = R.id.LLO_centerframe, isTitle = false)
public class EditDCActivity extends BaseActivity {	
	private final String tag="EditDCActivity";
	
	private EditText txt_ktName;
	private Button btn_save;
	/** 初始化方法
	 * init方法会调用2次，实验下
	 * */
	@InjectInit
	public void init() {	
		initialView();
		Bundle bundle=getIntent().getExtras();
		EntityAbsDevice device=(EntityAbsDevice)bundle.getSerializable(ConstantHelper.KEY_ENTITY_ABSDEVICE);
		setView(device.getfMac(),device.getFtype(),device.getFname());
	}
	
	/**
	 * initialization View Object
	 * */
	private void initialView(){
		txt_ktName=(EditText)findViewById(R.id.txt_edit_dc_rename);
		btn_save=(Button)findViewById(R.id.btn_editdc_save);		
	}
	
	/**
	 * validate the input parameter
	 * */
	private boolean validateView(){
		if(txt_ktName.getText().toString().equals(ConstantHelper.DEFAULT_STRING_NULL)){
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * set the view
	 * */
	private void setView(final String fmac,final String ftype,final String fname){
		txt_ktName.setText(fname);
		btn_save.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//validate the date
				if(validateView()){					
					saveToServer(fmac,ftype);
				}
			}
		});
	}

	
	/**
	 * asynctask post the ktinfo to server
	 * */
	private void saveToServer(String fmac,String ftype){
		String newName=txt_ktName.getText().toString();
		SaveToServerAsynTask saveAsynTask=new SaveToServerAsynTask();
		saveAsynTask.execute(fmac,ftype,newName);
	}
	
	/**
	 * 修改数据到服务器和本地
	 * */
	private class SaveToServerAsynTask extends AsyncTask<String,String,EntityMessage>{
		@Override
		protected EntityMessage doInBackground(String... items){
			EntityMessage message=null;
			try{
				//complete to submit to server
				message=KTService.getInstance().remote_UpdateKTReg(items[0],items[1],items[2]);	
				//how to deal with this situation when the message object is null				
			}catch(NameNotFoundException e){
				Log.e(tag, "SaveToServerAsynTask", e);
			}
			return message;
		}
		
		@Override
		protected void onPostExecute(EntityMessage message){			
			handleResult(message);
		}
	}
	
	/**
	 * 根据远程返回的message对象显示结果
	 * @param message 远程对象
	 * */
	private void handleResult(EntityMessage message){
		if(message==null){
			super.MakeToast(ConstantHelper.msg_global_DisConnect);
		}else if(message.getMsgType().equals(ConstantHelper.MSGTYPE_T)){
			//completed edited and return back
			Intent intent=new Intent(EditDCActivity.this,KTListActivity.class);
			startActivity(intent);
		}else{
			this.MakeToast(ConstantHelper.msg_global_unknown_data);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_dc, menu);
		return true;
	}

}
