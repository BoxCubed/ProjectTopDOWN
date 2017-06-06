package com.boxcubed.utils;

import java.util.ArrayList;

import me.boxcubed.main.Objects.Clock;

public class EventHandler {
	float gameTime;
	ArrayList<Event> eventList;
	Timer timer;
	
	public EventHandler(Clock clock){
		gameTime=clock.amToTimeFloat();
		eventList=new ArrayList<Event>();
	}
	
	public void createEvent(String id,int type){
		eventList.add(new Event(id, type, this));
	}
	
	public int eventAmount(){
		return eventList.size();
	}
}
