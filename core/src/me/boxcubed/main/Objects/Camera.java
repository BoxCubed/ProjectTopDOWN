package me.boxcubed.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * @deprecated Not used, could be tho to make GameState neater
 * 
 * 
 */

public class Camera {
    public static OrthographicCamera cam;
    Vector2 camCoords;
    FitViewport viewport;

    public Camera(){
        camCoords = new Vector2(); 
        cam = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);//Cbs working out the ratio of the width:height
    }
    public Vector2 cameraXY(){
        //This returns the co-ordinates of the camera
        return camCoords;
    }
    public void create(SpriteBatch batch){
        batch.setProjectionMatrix(cam.combined);
    }
    public static void update(){
        cam.update();
    }

    public void resized(SpriteBatch batch){//If the window has been resized
        batch.setTransformMatrix(viewport.getCamera().view);
        batch.setProjectionMatrix(viewport.getCamera().projection);
    }
    public void cameraMOVE(int x, int y){//This method can be called anytime the camera needs to be moved
        cam.position.set(x, y, 0);
        cam.update();
    }
}
