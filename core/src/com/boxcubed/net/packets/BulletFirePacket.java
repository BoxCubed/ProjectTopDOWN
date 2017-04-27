package com.boxcubed.net.packets;

import com.badlogic.gdx.math.Vector2;

public class BulletFirePacket {
	public Vector2 location;
	public float rotation;
	public String type;
	public BulletFirePacket() {
	}
	public BulletFirePacket(Vector2 location, float rotation, String type) {
		this.location = location;
		this.rotation = rotation;
		this.type = type;
	}
	public BulletFirePacket(float rotation, String type) {
		super();
		this.rotation = rotation;
		this.type = type;
	}
	
}
