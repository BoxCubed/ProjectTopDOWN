package com.boxcubed.net;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import com.badlogic.gdx.math.Vector2;

public class SocketPlayer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 927189234076433419L;
	public transient Socket socket;
	public String name;
	public Vector2 loc;
	public float rotation;
	public transient ObjectInputStream in;
	public transient ObjectOutputStream out;
	public transient Multiplayer_Player player;
	public SocketPlayer(Socket socket, String name, ObjectOutputStream out, ObjectInputStream in,Multiplayer_Player player) {
		
		this.socket = socket;
		this.name = name;
		this.in = in;
		this.out = out;
		this.player=player;
		loc=player.getPos().cpy();
		rotation=player.rotation;
	}
	
	
}