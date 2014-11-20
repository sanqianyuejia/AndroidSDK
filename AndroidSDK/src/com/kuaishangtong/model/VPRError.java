package com.kuaishangtong.model;

public class VPRError {
	private int errorCode;
	private String errorStr;
	
	public VPRError(){
		errorCode=-1;
		errorStr="";
	}
	
	public VPRError(int errorCode,String errorStr){
		this.errorCode=errorCode;
		this.errorStr=errorStr;
	}
	
	public void setErrorParam(int errorCode,String errorStr){
		this.errorCode=errorCode;
		this.errorStr=errorStr;
	}
	
	public void setErrorCode(int errorCode){
		this.errorCode=errorCode;
	}
	
	public void setErrorStr(String errorStr){
		this.errorStr=errorStr;
	}
	
	public int getErrorCode(){
		return this.errorCode;
	}
	
	public String getErrorStr(){
		return this.errorStr;
	}
}
