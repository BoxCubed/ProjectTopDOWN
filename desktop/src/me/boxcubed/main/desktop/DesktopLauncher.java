package me.boxcubed.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.boxcubed.main.TopDown;


public class DesktopLauncher {
	static boolean debug=false;
	public static void main (String[] arg) {
		for(String s:arg)System.out.println(s);
		if(arg.length==1&&arg[0].equals("-debug")){
			debug=true;
			System.out.println("Debug was set to true");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TopDown(debug), config);
		config.resizable = true;
		config.height = 900;
		config.width = 1280;
		//config.fullscreen = true;

	}
}
