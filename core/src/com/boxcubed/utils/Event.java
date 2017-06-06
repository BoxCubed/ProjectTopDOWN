package com.boxcubed.utils;

import me.boxcubed.main.Objects.StopWatch;

public class Event {
	StopWatch timer;
	String id;
	float triggerTime;
	int type;
	
	
	public Event(String id, int type, float triggerTime, EventHandler eventHandler){
		this.id=id;
		this.type=type;
		this.triggerTime=triggerTime;
		timer = new StopWatch();
		timer.start();
	}
	
	public void eventEnd(){
		timer.reset();
	}
	
	public boolean isTriggered(){
		if(triggerTime<EventHandler.gameTime){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void setTimeTrigger(float triggerTime){
		this.triggerTime=triggerTime;
	}
	
	public float getElapsedTime(){
		return timer.getElapsedTime();
	}
	
	public int getType(){
		return type;
	}
}
