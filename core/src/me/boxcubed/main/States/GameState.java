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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.Sprites.Player;


public class GameState implements Screen,InputProcessor {
    public World gameWORLD;
    Box2DDebugRenderer b2dr;
    Camera cam;
    Player player;
    List<LivingEntity>entities;
    public static final int PPM = 200;
    
	
	public void update(float delta) {
		handleInput();
		cam.update();
        gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);

	}

	@Override
	public void render(float delta) {
		update(delta);
        b2dr.render(gameWORLD,cam.combined);//Some matrix int he second argument
    }

	
	public void handleInput() {
		//Walk controls
		Input input=Gdx.input;
	//And that, is how you actually do this without making a mess. Now which autistic kid decided to name the methods? cbs fixing for now
		//boolean shiftPressed=input.isKeyPressed(Input.Keys.SHIFT_LEFT);
		
		if(input.isKeyPressed(Input.Keys.UP)){
			processMovment("UP");
		}//
		if(input.isKeyPressed(Input.Keys.DOWN)){
			processMovment("DOWN");
		}
		if(input.isKeyPressed(Input.Keys.LEFT)){
			processMovment("LEFT");
		}
		if(input.isKeyPressed(Input.Keys.RIGHT)){
			processMovment("RIGHT");

		}

	}
	@Override
	public void show() {
		
		System.out.println("Init");
		entities=new ArrayList<LivingEntity>();
        cam = new OrthographicCamera(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        cam = new OrthographicCamera((Gdx.graphics.getWidth()/2),(Gdx.graphics.getHeight()/2));
        gameWORLD = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        player = new Player(gameWORLD);
        Gdx.input.setInputProcessor(this);
       
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
		player=null;
		entities.clear();
		entities=null;
	}
	private boolean processMovment(String key){
		String method;
		if(Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT))method="run";
		else method="go";
		
		method+=key;
		Method m;
		try {
			//this, my friends, is reflection. Learn it. Its good.
			m=player.getClass().getMethod(method, null);
			m.invoke(player, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
		
	
	}
//BEGIN INPUT DETECTION*****************************************************
	//***************************************************************
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		
		
		switch(keycode){
		case Keys.ESCAPE:
		TopDown.instance.setScreen(new GameState());break;
		default: return false;
		
		}
		return true;
	}

	@Override
	public boolean keyUp(int key) {
		// TODO Auto-generated method stub
		if((key==Keys.UP||key==Keys.DOWN||key==Keys.LEFT||key==Keys.RIGHT)
				&&(!Gdx.input.isKeyPressed(Keys.UP)&&!Gdx.input.isKeyPressed(Keys.DOWN)&&!Gdx.input.isKeyPressed(Keys.LEFT)&&!Gdx.input.isKeyPressed(Keys.RIGHT)))
		player.stop();
		return true;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	


}