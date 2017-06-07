package com.boxcubed.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class EventHandler {
	private static HashSet<EventListener> listeners=new HashSet<>();
	
	
	
	/**
	 * Register an event other listeners can listen to
	 * @param event the event to add
	 */
	public static void registerListener(EventListener event){
		listeners.add(event);
	}
	/**
	 * Unregister a listener form the EventHandler
	 * @param event
	 */
	public static void unregisterListener(EventListener event){
		listeners.remove(event);
	}
	/**
	 * Calls an event
	 * @return the given event to analyze results
	 */
	public static Event callEvent(Event event)throws IllegalAccessException,IllegalArgumentException,InvocationTargetException{
		for(EventListener e:listeners){
			for(Method method:e.getClass().getMethods()){
				if(method.isAnnotationPresent(EventMethod.class)){
					Class<?>[]types=method.getParameterTypes();
						if(types.length!=1) throw new IllegalArgumentException("The given Event "+event.getClass().getSimpleName()+" is not a valid event!\n "
								+ "The parameters in method "+method.getName()+" has more than one parameter!");
						if(!method.getReturnType().equals(Void.TYPE))throw new IllegalArgumentException("The given Event "+event.getClass().getSimpleName()+" is not a valid event!\n "
								+ "The method "+method.getName()+" must not return a value!");
						
						if(types[0].equals(event.getClass()))
						method.invoke(e, event);
					}
				}
			}
		return event;
			
	}
	
	
	/**
	 * The amount of registered Listeners
	 * @return The amount of registered Listeners
	 */
	public static int getAmountRegisteredListeners(){
		return listeners.size();
	}
}
