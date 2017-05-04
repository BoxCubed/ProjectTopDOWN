package com.boxcubed.net.packets;

public class InputPacket {
	/**
	 * 
	 */
	public byte w,a,s,d,space,shift;
	public float rotation;
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
	public boolean equals(Object obj) {
		if(!(obj instanceof InputPacket))return false;
		InputPacket packet=(InputPacket)obj;
		try{
		return w==packet.w&&
				a==packet.a&&
				s==packet.s&&
				d==packet.d&&
				space==packet.space&&
				shift==packet.shift&&
				rotation==packet.rotation;}
		catch(Exception e){return false;}
		
		
	}
	@Override
	public String toString() {
		return w+":"+a+":"+s+":"+d+":"+space+":"+shift+":"+rotation;
	}
	

}
