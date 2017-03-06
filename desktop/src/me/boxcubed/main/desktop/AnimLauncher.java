package me.boxcubed.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.boxcubed.animtest.AnimationTester;


public class AnimLauncher {
	public static void main(String[] args){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new AnimationTester(), config);
		config.resizable = true;
		config.height = 600;
		config.width = 800;
		config.fullscreen = false;
	}
}
