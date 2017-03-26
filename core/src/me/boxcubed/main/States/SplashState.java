package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import me.boxcubed.main.TopDown;

public class SplashState implements State {
private float elapsedTime,rotation=0;
private Texture logo;
private Sprite logoSprite;
private final int LOGO_WIDTH=300,LOGO_HEIGHT=300,TIME=3;
private OrthographicCamera camera;
private BitmapFont font;
private SpriteBatch batch=new SpriteBatch();


	@Override
	public void show() {
		Gdx.graphics.setResizable(false);
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(500, 700);
		camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.y=camera.viewportHeight*2;
		camera.update();
		
		font =new BitmapFont();
		logo=new Texture("assets/img/logo.png");
		logoSprite=new Sprite(logo);

	}
	boolean loaded=false;
	private GameState loadingInstance;
	@Override
	public void update(float delta) {
		elapsedTime+=delta;
		rotation-=360f/TIME*delta;
		lerpToPos(0, 0);
		camera.update();
		if(elapsedTime>TIME/2&&!loaded){
			loaded=true;
			loadingInstance=new GameState();
			}
		if(elapsedTime>TIME){
			Gdx.graphics.setUndecorated(false);
			Gdx.graphics.setResizable(true);
			Gdx.graphics.setWindowedMode(1280, 900);
			TopDown.instance.setScreen(new MenuState(loadingInstance));
		}

	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(logoSprite, -LOGO_WIDTH/2, -LOGO_HEIGHT/2, 
				LOGO_WIDTH/2, LOGO_HEIGHT/2, LOGO_WIDTH, LOGO_HEIGHT, 1, 1, rotation);
		font.getData().setScale(1f);
		font.draw(batch, "Project Top Down", -60, camera.viewportHeight/2);
		font.draw(batch, "Brought to you by Box Cubed BITCH", -100, camera.viewportHeight/2-50);
		font.getData().setScale(2f);
		font.draw(batch, "Loading...", -50, -camera.viewportHeight/2+100);

		
		batch.end();
	}




	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();

	}
	private void lerpToPos(float x,float y){
		final float speed=0.1f,ispeed=1.0f-speed;
		Vector3 target = new Vector3(
				(float)x, 
				(float)y, 
				0);
		Vector3 cameraPosition = camera.position;
		cameraPosition.scl(ispeed);
		target.scl(speed);
		cameraPosition.add(target);

		camera.position.set(cameraPosition);
	}

}
