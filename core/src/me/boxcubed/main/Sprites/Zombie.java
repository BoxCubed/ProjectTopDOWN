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
import me.boxcubed.main.States.GameState;

public class Zombie extends Sprite implements LivingEntity {
	public BodyDef Def;
	FixtureDef fixtureDef;
	PolygonShape Shape;
	public Body Body;
	Fixture fixture;
	Vector2 position,vel,target;
	public Zombie(World world) {
		super( new Texture(Gdx.files.internal("assets/img/skeleton-idle_0.png")));
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
		Body.setTransform(100, 100, 0);
	}

	@Override
	public void update(float delta) {
		position=Body.getPosition();
		target=GameState.instance.player.getPos();
		vel=target.cpy().sub(position).nor().scl(100);
		
		float x,y;
		if(target.x>position.x){
			x=position.x+delta*50;}
		else x=position.x-delta*50;
		if(target.y>position.y)
			y=position.y+delta*50;
		else y=position.y-delta*50;
		Body.setLinearVelocity(0,0);
		Body.setTransform(x, y,0);
		//Body.applyLinearImpulse(target, vel, true);
				
	}
	@Override
	public void render(SpriteBatch sb) {
		
		
	}
	@Override
	public Vector2 getPos() {
		return null;
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

	

	@Override
	public void goUP() {

	}

	
	

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

	@Override
	public void goDOWN() {

	}

	@Override
	public void goLEFT() {

	}

	@Override
	public void goRIGHT() {

	}
	

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return Body;
	}
	

}
