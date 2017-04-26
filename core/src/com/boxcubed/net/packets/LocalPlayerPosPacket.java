package com.boxcubed.net.packets;

import com.badlogic.gdx.math.Vector2;
/**
 * Incoming packet for position update if player moves too quickly<br>
 * To be used for anti cheat purposes
 * @author ryan9
 *
 */
public class LocalPlayerPosPacket{
	/**
	 * 
	 */
	public Vector2 pos;
	
	public LocalPlayerPosPacket(Vector2 pos) {
		this.pos=pos.cpy();
	}
	public LocalPlayerPosPacket(){}
	
}
