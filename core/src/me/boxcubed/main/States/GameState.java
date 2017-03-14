package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import me.boxcubed.main.Objects.Spawner;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.collision.CollisionDetection;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.PlayerLight;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Screen{
	public World gameWORLD;
	public OrthographicCamera cam,textCam;
	public Player player;
	public static GameState instance;
	public List<Entity> entities; //Gay shit. We shout have multiple arraylists for each different entity.
	//Would 10/10 make it easier
	public SpriteBatch sb;
	public static final int PPM = 200;
	private PlayerLight playerLight;
	//Zombie zombie;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	Box2DDebugRenderer b2dr;
	public SteeringAI playerAI;
	//MapCollision mp;
	Spawner zombieSpawner;
	BitmapFont font=new BitmapFont();
	//SteeringAI zombieAI;
	//Bullet
	
	 Vector2 maths;

	boolean noZombie=false,noTime=false;
	@Override
	public void show() {
		instance=this;
		System.out.println("Init");
	    maths = new Vector2(0, 0);
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		cam.update();
		tiledMap = new TmxMapLoader().load("assets/maps/map.tmx");
	
		textCam=new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		textCam.update();
		
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        b2dr=new Box2DDebugRenderer();
        
		
		sb = new SpriteBatch();
		entities = new ArrayList<Entity>();
		

		initMap();
		gameWORLD = new World(new Vector2(0, 0), true);
		gameWORLD.setContactListener(new CollisionDetection());
		player = new Player(gameWORLD);
		//player.setSize(20, 20);
		playerAI=new SteeringAI(player, player.getWidth());
		playerAI.setBehavior(new LookWhereYouAreGoing<Vector2>(playerAI));
/*		for(int i=0;i<10;i++)
		entities.add(new Zombie(gameWORLD,playerAI));*/
		
		zombieSpawner=new Spawner(EntityType.ZOMBIE, new Vector2(100, 100), 100,20);
		playerLight = new PlayerLight(gameWORLD,player.getBody());
		
		//mp=new MapCollision(tiledMap,gameWORLD);
		MapBodyBuilder.buildShapes(tiledMap, 1f, gameWORLD);

	}

	public void update(float delta) {
		//Removes bullets after they are not needed
		//TODO BULLET

		handleInput();
		//cam.position.x=player.getPos().x;
		textCam.update();
		gameWORLD.step(Gdx.graphics.getDeltaTime(), 8, 2);
		
		player.setPosition(player.playerBody.getPosition().x, player.playerBody.getPosition().y);
		playerLight.updateLightPos(player.playerBody.getPosition().x, player.playerBody.getPosition().y,player.getRotation(),delta);
		playerLight.rayHandler.update();
		
		for(int i=0;i<gameWORLD.getContactList().size;i++){
			/*Contact contact = gameWORLD.getContactList().get(i);               just in case*/
		}
				
		if(!noZombie){zombieSpawner.update(delta);}
		List<Entity>dispose =new ArrayList<>();
		player.update(delta);
		entities.forEach(entity->{
			if(entity.isDisposable()){
				entity.dispose();
				dispose.add(entity);
			}else entity.update(delta);
		});
		entities.removeAll(dispose);
		dispose.clear();
		
		//cam.position.set(player.getPos(),0);
		//TODO don't divide by 2 every time
		cam.position.x = MathUtils.clamp(player.getPos().x, cam.viewportWidth/2, 1576- cam.viewportWidth/2);
		cam.position.y = MathUtils.clamp(player.getPos().y, cam.viewportHeight/2, 1576 - cam.viewportHeight/2);
		//System.out.println(player.getPos());

	}
	int counter=0;
	private void handleInput() {

		Input input=Gdx.input;

		//float mouseX = input.getX();

		float mouseX = input.getX()-300;
		float mouseY = Gdx.graphics.getHeight()-input.getY();

		float angle = (float) Math.atan2(mouseY - player.getX(), mouseX - player.getY());
		angle = (float) Math.toDegrees(angle);

		player.setRotation(angle);

		//System.out.println(mouseX+", "+player.getX());
		if(angle<0){
			angle+=360;
		}

		player.setRotation((float) angle);
		if(input.isKeyJustPressed(Input.Keys.Z)){
			GameState.instance.entities.forEach(entity->entity.dispose());
			GameState.instance.entities.clear();
			noZombie=!noZombie;
		}
		if(input.isKeyJustPressed(Input.Keys.T)){
			noTime=!noTime;
			if(noTime){
				PlayerLight.amlight=13;
			}else{PlayerLight.amlight=1f;}
		}
		
		if(input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
		if(input.isKeyJustPressed(Keys.H)){
			if(player.isAlive())
				player.setHealth(player.getMaxHealth());
			else player=new Player(gameWORLD);
		}
		if (input.isKeyJustPressed(Keys.S)) {
		    //Creates new bullet
			//bullets.add(new Bullet(gameWORLD, player.getPos().x, player.getPos().y));
			entities.add(new Bullet(gameWORLD, player.getPos().x, player.getPos().y));
		}
		
	}
	
	
	
	DecimalFormat format=new DecimalFormat("#.##");
	@Override
	public void render(float delta) {
		update(delta*100);
		
		sb.setProjectionMatrix(cam.combined);
		
        cam.update();
        tiledMapRenderer.setView(cam);
        tiledMapRenderer.render();
	

        String health="";
        int i;
       
        for(i=0;i<player.getHealth()/player.getMaxHealth()*100f;i++){
        	health+="|";
        }
        for(;i<100;i++)
        	health+="-";

        
        b2dr.render(gameWORLD, cam.combined);
       playerLight.rayHandler.setCombinedMatrix(cam);

		sb.begin();

		entities.forEach(entity->entity.render(sb));

		sb.end();
		playerLight.rayHandler.render();
		sb.begin();
		player.render(sb);
		//UGLY SHIT
		sb.setProjectionMatrix(textCam.combined);
		font.draw(sb, "Delta: "+format.format(delta*100), -230, textCam.viewportHeight/2);
		font.draw(sb, "Entity Number: "+entities.size(), -380, textCam.viewportHeight/2);
		font.draw(sb, "Time: "+/*format.format((PlayerLight.amlight*100)/8)*/PlayerLight.amToTime(), -120, textCam.viewportHeight/2);
		font.draw(sb, "noZombieMode: "+noZombie, 0, textCam.viewportHeight/2);
		font.draw(sb, "noTimeMode: "+noTime, 150, textCam.viewportHeight/2);
		font.draw(sb, "Player Position: "+format.format(player.getBody().getPosition().x)+","+format.format(player.getBody().getPosition().y), -600, textCam.viewportHeight/2);
		font.draw(sb, health, -200, textCam.viewportHeight/2-50);
		
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
		playerLight.dispose();
		player = null;
		entities.clear();
		entities = null;
		tiledMap.dispose();
		playerLight.dispose();
		gameWORLD.dispose();
		font.dispose();
		sb.dispose();
	}
	

}
