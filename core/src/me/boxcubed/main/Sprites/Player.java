package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class Player extends Sprite{
	World w;
	Body b;
	
	public Player(World w) {
		this.w=w;
		BodyDef bd=new BodyDef();
		b=w.createBody(bd);
	
	}
	public void render(SpriteBatch sb){
		
		
	}
	public void handleInput(Input i){
		
		
		
	}
}
