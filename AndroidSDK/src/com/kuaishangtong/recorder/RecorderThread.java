package com.kuaishangtong.recorder;
/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * musicg api in Google Code: http://code.google.com/p/musicg/
 * Android Application in Google Play: https://play.google.com/store/apps/details?id=com.whistleapp
 * 
 */



import java.io.OutputStream;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class RecorderThread extends Thread {
	
	private AudioRecord audioRecord;
	private boolean isRecording;
	private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int sampleRate = 16000;
	private int frameByteSize = 1024; // for 1024 fft size (16bit sample size)
	short[] buffer;
	float averageAbsValue = 0.0f;
	OutputStream os;
	
	public RecorderThread(){
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, 8192);
		buffer = new short[frameByteSize];
	}
	
	public AudioRecord getAudioRecord(){
		return audioRecord;
	}
	
	public boolean isRecording(){
		return this.isAlive() && isRecording;
	}
	
	public void startRecording(){
		try{
			audioRecord.startRecording();
			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecording(){
		try{
			audioRecord.stop();
			isRecording = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public short[] getFrameBytes(){
		audioRecord.read(buffer, 0, buffer.length);
		
		// analyze sound
		int totalAbsValue = 0;
        averageAbsValue = 0.0f;
        
        for (int i = 0; i < buffer.length; i++) {
            totalAbsValue += Math.abs(buffer[i]);
        }
        averageAbsValue = totalAbsValue / buffer.length;
        
        // no input
        if (averageAbsValue < 30){
        	return null;
        }
        
		return buffer;
	}
	
	public void run() {
		startRecording();
	}
	
	public float getAverageAbsValue() { return this.averageAbsValue; }
	public int getFrameSize() { return this.buffer.length; }
}