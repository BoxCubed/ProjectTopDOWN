package com.boxcubed.net;

import java.io.Serializable;

public class DataPacket implements Serializable{
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 6069159412880186977L;
	public float loc,loc2;
	public float rotation;
	public DataPacket(float loc, float loc2, float rotation) {
		this.loc = loc;
		this.loc2 = loc2;
		this.rotation = rotation;
	}
	
}
