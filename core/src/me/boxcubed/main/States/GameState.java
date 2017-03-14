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

	Spawner zombieSpawner;
	BitmapFont font = new BitmapFont();

	boolean noZombie = false, noTime = false;

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
		textCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		textCam.update();

		// Box2D Stuff
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		b2dr = new Box2DDebugRenderer();

		// Rendering
		sb = new SpriteBatch();

		// Lists
		entities = new ArrayList<Entity>();
		dispose =new ArrayList<Entity>();

		// Initialize Map
		initMap();

		// Adding player
		player = new Player(gameWORLD);

		zombieSpawner = new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100, 20);

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
		textCam.update();
		
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
			// Creates new bullet
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

		String health = "";
		int i;

		for (i = 0; i < player.getHealth() / player.getMaxHealth() * 100f; i++) {
			health += "|";
		}
		for (; i < 100; i++)
			health += "-";

		b2dr.render(gameWORLD, cam.combined);
		playerLight.rayHandler.setCombinedMatrix(cam);

		sb.begin();

		entities.forEach(entity -> entity.render(sb));

		sb.end();
		playerLight.rayHandler.render();
		sb.begin();
		player.render(sb);
		// UGLY SHIT
		sb.setProjectionMatrix(textCam.combined);
		font.draw(sb, "Delta: " + format.format(delta * 100), -230, textCam.viewportHeight / 2);
		font.draw(sb, "Entity Number: " + entities.size(), -380, textCam.viewportHeight / 2);
		font.draw(sb,
				"Time: " + /* format.format((PlayerLight.amlight*100)/8) */PlayerLight.amToTime(), -120,
				textCam.viewportHeight / 2);
		font.draw(sb, "noZombieMode: " + noZombie, 0, textCam.viewportHeight / 2);
		font.draw(sb, "noTimeMode: " + noTime, 150, textCam.viewportHeight / 2);
		font.draw(sb, "Player Position: " + format.format(player.getBody().getPosition().x) + ","
				+ format.format(player.getBody().getPosition().y), -600, textCam.viewportHeight / 2);
		font.draw(sb, health, -200, textCam.viewportHeight / 2 - 50);

		sb.end();

	}

	public World getWorld() {
		return gameWORLD;
	}

	public Matrix4 camCombined() {
		return cam.combined;
	}

	float rotation = 0;

	private void initMap() {

		// MapCollision map = new MapCollision(tm,gameWORLD);
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
	public boolean mouseMoved(int screenX, int screenY) {

		Vector2 centerPosition = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
		// Vector2 centerPosition = new Vector2(getX(), getY());

		screenY = Gdx.graphics.getHeight() - screenY; // Inverse the Y

		Vector2 mouseLoc = new Vector2(screenX, screenY);

		Vector2 direction = mouseLoc.sub(centerPosition);
		float mouseAngle = direction
				.angle();/* (float)(Math. atan2(direction.y, direction.x)); */
		player.setRotation(mouseAngle);

		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
