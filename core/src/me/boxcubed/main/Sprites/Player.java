
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

/**
 * Created by Dank Memes on 23/02/2017.
 */
public class Player extends Sprite implements LivingEntity {
	private float delta;
	private static Texture tex = new Texture(Gdx.files.internal("assets/img/player.png"));
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	PolygonShape playerShape;
	public Body playerBody;
	Fixture fixture;
	Vector2 position;// Player position
	PlayerLight playerLight;
	Body body;
	public float rotation=0;
	public Player(World world) {
		super(tex);
		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;

		// Shape
		playerShape = new PolygonShape();
		playerShape.setAsBox(5, 5);
		
		// Fixture def
		fixtureDefPlayer = new FixtureDef();
		fixtureDefPlayer.shape = playerShape;

		fixtureDefPlayer.friction = 0f;
		// Creates the body and assigns vars to all important values
		playerBody = world.createBody(playerDef);
		fixture = playerBody.createFixture(fixtureDefPlayer);
		playerLight = new PlayerLight(world);
		
		playerBody.setTransform(100, 100, 0);

		/*BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(200, 200);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20, 20);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;*/
        playerShape.dispose();
        
	}

	public void render(SpriteBatch sb) {
		sb.draw(this, playerBody.getPosition().x-getWidth()/2,playerBody.getPosition().y-getHeight()/2,10,10,30,30,1,1,rotation);
	}

	@Override
	public Vector2 getPos() {
		return playerBody.getPosition();
	}

	@Override
	public void setPos(Vector2 pos) {

	}

	@Override
	public Sprite getSprite() {
		return this;
	}

	@Override
	public Animation animation() {
		return null;
	}

	
	// Walking
	@Override
	public void goUP() {
		playerBody.applyLinearImpulse(new Vector2(0, 80*delta), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
	}

	@Override
	public void goDOWN() {
		playerBody.applyLinearImpulse(new Vector2(0f, -80f*delta), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(-5f);
	}

	@Override
	public void goLEFT() {
		playerBody.applyLinearImpulse(new Vector2(-80f*delta, 0), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
	}

	@Override
	public void goRIGHT() {
		playerBody.applyLinearImpulse(new Vector2(80*delta, 0), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(-5f);
	}// d
		// Running actions

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

	public void stop() {
		playerBody.setLinearVelocity(0f, 0f);
		playerBody.setAngularVelocity(0);
	}

	
	@Override
	public void update(float delta) {	if(delta<1f)this.delta=1f; else this.delta=delta; }

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return playerBody;
	}
}
