package com.boxcubed.events;

import java.lang.reflect.Method;
import java.util.HashSet;

import com.boxcubed.utils.Timer;

public class EventHandler {
	public HashSet<Class<? extends Event>> eventList;
	public HashSet<EventListener> listeners;
	Timer timer;
	
	public EventHandler(){
		eventList=new HashSet<Class<? extends Event>>();
		listeners=new HashSet<EventListener>();
	}
	
	public void update(){
		
	}
	/**
	 * Register an event other listeners can listen to
	 * @param event the event to add
	 */
	public void registerEvent(Class<? extends Event> event){
		eventList.add(event);
	}
	public void unregisterEvent(Class<? extends Event> event){
		eventList.remove(event);
	}
	/**
	 * Calls an event
	 */
	public void callEvent(Event event){
		for(EventListener e:listeners){
			for(Method method:e.getClass().getMethods()){
				if(method.isAnnotationPresent(EventMethod.class)){
					//TODO
					method.invoke(e, null);
				}
			}
			
		}
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
