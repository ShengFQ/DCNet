package com.bandary.dcnet.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.util.Handler_TextStyle;
/**
 * 该类存在的理由：
 * 本来是想把验证特征写入BaseActivity里面的，但是发现BaseActivity是所有我Activity的父类，而所有子类并不都具备验证器特征，也就是
 * 只有一部分才需要验证器，所以单独从BaseActivity里面抽取一个分支用于验证器，所有需要验证的Activity都继承自BaseValidationActivity.
 * 验证器的使用方法：
 * 1.实现验证器接口-实现验证成功和失败方法(证明该类具有可验证性特征，实现接口就是实现某个特征)
 * 2.加入验证器对象
 * 3.触发验证事件中，初始化并调用验证器
 * */
public abstract class BaseValidationActivity extends BaseActivity implements
		ValidationListener {
	private Validator validator;
	/**
	 * IOC框架自带的验证框架
	 * 验证成功的处理,一般验证成功方法里面都是表单提交，这里不做限制，由子类去实现
	 * */
	public abstract void onValidationSucceeded();
	/**
	 * IOC框架自带的验证框架
	 * 验证失败的处理
	 * */
	public void onValidationFailed(View failedView, Rule<?> failedRule){
		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText) {
			failedView.requestFocus();
			Handler_TextStyle handler_TextStyle = new Handler_TextStyle();
			handler_TextStyle.setString(message);
			handler_TextStyle
					.setBackgroundColor(Color.RED, 0, message.length());
			((EditText) failedView).setError(handler_TextStyle
					.getSpannableString());
		} else {
			MakeToast(message);
		}
	}
	/**
	 * 验证器被初始化
	 **/
	public void initValidator(BaseValidationActivity baseValidationActivity){
		validator = new Validator(baseValidationActivity);
		validator.setValidationListener(baseValidationActivity);
		validator.validate();
	}
}
