package me.boxcubed.main.Objects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
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
	Vector2 pos;
	private float elapsedTime=0;
	private float delay;
	private int limit;
	public Spawner(EntityType entity, Vector2 pos,float delay,int limit){
		this.pos=pos;
		this.entity=entity;
		this.delay=delay;
		this.limit=limit;
	}
	/**
	 * 
	 * @param delta in same units as delay given
	 */
	public void update(float delta){
		
		if(GameState.instance.entities.size()<limit){  
			elapsedTime+=delta;
		if(elapsedTime>=delay){
			pos.x=random.nextInt(680-22)+22;
			pos.y=random.nextInt(345-40)+40;
			LivingEntity spawnEntity=null;
			        //ryan if you get rid of the limiter again I will murder you 
				if(entity.equals(EntityType.ZOMBIE))
					
					spawnEntity=new Zombie(GameState.instance.getWorld(),  GameState.instance.playerAI);
					
				
				if(entity.equals(EntityType.PLAYER))
					spawnEntity=new Player(GameState.instance.getWorld());
				
				if(spawnEntity!=null){
				spawnEntity.getBody().setTransform(pos, spawnEntity.getBody().getAngle());
				GameState.instance.entities.add(spawnEntity);
				}
				elapsedTime=0;
			
		
				}}
		}
	
}// lol noob
