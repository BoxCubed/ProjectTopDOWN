package me.boxcubed.main;

import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import me.boxcubed.main.TopDown;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TopDown(false), config);
	}

	@Override
	public  void onWindowFocusChanged(boolean  hasFocus){
		super.onWindowFocusChanged(hasFocus);
		View decorView = getWindow().getDecorView();
		if(hasFocus){

			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  // this flag do=Semi-transparent bars temporarily appear and then hide again
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // Make Content Appear Behind the status  Bar
					|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  // it Make Content Appear Behind the Navigation Bar
					|View.SYSTEM_UI_FLAG_FULLSCREEN  // hide status bar
					|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	}
}
