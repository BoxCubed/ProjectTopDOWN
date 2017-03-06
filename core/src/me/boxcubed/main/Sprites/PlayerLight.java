package me.boxcubed.main.Sprites;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 3/03/2017.
 */
public class PlayerLight extends GameState{
    public RayHandler rayHandler;
    GameState gameState;
    PointLight pointLight;
    Player player;
    public PlayerLight(World world){
        //LIGHT init
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(2f);
        pointLight = new PointLight(rayHandler, 300, Color.CYAN, 600, 0,0);
    }

    public void updateLightPos(float x, float y){
        //Makes sure that the light moves with the player
        pointLight.setPosition(x+2.5f, y+2.5f);
        rayHandler.update();

    }
    public void renderLIGHT(){
        rayHandler.render();
    }
}
