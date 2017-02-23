package me.boxcubed.main.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.print.StreamPrintService;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class Camera {
    private static OrthographicCamera cam;
    Vector2 camCoords;
    FitViewport viewport;
    public Camera(){
        camCoords = new Vector2();
        cam = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//Cbs working out the ratio of the width:height
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
}
