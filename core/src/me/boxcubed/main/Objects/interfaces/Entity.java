package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Disposable;

import me.boxcubed.main.States.GameState;

public interface Entity extends Disposable,Renderable{
	  public default Vector2 getPos(boolean asPixels){
		  if(asPixels)
			  return getBody().getPosition().cpy().scl(GameState.PPM);
		  else return getBody().getPosition().cpy();
	  };
	  public Body getBody();

	  public EntityType getID();
		public Sprite getSprite();
        public void playAnimation(String key);
       
        public Fixture getFixture();
        public boolean isDisposable();
        public void setDisposable(boolean disposable);
        }
  
