package com.bandary.dcnet.utils;

import com.bandary.dcnet.ui.ConstantHelper;

import android.content.Context;
import android.os.Vibrator;
import android.telephony.TelephonyManager;

/**
 * 对手机权限的请求和底层功能的申请都放到一个访问器
 * */
public class GetPhonePriorityHelper {
	/**
	 * 	1.	getPhoneNumber方法返回当前手机的电话号码，
	 * 同时必须在androidmanifest.xml中
	 * 加入 android.permission.READ_PHONE_STATE 这个权限，
	 * 	2.	主流的获取用户手机号码一般采用用户主动发送短信到SP或接收手机来获取。
	 * @param context
	 */
	public static String getPhoneNumber(Context context){  
	    TelephonyManager mTelephonyMgr;  
	    mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
	    if(mTelephonyMgr==null){
	    	return "default phone number";
	    }
	    return mTelephonyMgr.getLine1Number();  
	}  
	/**
	 * 获取手机震动服务对象
	 * */
	public static Vibrator getPhoneVibrator(Context context){
		Vibrator mVibrator;
		mVibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		return mVibrator;
	}
	
	/**
	 * 使用长震动，还是短震动
	 * */
	public static void useVibrator(Vibrator vibrator,boolean isLong) {
		if (isLong) {
			vibrator.vibrate(ConstantHelper.vibrator_pattern_long, -1);
		} else {
			vibrator.vibrate(ConstantHelper.vibrator_pattern_short, -1);
		}
	}
}
