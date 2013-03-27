package com.apptarixtv;


import com.apptarixtv.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_splash);
        
    	Thread splash;
		splash = new Thread() {
			@Override
			public void run() {
				super.run();

				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e("@@@@@", "excepion");
					System.out.println("e.printStackTrace()");
				} finally {
					Log.e("@@@@@", "finally");
			
					finish();
					 Intent startIntent=new Intent(getApplicationContext(), FbLoginScreen.class);
				        startActivity(startIntent);
					// stop();
	

				}

			}
		};
		splash.start();
    }

   

    
}
