package me.boxcubed.main.States;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Sprites.PlayerLight;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.Zombie;

public class GameState implements Screen, InputProcessor {
	public World gameWORLD;
	Box2DDebugRenderer b2dr;
	Camera cam;
	public Player player;
	public static GameState instance;
	List<LivingEntity> entities;
	SpriteBatch sb;
	public static final int PPM = 200;
	private PlayerLight playerLight;
	Zombie zombie;

	public GameState() {
		instance=this;
	}

	public void update(float delta) {
		handleInput();
		cam.update();
		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);
		player.setPosition(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		playerLight.updateLightPos(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		playerLight.rayHandler.update();
		zombie.update(delta);

		System.out.println(player.getPos());
	}
	public World getWorld(){
		return gameWORLD;
	}
	public Matrix4 camCombined(){
		return cam.combined;
	}
	@Override
	public void render(float delta) {
		update(delta);
		sb.begin();
		sb.setProjectionMatrix(cam.combined);
		playerLight.rayHandler.setCombinedMatrix(cam.combined);
		playerLight.rayHandler.render();
		sb.draw(player, player.playerBody.getPosition().x,player.playerBody.getPosition().y);
		//b2dr.render(gameWORLD, cam.combined);
		// Some matrix int he second argument
		sb.end();

	}

	public void handleInput() {
		// Walk controls
		Input input = Gdx.input;
		// And that, is how you actually do this without making a mess. Now
		// which autistic kid decided to name the methods? cbs fixing for now
		// boolean shiftPressed=input.isKeyPressed(Input.Keys.SHIFT_LEFT);

		if (input.isKeyPressed(Input.Keys.UP)) {
			processMovment("UP");
		} //
		if (input.isKeyPressed(Input.Keys.DOWN)) {
			processMovment("DOWN");
		}
		if (input.isKeyPressed(Input.Keys.LEFT)) {
			processMovment("LEFT");
		}
		if (input.isKeyPressed(Input.Keys.RIGHT)) {
			processMovment("RIGHT");

		}

	}

	@Override
	public void show() {

		System.out.println("Init");
		sb = new SpriteBatch();
		entities = new ArrayList<LivingEntity>();
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		gameWORLD = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		player = new Player(gameWORLD);
		zombie=new Zombie(gameWORLD);
		Gdx.input.setInputProcessor(this);
		playerLight = new PlayerLight(gameWORLD);
	}

	@Override
	public void resize(int width, int height) {
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
		dispose();
	}

	@Override
	public void dispose() {
		gameWORLD.dispose();
		b2dr.dispose();
		playerLight.rayHandler.dispose();
		player = null;
		entities.clear();
		entities = null;
		sb.dispose();
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
			m = player.getClass().getMethod(method, null);
			m.invoke(player, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	// BEGIN INPUT
	// DETECTION*****************************************************
	// ***************************************************************
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.ESCAPE:
			TopDown.instance.setScreen(new GameState());
			break;
		default:
			return false;

		}
		return true;
	}

	@Override
	public boolean keyUp(int key) {
		if ((key == Keys.UP || key == Keys.DOWN || key == Keys.LEFT || key == Keys.RIGHT)
				&& (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN)
						&& !Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)))
			player.stop();
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}