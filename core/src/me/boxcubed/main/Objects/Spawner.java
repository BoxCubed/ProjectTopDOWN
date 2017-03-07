package me.boxcubed.main.Objects;

import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Sprites.Zombie;
import me.boxcubed.main.States.GameState;

/**
 * :)
 * @author ryan9
 *
 */
public class Spawner {
	EntityType entity;
	Vector2 loc;
	private float elapsedTime=0;
	private float delay;
	public Spawner(EntityType entity, Vector2 loc,float delay){
		this.loc=loc;
		this.entity=entity;
		this.delay=delay;
	}
	/**
	 * 
	 * @param delta in same units as delay given
	 */
	public void update(float delta){
		elapsedTime+=delta;
		if(elapsedTime>=delay){
			if(entity.equals(EntityType.ZOMBIE)){
				Zombie spawn=new Zombie(GameState.instance.getWorld(),  GameState.instance.playerAI);
				spawn.getBody().setTransform(loc, spawn.getBody().getAngle());
			GameState.instance.entities.add(spawn);
			}
			elapsedTime=0;
		}
		
	}
}// lol noob
