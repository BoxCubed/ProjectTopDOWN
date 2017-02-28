
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.States.GameState;

/**
 * Created by Dank Memes on 23/02/2017.
 */
public class Player extends Sprite implements LivingEntity
{
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	PolygonShape playerShape;
	public Body playerBody;
	Fixture fixture;
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
		//Creates the body and assigns vars to all important values
	playerBody =world.createBody(playerDef);
	fixture=playerBody.createFixture(fixtureDefPlayer);
	
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
		playerBody.applyLinearImpulse(new Vector2(0, 50),playerBody.getWorldCenter(), true);

	}
	@Override
	public void goDOWN() {
		playerBody.applyLinearImpulse(new Vector2(0f, -50f),playerBody.getWorldCenter(), true);
	}
	@Override
	public void goLEFT() {
		playerBody.applyLinearImpulse(new Vector2(-50f, 0),playerBody.getWorldCenter(), true);

	}
	@Override
	public void goRIGHT() {
		playerBody.applyLinearImpulse(new Vector2(50, 0),playerBody.getWorldCenter(), true);
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
	public void stop(){
		playerBody.setLinearVelocity(0f, 0f);
		playerBody.setAngularVelocity(0);
	}
}
