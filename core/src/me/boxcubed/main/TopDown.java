package me.boxcubed.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import me.boxcubed.main.States.GameState;
import me.boxcubed.main.States.SplashState;

public class TopDown extends Game {
	public static TopDown instance;

	@Override
	public void create () {
		instance=this;
		
		
		setScreen(new SplashState());
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
