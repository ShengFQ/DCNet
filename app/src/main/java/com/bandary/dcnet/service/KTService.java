package com.bandary.dcnet.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.ResponseEntity;
import com.bandary.dcnet.dao.DBKTDao;
import com.bandary.dcnet.entity.EntityCtrlOrder;
import com.bandary.dcnet.entity.EntityKTInfo;
import com.bandary.dcnet.entity.EntityMessage;
import com.bandary.dcnet.ui.ConstantHelper;
import com.bandary.dcnet.utils.HexConvertHelper;

/**
 * 业务层 空调设备网络访问和数据库访问的处理类 网络访问返回的字符串为json格式数据，like
 * {"msg":"JSONObject/JSONArray/null","msgType":"T/F"} 网络访问需要注意的地方：
 * 1.如果网络不通，获取不到网络时 2.如果能够访问到服务，但是得不到访问结果时 3.要判断给我的msg内容为空的情况
 * 
 * 所有接收变量一律进行初始化，引用类型全部new出对象
 * 
 ***** 服务器上的参数都是代码，需要本地化，所以在接口处就将参数本地化是最理想的，避免到各个业务还去转******
 * */
public class KTService {
	// private String net_error_message = "网络连接异常";
	// public final String TAG = "KTService";
	private String tag = "KTService";
	private static KTService instance;

	private KTService() {
		ktDao = DBKTDao.getInstance();
	}

	public static KTService getInstance() {
		instance = new KTService();
		return instance;
	}

	private DBKTDao ktDao;

	/**
	 * 从服务器端获取简要的空调信息列表
	 * 
	 * @param usid
	 *            用户编号
	 * @param typeid
	 *            设备类型编号
	 * @return 1.如果超时则返回null 2.不超时则返回Message对象
	 * */
	public EntityMessage remote_GetKTs(String usid, String ftype)
			throws UnknownHostException {
		// 网络访问-----------------如果网络中断怎么处理(异常处理)------------如果网络延时怎么处理
		EntityMessage message = null;
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put(ConstantHelper.REMOTE_ARGS_USID, usid);
		hashMap.put("ftype", ftype);
		ResponseEntity responseEntity = null;
		try {
			responseEntity = FastHttp.post(ConstantHelper.HostNameSpace
					+ ConstantHelper.URI_GETKTS, hashMap);
			/*Log.i(tag, responseEntity.toString());*/
			if (responseEntity != null) {
				JSONObject jsonObjects = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				message.setMsg(jsonObjects.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObjects
						.getString(ConstantHelper.MSGTYPE));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return message;
	}

	/**
	 * 从远程服务器端读取的message信息中分离出KTInfo的概要信息
	 * */
	public List<EntityKTInfo> local_GetKTListFromEntityMessage(
			EntityMessage message) {
		List<EntityKTInfo> ktlist = null;
		if (message == null) {
			return ktlist;
		}
		try {
			if (message.getMsgType().equals(ConstantHelper.MSGTYPE_T)) {
				ktlist = new ArrayList<EntityKTInfo>();
				JSONArray jsonArray = new JSONArray(message.getMsg());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					EntityKTInfo kTInfo = new EntityKTInfo();
					kTInfo.setFname(jsonObject.getString("fname"));
					kTInfo.setfMac(jsonObject.getString("fmac"));
					kTInfo.setfCommstate(jsonObject.getString("fcommstate"));
					kTInfo.setFroomtemp(jsonObject.getString("froomtemp"));
					kTInfo.setfModel(jsonObject.getString("fmodel"));
					kTInfo.setFspeed(jsonObject.getString("fspeed"));
					kTInfo.setfState(jsonObject.getString("fstate"));
					kTInfo.setfNetSocketNo(jsonObject.getString("fnetsocketno"));
					ktlist.add(HexConvertHelper.translate(
							ConstantHelper.DEFAULT_LANG, kTInfo));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ktlist;
	}

	/**
	 * 从服务器端获取单个空调的状态
	 * 
	 * @param usid
	 *            用户编号
	 * @param fmac
	 *            mac地址
	 * @return 完整的kt对象
	 * */
	public EntityMessage remote_GetKT(String usid, String fmac)
			throws NameNotFoundException {
		// Log.i(tag, "getKTs:" + "usid:" + usid + "fmac:" + fmac);
		HashMap<String, String> hashMap = new HashMap<String, String>();
		EntityMessage message = null;
		hashMap.put("usid", usid);
		hashMap.put("fmac", fmac);
		try {
			ResponseEntity responseEntity = FastHttp
					.post(ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_getKTInfo, hashMap);
			// Log.i(tag, "JSON result:" + responseEntity);
			if (responseEntity != null) {
				message = new EntityMessage();
				JSONObject jsonObjects = new JSONObject(
						responseEntity.getContentAsString());
				message.setMsg(jsonObjects.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObjects
						.getString(ConstantHelper.MSGTYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return message;
	}

	/**
	 * 从message里面获取KTInfo对象
	 * 
	 * @param message
	 *            远程获取的数据对象
	 * @return KTObject
	 * */
	public EntityKTInfo local_GetKTFromEntityMessage(EntityMessage message) {
		EntityKTInfo kTInfo = null;
		if (message == null) {
			return kTInfo;
		}
		try {
			if (message.getMsgType().equals(ConstantHelper.MSGTYPE_T)) {
				JSONObject jsonObject = new JSONObject(message.getMsg());
				kTInfo = new EntityKTInfo();
				kTInfo.setfMac(jsonObject.getString("fmac"));
				kTInfo.setfCommstate(jsonObject.getString("fcommstate"));
				kTInfo.setFroomtemp(jsonObject.getString("froomtemp"));
				kTInfo.setFsettemp(jsonObject.getString("fsettemp"));
				kTInfo.setfModel(jsonObject.getString("fmodel"));
				kTInfo.setFspeed(jsonObject.getString("fspeed"));
				kTInfo.setfState(jsonObject.getString("fstate"));
				kTInfo.setfNetSocketNo(jsonObject.getString("fnetsocketno"));
				kTInfo.setFlock(jsonObject.getString("flock"));
				kTInfo.setFtrouble(jsonObject.getString("ftrouble"));
				kTInfo.setFsetspeed(jsonObject.getString("fsetspeed"));// add in
																		// 2014-04-6
				// Log.i(tag, "remote_GetKT:" + kTInfo.toString()); //
				// 蔡工如果不修改，我这里将报错。
				return HexConvertHelper.translateSimple(
						ConstantHelper.DEFAULT_LANG, kTInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kTInfo;
	}

	/**
	 * 发送一条给服务器的控制报文
	 * 
	 * @param socketno
	 * @param mac
	 * @param code
	 * @return 执行返回的消息
	 * */
	public EntityMessage remote_writeContrl(EntityCtrlOrder ctrlCode) {
		String codes = "{{" + ctrlCode.getSocketNo() + "," + ctrlCode.getMac()
				+ "," + ctrlCode.getCode() + "}}";
		return remote_WriteToServerKtCtrl(codes);
	}

	/**
	 * 发送多条给服务器的控制报文
	 * 
	 * @param item
	 *            一条发送给服务器的控制报文对象
	 * @return 执行返回的消息
	 * */
	public EntityMessage remote_writeContrl(List<EntityCtrlOrder> list) {
		StringBuffer code = new StringBuffer("{");
		for (EntityCtrlOrder item : list) {
			code.append("{" + item.getSocketNo() + "," + item.getMac() + ","
					+ item.getCode() + "}");
			code.append(",");
		}
		if (!code.equals("{")) {
			code.delete(code.length() - 1, code.length()); // 删除最后一个逗号
			code.append("}");
			return remote_WriteToServerKtCtrl(code.toString());
		}
		return new EntityMessage(ConstantHelper.msg_global_verify_failure,ConstantHelper.MSGTYPE_F);
	}

	/**
	 * 空调控制数据下发
	 * 
	 * @param ctrlstr
	 *            控制字符串
	 * @return EntityMessage
	 * */
	private EntityMessage remote_WriteToServerKtCtrl(String ctrlcode) {
		EntityMessage message = null;
		if (ConstantHelper.DEFAULT_STRING_NULL.equals(ctrlcode)) {
			return new EntityMessage(ConstantHelper.msg_global_verify_failure,ConstantHelper.MSGTYPE_F);
		}
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("ctrlcode", ctrlcode);
		Log.i(tag, "ctrlcode:"+ctrlcode);
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_WRITETOKTCTRL, hashMap);			
			if (responseEntity != null) {
				JSONObject jsonObjects = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				message.setMsg(jsonObjects.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObjects
						.getString(ConstantHelper.MSGTYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return message;
	}

	/**
	 * 获取已经注册的所有设备列表
	 * 
	 * @param usid
	 *            用户编号
	 * @return 空调的注册信息
	 * */
	public List<EntityKTInfo> remote_GetKTsRegister(String usid) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		EntityMessage message = new EntityMessage();
		hashMap.put("usid", usid);
		List<EntityKTInfo> ktlist = new ArrayList<EntityKTInfo>();
		try {
			ResponseEntity responseEntity = FastHttp
					.post(ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_GETKTREGS, hashMap);
			// Log.i(tag, "JSON result:" + responseEntity);
			if (responseEntity == null
					|| ConstantHelper.DEFAULT_STRING_NULL.equals(responseEntity
							.getContentAsString())) {
				throw new NetworkErrorException(
						ConstantHelper.msg_global_DisConnect);
			}
			JSONObject jsonObjects = new JSONObject(
					responseEntity.getContentAsString());
			message.setMsg(jsonObjects.getString(ConstantHelper.MSG));
			message.setMsgType(jsonObjects.getString(ConstantHelper.MSGTYPE));
			if (ConstantHelper.MSGTYPE_T.equals(message.getMsgType())) {
				JSONArray jsonArray = new JSONArray(message.getMsg());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					EntityKTInfo kTInfo = new EntityKTInfo();
					kTInfo.setFname(jsonObject.getString("fname"));
					kTInfo.setfMac(jsonObject.getString("fmac"));
					kTInfo.setFtype(jsonObject.getString("ftype"));
					kTInfo.setFregstate(jsonObject.getString("fregstate"));
					kTInfo.setFlocation(jsonObject.getString("flocation"));
					kTInfo.setFregtime(jsonObject.getString("regtime"));
					kTInfo.setfRegNo(jsonObject.getString("fregno"));
					ktlist.add(kTInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ktlist;
	}

	/**
	 * 是否允许注册
	 * 判断逻辑：在[对应设备表]内，根据fmac查找，1.是否存在该[fmac]，如果存在2.是否[请求注册]，如果请求注册3.[注册码是否相等]
	 * 
	 * @param fmac
	 *            mac地址
	 * @param fregno
	 *            注册码
	 * @param ftype
	 *            类别-相应的设备信息表
	 * @EntityMessage 是否允许注册
	 * */
	public EntityMessage remote_IsRegKTPermisson(String fmac, String fregno,
			String ftype) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fmac", fmac);
		hashMap.put("fregno", fregno);
		hashMap.put("ftype", ftype);
		EntityMessage message = new EntityMessage();
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_isRegPermission, hashMap);
			if (responseEntity == null
					|| ConstantHelper.DEFAULT_STRING_NULL.equals(responseEntity
							.getContentAsString())) {
				throw new NetworkErrorException(
						ConstantHelper.msg_global_DisConnect);
			}
			JSONObject jsonObjects = new JSONObject(
					responseEntity.getContentAsString());
			message.setMsg(jsonObjects.getString(ConstantHelper.MSG));
			message.setMsgType(jsonObjects.getString(ConstantHelper.MSGTYPE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 添加空调注册
	 * 
	 * @param info
	 *            空调注册信息
	 * 提交参数:fmac,usid,ftype,fname,fregstate,flocation,fregtime          
	 * */
	public EntityMessage remote_AddKTReg(EntityKTInfo info) throws NameNotFoundException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fmac", info.getfMac());
		hashMap.put("usid", info.getUsid());
		hashMap.put("ftype", info.getFtype());
		hashMap.put("fname", info.getFname());
		hashMap.put("fregstate", info.getFregstate());
		hashMap.put("flocation", info.getFlocation());
		hashMap.put("fregtime", info.getFregtime());
		EntityMessage message = null;
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_addEqRegInfo, hashMap);
			if (responseEntity != null) {
				JSONObject jsonObject = new JSONObject(
						responseEntity.getContentAsString());
				message = new EntityMessage();
				message.setMsg(jsonObject.getString(ConstantHelper.MSG));
				message.setMsgType(jsonObject.getString(ConstantHelper.MSGTYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 修改空调注册
	 * 
	 * @param info
	 *            空调注册信息
	 * 提交参数：fmac,usid,ftype,fname,fregstate,flocation,fregtime
	 * */
	public EntityMessage remote_UpdateKTReg(String fmac,String ftype,String fname) throws NameNotFoundException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fmac", fmac);
		hashMap.put("ftype",ftype);
		hashMap.put("fname",fname);				
		EntityMessage message = null;
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_updateEqRegInfo, hashMap);
			if (responseEntity != null) {
				/*Log.i(tag, responseEntity.toString());*/
			JSONObject jsonObject = new JSONObject(
					responseEntity.getContentAsString());
			message = new EntityMessage();
			message.setMsg(jsonObject.getString(ConstantHelper.MSG));
			message.setMsgType(jsonObject.getString(ConstantHelper.MSGTYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 删除空调注册
	 * 
	 * @param fmac
	 *            mac地址
	 * @param usid
	 *            用户编号
	 * */
	public EntityMessage remote_DeleteKTReg(String fmac, String usid) throws NameNotFoundException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("fmac", fmac);
		hashMap.put("usid", usid);

		EntityMessage message = null;
		try {
			ResponseEntity responseEntity = FastHttp.post(
					ConstantHelper.HostNameSpace
							+ ConstantHelper.URI_deleteEqRegInfo, hashMap);			
			if (responseEntity != null) {	
			JSONObject jsonObject = new JSONObject(
					responseEntity.getContentAsString());
			message = new EntityMessage();
			message.setMsg(jsonObject.getString(ConstantHelper.MSG));
			message.setMsgType(jsonObject.getString(ConstantHelper.MSGTYPE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	// 本地数据库操作
	/**
	 * 空调操作界面详细通讯控制信息更新
	 * */
	public long local_saveKTDetailInfo(Context context, EntityKTInfo info) {
		return ktDao.saveKTDetailInfo(context, info);
	}

	/**
	 * 批量添加空调通讯信息
	 * */
	public long local_saveKTList(Context context, List<EntityKTInfo> ktList) {
		return ktDao.saveKTList(context, ktList);
	}

	/**
	 * 添加产品注册信息
	 * */
	public long local_addKTRegister(Context context, EntityKTInfo ktinfo) {
		return ktDao.addKTRegister(context, ktinfo);
	}
	
	/**
	 * @ 注销方法
	 * 修改产品注册信息
	 * */
	@Deprecated
	public long local_updateKTRegister(Context context,EntityKTInfo ktinfo){
		return ktDao.updateKTRegister(context, ktinfo);
	}
	
	/**
	 * 删除产品 
	 * */
	
	public long local_deleteKTRegister(Context context,String fmac){
		return ktDao.deleteKTRegister(context, fmac);
	}

	/**
	 * 获取空调通讯列表，简要
	 * */
	public List<EntityKTInfo> local_getKTInfoList(Context context, String usid) {
		return ktDao.getKTInfos(context, usid);
	}
	
	/**
	 * 获取本地空调注册缓存信息
	 * @param fmac mac地址
	 * */
	public EntityKTInfo local_getKTInfo(Context context,String fmac){
		return ktDao.getKTInfo(context, fmac);
	}
}
