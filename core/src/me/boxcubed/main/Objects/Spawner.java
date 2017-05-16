package me.boxcubed.main.Objects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.Zombie;
import me.boxcubed.main.States.GameState;

/**
 * Spawns any entity at an interval
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
	private Clock clock;
	/**
	 * The total amount of entities this spawner spawned
	 */
	public int amount;
	public Spawner(EntityType entity, Vector2 pos,float delay,int limit,Clock clock){
		this.pos=pos;
		this.entity=entity;
		this.delay=delay;
		this.limit=limit;
		this.clock=clock;
	}
	/**
	 * 
	 * @param delta in same units as delay given
	 */
	public void update(float delta,int currentAmount){
		if(clock.amlight>0.3f)return;
		if(currentAmount<limit){  
			elapsedTime+=delta;
		if(elapsedTime>=delay){
			amount++;
			
			pos.x=random.nextInt(1570);
			pos.y=random.nextInt(1570);
			while(MapBodyBuilder.checkCollision(pos))
			{
			pos.x=random.nextInt(1570);
			pos.y=random.nextInt(1570);
			}
			LivingEntity spawnEntity=null;
				if(entity.equals(EntityType.ZOMBIE))
					
					spawnEntity=new Zombie(GameState.instance.getWorld(),  GameState.instance.playerAI);
					
				
				if(entity.equals(EntityType.PLAYER))
					spawnEntity=new Player(GameState.instance.getWorld(),0);
				
				if(spawnEntity!=null){
				spawnEntity.getBody().setTransform(pos.scl(1f/GameState.PPM), spawnEntity.getBody().getAngle());
				GameState.instance.entities.add(spawnEntity);
				}
				elapsedTime=0;
			
		
				}}
		}
	
}
