package me.boxcubed.main.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.Light;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight{
	//TODO make new class for just time handling
    boolean flashlightState=true;
    Light pointLight;
    public PlayerLight(World world,Body bod,Light light){
    	this.pointLight=light;
    }
    ConeLight light;
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
