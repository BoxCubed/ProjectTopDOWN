
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
	PolygonShape playerShape;
	public Body playerBody;
	Vector2 position;//Player position
	public Player(World world) {

		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;
		playerDef.position.set(300/GameState.PPM, 400/GameState.PPM);
		//Shape
		playerShape = new PolygonShape();
		playerShape.setAsBox(5, 5);
		//Fixture def
		fixtureDefPlayer = new FixtureDef();
		fixtureDefPlayer.shape = playerShape;
		fixtureDefPlayer.density = 1f;
		fixtureDefPlayer.restitution = 0f;
		fixtureDefPlayer.friction = 2.5f;
		//Creates the body
        playerBody = world.createBody(playerDef);
        playerBody.createFixture(fixtureDefPlayer);
		//world.createBody(playerDef).createFixture(fixtureDefPlayer);

	
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

		playerBody.applyLinearImpulse(new Vector2(-50f, 0),playerBody.getWorldCenter(), true);

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
