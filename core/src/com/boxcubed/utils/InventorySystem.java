package com.boxcubed.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class InventorySystem {
	HashMap<String, InventoryItem> inventory;
	ArrayList<String> items; //to keep track of which item is at which index in the hashmap
	private static final int maxInventoryItems = 6;
	private int currentInventoryItems = 0; 
	public InventorySystem(){
		inventory = new HashMap<String, InventoryItem>();
	}
	public void addItem(int index, String name, InventoryItem item){
		if(currentInventoryItems < maxInventoryItems){
		inventory.put(name, item);
		items.add(name);
		currentInventoryItems++;
		}else{
			System.out.println("Sorry, inventory is full");
		}
	}
	public void listItems(){
		System.out.println("Inventory size: "+ inventory.size());
		System.out.println(inventory.get("ak47"));
	}
	public InventoryItem returnItem(String name){
		return inventory.get(name);
	}
	public void removeItem(String itemName){
		inventory.remove(itemName);
		System.out.println(itemName+ " has been removed from inventory");
	}
	public void clearInventory(){
		inventory.clear();
	}
	
}
