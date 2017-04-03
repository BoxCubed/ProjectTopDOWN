
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
		super();
		this.w = w;
		this.a = a;
		this.s = s;
		this.d = d;
		this.space = space;
		this.shift = shift;
		this.rotation = rotation;
	}
	@Override
	public String toString() {
		return w+":"+a+":"+s+":"+d+":"+space+":"+shift+":"+rotation;
	}
	

}

