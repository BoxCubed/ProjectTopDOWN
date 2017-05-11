package com.boxcubed.utils;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import me.boxcubed.main.States.GameState;

public class InventorySystem {
	//InventoryItem[] inventory;
	HashMap<String, InventoryItem> inventoryItems= new HashMap<String, InventoryItem>();
	private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!no.
	private int currentInventoryItems = 0; 
	public InventorySystem(){
		clearInventory();
	}
	public void addItem(String name, int index, InventoryItem item){
		if(currentInventoryItems < MAX_INVENTORY_ITEMS){
		//inventory[index]=item;
		inventoryItems.put(name, item);
		currentInventoryItems++;
		System.out.println(name+ " has been added to the inventory");
		}else throw new IllegalArgumentException("Given index is larger than inventory size!");
		
	}
	//TODO Debugging ONLY
	public void listItems(){
		for(Entry<String, InventoryItem> key: inventoryItems.entrySet()){
			System.out.println(key);
		}
		System.out.println("Inventory size: "+currentInventoryItems);
		System.out.println(inventoryItems.entrySet());
		//System.out.println(inventory[0]);
	}
	public void render(ShapeRenderer shaper){
		for(Entry<String, InventoryItem> key: inventoryItems.entrySet()){
			//System.out.println("key: "+key); This works
		}
	}
	public void render(SpriteBatch batcher){
		for(Entry<String, InventoryItem> key: inventoryItems.entrySet()){
			//System.out.println("key: "+key); This works
			//batcher.draw(key.getValue().getTexture(), GameState.instance.);
		}
	}
	/**
	 * May return null if no item is present in that position. 
	 * Could throw an error if item index is larger than {@value #MAX_INVENTORY_ITEMS}
	 * 
	 * @param index the index to add the item to.
	 * @return null or item given
	 * @throws IllegalArgumentException
	 */
	public InventoryItem getItem(String name, int index){
		return inventoryItems.get(name);//inventory[index];
	}
	
	public void removeItem(String name, int index){
		/*System.out.println(inventory[index].getItemName()+ " has been removed from inventory");
		inventory[index]=null;*/
		inventoryItems.remove(name);
		currentInventoryItems--;
	}
	public void clearInventory(){
		inventoryItems.clear();
		//inventory=new InventoryItem[MAX_INVENTORY_ITEMS];
		//See in hashmaps how there is a method to clear the array, it would be great of we could go back to it
	}
	
}
