package com.bandary.dcnet.ui;

import java.text.DateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.pc.ioc.verification.annotation.ConfirmPassword;
import com.android.pc.ioc.verification.annotation.Email;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Required;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.entity.EntityPhoneUserInfo;
import com.bandary.dcnet.entity.EntityUserInfo;
import com.bandary.dcnet.service.UserService;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;
import com.bandary.dcnet.utils.GetPhonePriorityHelper;

/**
 * building 2014-6-30 for user sign up
 * 1.定义输入接口 2.校验接口字段-实现了验证器接口 3.异步提交 4.注册事件驱动
 * */

public class RegisterActivity extends BaseValidationActivity  {
	// 响应输入事件
	@Email(empty = false, message = "邮箱格式错误", order = 1)
	private EditText edit_txt_register_email;
	@Password(message = "请输入密码", order = 2)
	private EditText edit_txt_register_password;
	@ConfirmPassword(message = "请输入重复密码与密码一致", order = 3)
	private EditText edit_txt_register_repassword;	
	private Spinner spi_txt_register_securityQue;
	@Required(message = "密保答案不能为空", order = 5)
	private EditText edit_txt_register_securityAns;

	private Button btn_register_submit;
	private Button btn_forward_Login;
	private EntityPhoneUserInfo mEntityPhoneUserInfo;
	/*public void initHandler(Activity activity){
		super.initHandler(activity);
	}*/
	public EntityPhoneUserInfo getEntityPhoneUserInfo() {
		return mEntityPhoneUserInfo;
	}
	public void setEntityPhoneUserInfo(EntityPhoneUserInfo entityPhoneUserInfo) {
		mEntityPhoneUserInfo = entityPhoneUserInfo;
	}

	

	/**
	 * 初始化界面接口
	 * */
	protected void findID() {
		edit_txt_register_email = (EditText) findViewById(R.id.txt_register_email);
		edit_txt_register_password = (EditText) findViewById(R.id.txt_register_password);
		edit_txt_register_repassword = (EditText) findViewById(R.id.txt_register_repassword);
		spi_txt_register_securityQue = (Spinner) findViewById(R.id.txt_register_securityQue);
		edit_txt_register_securityAns = (EditText) findViewById(R.id.txt_register_securityAns);
		btn_register_submit = (Button) findViewById(R.id.btn_register_submit);
		btn_forward_Login = (Button) findViewById(R.id.btn_register_forward_Login);
		/**
		 * 绑定数据源
		 * */
		getBaseHander().setSpinnerAdapter(spi_txt_register_securityQue, R.array.arrsecurityQue);
		/**
		 * 注册事件：点击注册按钮
		 * */
		btn_register_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 这里是做验证
				initValidator(RegisterActivity.this);
			}
		});
		/**
		 * 注册事件：点击登录按钮
		 * */
		btn_forward_Login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Activity的生命周期初始化函数
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);		
		findID();
	}

	// 异步提交任务

	// 重写选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	

	// 数据验证成功
	@Override
	public void onValidationSucceeded() {
		String sp=spi_txt_register_securityQue.getSelectedItem().toString();
		MakeToast(sp);
		String form_email = edit_txt_register_email.getText().toString();
		String form_pwd = edit_txt_register_password.getText().toString();
		String form_secAns=edit_txt_register_securityAns.getText().toString();
		String form_tel = GetPhonePriorityHelper
				.getPhoneNumber(RegisterActivity.this);
		getEntityPhoneUserInfo().setUsName(form_email);
		getEntityPhoneUserInfo().setUsMail(form_email);
		getEntityPhoneUserInfo().setUsPwd(form_pwd);
		getEntityPhoneUserInfo().setUsEnable(ConstantHelper.DEFAULT_STRING_Y);
		getEntityPhoneUserInfo().setUsVerify(ConstantHelper.DEFAULT_STRING_N);
		getEntityPhoneUserInfo().setUsSecurityQue(sp);//TODO 乱码
		getEntityPhoneUserInfo().setUsSecurityAns(form_secAns);
		getEntityPhoneUserInfo().setUsPhoneNum(form_tel);//TODO 平板没有手机号
		getEntityPhoneUserInfo().setUsRegtime(DateFormat.getInstance().format(new Date()));
		
		// post to server
		FormAsyncTAsk task = new FormAsyncTAsk();
		task.execute(getEntityPhoneUserInfo());
	}

	

	public class FormAsyncTAsk extends AsyncTask<EntityPhoneUserInfo, String, EntityMessage> {
		@Override
		protected EntityMessage doInBackground(EntityPhoneUserInfo... params) {
			EntityMessage message=null;
			try{
				message=UserService.getInstance().remote_doSignUp(params[0]);
				if(message.getMsgType().equals(ConstantHelper.MSGTYPE_T)){
					//TODO 如果注册方法 注册成功返回用户的编号和登录信息，则该方法可去掉，目前测试采用再次登录的方式
					message=UserService.getInstance().remote_doSignIn(params[0].getUsName(), params[0].getUsPwd());
					EntityUserInfo info=UserService.getInstance().local_getUserInfoFromEntityMessage(message);
					UserSharePreferencesHandler.saveUserIDToLocal(getApplicationContext(), info.getUsId());
					UserSharePreferencesHandler.saveUserNameToLocal(getApplicationContext(), info.getUsName());
					UserSharePreferencesHandler.saveUserPwdToLocal(getApplicationContext(), info.getUsPwd());					
				}
			}catch(NameNotFoundException e){
				message=null;
				e.printStackTrace();			
			}
			return message;
		}

		protected void onPostExecute(EntityMessage message) {
			if(message==null){
				MakeToast(ConstantHelper.msg_global_DisConnect);
				return;
			}else if(message.getMsgType().equals(ConstantHelper.MSGTYPE_F)){
				MakeToast(ConstantHelper.msg_global_register_failure);
				return;
			}else if(message.getMsgType().equals(ConstantHelper.MSGTYPE_T)){
				Intent intent=new Intent(RegisterActivity.this, IndexMainActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}
}
