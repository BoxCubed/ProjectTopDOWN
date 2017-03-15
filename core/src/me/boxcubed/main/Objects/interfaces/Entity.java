package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface Entity {
	  public Vector2 getPos();
	  public Body getBody();

	  public String getID();
	    public void setPos(Vector2 pos);
		public Sprite getSprite();
        public void playAnimation(String key);
       
        public void update(float delta);
        public void render(SpriteBatch sb);
        public void dispose();
        public Fixture getFixture();
        public boolean isDisposable();
        public void setDisposable(boolean disposable);
        public default void renderShapes(SpriteBatch sb){};
        }
  
