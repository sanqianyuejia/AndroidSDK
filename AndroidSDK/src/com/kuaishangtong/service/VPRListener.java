package com.kuaishangtong.service;

import com.kuaishangtong.model.Person;
import com.kuaishangtong.model.VPRError;

public interface VPRListener {
	// 初始化服务
	public void onServiceInit(boolean flag,int stepNum,int statusNum,String keyString);
	
	//语音上传结果
	public void onSpeechResult(boolean flag);
	
	//服务结束
	public void onServiceEnd(boolean flag,Person person,double similarity);
	
	//服务所需步骤
	public void onFlowStepChanged(int stepNum,int statusNum,String keyString);
	
	//服务错误
	public void onServiceError(VPRError error);
}
