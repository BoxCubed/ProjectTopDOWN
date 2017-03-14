package me.boxcubed.main.States;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.utils.Hud;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;


import me.boxcubed.main.Objects.Spawner;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.collision.CollisionDetection;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.PlayerLight;

public class GameState implements Screen, InputProcessor {
	public World gameWORLD;

	public OrthographicCamera cam, textCam;

	public List<Entity> entities;
	public List<Entity>dispose;
	public Player player;

	public static GameState instance;

	public SpriteBatch sb;
	public static final int PPM = 200;
	private PlayerLight playerLight;

	public float mouseX, mouseY;
	public SteeringAI playerAI;

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	Box2DDebugRenderer b2dr;
	
	Hud hud;

	Spawner zombieSpawner;
	BitmapFont font = new BitmapFont();

	public boolean noZombie = false;

	public boolean noTime = false;

	@Override
	public void show() {
		// Instance of the game, for ease of access
		instance = this;

		// Camera and Map
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.update();
		tiledMap = new TmxMapLoader().load("assets/maps/map.tmx");

		// World Init
		gameWORLD = new World(new Vector2(0, 0), true);
		gameWORLD.setContactListener(new CollisionDetection());
		
		// HUD initializing
		hud = new Hud();
		hud.update();

		// Box2D Stuff
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		b2dr = new Box2DDebugRenderer();

		// Rendering
		sb = new SpriteBatch();

		// Lists
		entities = new ArrayList<Entity>();
		dispose =new ArrayList<Entity>();

		// Adding player
		player = new Player(gameWORLD);

		zombieSpawner = new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100, 20);

		//Ryan better rename this to Zombie AI
	    playerAI=new SteeringAI(player, player.getWidth());
		playerAI.setBehavior(new LookWhereYouAreGoing<Vector2>(playerAI));
		
		// Apparently the lighting to the whole map, not sure why its player
		// light
		
		playerLight = new PlayerLight(gameWORLD, player.getBody());

		// Making all the collision shapes
		MapBodyBuilder.buildShapes(tiledMap, 1f, gameWORLD);

		// This is a cancer we need
		Gdx.input.setInputProcessor(this);

	}

	public void update(float delta) {
		handleInput();

		//Updating HUD
		hud.update();
		
		//Updating World
		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);

		//Updating player
		player.setPosition(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		player.update(delta);

		//Updating Light
		playerLight.updateLightPos(player.playerBody.getPosition().x, player.playerBody.getPosition().y,
		player.getRotation(), delta);
		playerLight.rayHandler.update();

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

		//List updating
		entities.removeAll(dispose);
		dispose.clear();

		// Keeping camera on player and within range
		cam.position.x = MathUtils.clamp(player.getPos().x, cam.viewportWidth / 2, 1576 - cam.viewportWidth / 2);
		cam.position.y = MathUtils.clamp(player.getPos().y, cam.viewportHeight / 2, 1576 - cam.viewportHeight / 2);
	}

	private void handleInput() {

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
			else
				player = new Player(gameWORLD);
		}
		if (input.isKeyJustPressed(Keys.SPACE)) {
			entities.add(new Bullet(gameWORLD, player.getPos().x, player.getPos().y));
		}

	}

	DecimalFormat format = new DecimalFormat("#.##");

	@Override
	public void render(float delta) {
		update(delta * 100);

		sb.setProjectionMatrix(cam.combined);

		cam.update();
		tiledMapRenderer.setView(cam);
		tiledMapRenderer.render();

		b2dr.render(gameWORLD, cam.combined);
		playerLight.rayHandler.setCombinedMatrix(cam);

		sb.begin();                                                   //-------------------------------------\\
                                                                      //       SEE THIS RENDER METHOD?       \\
		entities.forEach(entity -> entity.render(sb));                //    SEE HOW IT'S CLEAN, AND NOT      \\
                                                                      //   AUSTIC, I'D LIKE TO KEEP IT THAT  \\
		sb.end();                                                     //                 WAY                 \\
		                                                              //-------------------------------------\\                                            
		playerLight.rayHandler.render();
		sb.begin();
		player.render(sb);
		sb.setProjectionMatrix(hud.textCam.combined);
		
		hud.render(sb);
		
		sb.end();

	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		
		Vector2 centerPosition = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

		screenY = Gdx.graphics.getHeight() - screenY;

		Vector2 mouseLoc = new Vector2(screenX, screenY);

		mouseX = mouseLoc.x;
		mouseY = mouseLoc.y;
		
		Vector2 direction = mouseLoc.sub(centerPosition);
		float mouseAngle = direction
				.angle();
		player.setRotation(mouseAngle);

		return true;
	}
	
	public Vector2 getMouseCords(){
		return new Vector2(mouseX, mouseY);
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
		sb.dispose();
	}
	@Override
	public void resize(int width, int height) {
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
