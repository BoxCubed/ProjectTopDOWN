package com.boxcubed.net;

import com.badlogic.gdx.math.Vector2;

public class DataPacket {
	public Vector2 loc,loc2;
	public float rotation;
	public DataPacket(Vector2 loc, Vector2 loc2, float rotation) {
		this.loc = loc;
		this.loc2 = loc2;
		this.rotation = rotation;
	}
	

}
