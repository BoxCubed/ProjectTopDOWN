package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Disposable;

public interface Entity extends Disposable,Renderable{
	  Vector2 getPos(boolean asPixels);
	  Body getBody();

	  EntityType getID();
		Sprite getSprite();
        void playAnimation(String key);
       
        Fixture getFixture();
        boolean isDisposable();
        void setDisposable(boolean disposable);
        }
  
