package com.boxcubed.net;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class DataPacket implements Serializable{
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 6069159412880186977L;
	public Vector2 loc,loc2;
	public float rotation;
	public DataPacket(Vector2 loc, Vector2 loc2, float rotation) {
		this.loc = loc.cpy();
		this.loc2 = loc2.cpy();
		this.rotation = rotation;
	}
	@Override
	public String toString() {
		return loc.toString()+" "+loc2.toString()+" "+rotation;
	}
	
}
