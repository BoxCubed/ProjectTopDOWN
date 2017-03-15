package me.boxcubed.main.Objects;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class FileAtlas {
	
	
	
	public static Class<? extends FileAtlas> fileAt;
	public static TiledMap map=new TmxMapLoader().load("assets/maps/map.tmx");

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
