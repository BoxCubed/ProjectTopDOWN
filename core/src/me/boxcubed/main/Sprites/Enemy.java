package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;

/**
 * Created by Tej Sidhu on 27/02/2017.
 * @deprecated Why do we have this?
 */
public class Enemy implements Entity {


   
  

    @Override
    public Sprite getSprite() {
        return null;
    }

   

    
   
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Fixture getFixture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playAnimation(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EntityType getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDisposable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderShapes(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}
}
