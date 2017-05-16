package com.boxcubed.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import me.boxcubed.main.States.GameState;

public class InventorySystem {
	//InventoryItem[] inventory;
	LinkedHashMap<Integer, InventoryItem> inventoryItems= new LinkedHashMap<Integer, InventoryItem>();
	private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!no.
	private int currentInventoryItems = 0; 
	private static final int OFFSET = 80;
	Texture inventoryUI = new Texture("img/inventoryUI.png");
	public InventorySystem(){
		clearInventory();
	}
	public void addItem(String name, int index, InventoryItem item){
		if(currentInventoryItems < MAX_INVENTORY_ITEMS){
		//inventory[index]=item;
		inventoryItems.put(index, item);
		currentInventoryItems++;
		System.out.println(name+ " has been added to the inventory");
		}else throw new IllegalArgumentException("Given index is larger than inventory size!");
		
	}
	//TODO Debugging ONLY
	public void listItems(){
		for(Entry<Integer, InventoryItem> key: inventoryItems.entrySet()){
			System.out.println(key);
		}
		System.out.println("Inventory size: "+currentInventoryItems);
		System.out.println(inventoryItems.entrySet());
		//System.out.println(inventory[0]);
	}
	float previousX;
	Vector3 touchPos = new Vector3();
	public void render(SpriteBatch batcher){
		batcher.draw(inventoryUI, GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3, GameState.instance.hud.textCam.position.y/4 - GameState.instance.hud.textCam.viewportHeight/2, 650, 80);
		for(Entry<Integer, InventoryItem> key: inventoryItems.entrySet()){
			//batcher.draw(key.getValue().getTexture(), GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3,GameState.instance.hud.textCam.position.y/3 - GameState.instance.hud.textCam.viewportHeight/3, 80, 80);
			float x = GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3;//+ key.getValue().getIndex()*OFFSET;
		    float y = GameState.instance.hud.textCam.position.y/4 - GameState.instance.hud.textCam.viewportHeight/2;
			if(key.getKey() == itemSelected){
				batcher.draw(key.getValue().getActiveTexture(),previousX + OFFSET*key.getValue().getIndex(),y, 80, 80);
			}else{
			    batcher.draw(key.getValue().getTexture(),previousX + OFFSET*key.getValue().getIndex(),y, 80, 80);
			}
		    //System.out.println();
		    previousX = x;
		    }
		update(Gdx.graphics.getDeltaTime());
	}
	public int itemSelected = 0;
	public void update(float delta){
		if(Gdx.input.isKeyPressed(Keys.NUM_1)){
			itemSelected = 0;
		}else if(Gdx.input.isKeyPressed(Keys.NUM_2)){
			itemSelected = 1;
		}
	}
	
	public InventoryItem getItem(String name, int index){
		return inventoryItems.get(name);//inventory[index];
	}
	
	public void removeItem(String name, int index){
		/*System.out.println(inventory[index].getItemName()+ " has been removed from inventory");
		inventory[index]=null;*/
		inventoryItems.remove(index);
		currentInventoryItems--;
	}
	public void clearInventory(){
		inventoryItems.clear();
		//inventory=new InventoryItem[MAX_INVENTORY_ITEMS];
		//See in hashmaps how there is a method to clear the array, it would be great of we could go back to it
	}
	
}
