package me.boxcubed.main.States;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.Sprites.Player;


public class GameState implements Screen {
    public World gameWORLD;
    Box2DDebugRenderer b2dr;
    Camera cam;
    Player player;
    List<LivingEntity>entities;
    public static final int PPM = 200;
    
	public GameState (){
		//Basically the create method
		entities=new ArrayList<LivingEntity>();
        cam = new OrthographicCamera(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        cam = new OrthographicCamera((Gdx.graphics.getWidth()/2),(Gdx.graphics.getHeight()/2));
        gameWORLD = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        player = new Player(gameWORLD);
        
	}

	public void handleInput() {
		//Walk controls
		Input input=Gdx.input;
	//And that, is how you actually do this without making a mess. Now which autistic kid decided to name the methods? cbs fixing for now
		boolean shiftPressed=input.isKeyPressed(Input.Keys.SHIFT_LEFT);
		if(input.isKeyPressed(Input.Keys.UP)){
			if(shiftPressed)
			player.runUP();
			else player.goUP();
		}//
		if(input.isKeyPressed(Input.Keys.DOWN)){
			if(shiftPressed)
				player.runDOWN();
			else player.goDown();
		}
		if(input.isKeyPressed(Input.Keys.LEFT)){
			if(shiftPressed)
				player.runLEFT();
			else player.goLeft();
		}
		if(input.isKeyPressed(Input.Keys.RIGHT)){
			if(shiftPressed)
				player.runRIGHT();
			else player.goRight();

		}

	}

	public void update(float delta) {
		  handleInput();
		cam.update();
        gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);

	}

	@Override
	public void render(float delta) {
		System.out.println("RENDER");
		update(delta);
        b2dr.render(gameWORLD,cam.combined);//Some matrix int he second argument
    }

	@Override
	public void show() {

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

	}

	@Override
	public void dispose() {

	}
}