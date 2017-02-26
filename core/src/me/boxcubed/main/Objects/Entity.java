package me.boxcubed.main.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface Entity {
	  public Vector2 getPos();

	    public void setPos(Vector2 pos);
		public void setSprite(Sprite sprite);
		public Sprite getSprite();

	    public void goUP(int vel);
	    public void goDown(int vel);
	    public void goLeft(int vel);
	    public void goRight(int vel);
	    //public Animation animation();
	    public Sprite sprite();

}
  
