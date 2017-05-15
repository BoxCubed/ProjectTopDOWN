package me.boxcubed.main.States;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.node_server.server;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import com.boxcubed.utils.CleanInputProcessor;
import com.boxcubed.utils.GIFDecoder;
import com.boxcubed.utils.Hud;
import box2dLight.ConeLight;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.Clock;
import me.boxcubed.main.Objects.PlayerLight;
import me.boxcubed.main.Objects.Spawner;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.collision.CollisionDetection;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Sprites.Pack;
import me.boxcubed.main.Sprites.Pack.PackType;
import me.boxcubed.main.Sprites.Player;
/*import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.LibraryJavaSound;*/

public class GameState implements State, CleanInputProcessor {
	// get rid of that random box that spawns next to the player...DONE i
	// think...
	private World gameWORLD;
	public OrthographicCamera cam;
	private SpriteBatch batch = new SpriteBatch();
	public List<Entity> entities;
	public List<Entity> dispose;
	public Player player;
	public static GameState instance;
	private ShapeRenderer sr;
	public static final int PPM = 20;
	private PlayerLight playerLight;
	private Clock clock;
	// public float mouseX, mouseY;
	public SteeringAI playerAI;
	// Support multiple players: DONE!

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	Box2DDebugRenderer b2dr;
	Music ambientMusic;
	Sound zombieGroan;
	public Hud hud;
	Vector2 mouseLoc;
	Spawner zombieSpawner;
	BitmapFont font = new BitmapFont();
	float groanTimer = 0;
	public boolean noZombie = false;
	public boolean noTime = false;
	server server;
	private HashMap<String, Player> clients = new HashMap<String, Player>();
	private Assets assets = TopDown.assets;
	public Animation<TextureRegion> anim;

	// sound system
	/*
	 * protected SoundSystem soundSys;
	 */ @SuppressWarnings("unchecked")
	public GameState() {

		// Instance of the game, for ease of access
		instance = this;
		// Camera and Map
		tiledMap = assets.get(Assets.MainMAP, TiledMap.class);

		// World Init
		gameWORLD = new World(new Vector2(0, 0), true);
		gameWORLD.setContactListener(new CollisionDetection());
		clock = new Clock(gameWORLD);

		World.setVelocityThreshold(1000);
		// HUD initializing
		hud = new Hud(clock);
		hud.update();

		// Box2D Stuff
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		if (TopDown.debug)
			b2dr = new Box2DDebugRenderer();

		// Rendering
		sr = new ShapeRenderer();

		// Lists
		entities = new ArrayList<Entity>();
		dispose = new ArrayList<Entity>();

		// sound stuff TODO get paulscode 3d sound library
		ambientMusic = assets.get(Assets.ambientMUSIC, Music.class);
		ambientMusic.setLooping(true);
		ambientMusic.setVolume(0.6f);
		ambientMusic.play();
		zombieGroan = assets.get(Assets.ZScreamsSOUND, Sound.class);
		// Adding player
		if (TopDown.debug)
			newPlayer(0);
		zombieSpawner = new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100, 20, clock);
		// light
		playerLight = new PlayerLight(new ConeLight(clock.rayHandler, 1000, Color.YELLOW, 0, 100, 100, 90, 45));
		// Making all the collision shapes
		MapBodyBuilder.buildShapes(tiledMap, PPM, gameWORLD);
		// packs
		entities.add(new Pack(PackType.HEALTH, 250 / PPM, 250 / PPM, gameWORLD));
		anim = GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/health.gif").read());
		// Server stuff
		server = new server();
		/*
		 * try { soundSys=new SoundSystem(LibraryJavaSound.class); } catch
		 * (SoundSystemException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * SoundSystemConfig.setSoundFilesPackage("sounds");
		 * 
		 * soundSys.backgroundMusic("Gun", "gunshot.mp3", true);
		 */

	}

	public void createNewPlayer(String id) {// Used for the server
		Player player1 = new Player(gameWORLD, 2);
		clients.put(id, player1);
		// or it can be clients.put(id, new Player(gameWORLD, 0));
		// both dont work for some reason
	}

	public void moveClients(String id, float x, float y) {
		if (clients.get(id) != null) {
			clients.get(id).multiPos = new Vector2(x, y);
		} else {
			createNewPlayer(id);
		}
	}

	public void update(float delta) {

		// Updating HUD
		hud.update();

		// Updating World
		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);

		// Updating player

		player.update(delta);
		playerAI.update(delta);
		// TODO do this a better way

		clients.forEach((id, player) -> player.update(delta));
		if (player.state != 0)
			player.connection.updatePlayers(delta);

		// Updating Light TODO dont make this only for player aka make a
		// Flashlight class and and handling in gamestate
		playerLight.updateLightPos(player.getPos(false), player.rotation, delta, Gdx.input.isKeyPressed(Keys.L), true);
		clock.updateLight(delta);

		// Update Zombie Spawns
		if (!noZombie) {
			zombieSpawner.update(delta, entities.size());
		}

		// Checking the entity list for disposables and updating
		entities.forEach(entity -> {
			if (entity.isDisposable()) {
				entity.dispose();
				dispose.add(entity);
			} else
				entity.update(delta);
		});
		entities.removeAll(dispose);
		dispose.clear();

		groanTimer += delta;
		if (groanTimer > 2000) {
			zombieGroan.play(0.6f);
			groanTimer = 0;
		}
		if (groanTimer > 800) {
			zombieGroan.stop();
		}

		BoxoUtil.lerpToPos(new Vector2(
				MathUtils.clamp(player.getPos(true).x + player.crossH.offX * 30, cam.viewportWidth / 2,
						1576 - cam.viewportWidth / 2),
				MathUtils.clamp(player.getPos(true).y + player.crossH.offY * 30, cam.viewportHeight / 2,
						1576 - cam.viewportHeight / 2)),
				cam);
		cam.update();
		server.updateServer(delta);
	}

	@Override
	public void handleInput() {

		Input input = Gdx.input;

		if (input.isKeyJustPressed(Input.Keys.Z)) {
			GameState.instance.entities.forEach(entity -> entity.dispose());
			GameState.instance.entities.clear();
			noZombie = !noZombie;
		}

		if (input.isKeyJustPressed(Input.Keys.T)) {
			noTime = !noTime;
			if (noTime) {
				clock.amlight = 2f;
				clock.progressTime = false;
			} else {
				clock.amlight = 1f;
				clock.progressTime = true;
			}
		}

		if (input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		/*
		 * for(HashMap.Entry<String, Player> entry: clients.entrySet()){ }
		 */
		if (input.isKeyJustPressed(Keys.H)) {
			if (player.isAlive())
				player.setHealth(player.getMaxHealth());
			else {
				newPlayer(player.state);
			}
		}

		if (input.isKeyJustPressed(Keys.M)) {
			if (player.connection != null) {
				player.connection.stop = true;
				try {
					player.connection.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			TopDown.instance.setScreen(new MenuState(this));
			return;
		}
	}

	@Override
	public void render() {
		Gdx.gl.glLineWidth(1);
		batch.setProjectionMatrix(cam.combined);
		tiledMapRenderer.setView(cam);
		tiledMapRenderer.render();
		if (b2dr != null)
			b2dr.render(gameWORLD, cam.combined.scl(PPM));

		// Entity render
		batch.begin(); // -------------------------------------\\ // SEE THIS
						// RENDER METHOD? \\
		entities.forEach(entity -> {
			if (!entity.isDisposable())
				entity.render(batch);
		}); // SEE HOW IT'S CLEAN, AND NOT \\
			// AUSTIC, I'D LIKE TO KEEP IT THAT \\
		batch.end(); // WAY \\
		// Light render //-------------------------------------\\
		clock.renderLIGHT(cam);
		// Everything below is rendering not affected by light

		// Shape rendering
		// TODO get a texture for all shapes
		sr.setProjectionMatrix(cam.combined);
		sr.setAutoShapeType(true);
		sr.begin();
		entities.forEach(entity -> entity.renderShapes(sr));
		player.renderShapes(sr);
		hud.render(sr);
		sr.end();

		// rendering of hud and player
		batch.begin();
		for (HashMap.Entry<String, Player> entry : clients.entrySet()) {
			entry.getValue().render(batch);

		}
		if (player.state != 0)
			player.connection.renderPlayers(batch);
		player.render(batch);
		batch.setProjectionMatrix(hud.textCam.combined);

		hud.render(batch);

		batch.end();

	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		screenY = Gdx.graphics.getHeight() - screenY;
		Vector3 mouseCoords = getMouseCords();
		mouseLoc = new Vector2(mouseCoords.x, mouseCoords.y);

		Vector2 direction = mouseLoc.sub(player.getPos(true));
		float mouseAngle = direction.angle();
		player.rotation = mouseAngle;

		return true;
	}

	public Vector3 getMouseCords() {
		return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	public World getWorld() {
		return gameWORLD;
	}

	public void newPlayer(int state) {
		if (player != null) {
			player.dispose();
			if (player.connection != null && player.state == 0) {
				player.connection.stop = true;
				try {
					player.connection.join(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				player.connection = null;

			}
		}
		player = new Player(gameWORLD, state);
		playerAI = new SteeringAI(player, 40);
	}

	@Override
	public void dispose() {
		entities.forEach(entity -> entity.dispose());
		playerLight.dispose();
		player.dispose();
		player = null;
		entities.clear();
		entities = null;
		tiledMap.dispose();
		playerLight.dispose();
		clock.dispose();
		gameWORLD.dispose();
		font.dispose();
		batch.dispose();
		for (HashMap.Entry<String, Player> entry : clients.entrySet()) {
			entry.getValue().dispose();
		}
		/*
		 * if(soundSys!=null) soundSys.cleanup();
		 */
	}

	@Override
	public void resize(int width, int height) {
		// cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2,
		// Gdx.graphics.getHeight() / 2);
		// cam.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		BoxoUtil.remInputProcessor(this);
	}

	@Override
	public void show() {
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.update();
		BoxoUtil.addInputProcessor(this);

	}

}
