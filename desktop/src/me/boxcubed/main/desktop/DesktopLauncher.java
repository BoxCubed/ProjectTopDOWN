package me.boxcubed.main.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.boxcubed.main.TopDown;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TopDown(), config);
		config.resizable = true;
		config.height = 1000;
		config.width = 1000;
		config.fullscreen = true;

	}
}
