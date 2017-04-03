package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import me.boxcubed.main.Objects.StopWatch;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight{
    public RayHandler rayHandler;
    boolean flashlightState=true;
    ConeLight pointLight;
    StopWatch timer;
    Player player;
   private static boolean night=false;
   public static float amlight=1f;
    public PlayerLight(World world,Body bod){
    	timer = new StopWatch();
        //LIGHT init
        /*rayHandler = new RayHandler(world);
        pointLight =new ConeLight(rayHandler, 1000, Color.YELLOW, 0, 100, 100, 90, 45);
     */
        //pointLight.attachToBody(bod);
    }
    
    public void updateLightPos(float x, float y,float angle,float delta){
        //Makes sure that the light moves with the player
    	  GameState.instance.pointLight.setPosition(x+2.5f, y+2.5f);
          GameState.instance.pointLight.setDirection(angle);
          
      if(Gdx.input.isKeyJustPressed(Keys.F)){
    	flashlightState=!flashlightState;
      }
      
    	if(flashlightState){GameState.instance.pointLight.setDistance(100);}else{GameState.instance.pointLight.setDistance(0);}
        if(Gdx.input.isKeyPressed(Keys.L)){
        	if(flashlightState)GameState.instance.pointLight.setDistance(400);}
        else if(flashlightState)GameState.instance.pointLight.setDistance(100);
        
        if(Gdx.input.isKeyPressed(Keys.EQUALS))
        	amlight+=0.01f;
        if(Gdx.input.isKeyPressed(Keys.MINUS))
        	amlight-=0.01f; //jkjk
        
        if(amlight<0.07||amlight>1){
        	night=!night;}
        if(night){timer.start();}
        if(timer.getElapsedTimeSecs()>5){timer.stop();amlight+=0.0005;}
        if(!night&&!timer.isRunning()){amlight-=0.0005*delta;}

        GameState.instance.rayHandler.setAmbientLight(amlight);
        GameState.instance.rayHandler.update();
    }
    public void renderLIGHT(){
        rayHandler.render();
    }

	public void dispose() {
		// TODO Auto-generated method stub
        GameState.instance.rayHandler.dispose();
        GameState.instance.pointLight.dispose();
		
	}
	public static String amToTime(){
		float hrs,mins;
		if(night){
		hrs=(amlight*12);
		mins=hrs*60-(int)hrs*60;
		return (int)hrs+":"+(int)mins+"am";
				}
		else{
			hrs=12-(amlight*12);
			mins=hrs*60-(int)hrs*60;
			return (int)hrs+":"+(int)mins+"pm";
		}
		
		
		//return Float.toString(amlight);
	}
}
