package me.boxcubed.main.Objects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.boxcubed.events.EventHandler;
import com.boxcubed.events.ZombieSpawnEvent;

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
	Random random=new Random();
	EntityType entity;
	Vector2 pos;
	private float elapsedTime=0;
	private float delay;
	private int limit;
	/**
	 * The total amount of entities this spawner spawned
	 */
	public int amount;
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
	public void update(float delta,int currentAmount){
		if(Clock.gameTime<11)return;
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
			ZombieSpawnEvent e=new ZombieSpawnEvent(spawnEntity);
			
				if(entity.equals(EntityType.ZOMBIE))
					
					spawnEntity=new Zombie(GameState.instance.getWorld(),  GameState.instance.playerAI);
					
				
				if(entity.equals(EntityType.PLAYER))
					spawnEntity=new Player(GameState.instance.getWorld(),0);
				
				if(spawnEntity!=null){
					spawnEntity.update(delta);
				spawnEntity.getBody().setTransform(pos.scl(1f/GameState.PPM), spawnEntity.getBody().getAngle());
				
				try {
					EventHandler.callEvent(e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if(!e.isCancelled())
				GameState.instance.entities.add(spawnEntity);
				else{spawnEntity.dispose();spawnEntity=null;}
				}
				elapsedTime=0;
			
		
				}}
		}
	
}
