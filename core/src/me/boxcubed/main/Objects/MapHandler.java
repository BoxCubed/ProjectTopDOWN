package me.boxcubed.main.Objects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.boxcubed.events.DoorTouchEvent;
import com.boxcubed.events.EventHandler;
import com.boxcubed.events.MapSwitchEvent;

import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.States.GameState;

//TODO
/**
 * 
 * @author ryan9
 *
 */
public class MapHandler {
	private World world;
	private List<TiledMap> maps;
	private OrthogonalTiledMapRenderer mapRenderer;
	private int currentMap = -1;

	public MapHandler(World world, OrthographicCamera cam, SpriteBatch batch) {
		this.world = world;
		maps = new ArrayList<>();
		mapRenderer = new OrthogonalTiledMapRenderer(null,1, batch);
	}

	public void registerMap(TiledMap map, int id) {
		maps.set(id, map);

	}

	public void addMap(TiledMap map) {
		maps.add(map);
	}

	public void switchMaps(int id) {
		if (maps.get(id) != null){
			MapSwitchEvent event=new MapSwitchEvent(id,maps.get(id),mapRenderer.getMap());
			try {
				EventHandler.callEvent(event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			if(!event.isCancelled())
			currentMap = id;
			
		}
		else
			throw new IllegalArgumentException("The given map id hasn't been registered!");

	}

	public void update(float delta, OrthographicCamera cam) {
		mapRenderer.setView(cam);
		
		if (currentMap!=-1&&mapRenderer.getMap() != maps.get(currentMap)&&maps.get(currentMap)!=null) {
			mapRenderer.setMap(maps.get(currentMap));
			Array<Body> bodies = new Array<>();
			world.getBodies(bodies);
			bodies.forEach(body -> {
				if (body.getFixtureList().get(0).getUserData().equals("WALL")) {
					world.destroyBody(body);
				}
			});
			MapBodyBuilder.buildShapes(mapRenderer.getMap(), GameState.PPM, world);

		}
		
		
		
		if(mapRenderer.getMap()!=null)
		for(MapObject mo:mapRenderer.getMap().getLayers().get("Door").getObjects()){
			RectangleMapObject rMo=(RectangleMapObject)mo;
			try{
			if(rMo.getRectangle().contains(GameState.instance.player.getPos(true))){
					EventHandler.callEvent(new DoorTouchEvent(GameState.instance.player.getPos(true), mapRenderer.getMap(), currentMap,rMo));
				
			}}catch(Exception e){}
		}
	}

	public void render() {
		if (mapRenderer.getMap() != null)
			mapRenderer.render();

	}

	public void render(ShapeRenderer sr) {
	}
}
