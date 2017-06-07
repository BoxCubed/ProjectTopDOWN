package me.boxcubed.main.Objects;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.events.EventHandler;
import com.boxcubed.events.NightEvent;

import box2dLight.RayHandler;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.States.GameState;

public class Clock {
	// TODO make new class for just time handling
	public RayHandler rayHandler;
	boolean flashlightState = true;
	private StopWatch timer;
	public boolean progressTime;
	private boolean night = false;
	public float amlight = 1f;
	public static float gameTime;

	public Clock(World world) {
		timer = new StopWatch();
		rayHandler = new RayHandler(world);
		progressTime = !TopDown.debug;

		timer.start();
	}

	public void updateLight(float delta) {
		gameTime=amToTimeFloat();
		if (Gdx.input.isKeyPressed(Keys.EQUALS))
			amlight += 0.01f;
		if (Gdx.input.isKeyPressed(Keys.MINUS))
			amlight -= 0.01f;
		rayHandler.setAmbientLight(amlight);
		rayHandler.update();

		if(amlight>0.08){
			amlight-=0.0005f;
			
			
		}else{
			
				try {
					EventHandler.callEvent(new NightEvent(amToTimeFloat()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	@SuppressWarnings("deprecation")
	public void renderLIGHT(OrthographicCamera matrix) {
		rayHandler.setCombinedMatrix(matrix.combined.cpy().scl(GameState.PPM));
		rayHandler.render();

	}

	public void dispose() {
		rayHandler.dispose();
	}

	public String amToTime() {
		float hrs, mins;
		if (night) {
			hrs = (amlight * 12);
			mins = hrs * 60 - (int) hrs * 60;
			return (int) hrs + ":" + (int) mins + "am";
		} else {
			hrs = 12 - (amlight * 12);
			mins = hrs * 60 - (int) hrs * 60;
			return (int) hrs + ":" + (int) mins + "pm";
		}

		// return Float.toString(amlight);
	}
	
	public float amToTimeFloat() {
		float hrs, mins;
		if (night) {
			hrs = (amlight * 12);
			mins = hrs * 60 - (int) hrs * 60;
			return hrs+(mins)/100;
		} else {
			hrs = 12 - (amlight * 12);
			mins = hrs * 60 - (int) hrs * 60;
			return hrs+(mins)/100;
		}

		// return Float.toString(amlight);
	}

}
