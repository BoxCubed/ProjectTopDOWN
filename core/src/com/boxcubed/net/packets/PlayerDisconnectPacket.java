package com.boxcubed.net.packets;

public class PlayerDisconnectPacket {
	private String name;
    public String reason;
	public int id;
	
	
	
	
	public PlayerDisconnectPacket() {
		
    }



	public PlayerDisconnectPacket(String name, int id,String reason) {
		this.name = name;
		this.id = id;
		this.reason=reason;
	}
	
}
