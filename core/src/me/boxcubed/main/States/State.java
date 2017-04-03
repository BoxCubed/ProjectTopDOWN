package me.boxcubed.main.States;




import com.badlogic.gdx.Screen;
/**
 * The state interface<br>
 * Only Override the <code>render(float delta)</code> method if you wish to change how this method runs handleInput and update.<br>
 * See also {@link Screen} to understand how this functions<br>
 * Make sure to dispose the final SpriteBatch with {@code batch.dispose()} in the dispose method
 * 
 * @author ryan9
 */
public interface State  extends Screen {
    //protected Vector3 mouse;    //Mouse xy

    

    



     void handleInput();

    public void update(float delta);
    @Override
    default void render(float delta) {
    	handleInput();
    	update(delta*100);
    	render();
    	
    }

    public void render();
    /*
     * (non-Javadoc)
     * Remember to dispose batch with <code>batch.dispose()</code>
     * @see com.badlogic.gdx.Screen#dispose()
     */
    public void dispose();
}
