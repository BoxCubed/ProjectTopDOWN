package com.boxcubed.utils;

import java.util.ArrayList;

import me.boxcubed.main.Objects.Clock;

public class EventHandler {
	public static float gameTime;
	ArrayList<Event> eventList;
	Timer timer;
	
	public EventHandler(){
		eventList=new ArrayList<Event>();
	}
	
	public void update(Clock clock){
		gameTime=clock.amToTimeFloat();
		
		for(int i=0;i<eventList.size();i++){
			if(eventList.get(i).isTriggered()){
				System.out.println("Event: Night");
			}
		}
	}
	
	public void createEvent(String id,int type, float gameTime){
		eventList.add(new Event(id, type, gameTime, this));
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
