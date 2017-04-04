package me.boxcubed.main.States;

import java.text.DecimalFormat;
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
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.ClientConnection;
import com.boxcubed.node_server.server;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.CleanInputProcessor;
import com.boxcubed.utils.GIFDecoder;
import com.boxcubed.utils.Hud;
import box2dLight.ConeLight;
import box2dLight.RayHandler;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.Spawner;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.collision.CollisionDetection;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Sprites.Crosshair;
import me.boxcubed.main.Sprites.Pack;
import me.boxcubed.main.Sprites.Pack.PackType;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.PlayerLight;

public class GameState implements State, CleanInputProcessor{
	public World gameWORLD;
	public OrthographicCamera cam;
	private SpriteBatch batch=new SpriteBatch();
	public List<Entity> entities;
	public List<Entity>dispose;
	public Player player;
	public boolean debug=true;
	public static GameState instance;
	private ShapeRenderer sr;
	public static final int PPM = 200;
	private PlayerLight playerLight;
	//public float mouseX, mouseY;
	public SteeringAI playerAI;
	//Support multiple players: DONE!
	public List<Player> multiplayerPlayers;
	public int playerAddQueue;
	public int playerRemQueue;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	Box2DDebugRenderer b2dr;
	Music ambientMusic;
	Sound zombieGroan;
	Crosshair crosshair;
	Hud hud;
	public ClientConnection connection;
	Vector2 mouseLoc;
	Spawner zombieSpawner;
	BitmapFont font = new BitmapFont();
	float groanTimer=0;
	public boolean noZombie = false;
	public boolean noTime = false;
    com.boxcubed.node_server.server server;
    private HashMap<String, Player> clients;
    public RayHandler rayHandler;
    public ConeLight pointLight;
    public ParticleEffect effect;
    public Crosshair crossH;
    private Assets assets=TopDown.assets;
    public Animation<TextureRegion> anim;
	@SuppressWarnings("unchecked")
	public GameState() {

        // Instance of the game, for ease of access
				instance = this;
				crosshair = new Crosshair(10, player);
				// Camera and Map
				
				tiledMap = assets.get(Assets.MainMAP,TiledMap.class);

				// World Init
				gameWORLD = new World(new Vector2(0, 0), true);
				gameWORLD.setContactListener(new CollisionDetection());
				
				// HUD initializing
				hud = new Hud();
				hud.update();

				// Box2D Stuff
				tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
				if(TopDown.debug)
				b2dr = new Box2DDebugRenderer();

				// Rendering
				sr=new ShapeRenderer();
				
				// Lists
				entities = new ArrayList<Entity>();
				dispose =new ArrayList<Entity>();

        clients = new HashMap<String, Player>();
        //Sorry if anything is fucked up
        rayHandler = new RayHandler(gameWORLD);
        pointLight = new ConeLight(rayHandler, 1000, Color.YELLOW, 0, 100, 100, 90, 45);
        effect=assets.get(Assets.flameEFFECT, ParticleEffect.class);

        ambientMusic =assets.get(Assets.ambientMUSIC, Music.class);
		ambientMusic.setLooping(true);
		ambientMusic.setVolume(0.6f);
		ambientMusic.play();
		zombieGroan = assets.get(Assets.ZScreamsSOUND, Sound.class);
		// Adding player
        player = new Player(gameWORLD,0); //1 means multiplayer
        crossH =new Crosshair(100, player);
		//connection=new ClientConnection(player);
		//This is for multiplayer ^^^
		multiplayerPlayers=new ArrayList<>();
		zombieSpawner = new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100, 20);
		//Ryan better rename this to Zombie AI
		playerAI=new SteeringAI(player, player.getWidth());
		//	playerAI.setBehavior(new ReachOrientation<>(playerAI, new MouseLocaion()).setEnabled(true).setAlignTolerance(5).setDecelerationRadius(10));
		// Apparently the lighting to the whole map, not sure why its player
		// light
		playerLight = new PlayerLight(gameWORLD, player.getBody());
		// Making all the collision shapes
		MapBodyBuilder.buildShapes(tiledMap, 1f, gameWORLD);
		//packs
		entities.add(new Pack(PackType.HEALTH, player.getPos().x-50, player.getPos().y-50, gameWORLD));
		//Server stuff
        server = new server();
        anim= GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/health.gif").read());
        

    }
	public void createNewPlayer(String id){//Used for the server
        clients.put(id, new Player(gameWORLD, 0));
        /*playerAI = new SteeringAI(clients.get(id),clients.get(id).getWidth());
        playerLight = new PlayerLight(gameWORLD, clients.get(id).getBody());
        entities.add(new Pack(PackType.HEALTH, clients.get(id).getPos().x-50, clients.get(id).getPos().y-50, gameWORLD));*/
	}

	public void update(float delta) {

		//Updating HUD
		hud.update();
		
		//Updating World
		gameWORLD.step(delta/100, 8, 2);

		//Updating player
		
		player.setPosition(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		player.update(delta);
		playerAI.update(delta);
		if(playerAddQueue!=0){
			
				multiplayerPlayers.add(new Player(gameWORLD, 2));playerAddQueue=0;}
			
		if(playerRemQueue!=0){
			multiplayerPlayers.get(0).dispose();
			multiplayerPlayers.remove(0);
		playerRemQueue=0;}

		multiplayerPlayers.iterator().forEachRemaining(player->player.update(delta));

		//Updating Light
		playerLight.updateLightPos(player.playerBody.getPosition().x, player.playerBody.getPosition().y,
		player.getRotation(), delta);
		rayHandler.update();
		
		//Update Zombie Spawns
		if (!noZombie) {
			zombieSpawner.update(delta);
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
				
		groanTimer+=delta;
		if(groanTimer>2000){
			zombieGroan.play(0.6f);
			groanTimer=0;
		}
		if(groanTimer>800){
			zombieGroan.stop();
		}

		//List updating
		
		
		lerpToPos(MathUtils.clamp(player.getPos().x, cam.viewportWidth / 2, 1576 - cam.viewportWidth / 2), 
				 MathUtils.clamp(player.getPos().y, cam.viewportHeight / 2, 1576 - cam.viewportHeight / 2));
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
			if (noTime) {PlayerLight.amlight = 2f;} 
			else {
				PlayerLight.amlight = 1f;
			}
		}

		if (input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (input.isKeyJustPressed(Keys.H)) {
			if (player.isAlive())
				player.setHealth(player.getMaxHealth());
			else{
				player = new Player(gameWORLD,0);
				playerAI=new SteeringAI(player, player.getWidth());
			//	playerAI.setBehavior(new ReachOrientation<>(playerAI, new MouseLocaion()).setEnabled(true).setAlignTolerance(5).setDecelerationRadius(10));
			}
		}
		
		if (input.isKeyJustPressed(Keys.M)) {
			TopDown.instance.setScreen(new MenuState(this));
		}
		//mouseMoved(Gdx.input.getX(), Gdx.input.getY()); 
	}

	DecimalFormat format = new DecimalFormat("#.##");

	@Override
	public void render() {

		batch.setProjectionMatrix(cam.combined);
		cam.update();
		tiledMapRenderer.setView(cam);
		tiledMapRenderer.render();
		if(b2dr!=null)
		b2dr.render(gameWORLD, cam.combined);
		rayHandler.setCombinedMatrix(cam);
		//Entity render
		batch.begin();                                                   //-------------------------------------\\                                               //       SEE THIS RENDER METHOD?       \\
		entities.forEach(entity -> {
			if(!entity.isDisposable())
				entity.render(batch);
		});                                                           //    SEE HOW IT'S CLEAN, AND NOT      \\
                                                                      //   AUSTIC, I'D LIKE TO KEEP IT THAT  \\
		batch.end();                                                     //                 WAY                 \\
		//Light render                                                //-------------------------------------\\     
		rayHandler.render();
		
		//rendering not affected by light

		//Shape rendering
		//TODO get a texture for all shapes
		sr.setProjectionMatrix(camCombined());
		sr.setAutoShapeType(true);
		sr.begin();
		entities.forEach(entity->entity.renderShapes(sr));
		player.renderShapes(sr);
		sr.end();
		
		//rendering of hud and player
		batch.begin();
        for(HashMap.Entry<String, Player> entry: clients.entrySet()){
            entry.getValue().render(batch);
            entry.getValue().renderShapes(sr);
            entry.getValue().update(Gdx.graphics.getDeltaTime());
        }
		player.render(batch);
		multiplayerPlayers.iterator().forEachRemaining(player->player.render(batch));
		batch.setProjectionMatrix(hud.textCam.combined);
		
		hud.render(batch);

		batch.end();

	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		

		screenY = Gdx.graphics.getHeight() - screenY;

		mouseLoc = new Vector2(getMouseCords().x, getMouseCords().y);

		
		Vector2 direction = mouseLoc.sub(player.getPos());
		float mouseAngle = direction
				.angle();
		player.setRotation(mouseAngle);

		return true;
	}
	
	
	public Vector3 getMouseCords(){
		return cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}

	public World getWorld() {
		return gameWORLD;
	}

	public Matrix4 camCombined() {
		return cam.combined;
	}

	@Override
	public void dispose() {
		entities.forEach(entity -> entity.dispose());
		playerLight.dispose();
		player = null;
		entities.clear();
		entities = null;
		tiledMap.dispose();
		playerLight.dispose();
		gameWORLD.dispose();
		font.dispose();
		batch.dispose();
		
		//GameState.instance.dispose();
		//GameState.instance.dispose();
	}
	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		// dispose();
	}

    private void lerpToPos(float x,float y){
		final float speed=0.1f,ispeed=1.0f-speed;
		Vector3 target = new Vector3(
				(float)x, 
				(float)y, 
				0);
		Vector3 cameraPosition = cam.position;
		cameraPosition.scl(ispeed);
		target.scl(speed);
		cameraPosition.add(target);

		cam.position.set(cameraPosition);
	}
   
    @Override
	public void show() {
    	cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.update();
		Gdx.input.setInputProcessor(this);

	}
	

	

}
