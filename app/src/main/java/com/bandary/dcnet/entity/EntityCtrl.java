package com.bandary.dcnet.entity;

public abstract class EntityCtrl {
	private String type;// 协议:产品类型
	private String readWrite;// 协议：读写功能码
	private String beginAddress;// 协议：起始地址
	private String byteCount;// 协议：控制字节长度
	private String bytesArray;// 协议：控制字节内容
	private String crc;// 协议：cRC校验
	private String ctrlcode;// 控制命令


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReadWrite() {
		return readWrite;
	}

	public void setReadWrite(String readWrite) {
		this.readWrite = readWrite;
	}

	public String getBeginAddress() {
		return beginAddress;
	}

	public void setBeginAddress(String beginAddress) {
		this.beginAddress = beginAddress;
	}

	public String getByteCount() {
		return byteCount;
	}

	public void setByteCount(String byteCount) {
		this.byteCount = byteCount;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

	public String getBytesArray() {
		return bytesArray;
	}

	public void setBytesArray(String bytesArray) {
		this.bytesArray = bytesArray;
	}

	public String getCtrlcode() {
		return ctrlcode;
	}

	public void setCtrlcode(String ctrlcode) {
		this.ctrlcode = ctrlcode;
	}	

	public abstract String getHexCtrlForSingle();

	public abstract String getHexCtrlForMultiple();
}
