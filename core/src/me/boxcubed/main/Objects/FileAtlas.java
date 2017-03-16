package me.boxcubed.main.Objects;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class FileAtlas {
	
	
	
	public static Class<? extends FileAtlas> fileAt;
	public static TiledMap map=new TmxMapLoader().load("assets/maps/map.tmx");
	public static Texture zombieTex=new Texture(Gdx.files.internal("assets/img/skeleton-idle_0.png"));
	private static TextureAtlas zombieAtlas= new TextureAtlas(Gdx.files.internal("assets/spritesheets/zombieanim.atlas"));
	public static Animation<TextureRegion> zombieAnim=new Animation<TextureRegion>(1f/30f*150f,zombieAtlas.getRegions());

	public FileAtlas() {
		fileAt=this.getClass();
		
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getFile(String path) throws ClassCastException{
		try {
			Field field=FileAtlas.class.getField(path);
			field.setAccessible(true);
			Object ret=field.get(null);
			
			return (T)ret;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			Gdx.app.log("[TopDown]", "Error accessing file: "+e.getMessage(), e);
		}
		return null;
		
	}
}