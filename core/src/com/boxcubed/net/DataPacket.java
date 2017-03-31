package com.boxcubed.net;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import com.badlogic.gdx.math.Vector2;

public class DataPacket implements Serializable{
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 6069159412880186977L;
	public ArrayList<SocketPlayer> players;
	public Vector2 pos;
	
	public DataPacket(Vector2 pos, ArrayList<SocketPlayer> players) {
		this.pos=pos.cpy();
		this.players=new ArrayList<>(players);
	}
	public DataPacket(){}
	
	
}
