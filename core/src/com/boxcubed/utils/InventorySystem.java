package com.boxcubed.utils;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import me.boxcubed.main.States.GameState;

public class InventorySystem {
	//InventoryItem[] inventory;
	HashMap<String, InventoryItem> inventoryItems= new HashMap<String, InventoryItem>();
	private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!no.
	private int currentInventoryItems = 0;
	private static final int OFFSET = 100;
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
			float x = GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3 + key.getValue().getIndex()*OFFSET;
			float y = GameState.instance.hud.textCam.position.y/3 - GameState.instance.hud.textCam.viewportHeight/3;
			shaper.setColor(Color.CHARTREUSE);
			shaper.rect(x ,y,80,80);
		}
		
	}
	float x1, y1;
	public void render(SpriteBatch batcher){
		for(Entry<String, InventoryItem> key: inventoryItems.entrySet()){
			//System.out.println("key: "+key); This works
			float x = GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3 + key.getValue().getIndex()*OFFSET;
			float y = GameState.instance.hud.textCam.position.y/3 - GameState.instance.hud.textCam.viewportHeight/3;
			x1 =x;
			y1 = y;
			batcher.draw(key.getValue().getTexture(),x,y, 80, 80);
			
			
		}
	}
	
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
	}
	
}
