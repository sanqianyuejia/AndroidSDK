package com.kuaishangtong.Object;

public class Object {
	private String err;
	private int errcode;
	
	public String getLastErr() {
		return err;
	}
	public int getErrCode() {
		return errcode;
	}
	public void setLastErr(String err) {
		this.err = err;
	}
	public void setErrCode(int errcode) {
		this.errcode = errcode;
	}
}
