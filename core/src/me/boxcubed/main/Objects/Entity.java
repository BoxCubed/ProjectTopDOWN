package me.boxcubed.main.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Entity {
	World world;
	public Entity(World world,int x,int y){
		this.world=world;
		BodyDef bd=new BodyDef();
		bd=world.createBody(bd);
	}
	
	public Sprite setSprite(Sprite sprite){
		return sprite;
	}
	
	

}
