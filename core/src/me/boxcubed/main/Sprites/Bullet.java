package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import me.boxcubed.main.Objects.interfaces.Entity;

/**
 * 
 * @author
 *
 */
public class Bullet implements Entity{
	Animation<TextureRegion> sexwithryansdad;
	public Boolean remove;
	public Bullet(){

	}

	@Override
	public Vector2 getPos() {
		return null;
	}

	@Override
	public Body getBody() {
		return null;
	}

	@Override
	public void setPos(Vector2 pos) {
		
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	
	@Override
	public void update(float delta) {
        remove = true;
	}

	@Override
	public void render(SpriteBatch sb) {
        System.out.println("Hello");
    }

	@Override
	public void dispose() {

	}

	@Override
	public Fixture getFixture() {
		return null;
	}

	@Override
	public void playAnimation(String key) {

	}

}
