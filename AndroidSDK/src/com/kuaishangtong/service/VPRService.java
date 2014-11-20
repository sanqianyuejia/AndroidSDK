package com.kuaishangtong.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kuaishangtong.client.Client;
import com.kuaishangtong.client.VerifyRes;
import com.kuaishangtong.model.Person;
import com.kuaishangtong.model.Speech;
import com.kuaishangtong.model.VPRError;
import com.kuaishangtong.utils.Constants;


public class VPRService {
	private Client client=null;
	private Person person=null;
	private Speech speech=null;
	
	private int ret = -1;
	private String keyString="";
	
	private int mode=0;
	private int stepNum=0;
	
	private int regNum=0;
	private int verNum=0;
	private int statusNum=0;
	
	private double similar;
	private byte[] recordbuffer=null;
	private boolean paused;
	private String dirname="Voices";
	
	private VPRError error=new VPRError();
	
	private AudioService aservice;
	private static VPRService mInstance=null;
	private RecorderListener recordListener;
	private VPRListener voiceListener;

	public static final int REGISTER=0;
	public static final int VERIFY=1;
	public static final int IDENTIFY=2;

	VPRService(){
		
	}
	
	//获取声纹服务实例
	public synchronized static VPRService getInstance()
    {
        if(mInstance == null) 
            mInstance = new VPRService(); 
        return mInstance; 
    }
	
	//设置声纹服务参数
	public void setServiceParam(Client client,Person person,Speech speech){
		this.client=client;
		this.person=person;
		this.speech=speech;
	}

	//设置SD卡中录音存放目录
	public void setVoiceDir(String dirname){
		this.dirname=dirname;
	}
	
	//设置录音服务
	public void setAudioSerivce(){
		aservice=AudioService.getInstance();
		aservice.setAudioPermission();
		aservice.setVoiceDir(dirname);
	}
	
	//获取当前口令
	public String getKeyString(){
		//IsRun=false;
		paused=true;
		return this.keyString;
	}

	//获取当前进度步数
	public int getStepNum(){
		return this.stepNum;
	}
	
	//设置person
	public void setPerson(Person person){
		this.person=person;
	}
	
	//设置声纹服务接口
	public void setVPRListener(VPRListener vprListener){
		this.voiceListener=vprListener;
	}
	
	//设置录音回调接口
	public void setRecorderListener(RecorderListener recorderListener){
		this.recordListener=recorderListener;
	}
	
	//获取实时音量线程
	private boolean IsRun=false;
	private class ValueThread implements Runnable{
		@Override
		public void run()
		{		
			error.setErrorCode(-1);
			while(IsRun){
				try {
					//Thread.sleep(10);
					recordListener.onSoundChanged(getRecordValue());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					error.setErrorParam(0, e.getMessage());
					//throw new RuntimeException(e.getMessage(),e);
				}
			}
		}
	}

	//开始录音服务
	public void startRecord(){
		paused=true;
		aservice.setVoiceFile(person.getName()+stepNum);
		try{
			this.recordListener.onRecordBegin();
			this.aservice.startRecordAndFile();
			Thread valueThread=null;
			valueThread=new Thread(new ValueThread());
			IsRun=true;
			valueThread.start();
		}catch(Exception e)
		{
			error.setErrorParam(0, e.getMessage());
			this.recordListener.onRecordError(error);
		}
	}
	
	//停止录音服务
	public void stopRecord(){
		try{
			IsRun=false;
			this.aservice.stopRecordAndFile();
			this.recordListener.onRecordEnd();
		}catch(Exception e)
		{
			error.setErrorParam(0, e.getMessage());
			this.recordListener.onRecordError(error);
		}
	}
	
	//获取音量
	public float getRecordValue(){
		return this.aservice.getAverageAbsValue();
	}
	
	//调用重置服务模式
	public void resetService(int serviceMode){
		this.mode=serviceMode;
		this.setAudioSerivce();
		
		Thread resetThread=null;
		resetThread=new Thread(new ResetThread());
		
		paused=false;
		resetThread.start();
		
	}
	
	//处理重置服务模式
	private Handler resetHandler=new Handler(){	
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				voiceListener.onServiceInit(true,stepNum, statusNum, getKeyString());
				break;
			case 0:
				voiceListener.onServiceInit(false,stepNum, statusNum, getKeyString());
				break;
			default:
				break;
			}
			if(error.getErrorCode()!=-1)
				voiceListener.onServiceError(error);
		}
	};
	
	//重置服务模式的线程
	private class ResetThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			error.setErrorCode(-1);
			if(!paused){
				Message msg =new Message();
				try{
					resetMode();
					
					msg.what=1;
					switch (mode){
						case REGISTER:
							initRegister();
							break;
						case VERIFY:
							initVerify();
							break;
						case IDENTIFY:
							initIdentify();
							break;
						default:
							break;
					}
				}catch(RuntimeException e){
					msg.what=0;
					error.setErrorParam(0, e.getMessage());
					//throw new RuntimeException(e.getMessage(), e);
				}
				resetHandler.sendMessage(msg);
			}
		}
	}
	
	//重置服务模式
	private void resetMode(){
		//Delete Person				
		if ((ret = person.delete()) != Constants.RETURN_SUCCESS) {	
			Log.d("Delete Person",person.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret, person.getLastErr());
		}else{
			// Create Person					
			if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {					
				if ( (ret = person.create()) != Constants.RETURN_SUCCESS) {					
					Log.d("Create Person",person.getLastErr()+":"+String.valueOf(ret));
					error.setErrorParam(ret, person.getLastErr());
				}
			}
		}
	}
	
	//调用初始化服务
	public void initService(int serviceMode){
		this.mode=serviceMode;
		this.setAudioSerivce();
		
		Thread initThread=null;
		initThread=new Thread(new InitThread());
		
		paused=false;
		initThread.start();

	}
	
	//处理服务初始化
	private Handler initHandler=new Handler(){	
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				voiceListener.onServiceInit(true,stepNum, statusNum, getKeyString());
				if(stepNum>1){
					voiceListener.onFlowStepChanged(stepNum,statusNum,getKeyString());
				}
				break;
			case 0:
				voiceListener.onServiceInit(false,stepNum, statusNum, getKeyString());
				break;
			default:
				break;
			}
			if(error.getErrorCode()!=-1)
				voiceListener.onServiceError(error);
		}
	};
	
	//服务初始化的线程
	private class InitThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			error.setErrorCode(-1);
			if(!paused){
				Message msg =new Message();
				try{
					msg.what=1;
					switch (mode){
						case REGISTER:
							initRegister();
							break;
						case VERIFY:
							initVerify();
							break;
						case IDENTIFY:
							initIdentify();
							break;
						default:
							break;
					}
				}catch(RuntimeException e){
					msg.what=0;
					error.setErrorParam(0, e.getMessage());
					//throw new RuntimeException(e.getMessage(), e);
				}
				initHandler.sendMessage(msg);
			}
		}
	}
	
	//设置speech的录音文件
	public boolean setSpeechFile(String filepath){
		if(speech==null)
		    speech = new Speech("pcm/raw", 16000, true);

		
		speech.setRule(keyString);

		this.recordbuffer=null;
		this.recordbuffer=readWavform(filepath);

		if(recordbuffer==null)
			return false;
		
		speech.setData(recordbuffer);
		return true;
	}

	//开始调用服务
	private int modeResult;
	public void startService(){
		
		if(!this.setSpeechFile(aservice.getVoiceFile())){
			error.setErrorParam(0, "SD卡里的录音文件出错");
			this.recordListener.onRecordError(error);
			//this.voiceListener.onSpeechFileError();
		}

		Thread resultThread=null;
		resultThread=new Thread(new ResultThread());
		paused=false;
		resultThread.start();

	}
	
	//处理服务结果
	private Handler resultHandler=new Handler(){	
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch(msg.what){
			case 1:
				switch(modeResult){
					case 0:
						voiceListener.onSpeechResult(false);
						voiceListener.onFlowStepChanged(stepNum,statusNum, getKeyString());
						break;
					case 1:
						voiceListener.onSpeechResult(true);
						voiceListener.onFlowStepChanged(stepNum,statusNum, getKeyString());
						break;
					case 2:
						voiceListener.onServiceEnd(false,person,similar);
						voiceListener.onFlowStepChanged(stepNum,statusNum, getKeyString());
						break;
					case 3:
						voiceListener.onServiceEnd(true,person,similar);
						voiceListener.onFlowStepChanged(stepNum,statusNum, getKeyString());
						break;
					default:
						break;
				}
				break;
			case 0:
				break;
			default:
				break;
			}
			
			if(error.getErrorCode()!=-1)
				voiceListener.onServiceError(error);
		}
	};
	
	//获取服务结果的线程
	public class ResultThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			error.setErrorCode(-1);
			if(!paused){
				Message msg =new Message();
				try{
					msg.what=1;
					switch (mode){
						case REGISTER:
							modeResult=getRegister();
							break;
						case VERIFY:
							modeResult=getVerify();
							break;
						case IDENTIFY:
							modeResult=getIdentify();
							break;
						default:
							modeResult=-1;
							break;
					}
				}catch(RuntimeException e){
					msg.what=0;
					modeResult=-1;
					error.setErrorParam(0, e.getMessage());
					//Log.d("StartService",e.getMessage());
					//throw new RuntimeException(e.getMessage(), e);
				}
				resultHandler.sendMessage(msg);
			}
		}
	}
	
	//获取口令，不包括识别
	private void getkeycode1(){
		keyString="";
		if ((ret = person.getAuthCode()) != Constants.RETURN_SUCCESS) {
			Log.d("person.getAuthCode",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
		} else {
			keyString=person.getAuthCodeString();
		}
	}

	//获取识别接口的口令
	private void getkeycode2(){
		keyString="";
		if ((ret = person.getIdentifyAuthCode()) != Constants.RETURN_SUCCESS) {
			Log.d("person.getIdentifyAuthCode",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
		} else {
			keyString=person.getAuthCodeString();
		}
	}

	//注册初始化
	private void initRegister(){
		if ((ret = client.getSysInfo(person)) != Constants.RETURN_SUCCESS) {
			Log.d("client.getSysInfo",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
		}else{
			// Create Person					
			if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {					
				if ( (ret = person.create()) != Constants.RETURN_SUCCESS) {					
					Log.d("Create Person",person.getLastErr()+":"+String.valueOf(ret));
					error.setErrorParam(ret,person.getLastErr());
				}
			}
	
			if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
				Log.d("person.getInfo",person.getLastErr()+":"+String.valueOf(ret));
				error.setErrorParam(ret,person.getLastErr());
			}else if (person.getFlag() == false) {				
				Log.d("personInfo",person.getId()+"\t"+person.getName()+"\t"+person.getStep()+"/"+client.getRegSteps());													
			}
			
			this.stepNum=person.getStep();
			if(ret==Constants.RETURN_SUCCESS)
				getkeycode1();
	
			regNum= client.getRegSteps();
	
			this.statusNum=regNum;
		}
	}

	//识别初始化
	private void initIdentify(){

		if ((ret = client.getSysInfo(person)) != Constants.RETURN_SUCCESS) {
			Log.d("client.getSysInfo",client.getLastErr()+":"+String.valueOf(ret));	
			error.setErrorParam(ret,client.getLastErr());
		}else{

			getkeycode2();
	
			verNum= client.getVerSteps();
	
			this.statusNum=verNum;
		}
	}

	//验证初始化
	private void initVerify(){
		if ((ret = client.getSysInfo(person)) != Constants.RETURN_SUCCESS) {
			Log.d("client.getSysInfo",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
		}else{
			if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
				Log.d("person.getInfo",person.getLastErr()+":"+String.valueOf(ret));
				error.setErrorParam(ret,person.getLastErr());
			}else
				getkeycode1();
	
			this.statusNum = 1;
		}
	}

	//获取注册结果
	private int getRegister(){
		int myres = -1;

		if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
			Log.d("person.getInfo",person.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,person.getLastErr());
		} else if (person.getFlag() == false) {				
			Log.d("personInfo",person.getId()+"\t"+person.getName()+"\t"+person.getStep()+"/"+client.getRegSteps());													

			if ((ret = person.addSpeech(speech)) != Constants.RETURN_SUCCESS) {
				Log.d("person.addSpeech",person.getLastErr()+":"+String.valueOf(ret));
				error.setErrorParam(ret,person.getLastErr());
				myres=0;
			}
			else
				myres=1;
			
			if ( (ret = person.getInfo()) != Constants.RETURN_SUCCESS) {
				Log.d("person.getInfo",person.getLastErr()+":"+String.valueOf(ret));
				error.setErrorParam(ret,person.getLastErr());
			} else if (person.getFlag() == false) {				
				Log.d("personInfo",person.getId()+"\t"+person.getName()+"\t"+person.getStep()+"/"+client.getRegSteps());													
			}
			stepNum=person.getStep();
			
		    if(stepNum==statusNum&&myres==1){
		    	// Register voiceprint for speaker
		    	if ((ret = client.registerVoiceprint(person)) != Constants.RETURN_SUCCESS) {
		    		Log.d("client.registerVoiceprint",client.getLastErr()+":"+String.valueOf(ret));
		    		error.setErrorParam(ret,client.getLastErr());
		    		myres=2;
		    	}
		    	else
		    		myres=3;
		    	// Output result
		    	Log.d("Output result",person.getId()+"\t"+person.getName()+": Register voiceprint success.");			    
	    	}	
    	}

    	getkeycode1();
    	

    	return myres;
	}

	//获取验证结果
	private int getVerify(){
		int myres;
		this.stepNum=0;
		VerifyRes res = new VerifyRes();
		//msg.what=3;
		if ((ret = client.verifyVoiceprint(person, speech, res)) != Constants.RETURN_SUCCESS) {
			Log.d("client.verifyVoiceprint",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
			myres=0;
		}
		else
			myres=1;
		
		if(myres==1){
			if(res.getResult()){
				myres=3;
				this.stepNum=1;
			}
			else
				myres=2;	
		}
		Log.d("Output result",person.getId()+"\t"+person.getName()+": "+res.getResult()+"-"+res.getSimilarity());
		//username=person.getName();
		similar=res.getSimilarity();

		getkeycode1();

		return myres;
	}

	//获取识别结果
	private int getIdentify(){
		int myres;
		this.stepNum=0;
		
		VerifyRes res = new VerifyRes();

		if ((ret = client.identifyVoiceprint_2(person, speech, res)) != Constants.RETURN_SUCCESS) {
			Log.d("client.identifyVoiceprint_2",client.getLastErr()+":"+String.valueOf(ret));
			error.setErrorParam(ret,client.getLastErr());
			myres=0;
		}
		else
			myres=1;
			
		if(myres==1){
			if(res.getResult()){
				myres=3;
				this.stepNum=1;
			}
			else
				myres=2;
		}
		
		Log.d("Output result",person.getId()+"\t"+person.getName()+": "+res.getResult()+"-"+res.getSimilarity());
		String username=person.getName();
		similar=res.getSimilarity();

		getkeycode2();

		return myres;
	}

	//读取录音文件
	public static byte[] readWavform(String filename) {
	
	    int regLen = 0; 
	    byte[] regbuffer = null;
	    try {
	        FileInputStream inputsteam = new FileInputStream(new File(filename));           
	        inputsteam.skip(100);
	
	        regLen = inputsteam.available() - 100;
	        regbuffer = new byte[regLen];
	        if ((regLen = inputsteam.read(regbuffer, 0, regLen))<0) {
	            System.out.println("error when read pcm file.");
	        }
	
	        inputsteam.close();
	
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	
	    return regbuffer;
	}
}

