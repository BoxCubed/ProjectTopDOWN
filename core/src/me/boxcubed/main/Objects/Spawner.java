package me.boxcubed.main.Objects;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Sprites.Zombie;
import me.boxcubed.main.States.GameState;

/**
 * :)
 * @author ryan9
 *
 */
public class Spawner {
	LivingEntity entity;
	Vector2 loc;
	private float elapsedTime=0;
	private float delay;
	public Spawner(LivingEntity entity, Vector2 loc,float delay){
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
			GameState.instance.entities.add(new Zombie(GameState.instance.getWorld(), GameState.instance.playerAI));
		}
		
	}
}
