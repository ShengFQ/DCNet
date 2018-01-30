package com.bandary.dcnet.entity;

/**
 * 空调实体类 继承抽象设备类
 * **/
public class EntityKTInfo extends EntityAbsDevice{
	
	private String fCommstate;//通讯状态
	//private String fRegState;//请求注册状态
	private String fRegNo;	//注册码	
	private String fState;//运行状态
	private String fModel;//运行模式
	private String froomtemp;//室温
	private String fspeed;//风速
	private String fsettemp;//设置温度
	private String flock;//锁定按键
	
	private String fsettempmin;//设置温度下限
	private String fsettempmax;//设置温度上限
	
	//add in 2014-03-27
	private String ftrouble;
	
	//add in 2014-4-06
	private String fsetspeed;
	
	public EntityKTInfo(){
		super();
	}
	
	
	public EntityKTInfo(String fCommstate, String fRegNo
			,String fState, String fModel,
			String froomtemp, String fspeed, String fsettemp, String flock,
			String fsettempmin, String fsettempmax, String ftrouble,
			String fsetspeed) {
		super();
		this.fCommstate = fCommstate;
		this.fRegNo = fRegNo;
		this.fState = fState;
		this.fModel = fModel;
		this.froomtemp = froomtemp;
		this.fspeed = fspeed;
		this.fsettemp = fsettemp;
		this.flock = flock;
		this.fsettempmin = fsettempmin;
		this.fsettempmax = fsettempmax;
		this.ftrouble = ftrouble;
		this.fsetspeed = fsetspeed;
	}
	public String getfCommstate() {
		return fCommstate;
	}
	public void setfCommstate(String fCommstate) {
		this.fCommstate = fCommstate;
	}	
	public String getfRegNo() {
		return fRegNo;
	}
	public void setfRegNo(String fRegNo) {
		this.fRegNo = fRegNo;
	}
	
	public String getfState() {
		return fState;
	}
	public void setfState(String fState) {
		this.fState = fState;
	}
	public String getfModel() {
		return fModel;
	}
	public void setfModel(String fModel) {
		this.fModel = fModel;
	}

	public String getFroomtemp() {
		return froomtemp;
	}
	public void setFroomtemp(String froomtemp) {
		this.froomtemp = froomtemp;
	}
	public String getFspeed() {
		return fspeed;
	}
	public void setFspeed(String fspeed) {
		this.fspeed = fspeed;
	}
	public String getFsettemp() {
		return fsettemp;
	}
	public void setFsettemp(String fsettemp) {
		this.fsettemp = fsettemp;
	}
	public String getFlock() {
		return flock;
	}
	public void setFlock(String flock) {
		this.flock = flock;
	}
	public String getFtrouble() {
		return ftrouble;
	}
	public void setFtrouble(String ftrouble) {
		this.ftrouble = ftrouble;
	}
	
	public String getFsettempmin() {
		return fsettempmin;
	}
	public void setFsettempmin(String fsettempmin) {
		this.fsettempmin = fsettempmin;
	}
	public String getFsettempmax() {
		return fsettempmax;
	}
	public void setFsettempmax(String fsettempmax) {
		this.fsettempmax = fsettempmax;
	}
	public String getFsetspeed() {
		return fsetspeed;
	}
	public void setFsetspeed(String fsetspeed) {
		this.fsetspeed = fsetspeed;
	}
	@Override
	public String toString() {
		return "EntityKTInfo [" + ", fCommstate=" + fCommstate
				+ ", fRegNo=" + fRegNo + ","
				+ ", fState=" + fState + ", fModel=" + fModel + ", froomtemp="
				+ froomtemp + ", fspeed=" + fspeed + ", fsettemp=" + fsettemp
				+ ", flock=" + flock + ", fsettempmin=" + fsettempmin
				+ ", fsettempmax=" + fsettempmax + ", ftrouble=" + ftrouble
				+ ", fsetspeed=" + fsetspeed + "]";
	}
}
