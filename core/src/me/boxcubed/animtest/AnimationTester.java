package me.boxcubed.animtest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationTester extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite sprite;
	Animation<TextureRegion> anim;
	TextureAtlas atlas;
	float elapsedTime = 0f;
	
	public void create(){
		sprite = new Sprite();
		batch = new SpriteBatch();
		atlas = new TextureAtlas(Gdx.files.internal("spritesheets/playersheet.atlas"));
		anim = new Animation<TextureRegion>(1f/30f,atlas.getRegions());
	}
	
	public void dispose(){
		batch.dispose();
		atlas.dispose();
	}
	
	public void render(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.draw(anim.getKeyFrame(elapsedTime,true),0,0);
		batch.end();
	}
	
}
