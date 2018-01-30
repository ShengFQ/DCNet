package com.bandary.dcnet.upgrade;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.bandary.dcnet.R;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.ui.ConstantHelper;
import com.bandary.dcnet.utils.HexConvertHelper;

public class Config {
	private static final String tag = "Config";
	public static final String UPDATE_SERVER = ConstantHelper.HostNameSpace;
	public static final String UPDATE_APKNAME = "/DCNet.apk";
	public static final String UPDATE_VERJSON = "/upgrade.php";
	public static final String UPDATE_SAVENAME = "/DCNet.apk";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.bandary.dcnet", 0).versionCode;
		} catch (NameNotFoundException e) {

		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.bandary.dcnet", 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;

	}

	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}

	public static VersionInfo getSVersionInfo() {
		ResponseEntity responseEntity = null;
		EntityMessage message = null;
		VersionInfo vinfo = null;
		try {
			responseEntity = FastHttp.post(UPDATE_SERVER + UPDATE_VERJSON);
			Log.i(tag, responseEntity.toString());
			if (responseEntity != null) {
				JSONObject jsonObjectsMessage = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				vinfo = new VersionInfo();
				message.setMsg(jsonObjectsMessage.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObjectsMessage
						.getString(ConstantHelper.MSGTYPE));
				JSONObject jsonObjectSVersion = new JSONObject(message.getMsg());
				vinfo.setSappName(jsonObjectSVersion.getString("appname"));
				vinfo.setSapkName(jsonObjectSVersion.getString("apkname"));
				vinfo.setSversionName(jsonObjectSVersion.getString("vername"));
				vinfo.setSversionCode(HexConvertHelper.stringToInteger(
						jsonObjectSVersion.getString("vercode"),
						ConstantHelper.DEFAULT_INT_VALUE));
				vinfo.setSapkNote(jsonObjectSVersion.getString("note"));

			}
		} catch (JSONException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vinfo;
	}

	public static VersionInfo getCVersionInfo(Context context) {
		VersionInfo info = new VersionInfo();
		info.setCversionCode(getVerCode(context));
		info.setCversionName(getVerName(context));
		return info;
	}

	
	public static boolean doUpgrade(VersionInfo info) {
		if(info.getSversionCode()>info.getCversionCode()){
			return true;
		}
		return false;
	}

}