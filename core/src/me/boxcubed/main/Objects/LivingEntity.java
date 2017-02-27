package me.boxcubed.main.Objects;

import javax.security.sasl.SaslServer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public interface LivingEntity extends Movable {
	  public Vector2 getPos();

	    public void setPos(Vector2 pos);
		public void setSprite(Sprite sprite);
		public Sprite getSprite();
        public Animation animation();
        public Sprite sprite();
        
}
  
