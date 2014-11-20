package com.kuaishangtong.service;

import com.kuaishangtong.model.Person;
import com.kuaishangtong.model.VPRError;

public interface VPRListener {
	// ��ʼ������
	public void onServiceInit(boolean flag,int stepNum,int statusNum,String keyString);
	
	//�����ϴ����
	public void onSpeechResult(boolean flag);
	
	//�������
	public void onServiceEnd(boolean flag,Person person,double similarity);
	
	//�������貽��
	public void onFlowStepChanged(int stepNum,int statusNum,String keyString);
	
	//�������
	public void onServiceError(VPRError error);
}
