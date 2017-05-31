package me.boxcubed.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.boxcubed.utils.BoxoUtil;
import me.boxcubed.main.States.GameState;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("This project is on Android!");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		initialize(new TopDown(false), config);
	}

	@Override
	public  void onWindowFocusChanged(boolean  hasFocus){
		super.onWindowFocusChanged(hasFocus);
		System.out.println("redoing focus");
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


	@Override
	public void onBackPressed() {
		System.out.println("back was pressed");
		if (TopDown.instance.getScreen() instanceof GameState)
			BoxoUtil.onAndroidBackButtonPressed.run();
		else new AlertDialog.Builder(this).setMessage("Are you sure you want to exit HIIDE?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						AndroidLauncher.super.onBackPressed();
					}
				}).setNegativeButton("No", null).show();


	}
}
