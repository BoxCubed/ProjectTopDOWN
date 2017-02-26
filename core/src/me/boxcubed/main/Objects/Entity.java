package me.boxcubed.main.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface Entity {
	  public Vector2 getPos();

	    public void setPos(Vector2 pos);
		public void setSprite(Sprite sprite);
		public Sprite getSprite();
        public Animation animation();
        public Sprite sprite();
        //Walking
	    public void goUP();
	    public void goDown();
	    public void goLeft();
	    public void goRight();
	    //Running
        public void runUP();
        public void runDOWN();
        public void runLEFT();
        public void runRIGHT();
}
  
