package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
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
	
	private Animation<TextureRegion> zombieAnim,zombieWalk;
	private float walkTime=0;
	
	boolean idle;
	
	Vector2 p1,p2,collision,normal;
	
	RayCastCallback callback;
	
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
		Shape.setRadius(10);
		
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
	    zombieWalk = FileAtlas.<Animation<TextureRegion>>getFile("zombieWalkAnim");
	        
	        //put this in your file atlas cos i sure as hell cbs.
	        
		callback = new RayCastCallback(){

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getUserData().equals("ZOMBIE")||fixture.getUserData().equals("PLAYER")){
					
				}else{
						getBody().setLinearVelocity(getBody().getLinearVelocity().x-=Gdx.graphics.getDeltaTime(),getBody().getLinearVelocity().y-=Gdx.graphics.getDeltaTime());
						getBody().setAngularVelocity(0);
					
				
				idle=true;
				}
				
				return 1;
			}
			
		};
	
	}

	@Override
	public void update(float delta) {
		idle=false;
		
		p1 = new Vector2(Body.getPosition().x,Body.getPosition().y);
		p2=new Vector2(GameState.instance.player.getX(),GameState.instance.player.getY());
		
		GameState.instance.gameWORLD.rayCast(callback, p1, p2);
		
		
		if(GameState.instance.player.isAlive()&&isAlive()){
			if(!idle){
		ai.update(delta);
		if(attack)attackTime+=delta;
			}
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

		 if(!idle){
				if(!attack){
					sb.draw(zombieWalk.getKeyFrame(walkTime, true), Body.getPosition().x-25, Body.getPosition().y-20, getWidth()/2, getHeight()/2 , 40, 40, 
							1, 1, (float)Math.toDegrees(Body.getAngle())+90);}
				else {
					sb.draw(zombieAnim.getKeyFrame(attackTime, false), Body.getPosition().x-25, Body.getPosition().y-20, getWidth()/2, getHeight()/2, 40, 40, 
							1, 1, (float)Math.toDegrees(Body.getAngle())+90);
					
					if(zombieAnim.isAnimationFinished(attackTime))
					attack=false;	 
				
				}
		 }else{
			 sb.draw(zombieWalk.getKeyFrame(walkTime, true), Body.getPosition().x-25, Body.getPosition().y-20, getWidth()/2, getHeight()/2, 40, 40, 
						1, 1, (float)Math.toDegrees(Body.getAngle())+90);
		 }
	
	}
	boolean rayEnabled;
	
	@Override
	public void renderShapes(ShapeRenderer sr) {
		
		if(Gdx.input.isKeyJustPressed(Keys.P)){
			rayEnabled=!rayEnabled;
		}
		if(rayEnabled){
			sr.setColor(Color.RED);
			sr.line(p1, p2);
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

}