package com.bandary.dcnet.entity;

public class EntityMessage {
	public EntityMessage(){
		
	}
	public EntityMessage(String msg,String msgType){
		this.msg=msg;
		this.msgType=msgType;
	}
	
	private String msg;
	private String msgType;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Override
	public String toString(){
		return "JSONMessage -msg:"+this.msg+" msgType:"+this.msgType;
	}
}
