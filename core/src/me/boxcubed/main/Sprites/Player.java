
package me.boxcubed.main.Sprites;

import java.lang.reflect.Method;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.InventorySystem;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Objects.interfaces.Movable;
import me.boxcubed.main.Sprites.guns.AK47;
import me.boxcubed.main.Sprites.guns.Gun;
import me.boxcubed.main.Sprites.guns.Pistol;

public class Player implements LivingEntity, Movable {
	private Sprite sprite,rifleSprite;
	public float delta;
	private Texture tex = TopDown.assets.get(Assets.playerIMAGE, Texture.class);
	private Texture playerRifle = TopDown.assets.get(Assets.playerRIFLE,Texture.class);
	//Body stuff
	public BodyDef playerDef;
	private FixtureDef fixtureDefPlayer;
	private PolygonShape playerShape;
	private Body playerBody;
	private Fixture fixture;
	//end body stuff
	float elapsedTime = 0;
	public Crosshair crossH;
	
	double health = getMaxHealth();
	float stamina = getMaxStamina();//Why are you linking this to a method?
	
	private Animation<TextureRegion> animation, animationLeg, rifleAnimation;
	// private TextureAtlas atlas,atlas2;
	public float legOffX = 15, legOffY = 15;
	private boolean shooting = false;
	public NetworkManager connection;
	public GlyphLayout name = new GlyphLayout();
	private ParticleEffect bloodEffect = new ParticleEffect(TopDown.assets.get(Assets.bloodEFFECT, ParticleEffect.class));
	// This vector is used for multiplayer positioning so location can be added
	// when world isn't stepping
	public Vector2 multiPos = new Vector2(100, 100);
	private boolean initsed=false;
	private ParticleEffect effect=TopDown.assets.get(Assets.flameEFFECT, ParticleEffect.class);
	public int state;
	public float rotation = 0;
	
	private World world;
	Sound gunshotSound;
	Gun gun;
	AK47 ak= new AK47();
	Pistol pistol = new Pistol();
	public InventorySystem inventory;
	public Player(World world, int state) {
		if(state==1||state==2){
			this.world=world;
			this.state=state;
			return;
			}
		name=new GlyphLayout();
		init(world, state);
		
	}
	@SuppressWarnings("unchecked")
	private void init(World world,int state){
		sprite=new Sprite(tex);
		rifleSprite=new Sprite(playerRifle);
		this.state = state;
		this.world=world;
		
		animation = TopDown.assets.get(Assets.playerATLAS + ":anim", Animation.class);
		animationLeg = TopDown.assets.get(Assets.legATLAS + ":anim", Animation.class);
		rifleAnimation = TopDown.assets.get(Assets.rifleWALK+":anim",Animation.class);
		
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
		
		sprite.setSize(20, 20);
		crossH = new Crosshair(100, this);
		
		effect.start();

		legOffY = 10;
		legOffX = 10;

		gun=new AK47();
		//Inventory system
		inventory = new InventorySystem();
		//adding items here, for now
		//will create a method later to add items on events eg. walking over gun or heath pack
		inventory.addItem("pisstol", 1, pistol);
		inventory.addItem("ak47", 1, ak);
		inventory.listItems();

		gunshotSound = TopDown.assets.get(Assets.gunSOUND, Sound.class);
		if (state == 1)
			connection = new NetworkManager(this);
		
		initsed=true;
	}



	public void setConnection(NetworkManager connection, int state) {
		this.connection = connection;
		this.state = state;
	}

	@Override
	public void update(float delta) {
		if(!initsed){init(world, state);}
		if (isAlive()) {
			lastPos = playerBody.getPosition().cpy();
			if (delta < 1f)
				this.delta = 1f;
			else
				this.delta = delta;
			handleInput();
			if(state==2)playerBody.setLinearVelocity(0, 0);
			if (shooting&&state==0) {
				effect.setPosition(getPos().x, getPos().y);
				for (ParticleEmitter emit : effect.getEmitters()) {
					emit.getAngle().setHigh(rotation + 20);
					emit.getAngle().setLow(rotation - 20);
				}
				effect.setDuration(100);

				if (effect.isComplete()) {
					effect.reset();
					effect.start();
				}
			} else
				effect.allowCompletion();
			
			
			if (hurt) {
				bloodEffect.setPosition(getPos().x, getPos().y);
				bloodEffect.update(delta / 100);

				if (bloodEffect.isComplete())
					hurt = false;
			}
			effect.update(delta / 100);

			elapsedTime += delta;
		
			crossH.update(delta);
			sprite.setRotation(rotation);

		} else {
			getBody().setAngularVelocity(0);
			getBody().setLinearVelocity(0, 0);
		}

	}

	boolean isDisposed = false;
	@Override
	public void render(SpriteBatch sb) {
		//Need some sort of reflection method or something to draw the inventory items from here.
		
		if(!initsed)init(world, state);
		if (isAlive()) {
			if (state == 2 || state == 1)
				if(multiPos!=null){
				playerBody.setTransform(playerBody.getTransform().getPosition().lerp(multiPos, 0.5f), 0);
				multiPos=null;
				}
			//drawing of effects
			effect.draw(sb);
			bloodEffect.draw(sb);
			if (!keyPressed)
				sb.draw(rifleSprite, playerBody.getPosition().x - 15, playerBody.getPosition().y - 15, 15, 15, 40, 40, 1, 1,
						rotation);
			else {
				sb.draw(animationLeg.getKeyFrame(elapsedTime, true), playerBody.getPosition().x - 10,
						playerBody.getPosition().y - 15, legOffX, legOffY, 24, 24, 1, 1, rotation);
				sb.draw(rifleAnimation.getKeyFrame(elapsedTime,true), playerBody.getPosition().x-15, playerBody.getPosition().y-20,
						15, 15, 40, 40, 1, 1, rotation);
			/*	sb.draw(animation.getKeyFrame(elapsedTime, true), playerBody.getPosition().x - 15,
						playerBody.getPosition().y - 20, 15, 15, 40, 40, 1, 1, rotation);*/
			}
			
			crossH.render(sb);

		} else if (!isDisposed) {
			dispose();
			isDisposed = true;
		}
	}
	boolean keyPressed = false;
	public void handleInput() {
		
		if(state==2)return;
		if(state==1){processMovment("UNKNOWN");}
		Input input = Gdx.input;
		keyPressed=false;
		if(stamina<getMaxStamina()&&!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){stamina+=delta/4;}

		if(gun.willFire(input, delta, this)){
			
			gun.fire(world, this);
			if(state==1)
			gun.netFire(connection, world, this);
			}
		if(state==1)return;
		if (input.isKeyPressed(Keys.W) || input.isKeyPressed(Keys.UP)) {
			keyPressed = true;
			processMovment("UP");
		}
		if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Keys.DOWN)) {
			keyPressed = true;
			processMovment("DOWN");
		}
		if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Keys.LEFT)) {
			keyPressed = true;
			processMovment("LEFT");
		}
		if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Keys.RIGHT)) {
			keyPressed = true;
			processMovment("RIGHT");
		}
		if (!keyPressed)
			stop();
		if (input.isKeyPressed(Keys.NUM_0))
			shooting = true;
		else
			shooting = false;

	}

	private boolean processMovment(String key) {

		String method;
		
		
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){	
			if(stamina>0){stamina-=delta/4;elapsedTime+=1;
			method = "run";}
			else{method="go";}
			}
		
		else
			method = "go";

		method += key;
		if (key=="UNKNOWN") {
			
			connection.move.w = (byte) (Gdx.input.isKeyPressed(Keys.W) ? 1 : 0);
			connection.move.s = (byte) (Gdx.input.isKeyPressed(Keys.S) ? 1 : 0);
			connection.move.a = (byte) (Gdx.input.isKeyPressed(Keys.A) ? 1 : 0);
			
			connection.move.d = (byte) (Gdx.input.isKeyPressed(Keys.D) ? 1 : 0);
			connection.move.shift = (byte) (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 1 : 0);
			connection.move.space = (byte) (Gdx.input.isKeyPressed(Keys.SPACE) ? 1 : 0);
			connection.move.rotation = rotation;
			return true;
		}
		if (state == 2||state==1)
			return true;
		Method m;
		try {
			m = getClass().getMethod(method, (Class<?>[]) null);
			m.invoke(this, (Object[]) null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	Vector2 lastPos = new Vector2();

	@Override
	public Vector2 getPos() {
		// For thread safety
		if (state == 1 || state == 2)
			return lastPos;
		else
			return playerBody.getPosition();
	}

	
	@Override
	public Sprite getSprite() {
		return sprite;
	}

	// Walking
	@Override
	public void goUP() {
		if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)||stamina<=0){
			playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y+=1.2f, playerBody.getAngle())
			;}
		}
	

	@Override
	public void goDOWN() {
		if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)||stamina<=0){
	playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y-=1.2f, playerBody.getAngle())
	;}
	}

	@Override
	public void goLEFT() {
		if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)||stamina<=0){
			playerBody.setTransform(playerBody.getPosition().x-=1.2f,playerBody.getPosition().y, playerBody.getAngle())
			;}
	}

	@Override
	public void goRIGHT() {if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)||stamina<=0){
		playerBody.setTransform(playerBody.getPosition().x+=1.2f,playerBody.getPosition().y, playerBody.getAngle())
		;}
	}// d
		// Running actions

	@Override
	public void runUP() {
		playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y+=1.7f, playerBody.getAngle())
		;
	}

	@Override
	public void runDOWN() {
		playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y-=1.7f, playerBody.getAngle())
		;
	}

	@Override
	public void runLEFT() {
		playerBody.setTransform(playerBody.getPosition().x-=1.7,playerBody.getPosition().y, playerBody.getAngle())
		;
	}

	@Override
	public void runRIGHT() {
		playerBody.setTransform(playerBody.getPosition().x+=1.7,playerBody.getPosition().y, playerBody.getAngle())
		;
	}

	
	public void stop() {
		playerBody.setLinearVelocity(0f, 0f);
		// playerBody.setAngularVelocity(0);
	}

	@Override
	public Body getBody() {
		return playerBody;
	}

	@Override
	public void dispose() {
		world.destroyBody(playerBody);
		bloodEffect.dispose();

	}

	@Override
	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public double getHealth() {
		return health;
	}
	public float getStamina(){
		return stamina;
	}
	
	public void setStamina(float stamina){
		this.stamina=stamina;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	@Override
	public double getMaxHealth() {
		return 50;
	}
	
	public float getMaxStamina(){
		return 100;
	}

	boolean hurt = false;

	@Override
	public void playAnimation(String key) {
		if (key.equals("attacked")) {

			hurt = true;
			bloodEffect.reset();
			bloodEffect.start();
		}
	}

	@Override
	public EntityType getID() {
		return EntityType.PLAYER;
	}

	@Override
	public void renderShapes(ShapeRenderer sr) {

	}

}
