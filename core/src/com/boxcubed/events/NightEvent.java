package com.boxcubed.events;

public class NightEvent extends Event {
	private float triggerTime;
	public NightEvent(float triggerTime) {
		super();
		this.triggerTime=triggerTime;
		
		
	}
	public float getTriggerTime(){return triggerTime;}

}
