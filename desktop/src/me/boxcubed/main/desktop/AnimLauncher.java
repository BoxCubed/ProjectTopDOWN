package me.boxcubed.main.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimLauncher implements ApplicationListener {
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime = 0;
    
    @Override
    public void create() {        
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("assets/spritesheets/zombie_walk.atlas"));
        animation = new Animation<TextureRegion>(1/30f, textureAtlas.getRegions());
    }

    

    @Override
    public void render() {   
        elapsedTime += Gdx.graphics.getDeltaTime();
        
        System.out.println(elapsedTime);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();

        batch.draw(animation.getKeyFrame(elapsedTime, true), 0, 0);
        batch.end();
    }
    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
    
    public static void main(String[] args){
    	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new AnimLauncher(), config);
		config.resizable = true;
		config.height = 900;
		config.width = 1280;
    }
}