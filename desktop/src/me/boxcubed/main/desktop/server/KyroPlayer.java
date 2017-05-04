package me.boxcubed.main.desktop.server;
import com.esotericsoftware.kryonet.Connection;

public class KyroPlayer {
	/**
	 * 
	 */
	public transient Connection connection;
	public transient String reason="disconnected";
	public transient boolean connected=true;
	public String name;
	public transient Multiplayer_Player player;
	public int id,packetCount;
	
	/*
	 * Not recommended as poses thread safety issues
	 */
	public KyroPlayer(Connection socket, String name,Multiplayer_Player player) {
		
		this.connection = socket;
		this.name = name;
		this.player=player;
	}
public KyroPlayer(Connection socket, String name) {
		
		this.connection = socket;
		this.name = name;
		
	}
	public KyroPlayer() {
	}
	
}