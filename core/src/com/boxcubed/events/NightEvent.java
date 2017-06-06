package com.boxcubed.events;

public class NightEvent extends Event {

	public NightEvent(String id, int type, float triggerTime,EventHandler eventHandler) {
		super(id, type, triggerTime, eventHandler);
		
		
	}

	@Override
	public void eventEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTriggered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTriggerTime(float triggerTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getElapsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
