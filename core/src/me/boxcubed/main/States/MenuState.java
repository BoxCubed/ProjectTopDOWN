package me.boxcubed.main.States;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class MenuState extends ApplicationAdapter implements State {
    class MyActor extends Actor{
        Texture button = new Texture("assets/img/icon.png");//THIS IS JUST FOR A TEST
        private static final int buttonXNY =200;
        public void draw(Batch batch, float parentAlpha){
            batch.draw(button, Gdx.graphics.getWidth()/2 -buttonXNY/2, Gdx.graphics.getHeight()/2 - buttonXNY/2, buttonXNY, buttonXNY);
        }
    }
	Stage stage;
    public MenuState() {
     /*this.gsm=gsm;*/
     MyActor actor = new MyActor();
     stage = new Stage(new ScreenViewport());
     stage.addActor(actor);
     Gdx.input.setInputProcessor(stage);

    }

    @Override
	public void handleInput() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //THIS IS THE RENDER LOOP THAT WORKS
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
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

    }
}
