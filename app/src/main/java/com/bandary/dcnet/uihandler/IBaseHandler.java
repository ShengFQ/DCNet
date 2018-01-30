package com.bandary.dcnet.uihandler;

import android.widget.Spinner;

import com.bandary.dcnet.entity.EntityMessage;
/**
 * 服务于activity的操作类接口，主要是多种设备获取数据，处理数据，事件响应的处理办法是一样的
 * 那么就可以在基本操作定义规范，基本实现类实现基本操作，如果个别handler有特殊操作，则特别重写方法。也可以扩展接口。
 * 此类只定义目前1种设备的通用操作规范。做事件的定义，由Activity调用事件实现接口对象。
 * @author John
 * @since 2014-6-16
 * @version 1.6
 * */
public interface IBaseHandler {
	/**
	 * 所有activity基本都需要有输出Toast到屏幕
	 * */
	public void MakeToast(String title) ;
	/**
	 * RPC访问返回的是json格式的EntityMessage对象数据，根据消息类型显示RPC请求结果
	 * 一般为：成功、失败、超时
	 * */
	public void displayOperationResult(EntityMessage entityMessage);
	//还有比如访问远程数据的接口
    //保存数据到本地的接口方法
	//操作数据的下发
	//资源释放
	//每个Activity都要加入销毁队列ExitApplication.getInstanse().addActivity(this)，有的时候忘记写就会造成内存泄露，怎样约束。
	
	//public void initHandler(Activity activity);	
	
	//public void initHandler();
	/**
	 * 获取登录用户ID
	 * */
	public String getMusid();
	/**
	 * 设置登录用户ID
	 * */
	public void setMusid(String musid);
	/**
	 * 设置下拉框的数据源为数组
	 * */
	public void setSpinnerAdapter(Spinner spinner, String[] datasouce);
	/**
	 * 设置下拉框的数据源为资源文件数组
	 * */
	public void setSpinnerAdapter(Spinner spinner, int souceID);
}
