package com.kuaishangtong.model;

public class VerifyLog {
	private String identy;
	private String name;
	private float score;
	private boolean match;
	private String updatetime;
	
	public VerifyLog() {
		this.identy = "";
		this.name = "";
		this.score = 0.0f;
		this.match = false;
		this.updatetime = "";
	}
	
	public void setId(String id) { this.identy = id; }
	public void setName(String name) { this.name = name; }
	public void setScore(float score) { this.score = score; }
	public void setMatch(boolean match) { this.match = match; }
	public void setUpdatetime(String updatetime) { this.updatetime = updatetime; }
	
	public String getId() { return this.identy; }
	public String getName() { return this.name; }
	public float getScore() { return this.score; }
	public boolean getMatch() { return this.match; }
	public String getUpdatetime() { return this.updatetime; }

}
