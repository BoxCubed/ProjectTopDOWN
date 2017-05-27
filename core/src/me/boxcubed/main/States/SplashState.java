package me.boxcubed.main.States;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;

import me.boxcubed.main.TopDown;

public class SplashState extends State {
private float rotation=0;
    private Sprite logoSprite;
    private OrthographicCamera camera;
private BitmapFont font;
private final SpriteBatch batch=new SpriteBatch();
private Assets assets;

	@Override
	public void show() {
		if(Gdx.app.getType().equals(Application.ApplicationType.Android)){
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}else{
		Gdx.graphics.setResizable(false);
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(500, 700);}
		camera=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.y=camera.viewportHeight*2;
		camera.update();
		assets=TopDown.assets;
		font=new BitmapFont();
		assets.finishLoadingAsset(Assets.logo_IMAGE);
        Texture logo = assets.get(Assets.logo_IMAGE, Texture.class);
		logoSprite=new Sprite(logo);
		

	}
	private boolean loaded=false;
	private GameState loadingInstance;
	@Override
	public void update(float delta) {
		//Adjusts the rotation of the image based on the amount loaded
		rotation=-assets.getProgress()*360f+360;
		//lerps camera to the middle, giving a cool intro effect
		BoxoUtil.lerpToPos(new Vector2(0, 0), camera);
		//as usual
		camera.update();
		assets.update();
		//gamestate is loaded when assets have loaded 
		if(assets.getProgress()>=1f&&!loaded){
			//boolean to make sure it isnt done more than once, not really needed
			loaded=true;
			loadingInstance=new GameState();
			}
		if(assets.getProgress()>=1f){
			//resume window to normal size
			if(Gdx.app.getType().equals(Application.ApplicationType.Android))
				Gdx.graphics.setWindowedMode(1280, 900);
			else{
			Gdx.graphics.setUndecorated(false);
			Gdx.graphics.setResizable(true);
			Gdx.graphics.setWindowedMode(1280, 900);}
			//set screen to menu passing the loaded game to jump straight in
			TopDown.instance.setScreen(new MenuState(loadingInstance));
		}

	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
        int LOGO_HEIGHT = 300;
        int LOGO_WIDTH = 300;
        batch.draw(logoSprite, -LOGO_WIDTH /2, -LOGO_HEIGHT /2,
				LOGO_WIDTH /2, LOGO_HEIGHT /2, LOGO_WIDTH, LOGO_HEIGHT, 1, 1, rotation);
		font.getData().setScale(1f);
		font.draw(batch, "Project Top Down", -60, camera.viewportHeight/2);
		font.draw(batch, "Brought to you by Box Cubed", -100, camera.viewportHeight/2-50);
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
	

}
