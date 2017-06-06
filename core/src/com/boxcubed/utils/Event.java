package com.boxcubed.utils;

import me.boxcubed.main.Objects.StopWatch;

public class Event {
	StopWatch timer;
	String id;
	int type;
	
	
	public Event(String id, int type, EventHandler eventHandler){
		this.id=id;
		this.type=type;
		timer = new StopWatch();
		timer.start();
	}
	
	public void eventEnd(){
		timer.reset();
	}
	
	public void setTimeTrigger(float gameTime){
		
	}
	
	public float getElapsedTime(){
		return timer.getElapsedTime();
	}
	
	public int getType(){
		return type;
	}
}
