package com.bandary.dcnet.entity;

/**
 * 暂时不用
 * 设计组别
 * 所有设备均存放在组别里面，便于用户操作
 * 
 * 思来想去还是觉得组别只需要在界面上显示即可，后台数据组织不需要组别
 * */
public class EntityGroup {
	private String gId;//组ID
	private String gName;//组名称
	private String gMark;//组说明
	private String usId;//用户编号
	public String getgId() {
		return gId;
	}
	public void setgId(String gId) {
		this.gId = gId;
	}
	public String getgName() {
		return gName;
	}
	public void setgName(String gName) {
		this.gName = gName;
	}
	public String getgMark() {
		return gMark;
	}
	public void setgMark(String gMark) {
		this.gMark = gMark;
	}
	public String getUsId() {
		return usId;
	}
	public void setUsId(String usId) {
		this.usId = usId;
	}
}
