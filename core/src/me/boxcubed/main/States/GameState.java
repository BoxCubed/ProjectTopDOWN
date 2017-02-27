package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import me.boxcubed.main.Sprites.Player;


public class GameState extends State implements Screen {
    public World gameWORLD;
    Box2DDebugRenderer b2dr;
    Camera cam;
    Player player;
    public static final int PPM = 200;
	public GameState (GameStateManager gsm){
		super(gsm);
		//Basically the create method
        cam = new OrthographicCamera(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        gameWORLD = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        player = new Player(gameWORLD);
	}

	@Override
	protected void handleInput() {//This piece of shit is a mess. Need to find a better way of doing this
		//Walk controls
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
			    player.runUP();
            }else{
                player.goUP();
                System.out.println("HELLO");
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
				player.goDown();
			}else{
				player.runDOWN();
			}
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
				player.goLeft();
			}else{
				player.runLEFT();
			}
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
				player.goRight();
			}else{
				player.runRIGHT();
			}
        }
	}

	@Override
	public void update(float delta) {
	    handleInput();
        cam.update();
        gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);
	}

	@Override
	public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.3f, 0.2f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    b2dr.render(gameWORLD,cam.combined);
    }

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

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