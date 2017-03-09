package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.States.GameState;

public class Zombie extends Sprite implements LivingEntity {
	public BodyDef Def;
	FixtureDef fixtureDef;
	CircleShape Shape;
	public Body Body;
	Fixture fixture;
	double health;
	Vector2 position,vel,target;
	SteeringAI ai;
	public Zombie(World world,SteeringAI playerAI) {
		super( new Texture(Gdx.files.internal("assets/img/skeleton-idle_0.png")));
		setSize(50, 50);
		health=100;
		ai=new SteeringAI(this, 50);
		ai.setBehavior(new Arrive<>(ai, playerAI).setArrivalTolerance(2f).setDecelerationRadius(10));
		Def = new BodyDef();
		Def.type = BodyDef.BodyType.DynamicBody;
		Def.position.set(300 / GameState.PPM, 400 / GameState.PPM);

		// Shape
		Shape = new CircleShape();
		Shape.setRadius(5);
		
		// Fixture def
		fixtureDef = new FixtureDef();
		fixtureDef.shape = Shape;

		fixtureDef.friction = 0f;
		// Creates the body and assigns vars to all important values
		Body = world.createBody(Def);
		fixture = Body.createFixture(fixtureDef);
		fixture.setUserData("ZOMBIE");
		Body.setTransform(400, 100, 0);
		Shape.dispose();
		
	}

	@Override
	public void update(float delta) {
		//if(GameState.instance.player.isAlive())
		ai.update(delta);
		
				
	}
	@Override
	public void render(SpriteBatch sb) {
		sb.draw(this, Body.getPosition().x-5, Body.getPosition().y-5, 10, 10, GameState.instance.player.getWidth(), GameState.instance.player.getHeight(), 
				1, 1, (float)Math.toDegrees(Body.getAngle())+90);
		
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
	public Body getBody() {
		// TODO Auto-generated method stub
		return Body;
	}

	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(Body);
		
		
	}

	@Override
	public Fixture getFixture() {
		// TODO Auto-generated method stub
		return fixture;
	}

	@Override
	public double getHealth() {
		// TODO Auto-generated method stub
		return health;
	}

	@Override
	public void setHealth(double health) {
		// TODO Auto-generated method stub
		this.health=health;
	}

	@Override
	public double getMaxHealth() {
		// TODO Auto-generated method stub
		return 100;
	}
	

}
