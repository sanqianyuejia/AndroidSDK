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

package com.kuaishangtong.recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;
import android.util.Log;

public class RecordBufferThread extends Thread{

	private RecorderThread recorder;
	private volatile Thread _thread;
	LinkedBlockingQueue<short[]> q;	
	private OnImplSpeechListener onSignalsDetectedListener;
	
	public RecordBufferThread(RecorderThread recorder){
		this.recorder = recorder;
	}

	private void initBuffer(LinkedBlockingQueue<short[]> q) {
		this.q = q;
	}

	public void start() {
        Log.d(this.getClass().toString(), "start record...");	
		if (this.q != null) this.q.clear();
		else initBuffer(new LinkedBlockingQueue<short[]>());
		_thread = new Thread(this);
        _thread.start();
    }
	
	public void stopDetection(){
		Log.d(this.getClass().toString(), "stop");	
		_thread = null;
		this.recorder.stopRecording();
	}
	
	public void run() {
		try {
			short[] buffer;			
			recorder.start();
			
			Thread thisThread = Thread.currentThread();
			while (_thread == thisThread) {
				buffer = recorder.getFrameBytes();
				onSoundDetected((int)recorder.getAverageAbsValue());
				
				if (buffer != null) {
					// sound detected	
					this.q.add(buffer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onSoundDetected(int vol) {
		if (onSignalsDetectedListener != null){
			onSignalsDetectedListener.onSoundDetected(vol);
		}
	}
	
	public void setOnImplSpeechListener(OnImplSpeechListener listener){
		onSignalsDetectedListener = listener;
	}
	
	public boolean isRecording()  { return this.recorder.isRecording(); }
	
	public boolean testCPU() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            // System.out.println(is big ending);
            return true;
        } else {
            // System.out.println(is little ending);
            return false;
        }
    }
	
	public byte[] getBytes(short s, boolean bBigEnding) {
        byte[] buf = new byte[2];
        if (bBigEnding)
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        else
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x00ff);
                s >>= 8;
            }
        return buf;
    }
	
	public byte[] getBytes(short s) {
        return getBytes(s, this.testCPU());
    }
	
	public byte[] Shorts2Bytes(short[] s) {
        byte bLength = 2;
        byte[] buf = new byte[s.length * bLength];
        for (int iLoop = 0; iLoop < s.length; iLoop++) {
            byte[] temp = getBytes(s[iLoop]);
            for (int jLoop = 0; jLoop < bLength; jLoop++) {
                buf[iLoop * bLength + jLoop] = temp[jLoop];
            }
        }
        return buf;
    }
	
	public short[] getRecordBuffer() {
		int size = 0;
		short[] buffer = new short[this.q.size()*recorder.getFrameSize()];
		short[] temp = null;
		while ((temp = this.q.poll()) != null) {
			System.arraycopy(temp, 0, buffer, size, temp.length);
			size += temp.length;
		}
		
		return buffer;
	}
}