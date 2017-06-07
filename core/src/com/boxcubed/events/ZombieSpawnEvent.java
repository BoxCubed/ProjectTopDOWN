package com.boxcubed.events;

import me.boxcubed.main.Objects.interfaces.LivingEntity;

public class ZombieSpawnEvent extends Event implements Cancellable{
	private boolean cancelled=false;
	private LivingEntity entity;
	public ZombieSpawnEvent(LivingEntity entity) {
		super();
		this.entity=entity;
	}
	
	
	
	
	@Override
	public void setCancelled() {
		cancelled=true;
		
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	
	

}
