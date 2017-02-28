package me.boxcubed.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.boxcubed.main.TankDemo;

public class TankLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TankDemo(), config);
		config.resizable = true;
		config.height = 1000;
		config.width = 720;
	}
}
