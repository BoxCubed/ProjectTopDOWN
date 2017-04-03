package com.boxcubed.net;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPacket implements Serializable{
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 6069159412880186977L;
	public ArrayList<SocketPlayer> players;
	public Vector2 pos;
	public int position;
	
	public DataPacket(Vector2 pos, ArrayList<SocketPlayer> players,int position) {
		this.pos=pos.cpy();
		this.players=players;
		this.position=position;
	}
	public DataPacket(){}
	
	
}
