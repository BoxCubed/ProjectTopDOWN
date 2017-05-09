package me.boxcubed.main.Objects;

import com.badlogic.gdx.math.Vector2;

import box2dLight.Light;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight{
	//TODO make new class for just time handling
    boolean flashlightState=true;
    Light pointLight;
    public PlayerLight(Light light){
    	this.pointLight=light;
    }
    public void updateLightPos(Vector2 pos,float angle,float delta,boolean strong,boolean on){
        //Makes sure that the light moves with the player
    	  pointLight.setPosition(pos.x+2.5f, pos.y+2.5f);
          pointLight.setDirection(angle);
     flashlightState=on;
      
    	if(flashlightState){pointLight.setDistance(100);}else{pointLight.setDistance(0);}
        if(strong){
        	if(flashlightState)pointLight.setDistance(400);}
        else if(flashlightState)pointLight.setDistance(100);
        
    }
    

	public void dispose() {
        pointLight.dispose();
		
	}
	
}
