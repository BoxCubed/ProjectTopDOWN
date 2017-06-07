package com.boxcubed.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class EventHandler {
	private static HashSet<EventListener> listeners;
	
	public EventHandler(){
		listeners=new HashSet<EventListener>();
	}
	
	
	/**
	 * Register an event other listeners can listen to
	 * @param event the event to add
	 */
	public void registerListener(EventListener event){
		listeners.add(event);
	}
	public void unregisterEvent(EventListener event){
		listeners.remove(event);
	}
	/**
	 * Calls an event
	 */
	public static void callEvent(Event event)throws IllegalAccessException,IllegalArgumentException,InvocationTargetException{
		for(EventListener e:listeners){
			for(Method method:e.getClass().getMethods()){
				if(method.isAnnotationPresent(EventMethod.class)){
					//TODO
						if(method.getParameterTypes().length>1) throw new IllegalArgumentException("The given Event "+event.getClass().getSimpleName()+" is not a valid event!\n "
								+ "The parameters in method "+method.getName()+" has more than one parameter!");
						if(!method.getReturnType().equals(Void.TYPE))throw new IllegalArgumentException("The given Event "+event.getClass().getSimpleName()+" is not a valid event!\n "
								+ "The method "+method.getName()+" must not return a value!");
						method.invoke(e, event);
					}
				}
			}
			
		}
	
	
	/**
	 * The amount of registered Listeners
	 * @return The amount of registered Listeners
	 */
	public int getAmountRegisteredListeners(){
		return listeners.size();
	}
}
