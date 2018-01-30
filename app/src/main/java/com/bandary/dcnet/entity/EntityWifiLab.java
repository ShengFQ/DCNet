package com.bandary.dcnet.entity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * WIFI温控器 初始化ap模式后通讯连接，负责连接AP，切换AP
 * 负责wifi连接信息的组装工厂
 * */
public class EntityWifiLab {
	private static final String TAG = "EntityWifiLab";
	private static EntityWifiLab instance;
	private WifiManager wifiManager;
	private WifiInfo current_wifiInfo;// 当前连接的wifi
	public static EntityWifiLab getInstance(Context context){
		if(instance==null){
			instance=new EntityWifiLab(context);
		}
		return instance;
	}
	private Context mContext;
	
	private EntityWifiLab(){
		
	}
	private  EntityWifiLab(Context context){
		this.mContext=context;
	}
	//1.读取当前连接的wifi节点
	//2.如果扫描到WFJJ，切换到该AP的连接
	//3.通过http上传参数
	
	//服务管理，如果wifi服务关闭则打开。
	
	//获取当前已经连接的wifi连接点信息
	
	//扫描wifi列表保存到内存
	
	//将内存的wifi列表序列化到文件
	
	//
	
	
	
}
