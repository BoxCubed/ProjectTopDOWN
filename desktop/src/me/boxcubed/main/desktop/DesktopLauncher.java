package me.boxcubed.main.desktop;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.boxcubed.net.NetworkManager;
import com.esotericsoftware.minlog.Log;

import me.boxcubed.main.TopDown;


public class DesktopLauncher {
	private boolean debug=false;
	private LwjglApplication app;
	public DesktopLauncher(String[] args) {
		for(String s:args)System.out.println(s);
		List<String> largs=Arrays.asList(args);
		if(largs.contains("-debug")){
			debug=true;
			System.out.println("Debug was set to true");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		app=new LwjglApplication(new TopDown(debug), config);
		if(debug){
		config.resizable = true;
		config.height = 900;
		config.width = 1280;
		}else{
			config.resizable=false;
			config.height = 700;
			config.width = 500;
			Gdx.graphics.setUndecorated(true);
		}
		Thread.setDefaultUncaughtExceptionHandler((thread,ex)->{
			if(thread.getName().equals("LWJGL Application")){
				//TODO Error handling
				Gdx.app.setLogLevel(Log.LEVEL_ERROR);
				Gdx.app.log("[PTD]", "PTD has Crashed!");
				ex.printStackTrace();
				System.exit(-1);
			}
			if(thread.getName().equals("PTD Client")){
				Gdx.app.setLogLevel(Log.LEVEL_ERROR);
				Gdx.app.log("[PTD]", "PTD server connection has crashed!");
				ex.printStackTrace();
				((NetworkManager)thread).stop=true;
				try {
					((NetworkManager)thread).join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
	}
	public static void main (String[] arg) {
		/*Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
            String filename = DesktopLauncher.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            try {
				Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else{
            DesktopLauncher.main(new String[0]);
          //  System.out.println("Program has ended, please type 'exit' to close the console");
        }*/
		
		//config.fullscreen = true;
		new DesktopLauncher(arg);
	}
}
