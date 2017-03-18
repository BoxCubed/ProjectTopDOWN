package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boxcubed.utils.CleanInputProcessor;

import me.boxcubed.main.TopDown;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class MenuState implements State, CleanInputProcessor {
 //LOL//
	Stage stage;
    Texture button;
    private static final int buttonXNY =200;
    public MenuState() {
     /*this.gsm=gsm;*/
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(this);
        button = new Texture("assets/img/icon.png");
    }

    @Override
	public void handleInput() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
    	
        batch.begin();
        batch.draw(button, Gdx.graphics.getWidth()/2 -buttonXNY/2, Gdx.graphics.getHeight()/2 - buttonXNY/2, buttonXNY, buttonXNY);
        batch.end();
    }

    @Override
    public void show() {

    }

    

    @Override
    public void resize(int width, int height) {

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
        this.dispose();
    }
  
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        TopDown.instance.setScreen(new GameState());
        return true;
    }

   
}
