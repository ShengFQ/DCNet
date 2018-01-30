package com.bandary.dcnet.uihandler;

import com.bandary.dcnet.ui.ConstantHelper;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
/**
 * SharePreference basic operator
 * the SharePreference is a basic local storage for android app
 * we can storage basic type data into that as xml
 * this is the add/modify/delete/find
 * */
public class BaseSharePreferenceHandler {
	/**
	 * clear the preference by  tag and key
	 * 
	 * @param context
	 *            context object
	 * @param tag
	 *           which file
	 * @param key
	 *            which key
	 * */
	public static void clearSharePreferences(Context context, String tag,
			String key) {
		ContextWrapper wrapper = new ContextWrapper(context);
		SharedPreferences share = wrapper.getSharedPreferences(tag, 0);
		if(share.contains(key)){
		share.edit().putString(key, ConstantHelper.DEFAULT_STRING_NULL).commit();
		}
		share = null;
	}

	/**
	 * save the preference by tag and key and value
	* @param context
	 *            context object
	 * @param tag
	 *           which file
	 * @param key
	 *            which key
	 * */
	public static void saveSharePreferences(Context context, String tag,
			String key, String value) {
		ContextWrapper wrapper = new ContextWrapper(context);
		SharedPreferences share = wrapper.getSharedPreferences(tag, 0);
		share.edit().putString(key, value).commit();	
		share = null;
	}

	/**
	 * find the preference by tag and key you may set a default value
	 * @param context
	 *            context object
	 * @param tag
	 *           which file
	 * @param key
	 *            which key
	 * */
	public static String getSharePreferences(Context context, String tag,
			String key, String defaultValue) {
		ContextWrapper wrapper = new ContextWrapper(context);
		SharedPreferences share = wrapper.getSharedPreferences(tag, 0);
		return share.getString(key, defaultValue);
	}
	/**
	 * modify the preference by tag and key and value
	* @param context
	 *            context object
	 * @param tag
	 *           which file
	 * @param key
	 *            which key
	 * */
	public static void modifySharePreferences(Context context,String tag,String key,
			String value){
		saveSharePreferences(context, tag, key, value);
	}
}
