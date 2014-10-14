package com.example.testandroidsdk;

import com.kuaishangtong.client.Client;
import com.kuaishangtong.model.Person;
import com.kuaishangtong.recorder.RecordBufferThread;
import com.kuaishangtong.recorder.RecorderThread;
import com.kuaishangtong.utils.Constants;

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
	
	Client client = null;
	Person person = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnRecorder = (Button) findViewById(R.id.recorder);
		btnRecorder.setOnTouchListener(touchListener);	
		
		client = new Client("110832a5a394a7df7e5691a746c61b7c", "110832a5a394a7df7e5691a746c61b7c");
		client.setServer("114.215.103.99",11638,"1");		
		person = new Person(client, "dddddd", "dddddd");
		person.setPassType(Constants.VOICEPRINT_TYPE_RANDOM_DIGITS);		
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
				} else  if (event.getAction() == MotionEvent.ACTION_DOWN){
					Log.d(this.getClass().toString(), "start record...");
					new  Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								if (client.getSysInfo(person) != Constants.RETURN_SUCCESS) {
									Log.e("------------", client.getLastErr()+":"+client.getErrCode());
								} else {
									Log.d("------------", "OK.");
								}
								
								if (person.getInfo() != Constants.RETURN_SUCCESS) {
									Log.e("------------", person.getLastErr()+":"+person.getErrCode());
								} else {
									Log.d("------------", "OK.");
								}
							} catch (Exception e) {
								Log.e("------------", e.getMessage());
							}
						}
					}).start();
				}
			}
			
			return false;
		}
	};
}
