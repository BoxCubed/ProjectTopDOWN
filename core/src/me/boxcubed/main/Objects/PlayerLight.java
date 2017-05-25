package me.boxcubed.main.Objects;

import com.badlogic.gdx.math.Vector2;

import box2dLight.Light;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight{
	//TODO make new class for just time handling
    private boolean flashlightState=true;
    private final Light pointLight;
    public PlayerLight(Light light){
    	this.pointLight=light;
    }
    public void updateLightPos(Vector2 pos, float angle, float delta, boolean strong){
        //Makes sure that the light moves with the player
    	  pointLight.setPosition(pos.x, pos.y);
          pointLight.setDirection(angle);
     flashlightState= true;
      
    	if(flashlightState){pointLight.setDistance(5);}else{pointLight.setDistance(0);}
        if(strong){
        	if(flashlightState)pointLight.setDistance(20);}
        else if(flashlightState)pointLight.setDistance(5);
        
    }
    

	public void dispose() {
        pointLight.remove();
		
	}
	
}
