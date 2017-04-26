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
	public PlayerUpdatePacket(float rotation, Vector2 location) {
		this.rotation = rotation;
		this.location = location;
	}
	

}
