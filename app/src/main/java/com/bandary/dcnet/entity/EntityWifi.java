package com.bandary.dcnet.entity;

import android.content.Context;
import android.net.wifi.ScanResult;

/**
 * 2014-12-29
 * 当前连接的wifi的连接点信息
 * */
public class EntityWifi {
	/**
	 *华菱-蔡工-提供的参考.txt 下面部分是我自己的想法，示例代码是他的，可惜没有了
	 * /
	/**
	 * http传递的参数：
	 * url:http://192.168.10.1/hedbasic.html
	 * method:get
	 * param:
	 * Mode=0[默认] 说明：0-Sta 1-Adhoc 2-AP
	 * ApEnable=0[默认]
	 * Ssid=xxx[用户手机传递]
	 * Encry=0[用户手机传递] 说明 ：0-disable 1-WEP64 2-WEB128 3-WPA-PSK(TKIP) 4-WPA-PSK(CCMP) 5-WPA2-PSK(TKIP) 6-WPA2_PSK(CCMP)
	 * KeyType=1[默认] 说明：0-HEX 1-ASCII
	 * Key=xxx[用户手机传递]
	 * Save=Save
	 * 
	 * Dhcp=1[默认]
	 * Save=Save
	 * 
	 * Auto=1[默认]
	 * AutoHiden=1[默认]
	 * Protocol=0[默认] 说明：0-TCP 1-UDP
	 * Cs=0[默认] 说明：0-CLIENT 1-SERVER
	 * Domain=bas.66ip.net[默认] 说明：服务器域名
	 * Port=8091[默认]
	 * Save=Save
	 * 
	 * url:http://192.168.10.1/hedfirmware.html
	 * method:get
	 * param：
	 * restart=1[默认]
	 * 
	 * */
	/*
	 1.设置连接路由：   "http://169.254.1.1/hedbasic.html?Mode=0&Ssid="+ssid+"&ApEnable=0&Key="+password+"&Save=Save";
     2.设置网关：       "http://169.254.1.1/hedbasic.html?Dhcp=1&Save=Save"
     3.设置C/S和端口：  "http://169.254.1.1/hedbasic.html?Auto=1&AutoHiden=0&Protocol=1&Cs=1&TCP_TimeOut=200&Port=2000&Save=Save";
     4.重启路由：       "http://169.254.1.1/hedfirmware.html?MAC+Address=cc-d2-9b-f3-0c-ba&Hardware+Version=1.00.00.0000&restart=1";
	 * */
	
	/*
	 * 
	 * */
	private static final String DEFAULT_FCU_SSID="WFJJ";
	private static final boolean DEFAULT_FCU_AUTO_IP_ENABLE=true;
	private static final String DEFAULT_FCU_PROTOCOL="TCP";
	private static final String DEFAULT_FCU_CS_MODE="client";
	private static final String DEFAULT_FCU_SERVER_ADDRESS="bas.66ip.net";
	private static final int DEFAULT_FCU_PORT_NUMBER=8091;
	private static final String DEFAULT_FCU_RESTART="restart";
	
	private String ssid;
	private String bssid;//
	private String mac;
	private String capabilities;
	private String level;
	private String frequency;
	private String ssidpwd;
	private ScanResult scanResult;//当前wifi扫描结果
	private Context context;
	public EntityWifi(Context context){
		this.context=context;
	}
	public EntityWifi(Context context,String ssid,String spwd){
		this.context=context;
		this.ssid=ssid;
		this.ssidpwd=spwd;
	}
	
	public String getSSID(){
		return this.ssid;
	}
	
	public String getSSIDPwd(){
		return this.ssidpwd;
	}
	public void setSSID(String ssid){
		this.ssid=ssid;
	}
	
	public void setSSIDPwd(String spwd){
		this.ssidpwd=spwd;
	}
	
}
