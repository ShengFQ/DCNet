package com.bandary.dcnet.entity;

/**
 * 
 * 用户订的设备类别，一个用户可以可以有多个GridView自由选择设备类别
 * 设备类别分别是1-7，分别代表不同种类的设备，用户添加到设备类别不保存到服务器，只有该类别下有设备时，设备类别才会进入服务器数据
 * @version 1.0
 * */
public class EntityOrderItem {
	private String tNumber;//类别项的代码 【主键】1-7
	private String usId;//类别项哪个用户
	private String tName;//类别项的名称
	private String tLabel;//类别项的标签	
	private String tImage;//类别项的图标
	private boolean isVisible;//类别项是否可见
	public String gettName() {
		return tName;
	}
	public void settName(String tName) {
		this.tName = tName;
	}
	public String gettLabel() {
		return tLabel;
	}
	public void settLabel(String tLabel) {
		this.tLabel = tLabel;
	}
	public String gettNumber() {
		return tNumber;
	}
	public void settNumber(String tNumber) {
		this.tNumber = tNumber;
	}
	public String gettImage() {
		return tImage;
	}
	public void settImage(String tImage) {
		this.tImage = tImage;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public EntityOrderItem(String tName, String tLabel, String tNumber,
			String tImage, boolean isVisible) {
		super();
		this.tName = tName;
		this.tLabel = tLabel;
		this.tNumber = tNumber;
		this.tImage = tImage;
		this.isVisible = isVisible;
	}
	@Override
	public String toString() {
		return "EntityDCTypeItem [tName=" + tName + ", tLabel=" + tLabel
				+ ", tNumber=" + tNumber + ", tImage=" + tImage
				+ ", isVisible=" + isVisible + "]";
	}
	public String getUsId() {
		return usId;
	}
	public void setUsId(String usId) {
		this.usId = usId;
	}
	
	
}
