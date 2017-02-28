package me.boxcubed.main.States;

/**
 * Created by Tej Sidhu on 9/02/2017.
 */


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public interface State  extends Screen {
    //protected Vector3 mouse;    //Mouse xy

    



    



    void handleInput();

    public void update(float delta);

    public void render(SpriteBatch batch);

    public void dispose();
}
