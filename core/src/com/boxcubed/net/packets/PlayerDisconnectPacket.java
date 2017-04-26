package com.boxcubed.net.packets;

public class PlayerDisconnectPacket {
	String name;
	int id;
	
	
	
	public PlayerDisconnectPacket() {
		
    }



	public PlayerDisconnectPacket(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
}
