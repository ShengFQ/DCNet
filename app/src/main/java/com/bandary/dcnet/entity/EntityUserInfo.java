package com.bandary.dcnet.entity;

public class EntityUserInfo {
	private String usId;
	private String usName;
	private String usPwd;
	private String usMail;
	private String usEnable;
	private String usLogin;
	private String usRegtime;
	private String usServer;
	private String usPort;
	private String usVerify;
	private String sessionId;
	
	public EntityUserInfo(){}
	public EntityUserInfo(String usName,String usPwd){
		this.usName=usName;
		this.usPwd=usPwd;
	}
	
	public EntityUserInfo(String usid, String usName, String usPwd,String sessionId) {
		super();
		this.usId = usid;
		this.usName = usName;
		this.usPwd = usPwd;
		this.sessionId=sessionId;
	}
	
	
	public String getUsName() {
		return usName;
	}
	public void setUsName(String usName) {
		this.usName = usName;
	}
	public String getUsPwd() {
		return usPwd;
	}
	public void setUsPwd(String usPwd) {
		this.usPwd = usPwd;
	}
	public String getUsMail() {
		return usMail;
	}
	public void setUsMail(String usMail) {
		this.usMail = usMail;
	}
	public String getUsEnable() {
		return usEnable;
	}
	public void setUsEnable(String usEnable) {
		this.usEnable = usEnable;
	}
	public String getUsLogin() {
		return usLogin;
	}
	public void setUsLogin(String usLogin) {
		this.usLogin = usLogin;
	}
	public String getUsRegtime() {
		return usRegtime;
	}
	public void setUsRegtime(String usRegtime) {
		this.usRegtime = usRegtime;
	}
	public String getUsServer() {
		return usServer;
	}
	public void setUsServer(String usServer) {
		this.usServer = usServer;
	}
	public String getUsPort() {
		return usPort;
	}
	public void setUsPort(String usPort) {
		this.usPort = usPort;
	}
	public String getUsVerify() {
		return usVerify;
	}
	public void setUsVerify(String usVerify) {
		this.usVerify = usVerify;
	}	
	public EntityUserInfo(String usId, String usName, String usPwd,
			String usMail, String usEnable, String usLogin, String usRegtime,
			String usServer, String usPort, String usVerify, String sessionId) {
		super();
		this.usId = usId;
		this.usName = usName;
		this.usPwd = usPwd;
		this.usMail = usMail;
		this.usEnable = usEnable;
		this.usLogin = usLogin;
		this.usRegtime = usRegtime;
		this.usServer = usServer;
		this.usPort = usPort;
		this.usVerify = usVerify;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsId() {
		return usId;
	}

	public void setUsId(String usId) {
		this.usId = usId;
	}

	@Override
	public String toString() {
		return "EntityUserInfo [usId=" + usId + ", usName=" + usName
				+ ", usPwd=" + usPwd + ", usMail=" + usMail + ", usEnable="
				+ usEnable + ", usLogin=" + usLogin + ", usRegtime="
				+ usRegtime + ", usServer=" + usServer + ", usPort=" + usPort
				+ ", usVerify=" + usVerify + ", sessionId=" + sessionId + "]";
	}
}
