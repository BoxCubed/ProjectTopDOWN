
package me.boxcubed.main.Sprites;

import java.lang.reflect.Method;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.boxcubed.net.ClientConnection;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Objects.interfaces.Movable;
import me.boxcubed.main.States.GameState;

public class Player extends Sprite implements LivingEntity, Movable {
	public float delta;
	private static Texture tex = TopDown.assets.get(Assets.playerIMAGE, Texture.class);
	//Body stuff
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	PolygonShape playerShape;
	Body playerBody;
	Fixture fixture;
	//end body stuff
	
	ParticleEffect effect;
	public Crosshair crossH;
	double health = getMaxHealth();
	private Animation<TextureRegion> animation, animationLeg;
	// private TextureAtlas atlas,atlas2;
	public float legOffX = 15, legOffY = 15;
	boolean shooting = false;
	GameState gameState;
	public ClientConnection connection;
	public String name = Double.toString(Math.random());
	ParticleEffect bloodEffect = TopDown.assets.get(Assets.bloodEFFECT, ParticleEffect.class);
	// This vector is used for multiplayer positioning so location can be added
	// when world isn't stepping
	public Vector2 multiPos = new Vector2(100, 100);

	public int state;
	public float rotation = 0;
	
	Sound gunshotSound;
	GunType gun;

	/**
	 * Create a new Player
	 * 
	 * @param world
	 *            the world the player should be in
	 * @param state
	 *            The state the player should be in <br>
	 *            0 is local, 1 is player client, 2 is player server<br>
	 *            <br>
	 *            Make sure to supply a connection with
	 *            {@link Player#setConnection(ClientConnection)} if the state
	 *            isn't 0. Do this before calling update.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Player(World world, int state) {
		super(tex);
		this.state = state;
		
		/*
		 * atlas=new TextureAtlas(Gdx.files.internal(
		 * "assets/spritesheets/playersheet.atlas")); atlas2=new
		 * TextureAtlas(Gdx.files.internal("assets/spritesheets/leganim.atlas"))
		 * ;
		 */
		animation = TopDown.assets.get(Assets.playerATLAS + ":anim", Animation.class);
		animationLeg = TopDown.assets.get(Assets.legATLAS + ":anim", Animation.class);
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
		setSize(20, 20);
		crossH = new Crosshair(100, this);
		/* effect=new ParticleEffect(); */
		/*
		 * GameState.instance.effect.load(Gdx.files.internal(
		 * "assets/maps/effects/flame.p"),Gdx.files.internal(
		 * "assets/maps/effects/"));
		 */
		GameState.instance.effect.start();
		/* crossH =new Crosshair(100, this); */

		legOffY = 10;
		legOffX = 10;

		gun=GunType.PISTOL;
		
		

		gunshotSound = TopDown.assets.get(Assets.gunSOUND, Sound.class);
		if (state == 1)
			GameState.instance.connection = new ClientConnection(this);
		if (state == 0)
			GameState.instance.playerAI = new SteeringAI(this, getWidth());
	}

	float elapsedTime = 0;

	public void setConnection(ClientConnection connection, int state) {
		this.connection = connection;
		this.state = state;
	}

	@Override
	public void update(float delta) {
		if (isAlive()) {
			lastPos = playerBody.getPosition().cpy();
			if (delta < 1f)
				this.delta = 1f;
			else
				this.delta = delta;
			handleInput();
			if (shooting) {
				GameState.instance.effect.setPosition(getX(), getY());
				for (ParticleEmitter emit : GameState.instance.effect.getEmitters()) {
					emit.getAngle().setHigh(getRotation() + 20);
					emit.getAngle().setLow(getRotation() - 20);
				}
				GameState.instance.effect.setDuration(100);

				if (GameState.instance.effect.isComplete()) {
					GameState.instance.effect.reset();
					GameState.instance.effect.start();
				}
			} else
				GameState.instance.effect.allowCompletion();
			bloodEffect.update(delta / 100);
			if (hurt) {
				bloodEffect.setPosition(getPos().x, getPos().y);

				if (bloodEffect.isComplete())
					hurt = false;
			}
			GameState.instance.effect.update(delta / 100);

			elapsedTime += delta;

			crossH.update(delta);

		} else {
			getBody().setAngularVelocity(0);
			getBody().setLinearVelocity(0, 0);
		}

	}

	boolean isDisposed = false;

	public void render(SpriteBatch sb) {
		if (isAlive()) {
			if (state == 2 || state == 1)
				playerBody.setTransform(multiPos, 0);
			GameState.instance.effect.draw(sb);
			bloodEffect.draw(sb);
			if (playerBody.getLinearVelocity().isZero())
				sb.draw(this, playerBody.getPosition().x - 15, playerBody.getPosition().y - 15, 15, 15, 40, 40, 1, 1,
						getRotation());
			else {
				sb.draw(animationLeg.getKeyFrame(elapsedTime, true), playerBody.getPosition().x - 10,
						playerBody.getPosition().y - 15, legOffX, legOffY, 24, 24, 1, 1, getRotation());
				sb.draw(animation.getKeyFrame(elapsedTime, true), playerBody.getPosition().x - 15,
						playerBody.getPosition().y - 20, 15, 15, 40, 40, 1, 1, getRotation());
			}
			crossH.render(sb);

		} else if (!isDisposed) {
			dispose();
			isDisposed = true;
		}
		// finished bullets
	}

	public void handleInput() {
		if (state == 2) {
			getBody().setTransform(multiPos, 0);
			setRotation(rotation);
			return;
		}
		if (state == 1 && connection != null) {
			processMovment("UNKNOWN");
			return;
		}
		Input input = Gdx.input;

		boolean keyPressed = false;

		if (gun.equals(GunType.PISTOL)) {
			if(BoxoUtil.isButtonJustPressed(Buttons.LEFT) || input.isKeyJustPressed(Keys.SPACE)){
			gunshotSound.play(1.0f);
			GameState.instance.entities
					.add(new Bullet(GameState.instance.getWorld(), getPos().x, getPos().y, crossH.offX, crossH.offY));
			}
		}
		
		if(gun.equals(GunType.AK47)){
			//TODO ak47
		}

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
		if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT))
			method = "run";
		else
			method = "go";

		method += key;
		if (key=="UNKNOWN") {
			
			connection.w = (byte) (Gdx.input.isKeyPressed(Keys.W) ? 1 : 0);
			connection.a = (byte) (Gdx.input.isKeyPressed(Keys.A) ? 1 : 0);
			connection.s = (byte) (Gdx.input.isKeyPressed(Keys.S) ? 1 : 0);
			connection.d = (byte) (Gdx.input.isKeyPressed(Keys.D) ? 1 : 0);
			connection.shift = (byte) (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 1 : 0);
			connection.space = (byte) (Gdx.input.isKeyPressed(Keys.SPACE) ? 1 : 0);
			connection.rotation = getRotation();
			return true;
		}
		if (state == 2)
			return true;
		Method m;
		try {
			// this, my friends, is reflection. Learn it. Its good.
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
		// This shitttt pissed me off soo many times. Whenever i wanted the
		// player pos, this stupid shit kept
		// On giving me the wrong values and for so long i wondered why it never
		// worked properly

		// For thread safety
		if (state == 1 || state == 2)
			return lastPos;
		else
			return playerBody.getPosition();
	}

	
	@Override
	public Sprite getSprite() {
		return this;
	}

	// Walking
	@Override
	public void goUP() {
		playerBody.applyLinearImpulse(new Vector2(0, 80 * delta), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
	}

	@Override
	public void goDOWN() {
		playerBody.applyLinearImpulse(new Vector2(0f, -80f * delta), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(-5f);
	}

	@Override
	public void goLEFT() {
		playerBody.applyLinearImpulse(new Vector2(-80f * delta, 0), playerBody.getWorldCenter(), true);
		playerBody.setAngularVelocity(5f);
	}

	@Override
	public void goRIGHT() {
		playerBody.applyLinearImpulse(new Vector2(80 * delta, 0), playerBody.getWorldCenter(), true);
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
		// playerBody.setAngularVelocity(0);
	}

	@Override
	public Body getBody() {
		return playerBody;
	}

	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(playerBody);
		// GameState.instance.player=new Player(GameState.instance.getWorld());
		// diePos=getBody().getPosition();

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
		this.health = health;
	}

	@Override
	public double getMaxHealth() {
		return 50;
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
