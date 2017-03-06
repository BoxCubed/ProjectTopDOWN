package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.States.GameState;

public class Zombie extends Sprite implements LivingEntity {
	public BodyDef Def;
	FixtureDef fixtureDef;
	PolygonShape Shape;
	public Body Body;
	Fixture fixture;
	public Zombie(World world) {
		super( new Texture(Gdx.files.internal("assets/img/luigiFront.png")));
		setSize(50, 50);
		Def = new BodyDef();
		Def.type = BodyDef.BodyType.DynamicBody;
		Def.position.set(300 / GameState.PPM, 400 / GameState.PPM);

		// Shape
		Shape = new PolygonShape();
		Shape.setAsBox(5, 5);
		// Fixture def
		fixtureDef = new FixtureDef();
		fixtureDef.shape = Shape;

		fixtureDef.friction = 0f;
		// Creates the body and assigns vars to all important values
		Body = world.createBody(Def);
		fixture = Body.createFixture(fixtureDef);
		//Body.setTransform(-100, -100, 0);
	}
	Vector2 position,vel,target;
	@Override
	public void update(float delta) {
		position=Body.getPosition();
		target=GameState.instance.player.getPos();
		vel=target.cpy().sub(position).nor().scl(100);
		Body.applyLinearImpulse(target, vel, true);		
				
	}
	@Override
	public void render(SpriteBatch sb) {
		
		
	}
	@Override
	public Vector2 getPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPos(Vector2 pos) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public Sprite getSprite() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Animation animation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sprite sprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void goUP() {
		// TODO Auto-generated method stub

	}

	
	

	@Override
	public void runUP() {
		// TODO Auto-generated method stub

	}

	@Override
	public void runDOWN() {
		// TODO Auto-generated method stub

	}

	@Override
	public void runLEFT() {
		// TODO Auto-generated method stub

	}

	@Override
	public void runRIGHT() {
		// TODO Auto-generated method stub

	}

	@Override
	public void goDOWN() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goLEFT() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goRIGHT() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setSprite(Sprite sprite) {
		// TODO Auto-generated method stub
		
	}
	

}
