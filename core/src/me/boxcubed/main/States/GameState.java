package me.boxcubed.main.States;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.CollisionDetection;
import me.boxcubed.main.Objects.EntityType;
import me.boxcubed.main.Objects.LivingEntity;
import me.boxcubed.main.Objects.MapCollision;
import me.boxcubed.main.Objects.Spawner;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.PlayerLight;

public class GameState implements Screen{
	public World gameWORLD;
	OrthographicCamera cam,textCam;
	public Player player;
	public static GameState instance;
	public List<LivingEntity> entities;
	SpriteBatch sb;
	public static final int PPM = 200;
	private PlayerLight playerLight;
	//Zombie zombie;
	
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	Box2DDebugRenderer b2dr;
	public SteeringAI playerAI;
	MapCollision mp;
	Spawner zombieSpawner;
	//SteeringAI zombieAI;
	@Override
	public void show() {
		instance=this;
		System.out.println("Init");
		
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		cam.update();
		tiledMap = new TmxMapLoader().load("assets/maps/map2.tmx");
	
		textCam=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		textCam.update();
		
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        b2dr=new Box2DDebugRenderer();
        
		
		sb = new SpriteBatch();
		entities = new ArrayList<LivingEntity>();
		

		initMap();
		gameWORLD = new World(new Vector2(0, 0), true);
		gameWORLD.setContactListener(new CollisionDetection());
		player = new Player(gameWORLD);
		player.setSize(20, 20);
		playerAI=new SteeringAI(player, player.getWidth());
/*		for(int i=0;i<10;i++)
		entities.add(new Zombie(gameWORLD,playerAI));*/
		zombieSpawner=new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100,4);
		
		
		
		
		playerLight = new PlayerLight(gameWORLD);
		
		mp=new MapCollision(tiledMap,gameWORLD);
		
	}

	public void update(float delta) {
		handleInput();
		//cam.position.x=player.getPos().x;
		textCam.update();
		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);
		
		player.setPosition(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		playerLight.updateLightPos(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		playerLight.rayHandler.update();
		
		//zombie.update(delta);
		zombieSpawner.update(delta);
		entities.forEach(entity->entity.update(delta));
		player.update(delta);
		cam.position.set(player.getPos(),0);
		
		
		//System.out.println(player.getPos());
	}
	private void handleInput() {
		Input input=Gdx.input;
		if(input.isKeyJustPressed(Input.Keys.R)){
			GameState.instance.entities.forEach(entity->entity.dispose());
			GameState.instance.entities.clear();
		}
		if(input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
		
	}
	BitmapFont font=new BitmapFont();
	DecimalFormat format=new DecimalFormat("#.##");
	@Override
	public void render(float delta) {
		update(delta*100);
		
		sb.setProjectionMatrix(cam.combined);
		
        cam.update();
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
	
		/*playerLight.rayHandler.setCombinedMatrix(cam);
		playerLight.rayHandler.render();*/
        b2dr.render(gameWORLD, cam.combined);
		sb.begin();
		
		
		
		player.render(sb);
		entities.forEach(entity->entity.render(sb));
		sb.setProjectionMatrix(textCam.combined);
		font.draw(sb, "Delta: "+format.format(delta*100), 100, textCam.viewportHeight/2);
		font.draw(sb, "Entity.Amount: "+entities.size(), -100, textCam.viewportHeight/2);
		font.draw(sb, "Player.Loc: "+format.format(player.getBody().getPosition().x)+","+format.format(player.getBody().getPosition().y), -400, textCam.viewportHeight/2);
		
		sb.end();
		
	}
	public World getWorld(){
		return gameWORLD;
	}
	public Matrix4 camCombined(){
		return cam.combined;
	}
	float rotation=0;
	
	
	private void initMap(){

       //MapCollision map = new MapCollision(tm,gameWORLD); 
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
		//dispose();
	}

	@Override
	public void dispose() {
		entities.forEach(entity->entity.dispose());
		playerLight.rayHandler.dispose();
		player = null;
		entities.clear();
		entities = null;
		tiledMap.dispose();
		playerLight.dispose();
		gameWORLD.dispose();
		sb.dispose();
	}

	

	

}
		//port = new FitViewport(Gdx.graphics.getWidth()/2,  Gdx.graphics.getHeight()/2,cam);
