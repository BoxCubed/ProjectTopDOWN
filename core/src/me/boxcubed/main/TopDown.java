package me.boxcubed.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.States.GameStateManager;

public class TopDown extends ApplicationAdapter {
	SpriteBatch batch;
	GameStateManager boob;
	@Override
	public void create () {
		batch = new SpriteBatch();
		boob = new GameStateManager();
		boob.push(new GameState(boob));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		boob.update(Gdx.graphics.getDeltaTime());
		batch.begin();
		boob.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
