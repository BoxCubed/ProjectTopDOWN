package com.boxcubed.events;

import me.boxcubed.main.Objects.StopWatch;

public abstract class Event {
	StopWatch timer;
	String id;
	float triggerTime;
	int type;
	
	
	public Event(String id, int type, float triggerTime, EventHandler eventHandler){
		
	}
	
	public abstract void eventEnd();
	
	public abstract boolean isTriggered();
	
	public abstract void setTriggerTime(float triggerTime);
	
	public abstract float getElapsedTime();
	public abstract int getType();
}
