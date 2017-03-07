package me.boxcubed.main.Objects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.Zombie;
import me.boxcubed.main.States.GameState;

/**
 * :)
 * @author ryan9
 *
 */
public class Spawner {
	/*
	 * Map limits: y: 40-345
	 * x: 22-680
	 */
	Random random=new Random();
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
			loc.x=random.nextInt(680-22)+22;
			loc.y=random.nextInt(345-40)+40;
			LivingEntity spawnEntity=null;
			if(entity.equals(EntityType.ZOMBIE))
				
				spawnEntity=new Zombie(GameState.instance.getWorld(),  GameState.instance.playerAI);
				
			
			if(entity.equals(EntityType.PLAYER))
				spawnEntity=new Player(GameState.instance.getWorld());
			
			if(spawnEntity!=null){
			spawnEntity.getBody().setTransform(loc, spawnEntity.getBody().getAngle());
			GameState.instance.entities.add(spawnEntity);
			}
			elapsedTime=0;
		}
		
	}
}// lol noob
