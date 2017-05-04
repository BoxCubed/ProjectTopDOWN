package me.boxcubed.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

public class Clock {
	//TODO make new class for just time handling
    public RayHandler rayHandler;
    boolean flashlightState=true;
    private StopWatch timer;
    public boolean progressTime;
   private boolean night=false;
   public float amlight=1f;
    public Clock(World world){
    	timer = new StopWatch();
    	rayHandler=new RayHandler(world);
    	progressTime=true;
        //LIGHT init
        /*rayHandler = new RayHandler(world);
        pointLight =new ConeLight(rayHandler, 1000, Color.YELLOW, 0, 100, 100, 90, 45);
     */
        //pointLight.attachToBody(bod);
    }
    
    public void updateLight(float delta){
        
        if(Gdx.input.isKeyPressed(Keys.EQUALS))
        	amlight+=0.01f;
        if(Gdx.input.isKeyPressed(Keys.MINUS))
        	amlight-=0.01f; //jkjk
        if(!progressTime)return;
        if(amlight<0.07||amlight>1){
        	night=!night;}
        if(night){timer.start();}
        if(timer.getElapsedTimeSecs()>5){timer.stop();amlight+=0.0005;}
        if(!night&&!timer.isRunning()){amlight-=0.0005*delta;}

        rayHandler.setAmbientLight(amlight);
        rayHandler.update();
    }
    public void renderLIGHT(OrthographicCamera matrix){
    	rayHandler.setCombinedMatrix(matrix);
        rayHandler.render();
        
    }

	public void dispose() {
		rayHandler.dispose();
	}
	public String amToTime(){
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
