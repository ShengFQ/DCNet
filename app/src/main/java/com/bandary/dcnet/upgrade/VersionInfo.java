package com.bandary.dcnet.upgrade;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
/**
 * APK升级，需要定义的数据传输
 * */
public class VersionInfo {
	/*
	 * LIKE THIS {"msgType":"T","msg":{"appname":"掌上家居","apkname":"DCNet1.5.apk","vername":"1.7","vercode":"2","note":"1.测试自动升级<BR/>2.居然成功了"}}
	 * 
	 * */
	private int mCversionCode;//客户端版本代码，用于对比
	private String mCversionName;//客户端版本名称，用于显示给用户看
	
	private int mSversionCode;//服务器版本代码，用于对比
	private String mSversionName;//服务器本名称，用于显示给用户看
	
	private String mSappName;//服务器程序名称，中文名称
	private String mSapkName;//服务器apk名称，指明下载对象
	private String mSapkNote;//服务器更新日志，新特征
	public int getCversionCode() {
		return mCversionCode;
	}
	public void setCversionCode(int cversionCode) {
		mCversionCode = cversionCode;
	}
	public String getCversionName() {
		return mCversionName;
	}
	public void setCversionName(String cversionName) {
		mCversionName = cversionName;
	}
	public int getSversionCode() {
		return mSversionCode;
	}
	public void setSversionCode(int sversionCode) {
		mSversionCode = sversionCode;
	}
	public String getSversionName() {
		return mSversionName;
	}
	public void setSversionName(String sversionName) {
		mSversionName = sversionName;
	}
	public String getSappName() {
		return mSappName;
	}
	public void setSappName(String sappName) {
		mSappName = sappName;
	}
	public String getSapkName() {
		return mSapkName;
	}
	public void setSapkName(String sapkName) {
		mSapkName = sapkName;
	}
	public String getSapkNote() {
		return mSapkNote;
	}
	public void setSapkNote(String sapkNote) {
		mSapkNote = sapkNote;
	}
	
	private static final String tag="VersionInfo";
	//获取 mCversionCode
	public static int getVerCode(Context context){
		int verCode=0;
		try{
			verCode=context.getPackageManager().getPackageInfo("com.bandary.dcnet", 0).versionCode;
		}catch(NameNotFoundException e){
			Log.e(tag, e.getMessage());
		}
		return verCode;
	}
	
	//获取mCversionName
	public static String getVerName(Context context){
		String verName="";
		try{
			verName=context.getPackageManager().getPackageInfo("com.bandary.dcnet", 0).versionName;
		}catch(NameNotFoundException e){
			Log.e(tag, e.getMessage());
		}
		return verName;
	}
	
}
