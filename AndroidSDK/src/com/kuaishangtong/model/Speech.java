package com.kuaishangtong.model;

public class Speech {
	private String id = "";	// person id
	
	private byte[] data = null;
	private String md5 = "";
	private String asr = "";
	private String codec = "";
	private int sr = 0;
	private int size = 0;
	private boolean bverify = false;
	private String rule = "";
	
	public Speech() {
		
	}
	
	public Speech(String codec, int sr, boolean bverify) {
		this.codec = codec;
		this.sr = sr;
		this.bverify = bverify;
	}
	
	
	public void setId(String id) {
		this.id = id;
	}
	public void setSampleRate(int sr) {
		this.sr = sr;
	}	
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public void setVerify(boolean bverify) {
		this.bverify = bverify;
	}
	public void setMD5(String md5) {
		this.md5 = md5;
	}
	
	public String getId() {
		return this.id;
	}
	public int getSampleRate() {
		return this.sr;
	}
	public String getCodec() {
		return this.codec;
	}
	public String getMD5() {
		return this.md5;
	}
	public String getASR() {
		return this.asr;
	}
	public int getSize() {
		return this.size;
	}
	public boolean getVerify() {
		return this.bverify;
	}
	
	public String getRule() {
		return this.rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	
	
	public void setData(byte[] data) {
		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
	}
	public byte[] getData() {
		return this.data;
	}
}
