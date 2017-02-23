package me.boxcubed.main.States;

/**
 * Created by Tej Sidhu on 9/02/2017.
 */


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public abstract class State implements Screen {
    //protected Vector3 mouse;    //Mouse xy

    protected  GameStateManager gsm;



    protected State(GameStateManager gsm){
        this.gsm = gsm;
    }



    protected abstract void handleInput();

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();
}
