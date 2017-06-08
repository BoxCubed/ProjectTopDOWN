package com.boxcubed.events;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class DoorTouchEvent extends Event {
Vector2 position;
TiledMap map;
int id;
public DoorTouchEvent(Vector2 position, TiledMap map, int id) {
	super();
	this.position = position;
	this.map = map;
	this.id = id;
}
public Vector2 getPosition() {
	return position;
}
public TiledMap getMap() {
	return map;
}
public int getId() {
	return id;
}

}
