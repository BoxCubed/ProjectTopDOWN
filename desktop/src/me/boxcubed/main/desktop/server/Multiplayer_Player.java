package me.boxcubed.main.desktop.server;

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
import com.boxcubed.net.packets.InputPacket;

import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Objects.interfaces.Movable;
public class Multiplayer_Player implements LivingEntity,Movable{
	public float delta;
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	PolygonShape playerShape;
	public Body playerBody;
	Fixture fixture;
	Vector2 position;// Player position
	Body body;
	public InputPacket command;
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
		playerShape.setAsBox(0.5f, 0.5f);
		command=new InputPacket();
		
		// Fixture def
		fixtureDefPlayer = new FixtureDef();
		fixtureDefPlayer.shape = playerShape;

		fixtureDefPlayer.friction = 0f;
		// Creates the body and assigns vars to all important values
		playerBody = world.createBody(playerDef);
		fixture = playerBody.createFixture(fixtureDefPlayer);
		fixture.setUserData("PLAYER");

		
		playerBody.setTransform(340f/20f, 300f/20f, 0);

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
		
		processCommand(command);
		if (stamina < 100 && !shift) {
			stamina += delta / 4;
		}
		boolean pressed = shooting;
		if (pressed) {
			
			if(counter<1){
				//world.rayCast(callback, playerBody.getPosition(), new Vector2(GameState.instance.getMouseCords().x,GameState.instance.getMouseCords().y));
				//TODO add bullet 
				pressed=false;}
		   counter++;
		}
		else{counter=0;}
		
		
	}
	public float rotation=0;
	private float velX,velY,stamina=100;
	boolean shift=false;
	public boolean processCommand(InputPacket key) {
		//TODO add shooting toggle
		if(key==null)return false;
		boolean pressed=false;
		shift=key.shift!=0;
		if (shift) {

			if (stamina > 0) {
				stamina -= delta / 4;
				elapsedTime += 1;

			}else shift=false;
			}
		if(key.w!=0){
			pressed=true;
			if(shift)
				runUP();
			else goUP();
				}
		if(key.a!=0){
			pressed=true;
			if(shift)
				runLEFT();
			else goLEFT();
				}
		if(key.s!=0){
			pressed=true;
			if(shift)
				runDOWN();
			else goDOWN();
				}
		if(key.d!=0){
			pressed=true;
			if(shift)
				runRIGHT();
			else goRIGHT();
				}
		
		
		if(key.space!=0)
			shooting=true;
		else shooting=false;
		rotation=key.rotation;
		
		
		playerBody.setLinearVelocity(velX, velY);
		if(!pressed)stop();
		
		return true;

	}
	

	
	@Override
	public Sprite getSprite() {
		//Server side no sprite
		return null;
	}

	

	
	// Walking
	@Override

	public void goUP() {

		if (!shift || stamina <= 0) {

			velY = 5f;

		}

	}

	@Override

	public void goDOWN() {

		if (!shift || stamina <= 0) {

			velY = -5f;

		}

	}

	@Override

	public void goLEFT() {

		if (!shift || stamina <= 0) {

			velX = -5f;

		}

	}

	@Override

	public void goRIGHT() {

		if (!shift || stamina <= 0) {

			velX = 5f;

		}

	}// d

	// Running actions

	@Override

	public void runUP() {

		velY = 10f;

	}

	@Override

	public void runDOWN() {

		velY = -10f;

	}

	@Override

	public void runLEFT() {

		velX = -10f;

	}

	@Override

	public void runRIGHT() {

		velX = 10f;

	}

	public void stop() {
		playerBody.setLinearVelocity(0f, 0f);
		playerBody.setAngularVelocity(0);
	}

	
	
	@Override
	public Body getBody() {
		return playerBody;
	}
	@Override
	public Vector2 getPos(boolean asPixels) {
		return playerBody.getPosition();
	}

	@Override
	public void dispose() {
		//TODO
		world.destroyBody(getBody());
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
