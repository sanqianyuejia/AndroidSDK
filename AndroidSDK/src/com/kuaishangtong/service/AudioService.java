package com.kuaishangtong.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioService {
	// 音频获取源 

    private int audioSource = MediaRecorder.AudioSource.MIC; 

    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025 

    private static int sampleRateInHz = 16000; 

    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道 

    private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; 

    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。 

    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT; 

    // 缓冲区字节大小  www.2cto.com

    private int bufferSizeInBytes = 0; 
    
    private int frameByteSize = 1024; // for 1024 fft size (16bit sample size)


    private AudioRecord audioRecord;
    
    private boolean isRecord = false;// 设置正在录制的状态 

    
    public static String SDPATH2 = Environment.getExternalStorageDirectory().getPath()
            + "/VoicePrintTemp/";
    
    //AudioName裸音频数据文件 

    private  String RawAudioName; 

    //NewAudioName可播放的音频文件 

    private  String FinalAudioName;
    
    private String VoiceDirPath;
    
    private String VoiceFilePath;
    
    private static AudioService mInstance; 
    
    private float averageAbsValue=0.0f;
    

    
    private AudioService(){
    	
    }   
     
    
    public synchronized static AudioService getInstance()
    {
        if(mInstance == null) 
            mInstance = new AudioService(); 
        return mInstance; 
    }
    
    public void setVoiceDir(String dirname){
    	this.VoiceDirPath=Environment.getExternalStorageDirectory().getPath()
                + "/"+dirname+"/";
    	try {
    		FileService.deleteDir2(VoiceDirPath);
			FileService.createDir(dirname);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void setVoiceFile(String filename){
    	this.VoiceFilePath=this.VoiceDirPath+filename+".raw";
    }
    
    public String getVoiceFile(){
    	return this.VoiceFilePath;
    }
    
    public void setRawAudioName(String RawAudioName)
    {
    	this.RawAudioName=SDPATH2+"RawAudio/"+RawAudioName+".raw";
    }
    
    public void setFinalAudioName(String FinalAudioName)
    {
    	this.FinalAudioName=SDPATH2+"FinalAudio/"+FinalAudioName+".wav";
    }
    
    public String getFinalAudioName(String FinalAudioName)
    {
    	return SDPATH2+"FinalAudio/"+FinalAudioName+".wav";
    }
    
    public float getAverageAbsValue()
    {
    	return this.averageAbsValue;
    }
    
    public void setAudioPermission()
    {
    	this.creatAudioRecord();
    	this.close();
    }
    
    private void creatAudioRecord() { 
    	
    	//if(android.os.Build.VERSION.SDK_INT<11)
    	//	sampleRateInHz=8000;
    	
        // 获得缓冲区字节大小 
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, 
                channelConfig, audioFormat); 
        // 创建AudioRecord对象 
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, 
                channelConfig, audioFormat, bufferSizeInBytes); 
        
        bufferSizeInBytes=this.frameByteSize;
        //bufferSizeInBytes=this.frameByteSize;
    } 
    
    public void startRecordAndFile() {
    	if(audioRecord == null)     
    		creatAudioRecord();
    	
    	audioRecord.startRecording();                  
    	// 让录制状态为true                  
    	isRecord = true;   
    	// 开启音频文件写入线程
    	try{
    		new Thread(new AudioRecordThread()).start(); 
    	}catch(Exception e){
    		throw new RuntimeException(e.getMessage(),e);
    	}
    }  
    
    public void stopRecordAndFile(){
        close();
   }
    
    private void close() {  
        if (audioRecord != null) {  
            System.out.println("stopRecord");  
            isRecord = false;//停止文件写入  
            audioRecord.stop();  
            audioRecord.release();//释放资源  
            audioRecord = null;  
        }  
    }
    
    class AudioRecordThread implements Runnable { 
        @Override 
        public void run() { 
        	try{
	            writeDateTOFile();//往文件中写入裸数据 
	            //copyWaveFile(RawAudioName, FinalAudioName);//给裸数据加上头文件 
        	}
        	catch(Exception e)
        	{
        		throw new RuntimeException(e.getMessage(),e);
        	}
        } 
    } 
    
    private void writeDateTOFile() { 
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小 
    	// while (isRecord == true){
        byte[] audiodata = new byte[bufferSizeInBytes]; 
        FileOutputStream fos = null; 
        int readsize = 0;  
        try { 
            //File file = new File(RawAudioName); 
        	File file = new File(VoiceFilePath);
            if (file.exists()) { 
                file.delete(); 
            } 
            fos = new FileOutputStream(file);// 建立一个可存取字节的文件 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        while (isRecord == true) { 
            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes); 
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize) { 
                try { 
                    fos.write(audiodata); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
            
         // analyze sound    		
            int totalAbsValue = 0;
            short sample = 0; 

            
            for (int i = 0; i < bufferSizeInBytes; i += 2) {
                sample = (short)((audiodata[i]) | audiodata[i + 1] << 8);
                totalAbsValue += Math.abs(sample);
            }
            averageAbsValue = totalAbsValue / bufferSizeInBytes / 2;
            
        } 
        try { 
            fos.close();// 关闭写入流 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    	// }
    }   
   
    // 这里得到可播放的音频文件 
    private void copyWaveFile(String inFilename, String outFilename) { 
        FileInputStream in = null; 
        FileOutputStream out = null; 
        long totalAudioLen = 0; 
        long totalDataLen = totalAudioLen + 36; 
        long longSampleRate = sampleRateInHz; 
        int channels = 2; 
        long byteRate = 16 * sampleRateInHz * channels / 8; 
        byte[] data = new byte[bufferSizeInBytes]; 
        try { 
            in = new FileInputStream(inFilename); 
            out = new FileOutputStream(outFilename); 
            totalAudioLen = in.getChannel().size(); 
            totalDataLen = totalAudioLen + 36; 
            WriteWaveFileHeader(out, totalAudioLen, totalDataLen, 
                    longSampleRate, channels, byteRate); 
            while (in.read(data) != -1) { 
                out.write(data); 
            } 
            in.close(); 
            out.close(); 
        } catch (FileNotFoundException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
   
    /** 
     * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。 
     * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav 
     * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有 
     * 自己特有的头文件。 
     */ 
    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,  
            long totalDataLen, long longSampleRate, int channels, long byteRate)  
            throws IOException {  
        byte[] header = new byte[44];  
        header[0] = 'R'; // RIFF/WAVE header  
        header[1] = 'I';  
        header[2] = 'F';  
        header[3] = 'F';  
        header[4] = (byte) (totalDataLen & 0xff);  
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);  
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);  
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);  
        header[8] = 'W';  
        header[9] = 'A';  
        header[10] = 'V';  
        header[11] = 'E';  
        header[12] = 'f'; // 'fmt ' chunk  
        header[13] = 'm';  
        header[14] = 't';  
        header[15] = ' ';  
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk  
        header[17] = 0;  
        header[18] = 0;  
        header[19] = 0;  
        header[20] = 1; // format = 1  
        header[21] = 0;  
        header[22] = (byte) channels;  
        header[23] = 0;  
        header[24] = (byte) (longSampleRate & 0xff);  
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);  
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);  
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);  
        header[28] = (byte) (byteRate & 0xff);  
        header[29] = (byte) ((byteRate >> 8) & 0xff);  
        header[30] = (byte) ((byteRate >> 16) & 0xff);  
        header[31] = (byte) ((byteRate >> 24) & 0xff);  
        header[32] = (byte) (2 * 16 / 8); // block align  
        header[33] = 0;  
        header[34] = 16; // bits per sample  
        header[35] = 0;  
        header[36] = 'd';  
        header[37] = 'a';  
        header[38] = 't';  
        header[39] = 'a';  
        header[40] = (byte) (totalAudioLen & 0xff);  
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);  
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);  
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);  
        out.write(header, 0, 44);  
    }  
    
    
  //读取录音文件
  	public  byte[] readWavform(String filename) {
  		int regLen = 0;	
  		byte[] regbuffer = null;
  		try {
  			FileInputStream inputsteam = new FileInputStream(new File(filename));			
  			inputsteam.skip(100);
  			
  			regLen = inputsteam.available() - 100;
  			regbuffer = new byte[regLen];
  			if ((regLen = inputsteam.read(regbuffer, 0, regLen))<0) {
  				Log.e("readWavform","error when read pcm file.");
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
