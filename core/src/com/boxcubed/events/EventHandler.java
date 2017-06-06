package com.boxcubed.events;

import java.util.ArrayList;

import com.boxcubed.utils.Timer;

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
			}
		}
	}
	
	public void createEvent(String id,int type, float gameTime){
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
