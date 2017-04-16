package com.boxcubed.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Boxcube's wonderful util class filled with useful applications for LibGDX <br> 
 */
public class BoxoUtil implements InputProcessor{
	//all the input processors to process
	private static List<InputProcessor>inputProcessors=new ArrayList<>();
	//For thread safety when removing and adding input processors from multiple threads
	private static LinkedBlockingQueue<InputProcessor> qAdd=new LinkedBlockingQueue<>();
	private static LinkedBlockingQueue<InputProcessor> qRemove=new LinkedBlockingQueue<>();
	// for the ButtonJustPressed method
	private static boolean[] justPressed=new boolean[5];
	
	/**
	 * Call this to reset the API<br>
	 * This will clear the input processors and reset the Gdx input processor to this class
	 */
	public static void reset(){
		inputProcessors=new ArrayList<>();
		qAdd=new LinkedBlockingQueue<>();
		qRemove=new LinkedBlockingQueue<>();
		Gdx.input.setInputProcessor(new BoxoUtil());
	}
	
	/**
	 * Call this when updating the game <br>
	 * Make sure it is on the game thread and not another thread
	 */
	public static void update(){
		try{
			
		while(!qAdd.isEmpty())inputProcessors.add(qAdd.take());
		while(!qRemove.isEmpty())inputProcessors.remove(qRemove.take());
		
		}catch(InterruptedException e){e.printStackTrace();}
		for(int i=0;i<justPressed.length;i++)
			justPressed[i]=false;
		
	}
	/**
	 * This method allows you to lerp(ease in and out) from one coordinate to another<br>
	 * Does not require for this class to be initiated or updated
	 * @param start the current position, this should change every time you call this method
	 * @param finish the target position
	 * @return the new vector to move to
	 */
	public static Vector2 universalLerpToPos(Vector2 start,Vector2 finish){
    	final float speed=0.5f,ispeed=1.0f-speed;
		Vector3 target = new Vector3(
				(float)finish.x, 
				(float)finish.y, 
				0);
		Vector3 cameraPosition = new Vector3(start, 0);
		cameraPosition.scl(ispeed);
		target.scl(speed);
		cameraPosition.add(target);

		return new Vector2(cameraPosition.x, cameraPosition.y);
    	
    }
	/**
	 * Whether a button was just pressed 
	 * @param button the button to check
	 * @return whether the button was pressed in this update call. Returns false if button is invalid.
	 */
	public static boolean isButtonJustPressed(int button){
		try{
		return justPressed[button];}
		catch(IndexOutOfBoundsException e){return false;}
	}
	/**
	 * Clears the screen allowing you to draw on it again. Call every render method unless you want your game<br>
	 * to look like it's on drugs
	 */
	public static void clearScreen(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	/**
	 * 
	 * Add an input processor. This allows multiple input processors to be used.
	 * <br>
	 * <br>Make sure to save it locally so you can delete it later.<br>
	 * <br>
	 * Make sure to remove your input processor on dispose/whenever you need with. Or not if you plan to use it to return to a state <br>
	 * <br>
	 * Requires for this class to be initialized and for update to be called in order to work. 
	 * 
	 * {@link BoxoUtil#remInputProcessor(InputProcessor)}
	 * 
	 * @param add the processor to add
	 */
	public static void addInputProcessor(InputProcessor add){qAdd.add(add);}
	/**
	 * Remove an input processor from the list<br>
	 * This might call the input processor before removing it unless the update method is called after this method.
	 * @see BoxoUtil#addInputProcessor(InputProcessor)
	 * @param remove The processor to remove
	 */
	public static void remInputProcessor(InputProcessor remove){qRemove.add(remove);}
	/**
	 * Lerp(ease in and out) the camera given to a certain position<br>
	 * Currently only works with the 2D camera
	 * @param pos the Position to lerp to
	 * @param cam the camera to perform the lerp on
	 */
	 public static void lerpToPos(Vector2 pos, OrthographicCamera cam){
		 //TODO add 3d support
			final float speed=0.1f,ispeed=1.0f-speed;
			Vector3 target = new Vector3(
					pos.x, 
					pos.y, 
					0);
			Vector3 cameraPosition = cam.position;
			cameraPosition.scl(ispeed);
			target.scl(speed);
			cameraPosition.add(target);

			cam.position.set(cameraPosition);
		}
	@Override
	public boolean keyDown(int keycode) {
		inputProcessors.forEach(i->i.keyDown(keycode));
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		inputProcessors.forEach(i->i.keyDown(keycode));
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		inputProcessors.forEach(i->i.keyTyped(character));
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		justPressed[button]=true;
		inputProcessors.forEach(i->i.touchDown(screenX, screenY, pointer, button));
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		inputProcessors.forEach(i->i.touchUp(screenX, screenY, pointer, button));
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		inputProcessors.forEach(i->i.touchDragged(screenX, screenY, pointer));
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		inputProcessors.forEach(i->i.mouseMoved(screenX, screenY));
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		inputProcessors.forEach(i->i.scrolled(amount));
		return false;
	}
	
}
