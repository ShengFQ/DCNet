package com.bandary.dcnet.uihandler;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.ui.ConstantHelper;

/**
 * 定义为抽象类，抽象行为就必须要让子类实现，这就是规则
 * */
public class BaseHandler extends ContextWrapper implements IBaseHandler {
	// variable
	private final String tag = "BaseHandler";
	private String musid; // 登录用户ID
	private Context context;//add in 2014-7-1

	// getter and setter
	public String getMusid() {
		return musid;
	}

	public void setMusid(String musid) {
		this.musid = musid;
	}

	/**
	 * 基类构造函数定义了基类的初始化动作，子类只需要在构造函数里面加入super(base);就完成了基础函数的调用 constructor func
	 * */
	public BaseHandler(Context base) {
		super(base);
		this.context = base;
		//add in 2014-12-27 by shengfq
		initHandler((Activity) base);
	}

	/**
	 * 每个Hander对象都会自动调用该方法进行初始化
	 * 子类如果没有特别的初始化内容，都会调用父类的initHandler方法，前提是在子类的构造函数里面调用super();
	 * initHandler定义初始化需要执行的方法
	 * add in 2014-6-15
	 * */
	private void initHandler(Activity activity) {
		Log.i(tag, "initHandler "+activity.toString()+" is exec");
		// 将当前的Activity对象加入队列，进行统一内存管理
		ExitApplication.getInstanse().addActivity(activity);
	}
	
	/**
	 * add in 2014-7-3
	 * comment by shengfq in 2015-02-06
	 * 
	 * */
	/*public void initHandler() {
		Log.i(tag, "initHandler2 is exec");
		// 将当前的Activity对象加入队列，进行统一内存管理
		ExitApplication.getInstanse().addActivity((Activity)this.context);
	}*/

	/**
	 * 消息提示
	 * */
	public void MakeToast(String title) {
		Toast.makeText(getBaseContext(), title, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示RPC操作结果
	 * */
	public void displayOperationResult(EntityMessage entityMessage) {
		// Log.i(tag, result);
		// 返回3个状态 超时、成功、失败
		if (entityMessage == null) {
			MakeToast(ConstantHelper.msg_global_DisConnect);
			return;
		} else if (ConstantHelper.MSGTYPE_F.equals(entityMessage.getMsgType())) {
			MakeToast(ConstantHelper.msg_global_error);
			return;
		} else if (ConstantHelper.MSGTYPE_T.equals(entityMessage.getMsgType())) {
			MakeToast(ConstantHelper.msg_global_success);
			return;
		}
	}

	/**
	 * 使用代码数组作为下拉框数据源
	 */
	public void setSpinnerAdapter(Spinner spinner, String[] datasouce) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, datasouce);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
	}

	/**
	 * 使用XML文件的数组作为下拉框数据源 *
	 */
	public void setSpinnerAdapter(Spinner spinner, int souceID) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				context, souceID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
	}

}
