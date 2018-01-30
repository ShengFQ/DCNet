/*
 * WifiConfHelper
 * @author shengfq
 * @since 2015-01-30
 * @version 2.0 
 * 
 * */
package com.bandary.dcnet.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.util.Log;

import com.bandary.dcnet.R;

/**
 * wifi连接点操作类 应用在快连技术 类似于CrimeLab.java
 * 就是能将wifi的所有相关操作完成，并且还能将想要的数据对象获取和读写。
 * */
public class WifiConfHelper {

	// 日志tag
	private final String TAG = "WifiConfHelper";
	// wifi连接管理器
	// private WifiManager wifiManager;
	// 当前连接的wifi
	// private WifiInfo current_wifiInfo;
	// 保存wifi端点显示列表
	// private ArrayList<HashMap<String, Object>> hashmap_wifi_items = new
	// ArrayList<HashMap<String, Object>>();
	// appContext
	private Context mContext;
	// 扫描结果列表，每一个ScanResult都是一个wifi项
	private List<ScanResult> mResults;

	/**
	 * wifi配置类，需要Activity的实例才能使用它的服务，则通过构造函数依赖注入
	 * */
	private WifiConfHelper(Context context) {
		this.mContext = context;
		this.mResults=loadScanResult();
	}

	// 单例模式 --2015-02-06开始考虑使用单例模式
	// 后来考虑到context问题为不同的activity的时候，如果使用单例模式获取的本对象的实例会不会不会改变
	private static WifiConfHelper instance;
	/**
	 * 创建唯一的WifiConfHelper对象
	 * */
	public static WifiConfHelper getInstance(Context context) {
		if (instance == null) {
			instance = new WifiConfHelper(context);
		}
		
		return instance;
	}

	/**
	 * 获取服务对象
	 * */ 
	private WifiManager getWifiManagerService() {
		return (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 当前连接的wifi节点，如果当前没连接呢
	 * */ 
	private WifiInfo getWifiInfo() {
		WifiManager wifiManager = getWifiManagerService();
		setWifiEnabled();
		return wifiManager.getConnectionInfo();
	}

	/**
	 * 获取扫描的wifi结果列表，每一个wifi信号都是一个ScanResult对象
	 * */ 
	private List<ScanResult> loadScanResult() {
		WifiManager wifiManager = getWifiManagerService();		
		setWifiEnabled();
		wifiManager.startScan();
		return wifiManager.getScanResults();
	}
	/**
	 * 设置wifi开关为打开
	 * */ 
	private boolean setWifiEnabled() {
		WifiManager wifiManager = getWifiManagerService();
		if (!wifiManager.isWifiEnabled()) {
			Log.i(TAG, "wifi service is staring");
			wifiManager.setWifiEnabled(true);
		}
		Log.i(TAG, "wifi service was stared");
		return wifiManager.isWifiEnabled();
	}
	/**
	 * 刷新扫描结果
	 * */
	public void reFreshScanResult(){
		this.mResults=loadScanResult();
	}
	
	/**
	 * 返回扫描结果集合
	 * */
	public List<ScanResult> getScanResults(){
		return mResults;
	}
	
	/**
	 * 返回特定SSID的扫描项
	 * @param ssid ssid号
	 * @return 包含完整ssid的项
	 * */
	public ScanResult getScanResult(String ssid){
		for (ScanResult s : mResults) {
			if(s.SSID.equals(ssid)){
				return s;
			}
		}
		return null;
	}

	/**
	 * 通过扫描的结果判断加密名称
	 * 
	 * @param result
	 *            扫描结果对象
	 * @return {@link WIFI_CAPABILITIES}加密方式枚举对象
	 * */
	public WIFI_CAPABILITIES getSecurity(ScanResult result) {
		WIFI_CAPABILITIES instance;
		if (result.capabilities.contains(WIFI_CAPABILITIES.WPA_PSK.getValue())) {
			instance = WIFI_CAPABILITIES.WPA_PSK;
		} else if (result.capabilities.contains(WIFI_CAPABILITIES.WPA2_PSK
				.getValue())) {
			instance = WIFI_CAPABILITIES.WPA2_PSK;
		} else if (result.capabilities.contains(WIFI_CAPABILITIES.EAP
				.getValue())) {
			instance = WIFI_CAPABILITIES.EAP;
		} else if (result.capabilities.contains(WIFI_CAPABILITIES.WPA_PSK
				.getValue())
				&& result.capabilities.contains(WIFI_CAPABILITIES.WPA2_PSK
						.getValue())) {
			instance = WIFI_CAPABILITIES.WPA_WPA2;
		} else {
			instance = WIFI_CAPABILITIES.NONE_PASSWD;
		}
		return instance;
	}

	/**
	 * 通过扫描的结果判断信号等级
	 * 
	 * @param result
	 *            扫描结果对象
	 * @return {@link WifiSignalLevel}信号等级枚举对象
	 * */
	public WifiSignalLevel getSignalLevel(ScanResult result) {
		WifiSignalLevel instance;
		int level = Math.abs(result.level);
		if (level > 100) {
			instance = WifiSignalLevel.Level_four;
		} else if (level > 70) {
			instance = WifiSignalLevel.Level_three;
		} else if (level > 50) {
			instance = WifiSignalLevel.Level_two;
		} else {
			instance = WifiSignalLevel.Level_one;
		}
		return instance;
	}

	/**
	 * 将扫描的结果项封装到ArrayList，将要填充到ListView中
	 * @param list_scan 已经扫描到的结果
	 * @return ArrayList 的每一项都是一个HashMap对象
	 * */
	public ArrayList<HashMap<String, Object>> getAllNetWorkList() {
		String vStr = WIFI_CAPABILITIES.NONE_PASSWD.getValue();
		int imgID = WifiSignalLevel.Level_one.getValue();
		ArrayList<HashMap<String, Object>> hashmap_wifi_items = new ArrayList<HashMap<String, Object>>();
		//WifiManager wifiManager = getWifiManagerService();
		if (setWifiEnabled()) {
			//wifiManager.startScan();
			// 开始扫描网络
			// List<ScanResult> list_scan = getScanResult();
			if (mResults != null) {
				for (int i = 0; i < mResults.size(); i++) {
					// 得到扫描结果
					HashMap<String, Object> vMap = new HashMap<String, Object>();
					ScanResult scanResult = mResults.get(i);
					WIFI_CAPABILITIES capabilities = getSecurity(scanResult);
					vStr = capabilities.getValue();
					WifiSignalLevel signalLevel = getSignalLevel(scanResult);
					imgID = signalLevel.getValue();
					vMap.put("ItemTitle", scanResult.SSID + "(" + vStr + ")");
					vMap.put("ItemImage", imgID);
					hashmap_wifi_items.add(vMap);
				}
			}
		}

		return hashmap_wifi_items;
	}

	
}

/**
 * wifi加密名称枚举类
 * */
enum WIFI_CAPABILITIES {
	/**
	 * 5种加密名称代表wifi连接点可能的加密方式
	 * */
	WPA_PSK("WPA-PSK"), WPA2_PSK("WPA2-PSK"), EAP("EAP"), WPA_WPA2("WPA_WPA2"), NONE_PASSWD(
			"无密码");
	/**
	 * 加密方式的名称
	 * */
	private final String key;

	/**
	 * 构造方法
	 * */
	WIFI_CAPABILITIES(String key) {
		this.key = key;
	}

	/**
	 * 返回枚举对象固定的字符串常量
	 * */
	public String getValue() {
		return this.key;
	}
}

/**
 * wifi信号等级枚举类
 * */
enum WifiSignalLevel {
	/**
	 * 四个枚举对象，代表4个wifi信号强度等级
	 * */
	Level_one(R.drawable.device_list_wifi_1), Level_two(
			R.drawable.device_list_wifi_2), Level_three(
			R.drawable.device_list_wifi_3), Level_four(
			R.drawable.device_list_wifi_4);
	/**
	 * 信号等级图片的资源ID
	 * */
	private final int value;

	/**
	 * 返回枚举对象固定的字符串常量
	 * */
	public int getValue() {
		return value;
	}

	/**
	 * 构造方法
	 * */
	WifiSignalLevel(int v) {
		this.value = v;
	}
}
