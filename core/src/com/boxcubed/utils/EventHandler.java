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
	
	public void removeEvent(String id){
		eventList.remove(getEventIndex(id));
	}
	
	public int getEventIndex(String id){
		for(int i=0;i<eventList.size();i++){
			if(eventList.get(i).id==id){
				return i;
			}
		}
		return 0;
	}
	
	public int eventAmount(){
		return eventList.size();
	}
}
