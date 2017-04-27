package com.boxcubed.net;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

public class KyroPlayer {
	/**
	 * 
	 */
	public transient Connection connection;
	public transient String reason="disconnected";
	public transient boolean connected=true;
	public String name;
	public  Vector2 loc;
	public float rotation;
	public transient Multiplayer_Player player;
	/*
	 * Not recommended as poses thread safety issues
	 */
	public KyroPlayer(Connection socket, String name,Multiplayer_Player player,Vector2 loc) {
		
		this.connection = socket;
		this.name = name;
		this.player=player;
		this.loc=loc.cpy();
		rotation=player.rotation;
	}
public KyroPlayer(Connection socket, String name,Vector2 loc) {
		
		this.connection = socket;
		this.name = name;
		this.loc=loc.cpy();
		rotation=0;
		
	}
	public KyroPlayer() {
	}
	
}