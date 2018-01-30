package com.bandary.dcnet.uihandler;

import android.content.Context;

import com.bandary.dcnet.ui.ConstantHelper;

/**
 * SharePreference business Handler
 * */
public class UserSharePreferencesHandler {
	
	/**
	 * save user id from SharePreference
	 * */
	public static void saveUserIDToLocal(Context context,String userid){
	BaseSharePreferenceHandler.saveSharePreferences(context,
			ConstantHelper.SHARE_LOGIN_TAG,
			ConstantHelper.SHARE_LOGIN_USERID, userid);
	}
	/**
	 * save user name from SharePreference
	 * */
	public static void saveUserNameToLocal(Context context,String userName){
		BaseSharePreferenceHandler
		.saveSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_USERNAME,
				userName);
	}
	/**
	 * save user password from SharePreference
	 * */
	public static void saveUserPwdToLocal(Context context,String pwd){
		BaseSharePreferenceHandler.saveSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_PASSWORD,
				pwd);
	}
	/**
	 * find user id from SharePreference
	 * */
	public static String getUserIDFromLocal(Context context) {
		return BaseSharePreferenceHandler.getSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_USERID,
				ConstantHelper.DEFAULT_STRING_NULL);
	}
	/**
	 * find user name from SharePreference
	 * */
	public static String getUserNameFromLocal(Context context){
		return BaseSharePreferenceHandler.getSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_USERNAME,
				ConstantHelper.DEFAULT_STRING_NULL);
	}
	/**
	 * find user password from SharePreference
	 * */
	public static String getUserPwdFromLocal(Context context){
		return BaseSharePreferenceHandler.getSharePreferences(context,
				ConstantHelper.SHARE_LOGIN_TAG,
				ConstantHelper.SHARE_LOGIN_PASSWORD,
				ConstantHelper.DEFAULT_STRING_NULL);
	}
	
}
