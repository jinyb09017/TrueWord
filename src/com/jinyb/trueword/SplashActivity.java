package com.jinyb.trueword;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
        
        
	}
	
	class splashhandler implements Runnable{

	    public void run() {
	        startActivity(new Intent(SplashActivity.this,MainActivity.class));
	        SplashActivity.this.finish();
	    }
	    
	}
	

}
