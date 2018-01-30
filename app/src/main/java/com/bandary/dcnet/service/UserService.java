package com.bandary.dcnet.service;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.content.pm.PackageManager.NameNotFoundException;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.entity.EntityPhoneUserInfo;
import com.bandary.dcnet.entity.EntityUserInfo;
import com.bandary.dcnet.ui.ConstantHelper;

public class UserService {
	private String tag = "UserService";

	private static UserService instance;

	private UserService() {

	}

	public static UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	/**
	 * 检查用户登陆,服务器通过DataOutputStream的dos.writeInt(int);来判断是否登录成功(
	 * 服务器返回int>0登陆成功,否则失败),登陆成功后根据isRememberMe来判断是否保留密码(用户名是会保留的),
	 * 如果连接服务器超过5秒,也算连接失败.
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param validateUrl
	 *            检查登陆的地址
	 * @throws JSONException
	 * */
	public EntityMessage remote_doSignIn(String userName, String password)
			throws NameNotFoundException {
		// 用于标记登陆状态
		// boolean loginState = false;
		EntityMessage message = new EntityMessage();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(ConstantHelper.REMOTE_ARGS_UNAME, userName);
		params.put(ConstantHelper.REMOTE_ARGS_UPASSWORD, password);
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace + ConstantHelper.URI_LOGIN,
					params);
			Log.i(tag, "JSON result:" + responseEntity);
			if (responseEntity != null) {
				JSONObject jsonObject = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				message.setMsg(jsonObject.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObject.getString(ConstantHelper.MSGTYPE));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * get sign in user info from message
	 * */
	public EntityUserInfo local_getUserInfoFromEntityMessage(
			EntityMessage message) {
		JSONObject jsonObjectUser = null;
		EntityUserInfo user = null;
		try {
			jsonObjectUser = new JSONObject(message.getMsg());
			user = new EntityUserInfo();
			user.setUsId(jsonObjectUser
					.getString(ConstantHelper.REMOTE_ARGS_USID));
			user.setUsName(jsonObjectUser
					.getString(ConstantHelper.REMOTE_ARGS_UNAME));
			user.setSessionId(jsonObjectUser
					.getString(ConstantHelper.REMOTE_ARGS_SESSIONID));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * sign up for user add user into server
	 * */
	public EntityMessage remote_doSignUp(EntityPhoneUserInfo userinfo)
			throws NameNotFoundException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("usname", userinfo.getUsName());
		hashMap.put("uspwd", userinfo.getUsPwd());
		hashMap.put("usmail", userinfo.getUsMail());
		hashMap.put("usenable", userinfo.getUsEnable());
		hashMap.put("usregtime", userinfo.getUsRegtime());
		hashMap.put("usverify", userinfo.getUsVerify());
		hashMap.put("usquestion", userinfo.getUsSecurityQue());
		hashMap.put("usanswer", userinfo.getUsSecurityAns());
		hashMap.put("usphonenum", userinfo.getUsPhoneNum());
		EntityMessage message = null;
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_addUserInfo, hashMap);
			if (responseEntity != null) {
				Log.i(tag, responseEntity.toString());
				JSONObject jsonObject = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				message.setMsg(jsonObject.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObject.getString(ConstantHelper.MSGTYPE));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

}
