package me.boxcubed.main.States;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.Input;import com.badlogic.gdx.Input.Keys;import com.badlogic.gdx.audio.Music;import com.badlogic.gdx.audio.Sound;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.OrthographicCamera;import com.badlogic.gdx.graphics.g2d.Animation;import com.badlogic.gdx.graphics.g2d.BitmapFont;import com.badlogic.gdx.graphics.g2d.SpriteBatch;import com.badlogic.gdx.graphics.g2d.TextureRegion;import com.badlogic.gdx.graphics.glutils.ShapeRenderer;import com.badlogic.gdx.maps.tiled.TiledMap;import com.badlogic.gdx.math.MathUtils;import com.badlogic.gdx.math.Vector2;import com.badlogic.gdx.math.Vector3;import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;import com.badlogic.gdx.physics.box2d.World;import com.boxcubed.events.DoorTouchEvent;import com.boxcubed.events.EventHandler;import com.boxcubed.events.EventListener;import com.boxcubed.events.EventMethod;import com.boxcubed.events.NightEvent;import com.boxcubed.events.ZombieSpawnEvent;import com.boxcubed.node_server.server;import com.boxcubed.utils.Assets;import com.boxcubed.utils.BoxoUtil;import com.boxcubed.utils.CleanInputProcessor;import com.boxcubed.utils.GIFDecoder;import com.boxcubed.utils.Hud;import box2dLight.ConeLight;import me.boxcubed.main.TopDown;import me.boxcubed.main.Objects.Clock;import me.boxcubed.main.Objects.MapHandler;import me.boxcubed.main.Objects.PlayerLight;import me.boxcubed.main.Objects.Spawner;import me.boxcubed.main.Objects.SteeringAI;import me.boxcubed.main.Objects.collision.CollisionDetection;import me.boxcubed.main.Objects.interfaces.Entity;import me.boxcubed.main.Objects.interfaces.EntityType;import me.boxcubed.main.Sprites.Pack;import me.boxcubed.main.Sprites.Pack.PackType;import me.boxcubed.main.Sprites.Player;public class GameState implements State, CleanInputProcessor, EventListener {	private World gameWORLD;	private OrthographicCamera cam;	private SpriteBatch batch = new SpriteBatch();	public List<Entity> entities;	private List<Entity> dispose;	public Player player;	public static GameState instance;	private ShapeRenderer sr;	public static final int PPM = 20;	private PlayerLight playerLight;	public Clock clock;	public SteeringAI playerAI;	//maps	private TiledMap tiledMap;	MapHandler mapHandler;	Box2DDebugRenderer b2dr;	EventHandler eventHandler;	Music ambientMusic;	Sound zombieGroan;	public Hud hud;	Vector2 mouseLoc;	Spawner zombieSpawner;	BitmapFont font = new BitmapFont();	float groanTimer = 0;	public boolean noZombie = false;	public boolean noTime = false;	server server;	private HashMap<String, Player> clients = new HashMap<String, Player>();	private Assets assets = TopDown.assets;	public Animation<TextureRegion> anim;	public GameState() {		// Instance of the game, for ease of access		instance = this;		// Camera and Map		loadWorld();		// Events		loadHud_Events();		loadFramework();		loadMap();	}	private void loadMap() {		mapHandler=new MapHandler(gameWORLD, cam, batch);		tiledMap = assets.get(Assets.Main_MAP, TiledMap.class);				mapHandler.addMap(tiledMap);		mapHandler.addMap(assets.get(Assets.Test_MAP, TiledMap.class));		mapHandler.switchMaps(0);			}	private void loadFramework() {		// Rendering		sr = new ShapeRenderer();		// Lists		entities = new ArrayList<Entity>();		dispose = new ArrayList<Entity>();		// sound stuff TODO get paulscode 3d sound library		ambientMusic = assets.get(Assets.ambient_MUSIC, Music.class);		ambientMusic.setLooping(true);		ambientMusic.setVolume(0.6f);		zombieGroan = assets.get(Assets.ZScreams_SOUND, Sound.class);		// Adding player		if (TopDown.debug)			newPlayer(0);		zombieSpawner = new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100, 20);		// light		playerLight = new PlayerLight(new ConeLight(clock.rayHandler, 100, Color.YELLOW, 0, 100, 100, 90, 45));		// packs		entities.add(new Pack(PackType.HEALTH, 250 / PPM, 250 / PPM, gameWORLD));		anim = GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/health.gif").read());		// Server stuff		server = new server();	}	private void loadHud_Events() {		EventHandler.registerListener(this);		// HUD initializing		hud = new Hud(clock);		hud.update();	}	private void loadWorld() {				gameWORLD = new World(new Vector2(0, 0), true);				gameWORLD.setContactListener(new CollisionDetection());		clock = new Clock(gameWORLD);		World.setVelocityThreshold(20f);		// Box2D Stuff		if (TopDown.debug)			b2dr = new Box2DDebugRenderer();	}	@EventMethod	public void onNight(NightEvent e) {		System.out.println("It is night! Time is :" + e.getTriggerTime());	}	@EventMethod	public void onSpawn(ZombieSpawnEvent e) {		System.out.println("Zombie spawned!");	}	@EventMethod	public void onDoor(DoorTouchEvent e){		System.out.println("Touch door on map "+e.getId()+" at pos "+e.getPosition());		if(e.getId()==0&&e.getMapObject().getName().equals("mainDoor"))			mapHandler.switchMaps(1);		else if(e.getMapObject().getName().equals("subDoor"))mapHandler.switchMaps(0);	}	public void createNewPlayer(String id) {// Used for the server		Player player1 = new Player(gameWORLD, 2);		clients.put(id, player1);		// or it can be clients.put(id, new Player(gameWORLD, 0));		// both dont work for some reason	}	public void moveClients(String id, float x, float y) {		if (clients.get(id) != null) {			clients.get(id).multiPos = new Vector2(x, y);		} else {			createNewPlayer(id);		}	}	public void update(float delta) {		// Updating HUD		hud.update();		// Updating World		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);		mapHandler.update(delta, cam);		// Updating player		player.update(delta);		playerAI.update(delta);		clients.forEach((id, player) -> player.update(delta));		if (player.state != 0)			player.connection.updatePlayers(delta);		// Updating Light TODO dont make this only for player aka make a		// Flashlight class and and handling in gamestate		playerLight.updateLightPos(player.getPos(false), player.rotation, delta, Gdx.input.isKeyPressed(Keys.L), true);		clock.updateLight(delta);		// Update Zombie Spawns		if (!noZombie) {			zombieSpawner.update(delta, entities.size());		}		// Checking the entity list for disposables and updating		entities.forEach(entity -> {			if (entity.isDisposable()) {				entity.dispose();				dispose.add(entity);			} else				entity.update(delta);		});		entities.removeAll(dispose);		dispose.clear();		groanTimer += delta;		if (groanTimer > 2000) {			zombieGroan.play(0.6f);			groanTimer = 0;		}		if (groanTimer > 800) {			zombieGroan.stop();		}		BoxoUtil.lerpToPos(new Vector2(				MathUtils.clamp(player.getPos(true).x + player.crossH.offX * 30, cam.viewportWidth / 2,						1576 - cam.viewportWidth / 2),				MathUtils.clamp(player.getPos(true).y + player.crossH.offY * 30, cam.viewportHeight / 2,						1576 - cam.viewportHeight / 2)),				cam);		BoxoUtil.updateShake(Gdx.graphics.getDeltaTime(), cam);		cam.update();		server.updateServer(delta);	}	@Override	public void handleInput() {		Input input = Gdx.input;		if (input.isKeyJustPressed(Input.Keys.Z)) {			GameState.instance.entities.forEach(entity -> entity.dispose());			GameState.instance.entities.clear();			noZombie = !noZombie;		}		if (input.isKeyJustPressed(Input.Keys.T)) {			noTime = !noTime;			if (noTime) {				clock.amlight = 2f;				clock.progressTime = false;			} else {				clock.amlight = 1f;				clock.progressTime = true;			}		}		if (input.isKeyPressed(Input.Keys.ESCAPE)) {			Gdx.app.exit();		}		/*		 * 		 * for(HashMap.Entry<String, Player> entry: clients.entrySet()){ }		 * 		 */		if (input.isKeyJustPressed(Keys.H)) {			if (player.isAlive())				player.setHealth(player.getMaxHealth());			else {				newPlayer(player.state);			}		}		if (input.isKeyJustPressed(Keys.M)) {			if (player.connection != null) {				player.connection.stop = true;				try {					player.connection.join();				} catch (InterruptedException e) {					e.printStackTrace();				}			}			TopDown.instance.setScreen(new MenuState(this));			return;		}	}	@Override	public void render() {		batch.setProjectionMatrix(cam.combined);				mapHandler.render();		// Entity render		batch.begin();				entities.forEach(entity -> {			if (!entity.isDisposable())				entity.render(batch);		});		batch.end();		// Light render		clock.renderLIGHT(cam);		// Everything below is rendering not affected by light		if (b2dr != null)			b2dr.render(gameWORLD, cam.combined.cpy().scl(PPM));		// Shape rendering		// TODO get a texture for all shapes		sr.setProjectionMatrix(cam.combined);		sr.setAutoShapeType(true);		sr.begin();		entities.forEach(entity -> entity.renderShapes(sr));		player.renderShapes(sr);		hud.render(sr);		sr.end();		// rendering of hud and player		batch.begin();		for (HashMap.Entry<String, Player> entry : clients.entrySet()) {			entry.getValue().render(batch);		}		if (player.state != 0)			player.connection.renderPlayers(batch);		player.render(batch);		hud.render(batch);		batch.end();	}	@Override	public boolean mouseMoved(int screenX, int screenY) {		screenY = Gdx.graphics.getHeight() - screenY;		Vector3 mouseCoords = getMouseCords();		mouseLoc = new Vector2(mouseCoords.x, mouseCoords.y);		Vector2 direction = mouseLoc.sub(player.getPos(true));		float mouseAngle = direction.angle();		player.rotation = mouseAngle;		return true;	}	public Vector3 getMouseCords() {		return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));	}	public World getWorld() {		return gameWORLD;	}	public void newPlayer(int state) {		if (player != null) {			player.dispose();			if (player.connection != null && player.state == 0) {				player.connection.stop = true;				try {					player.connection.join(2000);				} catch (InterruptedException e) {					// TODO Auto-generated catch block					e.printStackTrace();				}				player.connection = null;			}		}		player = new Player(gameWORLD, state);		playerAI = new SteeringAI(player, 40);	}	@Override	public void dispose() {		entities.forEach(entity -> entity.dispose());		playerLight.dispose();		player.dispose();		player = null;		entities.clear();		entities = null;		tiledMap.dispose();		playerLight.dispose();		clock.dispose();		gameWORLD.dispose();		font.dispose();		batch.dispose();		for (HashMap.Entry<String, Player> entry : clients.entrySet()) {			entry.getValue().dispose();		}		zombieGroan.dispose();		ambientMusic.dispose();		/*		 * 		 * if(soundSys!=null) soundSys.cleanup();		 * 		 */	}	@Override	public void resize(int width, int height) {		// cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2,		// Gdx.graphics.getHeight() / 2);		// cam.update();	}	@Override	public void pause() {	}	@Override	public void resume() {	}	@Override	public void hide() {		BoxoUtil.remInputProcessor(this);		ambientMusic.stop();	}	@Override	public void show() {		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);		cam.update();		BoxoUtil.addInputProcessor(this);		ambientMusic.play();	}}