package com.example.testandroidsdk;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.kuaishangtong.client.*;
import com.kuaishangtong.utils.Constants;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Client client;
				
				// Create server
				client = new Client("65e02ffc45b0d01bd09fa3e0e9fe1b14", "65e02ffc45b0d01bd09fa3e0e9fe1b14");
				client.setServer("192.168.1.253", 11638, "1", Constants.TEXT_DEPENDENT);
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
