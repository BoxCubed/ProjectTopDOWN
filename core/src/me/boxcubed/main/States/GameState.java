package me.boxcubed.main.States;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.boxcubed.main.Entity.Camera;

public class GameState extends State implements Screen {
	public GameState (GameStateManager gsm){
		super(gsm);
		//Basically the create method
	}

	@Override
	protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            //Player.goUp
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            //Player.goDown
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            //Player.goLeft
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            //Player.goRight
        }
	}

	@Override
	public void update(float delta) {
        Camera.update();
	}

	@Override
	public void render(SpriteBatch batch) {

    }

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

	}

	@Override
	public void resize(int width, int height) {
	    Camera.update();
    }

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}