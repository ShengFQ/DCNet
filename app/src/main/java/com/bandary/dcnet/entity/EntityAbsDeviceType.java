package com.bandary.dcnet.entity;
/**
 * 设备类别抽象实体类，最原始的类别信息
 * */
public class EntityAbsDeviceType {
	private String typeName;//类别名称
	private String typeNumber; //类别编号
	private int resourceID;//图标
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public EntityAbsDeviceType(String typeName, String typeNumber,int resouceid) {
		super();
		this.typeName = typeName;
		this.typeNumber = typeNumber;
		this.resourceID=resouceid;
	}
	public String getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(String typeNumber) {
		this.typeNumber = typeNumber;
	}
	
	public int getResourceID() {
		return resourceID;
	}
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}
	@Override
	public String toString() {
		return "EntityAbsDeviceType [typeName=" + typeName + ", typeNumber="
				+ typeNumber + ", resourceID=" + resourceID + "]";
	}
	
}
