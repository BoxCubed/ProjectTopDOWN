package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import me.boxcubed.main.States.GameState;

public interface Entity {
	  public default Vector2 getPos(boolean asPixels){
		  if(asPixels)
			  return getBody().getPosition().scl(GameState.PPM);
		  else return getBody().getPosition();
	  };
	  public Body getBody();

	  public EntityType getID();
		public Sprite getSprite();
        public void playAnimation(String key);
       
        public void update(float delta);
        public void render(SpriteBatch sb);
        public void dispose();
        public Fixture getFixture();
        public boolean isDisposable();
        public void setDisposable(boolean disposable);
        public void renderShapes(ShapeRenderer sr);
		Vector2 getPos();
        }
  
