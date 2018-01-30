package com.bandary.dcnet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Required;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.entity.EntityUserInfo;
import com.bandary.dcnet.service.UserService;
import com.bandary.dcnet.uihandler.BaseSharePreferenceHandler;
import com.bandary.dcnet.uihandler.UserSharePreferencesHandler;

/**
 * 登录
 * */
@InjectLayer(value = R.layout.activity_login, isTitle = false, isFull = true)
public class LoginActivity extends BaseValidationActivity {
	// final variable
	private static final String tag = "LoginActivity";
	// storage text variable

	// UI variable
	// @Email(empty = false, message = "邮箱格式错误", order = 1)
	@Required(message = "用户名不能为空", order = 1)
	private EditText view_userName;
	@Password(message = "请输入密码", order = 2)
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;
	private Button view_loginRegister;
	private Button view_btn_forward_forgot_password;
	private Button view_btn_forward_relax;
	/** 登录loading提示框 */
	private ProgressDialog proDialog;
	private Thread loginThread;	

	@InjectInit
	public void init() {	
		findViewsById();
		initViewTextValue();
		setListener();
	}

	/**
	 * 初始化界面
	 * 
	 * @param isRememberMe
	 *            如果当时点击了RememberMe,并且登陆成功过一次,则saveSharePreferences(true,ture)后,
	 *            则直接进入
	 * */
	private void initViewTextValue() {
		String userName = UserSharePreferencesHandler
				.getUserNameFromLocal(getApplicationContext());
		String password = UserSharePreferencesHandler
				.getUserPwdFromLocal(getApplicationContext());
		if (!ConstantHelper.DEFAULT_STRING_NULL.equals(userName)) {
			view_userName.setText(userName);
		}
		if (!ConstantHelper.DEFAULT_STRING_NULL.equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}
		view_loginSubmit.requestFocus();
	}

	/**
	 * set the view id
	 * */
	private void findViewsById() {
		view_userName = (EditText) findViewById(R.id.txt_login_email);
		view_password = (EditText) findViewById(R.id.txt_login_password);
		view_rememberMe = (CheckBox) findViewById(R.id.chk_login_remenberme);
		view_loginSubmit = (Button) findViewById(R.id.btn_login_submit);
		view_loginRegister = (Button) findViewById(R.id.btn_login_forward_Register);
		view_btn_forward_forgot_password = (Button) findViewById(R.id.btn_login_forward_forgot_password);
		view_btn_forward_relax = (Button) findViewById(R.id.btn_login_forward_relax);
	}

	/** 设置监听器 */
	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_loginRegister.setOnClickListener(registerListener);
		view_btn_forward_forgot_password
				.setOnClickListener(forgotpasswordListener);
		view_btn_forward_relax.setOnClickListener(relaxListener);
	}

	/** 登录Button Listener */
	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 这里是做验证
			//initValidator(LoginActivity.this);
			
			//TODO 调试
			doForwardtoIndex();
		}
	};

	/** 注册Listener */
	private OnClickListener registerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			// 转向注册页面
			startActivity(intent);
		}
	};

	/**
	 * 忘记密码链接按钮触发事件
	 * */
	private OnClickListener forgotpasswordListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					ForgetPwdActivity.class);
			// 转向注册页面
			startActivity(intent);
		}
	};

	/**
	 * 激活设备链接按钮触发事件
	 * */
	private OnClickListener relaxListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,
					WifiPointCfgActivity.class);
			startActivity(intent);
		}
	};

	/** 记住我的选项是否勾选 */
	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	/**
	 * 保存登录信息到本地
	 * */
	private void doSaveLoginUserToLocal(EntityUserInfo user) {
		if (isRememberMe()) {
			UserSharePreferencesHandler.saveUserPwdToLocal(
					getApplicationContext(), user.getUsPwd());
		}
		UserSharePreferencesHandler.saveUserIDToLocal(getApplicationContext(),
				user.getUsId());
		UserSharePreferencesHandler.saveUserNameToLocal(
				getApplicationContext(), user.getUsName());
	}

	/**
	 * 跳转到主页
	 * */
	private void doForwardtoIndex() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, IndexMainActivity.class);
		startActivity(intent);
	}

	class LoginThread implements Runnable {
		@Override
		public void run() {
			// 登录验证
			// 保存数据
			// 跳转界面
			EntityMessage message = null;
			try {
				// validate user log in
				message = UserService.getInstance().remote_doSignIn(
						view_userName.getText().toString(),
						view_password.getText().toString());
				if (ConstantHelper.MSGTYPE_T.equals(message.getMsgType())) {
					EntityUserInfo user = UserService.getInstance()
							.local_getUserInfoFromEntityMessage(message);
					user.setUsPwd(view_password.getText().toString());
					doSaveLoginUserToLocal(user);
					doForwardtoIndex();
				} else if (ConstantHelper.MSGTYPE_F
						.equals(message.getMsgType())) {
					BaseSharePreferenceHandler.clearSharePreferences(
							LoginActivity.this, ConstantHelper.SHARE_LOGIN_TAG,
							ConstantHelper.SHARE_LOGIN_PASSWORD);
					Message messages = new Message();
					Bundle bundle = new Bundle();
					bundle.putBoolean(
							ConstantHelper.signal_handler_isNotValidated, true);
					messages.setData(bundle);
					loginHandler.sendMessage(messages);
				} else {
					// 密码验证失败
					// 通过调用handler来通知UI主线程更新UI,
					// proDialog.dismiss();
					Message messages = new Message();
					Bundle bundle = new Bundle();
					bundle.putBoolean(ConstantHelper.signal_handler_isNetError,
							true);
					messages.setData(bundle);
					loginHandler.sendMessage(messages);
				}
			} catch (RuntimeException e) {
				// 请求失败或连接失败
				Log.e(tag, e.getMessage());
			} catch (Exception e) {
				// json解析错误
				Log.e(tag, e.getMessage());
			} finally {
				proDialog.dismiss();
			}
		}
	}

	/** 登录后台通知更新UI线程,主要用于登录失败,通知UI线程更新界面 */
	Handler loginHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			boolean isNetError = msg.getData().getBoolean(
					ConstantHelper.signal_handler_isNetError);
			boolean isValidated = msg.getData().getBoolean(
					ConstantHelper.signal_handler_isNotValidated);
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(LoginActivity.this,
						ConstantHelper.msg_global_login_failure,
						Toast.LENGTH_SHORT).show();
			}
			// 用户名和密码错误
			else if (isValidated) {
				Toast.makeText(LoginActivity.this,
						ConstantHelper.msg_global_wrong_validate,
						Toast.LENGTH_SHORT).show();
				// 清除以前的SharePreferences密码
				BaseSharePreferenceHandler.clearSharePreferences(
						LoginActivity.this, ConstantHelper.SHARE_LOGIN_TAG,
						ConstantHelper.SHARE_LOGIN_PASSWORD);
			}
		}
	};

	// 数据验证成功
	@Override
	public void onValidationSucceeded() {
		proDialog = ProgressDialog.show(LoginActivity.this,
				ConstantHelper.msg_global_loading,
				ConstantHelper.msg_global_waitting, true, true);
		// 开一个线程进行登录验证
		loginThread = new Thread(new LoginThread());
		loginThread.start();
	}	
}
