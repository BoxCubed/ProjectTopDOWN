package me.boxcubed.main.States;

import box2dLight.RayHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
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
import de.tomgrill.gdxdialogs.core.dialogs.GDXTextPrompt;
import de.tomgrill.gdxdialogs.core.listener.TextPromptListener;
import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
    private final Sound buttonSound = TopDown.assets.get(Assets.buttonChange_SOUND, Sound.class);
    private final Music menuMusic = TopDown.assets.get(Assets.menu_MUSIC, Music.class);
    private final GameState loadedInstance;
    private Stage stage;
    private TextField ipField, nameField;
    private Button multiButton, singleButton;
    private float alpha = -0.1f;
    private SpriteBatch batch;
    private OrthographicCamera cam;
    private Viewport port;
    private ParallaxBackground bg;

    private NetworkManager connection;
    private boolean clicked = false;
    private float elapsedTime = 0;
    // Thread safe startup...DONE!
    private boolean init = false;

    //The only fade effect i know
    private RayHandler fader;
    private World world;

    public MenuState(final GameState loadedInstance) {
        menuMusic.setLooping(true);
        menuMusic.setVolume(0);

        this.loadedInstance = loadedInstance;
    }

    private void init(GameState loadedInstance) {
        // Init of debug shape rendrer
        batch = new SpriteBatch();
        world = new World(new Vector2(0, 0), false);
        fader = new RayHandler(world);
        cam = new OrthographicCamera(800, 400);
        // init of button

        //music

        // Stage setup
        port = new ScreenViewport(cam);
        stage = new Stage(port, batch);
        ipField = new TextField("101.182.234.23:22222", TopDown.assets.get(Assets.neut_SKIN, Skin.class));
        nameField = new TextField("BoxCubed", TopDown.assets.get(Assets.neut_SKIN, Skin.class));
        multiButton = new Button(TopDown.assets.get(Assets.star_SKIN, Skin.class));
        singleButton = new Button(TopDown.assets.get(Assets.star_SKIN, Skin.class));


        // ip field
        ipField.setSize(300, 30);
        ipField.setPosition(Gdx.graphics.getWidth() / 2 - ipField.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 100);

        stage.addActor(ipField);
        // name field
        nameField.setSize(300, 30);
        nameField.setPosition(Gdx.graphics.getWidth() / 2 - nameField.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 150);
        stage.addActor(nameField);

        multiButton.setSize(350, 80);
        multiButton.setPosition(Gdx.graphics.getWidth() / 2 - multiButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 50);

        multiButton.add("Start Multiplayer");
        stage.addActor(multiButton);

        singleButton.setSize(350, 80);
        singleButton.setPosition(Gdx.graphics.getWidth() / 2 - singleButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 50);

        singleButton.add("Start Singleplayer");

        stage.addActor(singleButton);
        BoxoUtil.addInputProcessor(stage);

        initButtons(loadedInstance);
        initFields();
        // Background setup
        TextureRegion bgRegion = new TextureRegion(TopDown.assets.get(Assets.scrollMenu_IMAGE, Texture.class));
        bg = new ParallaxBackground(
                new ParallaxLayer[]{new ParallaxLayer(bgRegion, new Vector2(0, 100), new Vector2(), new Vector2())},
                (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, new Vector2(0, 1));
        init = true;
    }

    private void initFields() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            nameField.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    TopDown.dialogs.newDialog(GDXTextPrompt.class).setTitle("Enter Multiplayer Name").setMessage("Enter a name others will see you by")
                            .setValue(nameField.getText()).setConfirmButtonLabel("Confirm").setTextPromptListener(new TextPromptListener() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void confirm(String text) {
                            nameField.setText(text);
                        }
                    }).build().show();
                }
            });


            ipField.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    TopDown.dialogs.newDialog(GDXTextPrompt.class).setTitle("Enter Server IP").setMessage("Enter the server you wish to connect to")
                            .setValue(ipField.getText()).setConfirmButtonLabel("Confirm").
                            setTextPromptListener(new TextPromptListener() {
                                @Override
                                public void cancel() {

                                }

                                @Override
                                public void confirm(String text) {
                                    ipField.setText(text);
                                }
                            }).build().show();
                }
            });

        }
    }

    public void handleInput() {

    }

    private void update(float delta) {
        if (!init)
            init(loadedInstance);
        if (alpha < 0.9f) {
            alpha += 0.3f * delta;
        }
        menuMusic.setVolume(alpha);
        cam.update();
        if (clicked) {
            multiButton.clearChildren();
            multiButton.add(connection.state.toString().toLowerCase().replaceAll("_", " "));
        }
        stage.act();
        elapsedTime += delta;
        if (elapsedTime > 2) {
        }
        //singleButton.setPosition(singleButton.getX(), 900 - Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight() / 2 - 50, 2));
        //multiButton.setPosition(multiButton.getX(), 900 - Bounce.easeOut(elapsedTime, 0, Gdx.graphics.getHeight() / 2 + 50, 2));

    }

    @Override
    public void show() {
        menuMusic.play();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        update(delta);
        bg.render(delta);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();


        batch.end();
        multiButton.getColor().a = alpha;
        singleButton.getColor().a = alpha;
        stage.draw();

    }


    private void initButtons(final GameState loadedInstance) {
        if (TopDown.debug) {
            multiButton.debug();
            singleButton.debug();
        }
        multiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clicked = true;

                if (connection == null) {
                    connection = new NetworkManager(loadedInstance.player, ipField.getText());
                    connection.name = nameField.getText();

                } else if (connection.state.equals(ConnectionState.CONNECTING)) {
                } else if (connection.state.equals(ConnectionState.CONNECTED)) {
                    loadedInstance.newPlayer(1);
                    loadedInstance.player.setConnection(connection);
                    TopDown.instance.setScreen(loadedInstance);
                } else if (connection.state.equals(ConnectionState.INVALID_IP) || connection.state.equals(ConnectionState.DISCONNECTED)) {
                    connection = new NetworkManager(loadedInstance.player, ipField.getText());
                }

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (fromActor == null) {
                    buttonSound.play();
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }

        });
        singleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuMusic.stop();
                loadedInstance.newPlayer(0);
                TopDown.instance.setScreen(loadedInstance);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (fromActor == null)
                    buttonSound.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
            }
        });

    }

    @Override
    public void resize(int width, int height) {
        //init(loadedInstance);
        if (init)
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
        //menuMusic.stop();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        stage.getBatch().dispose();
        menuMusic.dispose();
        fader.dispose();
        world.dispose();
    }

}
