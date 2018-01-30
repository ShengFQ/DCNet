package com.bandary.dcnet.entity;

/**
 * 按照服务器跟apk客户端的协议约定，
 * 发送给服务器的一段控制命令对象
 * 控制命令的內容对象
 * */
public class EntityCtrlOrder {
	private String socketNo;//服务器端设备链接的socket 号码
	private String mac;//设备的mac地址
	private String code;//设备的控制代码，只能是一个设备的控制代码，多个设备的控制代码我采用的是list<EntityCtrlCodeItem>的方式
	
	
	
	private static EntityCtrlOrder instance;
	private EntityCtrlOrder(){
		
	}
	/**
	 * 单例
	 * */
	public static EntityCtrlOrder getInstance(){
		if(instance==null){
			instance=new EntityCtrlOrder();
		}
		return instance;
	}
	
	public EntityCtrlOrder(String socketno,String mac,String code){
		this.socketNo=socketno;
		this.mac=mac;
		this.code=code;
	}
	public String getSocketNo() {
		return socketNo;
	}
	public void setSocketNo(String socketNo) {
		this.socketNo = socketNo;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	@Override
	public String toString() {
		return "EntityCtrlCodeItem [socketNo=" + socketNo + ", mac=" + mac
				+ ", code=" + code + "]";
	}
	
	/**
	 * 获取1个设备的一个控制点位的控制对象
	 * @param socket 设备的通讯socket号码
	 * @param mac 设备的MAC地址
	 * @param ctrl 抽象控制类对象
	 * */
	public EntityCtrlOrder getCtrlPostParam(String socket, String mac,
			EntityCtrl ctrl) {
		String ctrlcode =ctrl.getHexCtrlForSingle();
		this.socketNo=socket;
		this.mac=mac;
		this.code=ctrlcode;
		return this;
	}
	
	
}
