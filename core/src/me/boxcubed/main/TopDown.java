package me.boxcubed.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.boxcubed.utils.Assets;

import me.boxcubed.main.States.GameState;
import me.boxcubed.main.States.SplashState;

public class TopDown extends Game {
	public static TopDown instance;
	public static boolean debug;
	public static Assets assets;
	public TopDown(boolean debug) {
		TopDown.debug=debug;
		//TODO Move every single asset into this
		assets=new Assets();
		
		
		
	}

	@Override
	public void create () {
		instance=this;
		
		if(debug){
		setScreen(new GameState());
		GameState.instance.noTime=true;
		GameState.instance.noZombie=true;
		}
		else 
			setScreen(new SplashState());
		//new ClientServerTest(2);
	}

	@Override
	public void render () {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
			}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
