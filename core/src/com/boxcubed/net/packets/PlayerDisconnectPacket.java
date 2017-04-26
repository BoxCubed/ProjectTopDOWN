package com.boxcubed.net.packets;

public class PlayerDisconnectPacket {
	public String name,reason;
	public int id;
	
	
	
	
	public PlayerDisconnectPacket() {
		
    }



	public PlayerDisconnectPacket(String name, int id,String reason) {
		this.name = name;
		this.id = id;
		this.reason=reason;
	}
	
}
