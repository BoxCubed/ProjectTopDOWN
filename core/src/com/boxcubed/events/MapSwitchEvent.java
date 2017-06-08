package com.boxcubed.events;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapSwitchEvent extends Event implements Cancellable{
	boolean cancelled;
	int id;
	TiledMap newMap,oldMap;
	public MapSwitchEvent(int id,TiledMap newMap,TiledMap oldMap) {
		super();
		this.id=id;
		this.newMap=newMap;
		this.oldMap=oldMap;
	}

	@Override
	public void setCancelled() {
		cancelled=true;
		
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	public int getSwitched(){return id;}
	public TiledMap getNewMap(){return newMap;}
	public TiledMap getOldMap(){return oldMap;}

}
