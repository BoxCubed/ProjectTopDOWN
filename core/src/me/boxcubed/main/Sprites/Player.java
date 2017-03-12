
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Objects.interfaces.Movable;
import me.boxcubed.main.States.GameState;

import java.lang.reflect.Method;

/**
 * Created by Dank Memes on 23/02/2017.
 */
public class Player extends Sprite implements LivingEntity,Movable {
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
	ParticleEffect effect;
	double health=getMaxHealth();
	private Animation<TextureRegion> animation,animationLeg;
	private TextureAtlas atlas,atlas2;
	public float rotation=0,legOffX=15,legOffY=15;
	boolean shooting=false;
	public Player(World world) {
		super(tex);
		atlas=new TextureAtlas(Gdx.files.internal("assets/spritesheets/playersheet.atlas"));
		atlas2=new TextureAtlas(Gdx.files.internal("assets/spritesheets/leganim.atlas"));
		animation = new Animation<TextureRegion>(1f/30f*100,atlas.getRegions());
		animationLeg = new Animation<TextureRegion>(1f/30f*100,atlas2.getRegions());
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
		fixture.setUserData("PLAYER");
		playerLight = new PlayerLight(world,playerBody);
		
		playerBody.setTransform(100, 100, 0);

        playerShape.dispose();
        setSize(20, 20);
        effect=new ParticleEffect();
		effect.load(Gdx.files.internal("assets/maps/effects/flame.p"),Gdx.files.internal( "assets/maps/effects/"));
		effect.start();
        
	}
	float elapsedTime=0;
	@Override
	public void update(float delta) {
		if(isAlive()){
			if(delta<1f)this.delta=1f; else this.delta=delta; 
			handleInput(); 
			
			if(shooting){
			effect.setPosition(getX(), getY());
			for(ParticleEmitter emit:effect.getEmitters()){
				emit.getAngle().setHigh(rotation+20);
				emit.getAngle().setLow(rotation-20);
			}
			effect.setDuration(100);
			
			if(effect.isComplete()){effect.reset();effect.start();}
			}else effect.allowCompletion();
			
			effect.update(delta/100);
			
			elapsedTime+=delta;
		
		
		
		
		
		}
		else{
			getBody().setAngularVelocity(0);
			getBody().setLinearVelocity(0, 0);
		}
	
	}
	Vector2 diePos;
	boolean isDisposed= false;
	public void render(SpriteBatch sb) {
		if(isAlive()){
			
			effect.draw(sb);
		if(playerBody.getLinearVelocity().isZero())
		sb.draw(this, playerBody.getPosition().x-getWidth()-2/2,playerBody.getPosition().y-getHeight()/2,15,15,30,30,1,1,rotation);
		else{ 
		sb.draw(animationLeg.getKeyFrame(elapsedTime, true), playerBody.getPosition().x-getWidth()-2/2,playerBody.getPosition().y-getHeight()/2,legOffX,legOffY,18,18,1,1,rotation);
		sb.draw(animation.getKeyFrame(elapsedTime, true), 
				playerBody.getPosition().x-getWidth()-2/2,playerBody.getPosition().y-getHeight()/2
				,15,15,30,30,1,1,rotation);
		}
		}else if(!isDisposed){dispose();isDisposed=true;}
	//finished bullets		
	}
	public void handleInput() {
		//Reflection is a gay piece of shit.
		//At least meake it in the gamestate class
		// Walk controls
		Input input = Gdx.input;
		// and tej was that autistic kid
		boolean keyPressed=false;
		if (input.isKeyPressed(Input.Keys.UP)){
			keyPressed=true;
			legOffY=15;
			processMovment("UP");
			rotation=90;
			}
		if (input.isKeyPressed(Input.Keys.DOWN)){
			keyPressed=true;
			legOffY=15;
			processMovment("DOWN");
			rotation=-90;
			}
		if (input.isKeyPressed(Input.Keys.LEFT)){
			keyPressed=true;
			legOffY=14;
			processMovment("LEFT");
			rotation=-180;
			if(input.isKeyPressed(Keys.DOWN))rotation+=45;
			else if(input.isKeyPressed(Input.Keys.UP))rotation-=45;
			}
		if (input.isKeyPressed(Input.Keys.RIGHT)){
			keyPressed=true;
			legOffY=15;
			processMovment("RIGHT");
			rotation=0;
			if(input.isKeyPressed(Keys.DOWN))rotation-=45;
			else if(input.isKeyPressed(Input.Keys.UP))rotation+=45;
		}
		if(input.isKeyPressed(Keys.SPACE))
			shooting=true;
		else shooting=false;
		if(!keyPressed)stop();
	}
	private boolean processMovment(String key) {
		String method;
		if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT))
			method = "run";
		else
			method = "go";

		method += key;
		Method m;
		try {
			// this, my friends, is reflection. Learn it. Its good.
			m = getClass().getMethod(method, (Class<?>[])null);
			m.invoke( this,  (Object[])null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

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
	public Body getBody() {
		// TODO Auto-generated method stub
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
	@Override
	public void playAnimation(String key) {
		// TODO Auto-generated method stub
		
	}

}
