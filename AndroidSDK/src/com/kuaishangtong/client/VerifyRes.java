package com.kuaishangtong.client;

public class VerifyRes {
	private boolean result;
	private double similarity;
	
	public VerifyRes() {
		this.result = false;
		this.similarity = 0.0;
	}
	
	public boolean getResult() {
		return this.result;
	}
	public double getSimilarity() {
		return this.similarity;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
}
