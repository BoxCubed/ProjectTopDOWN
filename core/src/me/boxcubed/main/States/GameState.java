package me.boxcubed.main.States;

import box2dLight.ConeLight;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import com.boxcubed.utils.GIFDecoder;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;
import me.boxcubed.main.Objects.*;
import me.boxcubed.main.Objects.collision.CollisionDetection;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Sprites.Pack;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.TopDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GameState extends State implements InputProcessor {

    public static final int PPM = 20;
    public static GameState instance;
    public final Clock clock;
    public final Hud hud;
    public final Animation<TextureRegion> anim;
    private final World gameWORLD;
    private final SpriteBatch batch = new SpriteBatch();
    private final List<Entity> dispose;
    private final ShapeRenderer sr;
    private final PlayerLight playerLight;
    private final TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;
    // Support multiple players: DONE!
    private final Music ambientMusic;
    private final Sound zombieGroan;
    private final Spawner zombieSpawner;
    private final BitmapFont font = new BitmapFont();
    //	private final server server;
    private final HashMap<String, Player> clients = new HashMap<>();
    public List<Entity> entities;
    public Player player;
    // public float mouseX, mouseY;
    public SteeringAI playerAI;
    public boolean noZombie = false;
    public boolean noTime = false;
    public boolean firePressed;
    public Touchpad touchpad = null;
    public boolean lookWithJoy = true;
    private int lookPointer = 0;
    private OrthographicCamera cam;
    private Box2DDebugRenderer b2dr;
    private Vector2 mouseLoc;
    private float groanTimer = 0;
    private Image fireButton = null;
    private Touchpad lookpad = null;
    private Stage stage = null;

    // sound system
    /*
     * protected SoundSystem soundSys;
	 */
    @SuppressWarnings("unchecked")
    public GameState() {

        // Instance of the game, for ease of access
        instance = this;
        // Camera and Map
        Assets assets = TopDown.assets;
        tiledMap = assets.get(Assets.Main_MAP, TiledMap.class);

        // World Init
        gameWORLD = new World(new Vector2(0, 0), true);
        gameWORLD.setContactListener(new CollisionDetection());
        clock = new Clock(gameWORLD);

        //android
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            cam = new OrthographicCamera(800, 400);
            touchpad = new Touchpad(0f, TopDown.assets.get(Assets.neut_SKIN, Skin.class));
            stage = new Stage(new StretchViewport(800, 400, cam), batch);

            touchpad.setSize(110, 110);
            touchpad.setPosition(20, 20);

            lookpad = new Touchpad(0f, TopDown.assets.get(Assets.neut_SKIN, Skin.class));
            lookpad.setColor(Color.RED);
            lookpad.setSize(110, 110);
            lookpad.setResetOnTouchUp(false);
            lookpad.setPosition(cam.viewportWidth - 130, 20);

            fireButton = new Image(new TextureRegion(TopDown.assets.get(Assets.bulletFire_IMAGE, Texture.class)));
            fireButton.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    firePressed = true;
                    fireButton.setColor(Color.GRAY);

                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    firePressed = false;
                    fireButton.setColor(Color.WHITE);
                }
            });
            fireButton.setSize(50, 50);
            fireButton.setPosition(cam.viewportWidth - 70, 140);

            stage.addActor(touchpad);
            stage.addActor(lookpad);
            stage.addActor(fireButton);


        }

        World.setVelocityThreshold(20f);
        // HUD initializing
        hud = new Hud(clock);
        hud.update();

        // Box2D Stuff
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        if (TopDown.debug)
            b2dr = new Box2DDebugRenderer();

        // Rendering
        sr = new ShapeRenderer();

        // Lists
        entities = new ArrayList<Entity>();
        dispose = new ArrayList<Entity>();

        // sound stuff TODO get paulscode 3d sound library

        ambientMusic = assets.get(Assets.ambient_MUSIC, Music.class);
        ambientMusic.setLooping(true);
        ambientMusic.setVolume(0.6f);

        zombieGroan = assets.get(Assets.ZScreams_SOUND, Sound.class);
        // Adding player
        if (TopDown.debug)
            newPlayer(0);
        zombieSpawner = new Spawner(new Vector2(100, 100), clock, this);
        // light
        playerLight = new PlayerLight(new ConeLight(clock.rayHandler, 100, Color.YELLOW, 0, 100, 100, 90, 45));
        // Making all the collision shapes
        MapBodyBuilder.buildShapes(tiledMap, PPM, gameWORLD);
        // packs
        entities.add(new Pack(250 / PPM, 250 / PPM, gameWORLD));

        anim = GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("img/health.gif").read());
        // Server stuff
        //server = new server();

        System.out.println("Finished init Gamestate");
        if (Gdx.app.getType().equals(ApplicationType.Android))
            BoxoUtil.onAndroidBackButtonPressed = new Runnable() {

                @Override
                public void run() {

                    TopDown.dialogs.newDialog(GDXButtonDialog.class).setTitle("Exit to Menu").
                            setMessage("Are you sure you want to return to the menu?")
                            .addButton("Yes")
                            .addButton("No")
                            .setClickListener(new ButtonClickListener() {
                                @Override
                                public void click(int button) {
                                    if (button == 0) {
                                        if (player.connection != null) {
                                            player.connection.stop = true;
                                            try {
                                                player.connection.join();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        TopDown.instance.setScreen(new MenuState(GameState.this));

                                    }

                                }
                            }).build().show();


                }
            };

    }

    public void createNewPlayer(String id) {// Used for the server
        Player player1 = new Player(gameWORLD, 2);
        clients.put(id, player1);
        // or it can be clients.put(id, new Player(gameWORLD, 0));
        // both dont work for some reason
    }

    public void moveClients(String id, float x, float y) {
        if (clients.get(id) != null) {
            clients.get(id).multiPos = new Vector2(x, y);
        } else {
            createNewPlayer(id);
        }
    }

    public void update(float delta) {


        // Updating HUD
        hud.update();

        // Updating World
        gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);

        // Updating player
        player.update(delta);
        playerAI.update(delta);

        //clients.forEach((id, player) -> player.update(delta));
        if (player.state != 0)
            player.connection.updatePlayers(delta);

        // Updating Light TODO dont make this only for player aka make a
        // Flashlight class and and handling in gamestate
        playerLight.updateLightPos(player.getPos(false), player.rotation, delta, Gdx.input.isKeyPressed(Keys.L));
        clock.updateLight(delta);

        // Update Zombie Spawns
        if (!noZombie) {
            zombieSpawner.update(delta, entities.size());
        }

        // Checking the entity list for disposables and updating
        for (Entity entity : entities) {
            if (entity.isDisposable()) {
                entity.dispose();
                dispose.add(entity);
            } else
                entity.update(delta);
        }

        entities.removeAll(dispose);
        dispose.clear();

        groanTimer += delta;
        if (groanTimer > 2000) {
            zombieGroan.play(0.6f);
            groanTimer = 0;
        }
        if (groanTimer > 800) {
            zombieGroan.stop();
        }
        if (Gdx.app.getType().equals(Application.ApplicationType.Android))
            handleAndroid();


        BoxoUtil.lerpToPos(new Vector2(
                        MathUtils.clamp(player.getPos(true).x + player.crossH.offX * 30, cam.viewportWidth / 2,
                                1576 - cam.viewportWidth / 2),
                        MathUtils.clamp(player.getPos(true).y + player.crossH.offY * 30, cam.viewportHeight / 2,
                                1576 - cam.viewportHeight / 2)),
                cam);
        BoxoUtil.updateShake(Gdx.graphics.getDeltaTime(), cam);
        cam.update();
        //server.updateServer(delta);
    }

    private void handleAndroid() {
        stage.act();
        int touched = 0;
        for (int i = 0; i < 5; i++)
            if (Gdx.input.isTouched(i)) touched++;
        if (lookWithJoy) {
            Vector2 joy = new Vector2(lookpad.getKnobX(), lookpad.getKnobY()).sub(50, 50);
            if (!joy.isZero())
                player.rotation = joy.angle();

        } else return;
        if (touchpad.isTouched() && touched == 1) lookPointer = 1;
        else if (!touchpad.isTouched()) lookPointer = 0;

    }

    @Override
    public void handleInput() {

        Input input = Gdx.input;

        if (input.isKeyJustPressed(Input.Keys.Z)) {
            for (Entity e : entities) e.dispose();
            GameState.instance.entities.clear();
            noZombie = !noZombie;
        }

        if (input.isKeyJustPressed(Input.Keys.T)) {
            noTime = !noTime;
            if (noTime) {
                clock.amlight = 2f;
                clock.progressTime = false;
            } else {
                clock.amlight = 1f;
                clock.progressTime = true;
            }
        }

        if (input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        /*
		 * for(HashMap.Entry<String, Player> entry: clients.entrySet()){ }
		 */
        if (input.isKeyJustPressed(Keys.H)) {
            if (player.isAlive())
                player.setHealth(player.getMaxHealth());
            else {
                newPlayer(player.state);
            }
        }

        if (input.isKeyJustPressed(Keys.M)) {
            if (player.connection != null) {
                player.connection.stop = true;
                try {
                    player.connection.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            TopDown.instance.setScreen(new MenuState(this));
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(cam.combined);
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();

        // Entity render
        batch.begin();

        for (Entity entity : entities) {
            if (!entity.isDisposable())
                entity.render(batch);
        }
        batch.end();
        // Light render
        clock.renderLIGHT(cam);
        // Everything below is rendering not affected by light
        if (b2dr != null)
            b2dr.render(gameWORLD, cam.combined.cpy().scl(PPM));
        // Shape rendering
        // TODO get a texture for all shapes
        sr.setProjectionMatrix(cam.combined);
        sr.setAutoShapeType(true);
        sr.begin();
        for (Entity entity : entities) {
            entity.renderShapes(sr);
        }

        player.renderShapes(sr);
        hud.render(sr);
        sr.end();

        // rendering of hud and player
        batch.begin();
        for (HashMap.Entry<String, Player> entry : clients.entrySet()) {
            entry.getValue().render(batch);

        }
        if (player.state != 0)
            player.connection.renderPlayers(batch);
        player.render(batch);

        hud.render(batch);

        batch.end();
        if (stage != null) {
            stage.draw();
            batch.setColor(Color.WHITE);
        }

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {


        Vector3 mouseCoords = getMouseCords();
        mouseLoc = new Vector2(mouseCoords.x, mouseCoords.y);

        Vector2 direction = mouseLoc.sub(player.getPos(true));
        player.rotation = direction.angle();

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if ((touchpad != null && touchpad.isTouched() && pointer == 0) || lookWithJoy) return false;

        Vector3 mouseCoords = getMouseCords(lookPointer);
        mouseLoc = new Vector2(mouseCoords.x, mouseCoords.y);

        Vector2 direction = mouseLoc.sub(player.getPos(true));
        player.rotation = direction.angle();

        return true;
    }


    public Vector3 getMouseCords(int pointer) {
        return cam.unproject(new Vector3(Gdx.input.getX(pointer), Gdx.input.getY(pointer), 0));
    }

    public Vector3 getMouseCords() {
        return getMouseCords(0);
    }

    public World getWorld() {
        return gameWORLD;
    }

    public void newPlayer(int state) {
        if (player != null) {
            player.dispose();
            if (player.connection != null && player.state == 0) {
                player.connection.stop = true;
                try {
                    player.connection.join(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                player.connection = null;

            }
        }
        player = new Player(gameWORLD, state);
        playerAI = new SteeringAI(player, 40);
    }

    @Override
    public void dispose() {
        for (Entity entity : entities) {
            entity.dispose();
        }
        playerLight.dispose();
        player.dispose();
        player = null;
        entities.clear();
        entities = null;
        tiledMap.dispose();
        playerLight.dispose();
        clock.dispose();
        gameWORLD.dispose();
        font.dispose();
        batch.dispose();
        for (HashMap.Entry<String, Player> entry : clients.entrySet()) {
            entry.getValue().dispose();
        }
        zombieGroan.dispose();
        //ambientMusic.dispose();
        /*
		 * if(soundSys!=null) soundSys.cleanup();
		 */
    }

    @Override
    public void resize(int width, int height) {
        // cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2,
        // Gdx.graphics.getHeight() / 2);
        // cam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        BoxoUtil.remInputProcessor(this);
        if (stage != null)
            BoxoUtil.remInputProcessor(stage);
        ambientMusic.stop();
    }

    @Override
    public void show() {
        cam = new OrthographicCamera(800, 400);
        cam.update();
        BoxoUtil.addInputProcessor(this);
        if (stage != null)
            BoxoUtil.addInputProcessor(stage);
        if (!Gdx.app.getType().equals(ApplicationType.Desktop))
            ambientMusic.play();
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
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
    public boolean scrolled(int amount) {
        return false;
    }


}
