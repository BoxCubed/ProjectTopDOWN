package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.net.NetworkManager.ConnectionState;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import com.boxcubed.utils.ParallaxBackground;
import com.boxcubed.utils.ParallaxLayer;
import com.boxcubed.utils.easinglib.Bounce;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
	private Stage stage;
	private TextField ipField, nameField;
	private Button multiButton,singleButton;
	private float alpha = 0;

	SpriteBatch batch;
	private ShapeRenderer fader;
	private OrthographicCamera cam;
	private Viewport port;
	private Sound buttonSound=TopDown.assets.get(Assets.buttonChangeSOUND, Sound.class);
	BitmapFont font;
	GameState loadedInstance;
	GlyphLayout startGlyph, multiGlyph;
	ParallaxBackground bg;
	NetworkManager connection;
	boolean clicked = false;
	float elapsedTime = 0;
	// Thread safe startup...DONE!
	private boolean init = false;

	public MenuState(GameState loadedInstance) {

		this.loadedInstance = loadedInstance;
	}

	private void init(GameState loadedInstance) {
		// Init of debug shape rendrer
		batch = new SpriteBatch();
		fader = new ShapeRenderer(8);
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// init of button

		

		// Stage setup
		port=new ScreenViewport(cam);
		stage = new Stage(port, batch);
		ipField = new TextField("localhost:22222", TopDown.assets.get(Assets.neutSKIN, Skin.class));
		nameField = new TextField("BoxCubed", TopDown.assets.get(Assets.neutSKIN, Skin.class));
		multiButton=new Button(TopDown.assets.get(Assets.starSKIN, Skin.class));
		singleButton=new Button(TopDown.assets.get(Assets.starSKIN, Skin.class));
		
		BoxoUtil.addInputProcessor(stage);
		// ip field
		ipField.setPosition(Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 100);
		ipField.setSize(300, 30);
		stage.addActor(ipField);
		// name field
		nameField.setPosition(Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 150);
		stage.addActor(nameField);
		nameField.setSize(300, 30);
		
		multiButton.setPosition(Gdx.graphics.getWidth()/2-350/2, Gdx.graphics.getHeight()/2+50);
		multiButton.setSize(350, 80);
		multiButton.add("Start Multiplayer");
		stage.addActor(multiButton);
		
		singleButton.setPosition(Gdx.graphics.getWidth()/2-350/2, Gdx.graphics.getHeight()/2-50);
		singleButton.setSize(350, 80);
		singleButton.add("Start Singleplayer");
		
		stage.addActor(singleButton);

		initButtons();
		// Background setup
		TextureRegion bgRegion = new TextureRegion(TopDown.assets.get(Assets.scrollMenuIMAGE, Texture.class));
		bg = new ParallaxBackground(
				new ParallaxLayer[] { new ParallaxLayer(bgRegion, new Vector2(0, 100), new Vector2(), new Vector2()) },
				(float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, new Vector2(0, 1));
		init = true;
	}

	public void handleInput() {

	}

	public void update(float delta) {
		if (!init)
			init(loadedInstance);
		if (alpha < 1f) {
			alpha += 0.005f * delta;
		}
		cam.update();
		if(clicked){
			multiButton.clearChildren();
			multiButton.add(connection.state.toString().toLowerCase().replaceAll("_", " "));
		}
		stage.act();
		elapsedTime += delta;
		if (elapsedTime > 2)
			return;
		singleButton.setPosition(singleButton.getX(), 900 - Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight() / 2 - 50, 2));
		multiButton.setPosition(multiButton.getX(), 900 - Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight() / 2 + 50, 2));

	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		update(delta);
		bg.render(delta);
		fader.setProjectionMatrix(cam.combined);
		fader.begin(ShapeType.Line);
		fader.setColor(0, 0, 0, alpha);
	    fader.rect(0, 0, cam.viewportWidth, cam.viewportHeight);
		fader.end();

		batch.setProjectionMatrix(cam.combined);
		batch.begin();


		batch.end();
		stage.draw();

	}
	
	
	private void initButtons(){
		if(TopDown.debug){
			multiButton.debug();
			singleButton.debug();
		}
		multiButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clicked = true;

				if (connection == null) {
					connection = new NetworkManager(loadedInstance.player, ipField.getText());
					connection.name = nameField.getText();

				} else if (connection.state.equals(ConnectionState.CONNECTING)) {
					return;
				} else if (connection.state.equals(ConnectionState.CONNECTED)) {
					loadedInstance.player.setConnection(connection, 1);
					TopDown.instance.setScreen(loadedInstance);
				}

		else if (connection.state.equals(ConnectionState.INVALID_IP) || connection.state.equals(ConnectionState.DISCONNECTED)) {
					connection = new NetworkManager(loadedInstance.player, ipField.getText());
				}

			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if(fromActor==null){
				buttonSound.play();
				multiButton.setX(multiButton.getX()+10);}
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if(toActor==null)
				multiButton.setX(multiButton.getX()-10);
			}
			
		});
		singleButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				loadedInstance.newPlayer(0);

				TopDown.instance.setScreen(loadedInstance);
			}
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if(fromActor==null)
				buttonSound.play();
				singleButton.setX(singleButton.getX()+5);
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				singleButton.setX(singleButton.getX()-5);
			}
		});
		
	}

	@Override
	public void resize(int width, int height) {
		//init(loadedInstance);
		if(init)
		port.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		if (init)
			BoxoUtil.remInputProcessor(stage);
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		stage.getBatch().dispose();
	}

}
