package com.kuaishangtong.service;

import com.kuaishangtong.model.VPRError;

public interface RecorderListener {
	// ¼����ʼʱ����
	public void onRecordBegin();
	// ����ʵʱ¼��������soundΪ������С
	public void onSoundChanged(float sound);
	// ¼������ʱ����
	public void onRecordEnd();
	// ¼���쳣ʱ����
	public void onRecordError(VPRError error);
	
}
