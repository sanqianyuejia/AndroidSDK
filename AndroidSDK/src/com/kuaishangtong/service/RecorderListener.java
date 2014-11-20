package com.kuaishangtong.service;

import com.kuaishangtong.model.VPRError;

public interface RecorderListener {
	// 录音开始时调用
	public void onRecordBegin();
	// 返回实时录音音量，sound为音量大小
	public void onSoundChanged(float sound);
	// 录音结束时调用
	public void onRecordEnd();
	// 录音异常时调用
	public void onRecordError(VPRError error);
	
}
