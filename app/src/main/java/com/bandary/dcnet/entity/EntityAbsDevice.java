package com.bandary.dcnet.entity;

import java.io.Serializable;

/**
 * 
 * 设备抽象类，定义抽象的设备,具有一般性设备的共同属性和抽象方法
 * 
 * */
public class EntityAbsDevice implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fMac;//mac地址
	private String usid;//用户编号
	private String ftype;//类别号
	private String fname;//设备名
	private String fregstate;//注册状态
	private String flocation;//安装位置
	private String fregtime;//注册时间
	private String fNetSocketNo;//网络socket号
	//private String fregNo;
	//列表显示设备的简单信息需要此类支持
	//定义 位置 在线状态 开关机状态等设备的公共信息实时显示
	
	
	
	public String getUsid() {
		return usid;
	}
	
	public EntityAbsDevice(){
		super();
	}
	public EntityAbsDevice(String usid, String ftype, String fname,
			String fregstate, String flocation, String fregtime) {
		super();
		this.usid = usid;
		this.ftype = ftype;
		this.fname = fname;
		this.fregstate = fregstate;
		this.flocation = flocation;
		this.fregtime = fregtime;
	}
	public void setUsid(String usid) {
		this.usid = usid;
	}

	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFregstate() {
		return fregstate;
	}
	public void setFregstate(String fregstate) {
		this.fregstate = fregstate;
	}
	public String getFlocation() {
		return flocation;
	}
	public void setFlocation(String flocation) {
		this.flocation = flocation;
	}
	public String getFregtime() {
		return fregtime;
	}
	public void setFregtime(String fregtime) {
		this.fregtime = fregtime;
	}
	public String getFtype() {
		return ftype;
	}
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	

	public String getfMac() {
		return fMac;
	}

	public void setfMac(String fMac) {
		this.fMac = fMac;
	}

	public String getfNetSocketNo() {
		return fNetSocketNo;
	}

	public void setfNetSocketNo(String fNetSocketNo) {
		this.fNetSocketNo = fNetSocketNo;
	}

	@Override
	public String toString() {
		return "EntityAbsDevice [fMac=" + fMac + ", usid=" + usid + ", ftype="
				+ ftype + ", fname=" + fname + ", fregstate=" + fregstate
				+ ", flocation=" + flocation + ", fregtime=" + fregtime
				+ ", fNetSocketNo=" + fNetSocketNo + "]";
	}	
	
}
