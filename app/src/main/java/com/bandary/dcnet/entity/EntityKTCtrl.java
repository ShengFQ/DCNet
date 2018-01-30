package com.bandary.dcnet.entity;

import java.util.ArrayList;
import java.util.List;

import com.bandary.dcnet.utils.CRC16MHelper;
/**
 * 空调控制是按照协议对属性进行设置
 * 控制空调的参数对象，封装控制指令属性
 * 最终要封装成一个字符串的控制代码
 * */
public class EntityKTCtrl extends EntityCtrl{
	private String wstate;
	private String wmodel;
	private String wsettemp;
	private String wsetspeed;
	private String wsleep;
	private String wrestart;
	private String wlock;
	private String wsettempmin;
	private String wsettempmax;
	private String wspecialopt;
	public String getWstate() {
		return wstate;
	}
	public void setWstate(String wstate) {
		this.wstate = wstate;
	}
	public String getWmodel() {
		return wmodel;
	}
	public void setWmodel(String wmodel) {
		this.wmodel = wmodel;
	}
	public String getWsettemp() {
		return wsettemp;
	}
	public void setWsettemp(String wsettemp) {
		this.wsettemp = wsettemp;
	}
	public String getWsetspeed() {
		return wsetspeed;
	}
	public void setWsetspeed(String wsetspeed) {
		this.wsetspeed = wsetspeed;
	}
	public String getWsleep() {
		return wsleep;
	}
	public void setWsleep(String wsleep) {
		this.wsleep = wsleep;
	}
	public String getWrestart() {
		return wrestart;
	}
	public void setWrestart(String wrestart) {
		this.wrestart = wrestart;
	}
	public String getWlock() {
		return wlock;
	}
	public void setWlock(String wlock) {
		this.wlock = wlock;
	}
	public String getWsettempmin() {
		return wsettempmin;
	}
	public void setWsettempmin(String wsettempmin) {
		this.wsettempmin = wsettempmin;
	}
	public String getWsettempmax() {
		return wsettempmax;
	}
	public void setWsettempmax(String wsettempmax) {
		this.wsettempmax = wsettempmax;
	}
	public String getWspecialopt() {
		return wspecialopt;
	}

	public void setWspecialopt(String wspecialopt) {
		this.wspecialopt = wspecialopt;
	}
	public EntityKTCtrl(){
		
	}
	public EntityKTCtrl(String wstate, String wmodel, String wsettemp,
			String wsetspeed, String wsleep, String wrestart, String wlock,
			String wsettempmin, String wsettempmax, String wspecialopt) {
		super();
		this.wstate = wstate;
		this.wmodel = wmodel;
		this.wsettemp = wsettemp;
		this.wsetspeed = wsetspeed;
		this.wsleep = wsleep;
		this.wrestart = wrestart;
		this.wlock = wlock;
		this.wsettempmin = wsettempmin;
		this.wsettempmax = wsettempmax;
		this.wspecialopt = wspecialopt;
	}
	
	
	/**
	 * 已经测试通过
	 * 单变量控制下发命令组装
	 * 一个控制变量占用4位，不足则补0
	 * @param ctrl 里面存储的都是双字节的4位16进制数 只能携带一个控制属性值
	 * @return 单变量控制hex字符串
	 * */
	@Override
	public String getHexCtrlForSingle(){
		String bytes="";
		String codes="";
		setType("01");
		setReadWrite("06");		
		String[] btArray=new String[0];
		if(this.getWstate()!=null){
			setBeginAddress("0001");
			btArray=new String[]{this.getWstate()};
		}
		if(this.getWmodel()!=null){
			setBeginAddress("0002");
			btArray=new String[]{this.getWmodel()};
		}
		if(this.getWsettemp()!=null){
			setBeginAddress("0003");
			btArray=new String[]{this.getWsettemp()};
		}
		if(this.getWsetspeed()!=null){
			setBeginAddress("0004");
			btArray=new String[]{this.getWsetspeed()};
		}
		if(this.getWsleep()!=null){
			setBeginAddress("0005");
			btArray=new String[]{this.getWsleep()};
		}
		if(this.getWrestart()!=null){
			setBeginAddress("0006");
			btArray=new String[]{this.getWrestart()};
		}
		if(this.getWlock()!=null){
			setBeginAddress("0007");
			btArray=new String[]{this.getWlock()};
		}
		if(this.getWsettempmin()!=null){
			setBeginAddress("0008");
			btArray=new String[]{this.getWsettempmin()};
		}
		if(this.getWsettempmax()!=null){
			setBeginAddress("0009");
			btArray=new String[]{this.getWsettempmax()};
		}
		if(this.getWspecialopt()!=null){
			setBeginAddress("000A");
			btArray=new String[]{this.getWspecialopt()};
		}		
		//setByteCount(HexHelper.addZero(HexHelper.decimal2Hex((btArray.length)*2), 4));
		setByteCount("0002");
		for (int i = 0; i < btArray.length; i++) {
			codes+=btArray[i];
		}
		setBytesArray(codes);
		bytes=getType()+getReadWrite()+getBeginAddress()+getByteCount()+getBytesArray();		
		byte[] sbuf=CRC16MHelper.getSendBuf(bytes);
		bytes=CRC16MHelper.getBufHexStr(sbuf);
		setCtrlcode(bytes);
		return bytes;
	}	
	
	/**
	 * 未经测试,只针对一个mac地址
	 * 对于多变量的控制命令，我的处理方法是从第一个起始位置开始，固定发送20字节，没有写明控制属性的默认为FFFF
	 * @param ctrl 携带有多个控制属性值的控制对象
	 * @return complete hex code for KT
	 * */
	@Override
	public String getHexCtrlForMultiple(){
		String bytes="";
		String codes="";
		setType("01");
		setReadWrite("10");		
		List<String> list=new ArrayList<String>(10);
		for (int i = 0; i < list.size(); i++) {
			list.set(i, "FFFF");
		}
		if(this.getWstate()!=null){
			setBeginAddress("0001");
			list.set(0, this.getWstate());
		}
		if(this.getWmodel()!=null){
			//setBeginAddress("0002");
			list.set(1, this.getWmodel());
		}
		if(this.getWsettemp()!=null){
			list.set(2, this.getWsettemp());
		}
		if(this.getWsetspeed()!=null){
			list.set(3, this.getWsetspeed());
		}
		if(this.getWsleep()!=null){
			list.set(4, this.getWsleep());
		}
		if(this.getWrestart()!=null){
			list.set(5, this.getWrestart());
		}
		if(this.getWlock()!=null){
			list.set(6, this.getWlock());
		}
		if(this.getWsettempmin()!=null){
			list.set(7, this.getWsettempmin());
		}
		if(this.getWsettempmax()!=null){
			list.set(8, this.getWsettempmax());
		}
		if(this.getWspecialopt()!=null){
			list.set(9, this.getWspecialopt());
		}		
		//setByteCount(HexConvertHelper.addZero(HexConvertHelper.decimal2Hex((list.size())*2), 4));
		setByteCount("0014");
		for (int i = 0; i < list.size(); i++) {
			codes+=list.get(i);
		}
		setBytesArray(codes);
		bytes=getType()+getReadWrite()+getBeginAddress()+getByteCount()+getBytesArray();		
		byte[] sbuf=CRC16MHelper.getSendBuf(bytes);
		bytes=CRC16MHelper.getBufHexStr(sbuf);
		setCtrlcode(bytes);
		return bytes;
	}
}
