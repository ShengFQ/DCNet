package com.bandary.dcnet.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.verification.annotation.NumberRule;
import com.android.pc.ioc.verification.annotation.Regex;
import com.android.pc.ioc.verification.annotation.Required;
import com.android.pc.ioc.verification.annotation.NumberRule.NumberType;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.service.KTService;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;

/**
 * 通过WFJJ将数据下发下去后，要将温控器的MAC地址获取上来，并在该页面显示
 * Step 4
 * 
 * 设备注册所需资料 usid mac typeid name regstate location regtime regno
 * */
@InjectLayer(value = R.layout.activity_register_dc, parent = R.id.LLO_centerframe, isTitle = false)
public class RegisterDCActivity extends BaseValidationActivity {
	private String tag = "RegisterDCActivity";

	private RegisterDCAsyncTask registerAsynTask = null;
	private EntityKTInfo mEntityKTInfo;	
	private String mTypeID;
	public EntityKTInfo getEntityKTInfo() {
		return mEntityKTInfo;
	}
	public void setEntityKTInfo(EntityKTInfo entityKTInfo) {
		mEntityKTInfo = entityKTInfo;
	}
	public String getTypeID() {
		return mTypeID;
	}
	public void setTypeID(String typeID) {
		mTypeID = typeID;
	}
	@Required(message = "名称不能为空", order = 1)
	EditText txt_regkt_fname;	
	@Regex(message = "请输入12位MAC地址", trim = true, pattern = "[a-zA-Z0-9]{12,12}", order = 2)
	EditText txt_regkt_fmac;
	@NumberRule(type = NumberType.INTEGER,lt =1000,gt=1, message = "请输入4位数字注册码", order = 3)
	EditText txt_regkt_fregno;
	Button btn_regkt_register;
	ImageButton btn_regkt_back;

	// 初始化方法
	@InjectInit
	public void init() {
		initView();
		
		getBaseHander().setMusid(UserSharePreferencesHandler
				.getUserIDFromLocal(getApplicationContext()));
		setTypeID(getIntent().getStringExtra(
				ConstantHelper.REMOTE_ARGS_FTYPE));
		
	}
	
	/**
	 * 初始化界面元素
	 * */
	private void initView(){
		txt_regkt_fmac=(EditText)findViewById(R.id.txt_regkt_fname);
		txt_regkt_fregno=(EditText)findViewById(R.id.txt_regkt_fregno);
		txt_regkt_fmac=(EditText)findViewById(R.id.txt_regkt_fmac);
		btn_regkt_register=(Button)findViewById(R.id.btn_regkt_register);
		btn_regkt_back=(ImageButton)findViewById(R.id.btn_regkt_back);
		btn_regkt_register.setOnClickListener(doRegisterOnClickListener);
		btn_regkt_back.setOnClickListener(doBackOnClickListener);
	}

	/**
	 * 注册按钮验证
	 * */
	private OnClickListener doRegisterOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			initValidator(RegisterDCActivity.this);
		}
	};

	/**
	 * 后退按钮
	 * */
	private OnClickListener doBackOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RegisterDCActivity.this,
					KTListActivity.class);
			startActivity(intent);
		}
	};

	/**
	 * 异步提交任务
	 * */
	public class RegisterDCAsyncTask extends
			AsyncTask<EntityKTInfo, String, EntityMessage> {
		@Override
		protected EntityMessage doInBackground(EntityKTInfo... params) {
			EntityMessage message = null;
			// 是否允许注册
			/* T 表示允许注册 F表示不允许注册 在F条件下，msg：1表示设备不存在 2表示未处于注册请求状态 3表示注册码错误 */
			message = KTService.getInstance().remote_IsRegKTPermisson(
					params[0].getfMac(), params[0].getfRegNo(),
					params[0].getFtype());
			if (message != null
					&& ConstantHelper.MSGTYPE_T.equals(message.getMsgType())) {
				// 允许后立即注册
				try {
					message = KTService.getInstance()
							.remote_AddKTReg(params[0]);
				} catch (NameNotFoundException e) {
					Log.e(tag, "RegisterDCAsyncTask", e);
				}
			}
			return message;
		}

		@Override
		protected void onPostExecute(EntityMessage message) {
			// add into db
			// Log.i(tag, message.toString());
			if (message != null
					&& message.getMsgType().equals(ConstantHelper.MSGTYPE_T)) {
				// 添加到本地
				KTService.getInstance().local_addKTRegister(
						RegisterDCActivity.this,
						getEntityKTInfo());
				MakeToast(getEntityKTInfo().getFname()
						+ ConstantHelper.msg_global_regdc_success);
				return;
			} else if (message != null
					&& message.getMsgType().equals(ConstantHelper.MSGTYPE_F)) {
				if (ConstantHelper.singal_reg_failure_offline.equals(message
						.getMsg())) {
					MakeToast(ConstantHelper.msg_global_regdc_failure_offline);
					return;
				} else if (ConstantHelper.singal_reg_failure_norequest
						.equals(message.getMsg())) {
					MakeToast(ConstantHelper.msg_global_regdc_failure_norequest);
					return;
				} else if (ConstantHelper.singal_reg_failure_novalidate
						.equals(message.getMsg())) {
					MakeToast(ConstantHelper.msg_global_regdc_failure_novalidate);
					return;
				} else {
					MakeToast(ConstantHelper.msg_global_regdc_failure);
				}
			} else {
				MakeToast(ConstantHelper.msg_global_DisConnect);
				return;
			}

		}
	}

	

	/**
	 * 验证成功的处理
	 * */
	@Override
	public void onValidationSucceeded() {
		Toast.makeText(RegisterDCActivity.this,
				ConstantHelper.msg_global_waitting, Toast.LENGTH_SHORT).show();
		getEntityKTInfo().setFname(
				txt_regkt_fname.getText().toString());
		getEntityKTInfo().setfMac(
				txt_regkt_fmac.getText().toString());
		getEntityKTInfo().setfRegNo(
				txt_regkt_fregno.getText().toString());
		getEntityKTInfo().setFlocation(
				ConstantHelper.DEFAULT_STRING_NULL);
		getEntityKTInfo().setUsid(getBaseHander().getMusid());
		getEntityKTInfo().setFtype(getTypeID());
		getEntityKTInfo().setFregstate(ConstantHelper.DEFAULT_STRING_Y);
		getEntityKTInfo().setFregtime(
				new SimpleDateFormat(ConstantHelper.FORMAT_DATETIME)
						.format(new Date()));
		registerAsynTask = new RegisterDCAsyncTask();
		registerAsynTask.execute(getEntityKTInfo());
	}
}
