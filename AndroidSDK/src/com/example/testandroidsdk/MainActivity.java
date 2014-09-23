package com.example.testandroidsdk;

import com.kuaishangtong.recorder.OnImplSpeechListener;
import com.kuaishangtong.recorder.RecordBufferThread;
import com.kuaishangtong.recorder.RecorderThread;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button btnRecorder 						= null;
	TextView infoText 						= null;
	RecorderThread recorderThread 			= null;
	RecordBufferThread recordBufferThread 	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnRecorder = (Button) findViewById(R.id.recorder);
		infoText = (TextView) findViewById(R.id.infotext);
		btnRecorder.setOnTouchListener(touchListener);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	
	OnTouchListener touchListener = new View.OnTouchListener() {		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.recorder) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(this.getClass().toString(), "stop record");
					recordBufferThread.stopDetection();
					recordBufferThread.getRecordBuffer();
					
//					OutputStream os;
//					try {
//						os = new FileOutputStream(new File("/sdcard/Android/" + "speech.pcm"));
//						os.write(Shorts2Bytes(recordBufferThread.getRecordBuffer())); 
//						os.close(); 
//					} catch (FileNotFoundException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
				} else  if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d(this.getClass().toString(), "start record...");
					recordBufferThread = new RecordBufferThread(new RecorderThread());
					recordBufferThread.setOnImplSpeechListener(speechListener);
					recordBufferThread.start();
				}
			}
			
			return false;
		}
	};
	
	OnImplSpeechListener speechListener = new OnImplSpeechListener() {
		
		@Override
		public void onSoundDetected(int vol) {
			setVolumeIcon(vol);
		}
	};
	
	public void setVolumeIcon(int vol) {
		final int v = vol;
		this.infoText.post(new Runnable() {			
			@Override
			public void run() {
				infoText.setText("“Ù¡ø:"+v);
			}
		});
	}

}
