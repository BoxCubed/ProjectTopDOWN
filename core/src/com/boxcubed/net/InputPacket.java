package com.boxcubed.net;

import java.io.Serializable;

public class InputPacket implements Serializable {
	/**
	 * 
	 */
	private static final transient long serialVersionUID = 2440884723277785726L;
	byte w,a,s,d,space,shift;
	float rotation;
	public InputPacket(byte w, byte a, byte s, byte d, byte space, byte shift, float rotation) {
		this.w = w;
		this.a = a;
		this.s = s;
		this.d = d;
		this.space = space;
		this.shift = shift;
		this.rotation = rotation;
	}
	public InputPacket() {
	}
	public InputPacket(InputPacket pack){
		this.w = pack.w;
		this.a = pack.a;
		this.s = pack.s;
		this.d = pack.d;
		this.space = pack.space;
		this.shift = pack.shift;
		this.rotation = pack.rotation;
	}
	@Override
	public String toString() {
		return w+":"+a+":"+s+":"+d+":"+space+":"+shift+":"+rotation;
	}
	

}
