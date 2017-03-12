package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight{
    public RayHandler rayHandler;
    ConeLight pointLight;
    Player player;
    boolean night=false;
   public static float amlight=1f;
    public PlayerLight(World world,Body bod){
        //LIGHT init
        rayHandler = new RayHandler(world);
        pointLight =new ConeLight(rayHandler, 1000, Color.YELLOW, 100, 100, 100, 90, 45);
       

        //pointLight.attachToBody(bod);
    }

    public void updateLightPos(float x, float y,float angle,float delta){
        //Makes sure that the light moves with the player
        pointLight.setPosition(x+2.5f, y+2.5f);
        pointLight.setDirection(angle);
        
        if(amlight>0.3f)pointLight.setDistance(0);
        else pointLight.setDistance(100);
        if(Gdx.input.isKeyPressed(Keys.L))
			pointLight.setDistance(300);
        
        
        if(Gdx.input.isKeyPressed(Keys.EQUALS))
        	amlight+=0.01f;
        if(Gdx.input.isKeyPressed(Keys.MINUS))
        	amlight-=0.01f;
        
        if(amlight<=0||amlight>=1)
        	night=!night;
        if(night) amlight+=0.0005*delta;
        else amlight-=0.0005*delta;
        rayHandler.setAmbientLight(amlight);
        rayHandler.update();

    }
    public void renderLIGHT(){
        rayHandler.render();
    }

	public void dispose() {
		// TODO Auto-generated method stub
		rayHandler.dispose();
		pointLight.dispose();
		
	}
}
