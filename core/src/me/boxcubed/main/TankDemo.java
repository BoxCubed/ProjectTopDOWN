package me.boxcubed.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.boxcubed.util.TopDownBox2DTankTutorial;

public class TankDemo extends Game {
	SpriteBatch batch;
	//GameStateManager gsm;
	@Override
	public void create () {
		
		TopDownBox2DTankTutorial tut=new TopDownBox2DTankTutorial();
		
		setScreen(tut);
	}

	@Override
	public void render () {
       
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		
		}
	
	@Override
	public void dispose () {
		super.dispose();
		//batch.dispose();
	}
}
