package me.boxcubed.main.States;

/**
 * Created by Tej Sidhu on 9/02/2017.
 */


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public interface State  extends Screen {
    //protected Vector3 mouse;    //Mouse xy

    
	SpriteBatch batch=new SpriteBatch();


    



     void handleInput();

    public void update(float delta);
    @Override
    default void render(float delta) {
    	handleInput();
    	update(delta*100);
    	render(batch);
    	
    }

    public void render(SpriteBatch batch);

    public void dispose();
}
