package com.boxcubed.net;

import java.lang.reflect.Method;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Sprites.Player;

public class Multiplayer_Player extends Player {

	public Multiplayer_Player(World world) {
		super(world);
		
	}
	@Override
	public void update(float delta) {
		handleInput();
		this.delta=delta;
	
	
		
	}
	
	public void processCommand(String s){
		Method m;
		try {
			// this, my friends, is reflection. Learn it. Its good.
			m = getClass().getMethod(s, (Class<?>[])null);
			m.invoke( this,  (Object[])null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void handleInput(){}
	
	@Override
	public void render(SpriteBatch sb) {}
}
