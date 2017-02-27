
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.Entity;
import me.boxcubed.main.States.GameState;

/**
 * Created by Dank Memes on 23/02/2017.
 */
public class Player extends Sprite implements Entity
{
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	CircleShape playerShape;
	public Body playerBody;
	Vector2 position;//Player position
	public Player(World world) {
		 BodyDef bdef = new BodyDef();
	        bdef.position.set((Gdx.graphics.getWidth()/2)/GameState.PPM,(Gdx.graphics.getHeight()/2)/GameState.PPM);
	        bdef.type=BodyDef.BodyType.DynamicBody;
	        playerBody = world.createBody(bdef);

	        FixtureDef fdef = new FixtureDef();
	        PolygonShape shape1 = new PolygonShape();

	        shape1.setAsBox(5/GameState.PPM,5/GameState.PPM);

	        fdef.shape=shape1;
	        playerBody.createFixture(fdef);
	
	}
	public void render(SpriteBatch sb){
		//we do
	}
	@Override
	public Vector2 getPos() {
		return position;
	}

	@Override
	public void setPos(Vector2 pos) {

	}

	@Override
	public void setSprite(Sprite sprite) {

	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public Animation animation() {
		return null;
	}

	@Override
	public Sprite sprite() {
		return null;
	}
	//Walking
	@Override
	public void goUP() {

	}
	@Override
	public void goDown() {

	}
	@Override
	public void goLeft() {
		playerBody.applyLinearImpulse(new Vector2(-3f, 0),playerBody.getWorldCenter(), true);
	}
	@Override
	public void goRight() {
		playerBody.applyLinearImpulse(new Vector2(0.3f, 0),playerBody.getWorldCenter(), true);
	}
	//Running actions
	@Override
	public void runUP() {

	}

	@Override
	public void runDOWN() {

	}

	@Override
	public void runLEFT() {

	}

	@Override
	public void runRIGHT() {

	}
}
