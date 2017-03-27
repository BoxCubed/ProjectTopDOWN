package me.boxcubed.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import me.boxcubed.main.TopDown;


public class DesktopLauncher {
	static boolean debug=false;
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
