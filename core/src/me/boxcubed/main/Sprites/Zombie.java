package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.FileAtlas;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
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
	boolean attack;
	float attackTime;
	
	private TextureAtlas zombieWalkAtlas;
	private Animation<TextureRegion> zombieAnim,zombieWalk;
	private float walkTime=0;
	
	public Zombie(World world,SteeringAI playerAI) {
		super( FileAtlas.<Texture>getFile("zombieTex"));
		setSize(50, 50);
		health=100;
		ai=new SteeringAI(this, 50);
		ai.setBehavior(new Seek<>(ai, playerAI));
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
		zombieAnim = FileAtlas.<Animation<TextureRegion>>getFile("zombieAnim");

		 zombieWalkAtlas = new TextureAtlas(Gdx.files.internal("assets/spritesheets/zombie_walk.atlas"));
	        zombieWalk = new Animation<TextureRegion>(1/30f, zombieWalkAtlas.getRegions());
	        
	        //put this in your file atlas cos i sure as hell cbs.
	}

	@Override
	public void update(float delta) {
		if(GameState.instance.player.isAlive()&&isAlive()){
		ai.update(delta);
		if(attack)attackTime+=delta;
		
		}
		else{
			if(isAlive()){
			getBody().setLinearVelocity(0,0);
			getBody().setAngularVelocity(0);
			}
		}
		
		
				
	}
	@Override
	public void render(SpriteBatch sb) {
		 walkTime += Gdx.graphics.getDeltaTime();
		if(!attack){
			sb.draw(zombieWalk.getKeyFrame(walkTime, true), Body.getPosition().x-10, Body.getPosition().y-5, 10, 10, 27, 27, 
					1, 1, (float)Math.toDegrees(Body.getAngle())+90);}
		else {
			sb.draw(zombieAnim.getKeyFrame(attackTime, false), Body.getPosition().x-10, Body.getPosition().y-5, 10, 10, 27, 27, 
					1, 1, (float)Math.toDegrees(Body.getAngle())+90);
			
			if(zombieAnim.isAnimationFinished(attackTime))
			attack=false;	
		
		}
		
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
	public Body getBody() {
		return Body;
	}

	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(Body);
		
		
	}

	@Override
	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth(double health) {
		this.health=health;
	}

	@Override
	public double getMaxHealth() {
		return 100;
	}

	@Override
	public void playAnimation(String key) {
		if(key.toUpperCase().equals("ATTACK")){attack=true;attackTime=0;}
		
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "Zombie";
	}

	@Override
	public void renderShapes(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}

}