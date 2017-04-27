package com.boxcubed.net.packets;

import com.badlogic.gdx.math.Vector2;
/**
 * Outgoing and incoming packet for player position updates on client and server
 * @author ryan9
 *
 */
public class PlayerUpdatePacket {
	public float rotation;
	public Vector2 location;
	public int id;
	public String name;
	public double health;
	public PlayerUpdatePacket(float rotation, Vector2 location, int id, int health,String name) {
		this.rotation = rotation;
		this.location = location;
		this.id = id;
		this.name = name;
		this.health=health;
	}
	public PlayerUpdatePacket(float rotation, Vector2 location, int id) {
		this(rotation, location, id,0,"");
	}
	public PlayerUpdatePacket(){}
	

}
