package com.bandary.dcnet.entity;

public class EntityPhoneUserInfo extends EntityUserInfo {

	private String usSecurityQue;
	private String usSecurityAns;
	private String usPhoneNum;
	public String getUsSecurityQue() {
		return usSecurityQue;
	}
	public void setUsSecurityQue(String usSecurityQue) {
		this.usSecurityQue = usSecurityQue;
	}
	public String getUsSecurityAns() {
		return usSecurityAns;
	}
	public void setUsSecurityAns(String usSecurityAns) {
		this.usSecurityAns = usSecurityAns;
	}
	public String getUsPhoneNum() {
		return usPhoneNum;
	}
	public void setUsPhoneNum(String usPhoneNum) {
		this.usPhoneNum = usPhoneNum;
	}
	public EntityPhoneUserInfo(){
		super();
	}
	public EntityPhoneUserInfo(String usName,String usPwd,String usSecurityQue, String usSecurityAns,
			String usPhoneNum) {
		super(usName,usPwd);
		this.usSecurityQue = usSecurityQue;
		this.usSecurityAns = usSecurityAns;
		this.usPhoneNum = usPhoneNum;
	}
}
