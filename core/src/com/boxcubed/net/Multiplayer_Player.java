package com.boxcubed.net;

import java.lang.reflect.Method;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Objects.interfaces.Movable;
import me.boxcubed.main.States.GameState;

public class Multiplayer_Player implements LivingEntity,Movable{
	public float delta;
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	PolygonShape playerShape;
	public Body playerBody;
	Fixture fixture;
	Vector2 position;// Player position
	Body body;
	World world;
	double health=getMaxHealth();
	public float legOffX=15,legOffY=15;
	boolean shooting=false;
	
	RayCastCallback callback;
	
	int counter=0;
	
	
	public Multiplayer_Player(World world) {
		this.world=world;
		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;
		// Shape
		playerShape = new PolygonShape();
		playerShape.setAsBox(10, 10);
		
		// Fixture def
		fixtureDefPlayer = new FixtureDef();
		fixtureDefPlayer.shape = playerShape;

		fixtureDefPlayer.friction = 0f;
		// Creates the body and assigns vars to all important values
		playerBody = world.createBody(playerDef);
		fixture = playerBody.createFixture(fixtureDefPlayer);
		fixture.setUserData("PLAYER");

		
		playerBody.setTransform(340, 300, 0);

        playerShape.dispose();
		
		legOffY=10;
		legOffX=10;
		
		callback = new RayCastCallback(){
			//TODO Replace collision of Bullet to ray cast since collision detection is unrealiable with transformation
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if(fixture.getUserData()=="WALL"){
				//System.out.println("hit wall");
				return 0;
			}
			
			if(fixture.getUserData()!="WALL"){
				System.out.println("hit zombie");
				fixture.getBody().setTransform(new Vector2(-100,-100), 0);
				return 0;
			}
				return 0;
			}
			
		};
		
	}
	float elapsedTime=0;
	@Override
	public void update(float delta) {
		if(isAlive()){
			if(delta<1f)this.delta=1f; else this.delta=delta; 
			handleInput(); 
			
			
			
			
			
			elapsedTime+=delta;



        }
		else {//TODO Server death handling
			}
		
	
	}
	Vector2 diePos;
	boolean isDisposed= false;
@Override
public void render(SpriteBatch sb) {
//No use since server	
}		
	
	public void handleInput() {
		Input input = Gdx.input;
		
		boolean keyPressed=true;
		
		boolean pressed = shooting;
		if (pressed) {
			
			if(counter<1){
				//world.rayCast(callback, playerBody.getPosition(), new Vector2(GameState.instance.getMouseCords().x,GameState.instance.getMouseCords().y));
				//TODO add bullet 
				pressed=false;}
		   counter++;
		}
		else{counter=0;}
		
		
		if(!keyPressed)stop();
	}
	public float rotation=0;
	public boolean processCommand(String key) {
		//TODO add shooting toggle
		
		/*Method m;
		try {
			// this, my friends, is reflection. Learn it. Its good.
			m = getClass().getMethod(key, (Class<?>[])null);
			m.invoke( this,  (Object[])null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;*/
		byte[] pressed=new byte[6];
		String[] pressedS=key.split(":");
		for(int i=0;i<5;i++)
			pressed[i]=Byte.parseByte(pressedS[i]);
		if(pressed[0]!=0)
			if(pressed[4]!=0)runUP();
			goUP();
		if(pressed[1]!=0)
			if(pressed[4]!=0)runLEFT();
			goLEFT();
		if(pressed[2]!=0)
			if(pressed[4]!=0)runDOWN();
			goDOWN();
		if(pressed[3]!=0)
			if(pressed[4]!=0)runRIGHT();
			goRIGHT();
			
		if(pressed[5]!=0)shooting=true;
		else shooting=false;
		
		rotation=Float.parseFloat(pressedS[6]);
		/*for(String b:pressedS)
			System.out.print(b);*/
		System.out.println(getPos());
		
		
		
		
		
		
		
		return true;

	}
	
	@Override
	public Vector2 getPos() {
		//This shitttt pissed me off soo many times. Whenever i wanted the player pos, this stupid shit kept
		//On giving me the wrong values and for so long i wondered why it never worked properly
		return playerBody.getPosition();
	}

	@Override
	public void setPos(Vector2 pos) {

	}

	@Override
	public Sprite getSprite() {
		//Server side no sprite
		return null;
	}

	

	
	// Walking
	@Override
	public void goUP() {
		playerBody.applyLinearImpulse(new Vector2(0, 80), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
    }

	@Override
	public void goDOWN() {
		playerBody.applyLinearImpulse(new Vector2(0f, -80f), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(-5f);
	}

	@Override
	public void goLEFT() {
		playerBody.applyLinearImpulse(new Vector2(-80f, 0), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
	}

	@Override
	public void goRIGHT() {
		playerBody.applyLinearImpulse(new Vector2(80, 0), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(-5f);
	}// d
		// Running actions

	@Override
	public void runUP() {
		goUP();
	}

	@Override
	public void runDOWN() {
		goDOWN();
	}

	@Override
	public void runLEFT() {
		goLEFT();
	}

	@Override
	public void runRIGHT() {
		goRIGHT();
	}

	public void stop() {
		playerBody.setLinearVelocity(0f, 0f);
		//playerBody.setAngularVelocity(0);
	}

	
	
	@Override
	public Body getBody() {
		return playerBody;
	}

	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(playerBody);
		//GameState.instance.player=new Player(GameState.instance.getWorld());
		//diePos=getBody().getPosition();
	
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
		return 50;
	}
	@Override
	public void playAnimation(String key) {

	}
	@Override
	public EntityType getID() {
		return EntityType.PLAYER;
	}
	@Override
	public void renderShapes(ShapeRenderer sr) {
		
	}
}
