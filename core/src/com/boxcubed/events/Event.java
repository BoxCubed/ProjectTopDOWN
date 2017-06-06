package com.boxcubed.events;


public abstract class Event {
	private String name;
	private boolean async=false;
	protected int timesHandled=0;
	
	
	
	public Event(){
		async=!Thread.currentThread().getName().equals("LWJGL Application");
		name=getClass().getSimpleName();
		
	}
	/**
	 * Gets the Event name specified by the super class
	 * @return name of the event
	 */
	public String getName(){return name;}
	/**
	 * Indicates whether the event is running on the main thread.
	 * Take this into consideration when handling your events.
	 * @return whether the event is async
	 */
	public boolean isAsync(){return async;}
	
	/**
	 * The amount of other listeners who have already handled this event
	 * @return the amount of listeners who have handled this event
	 */
	public int getHandledTimes(){return timesHandled;}
}
