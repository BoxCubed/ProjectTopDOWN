package com.boxcubed.utils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import me.boxcubed.main.States.GameState;

public class InventorySystem {
	InventoryItem[] inventory;
	LinkedHashMap<Integer, InventoryItem> inventoryItems= new LinkedHashMap<Integer, InventoryItem>();
	private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!no.
	private int currentInventoryItems = 0; 
	private static final int OFFSET = 80;
	Texture inventoryUI = new Texture("img/inventoryUI.png");
	private int[] keys;
	public InventorySystem(){
		clearInventory();
		keys=new int[MAX_INVENTORY_ITEMS];
		for(int i=1;i<=MAX_INVENTORY_ITEMS;i++){
			try {
				Field f=Keys.class.getField("NUM_"+i);
				keys[i-1]=f.getInt(null);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
			
			
		}
		
	}
	public void addItem(int index, InventoryItem item){
		if(currentInventoryItems < MAX_INVENTORY_ITEMS){
		inventory[index]=item;
		inventoryItems.put(index, item);
		currentInventoryItems++;
		//System.out.println(item.getItemName()+ " has been added to the inventory");
		}else throw new IllegalArgumentException("Given index is larger than inventory size!");
		
	}
	//TODO Debugging ONLY
	public void listItems(){
		int i=0;
		for(InventoryItem item:inventory){
			if(item==null)continue;
			System.out.println("Item "+(i++)+"= "+item.getItemName());
			
		}
		System.out.println("Inventory size: "+currentInventoryItems);
		//System.out.println(inventory[0]);
	}
	float previousX;
	Vector3 touchPos = new Vector3();
	public void render(SpriteBatch batcher){
		batcher.draw(inventoryUI, GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3, GameState.instance.hud.textCam.position.y/4 - GameState.instance.hud.textCam.viewportHeight/2, 650, 80);
		for(int i=0;i<inventory.length;i++){
			
			InventoryItem key=inventory[i];
			if(key==null)continue;
			//batcher.draw(key.getValue().getTexture(), GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3,GameState.instance.hud.textCam.position.y/3 - GameState.instance.hud.textCam.viewportHeight/3, 80, 80);
			float x = GameState.instance.hud.textCam.position.x/2 - GameState.instance.hud.textCam.viewportWidth/3;//+ key.getValue().getIndex()*OFFSET;
		    float y = GameState.instance.hud.textCam.position.y/4 - GameState.instance.hud.textCam.viewportHeight/2;
			if(key.equals(itemSelected)){
				batcher.draw(key.getActiveTexture(),previousX + OFFSET*i,y, 80, 80);
			}else{
			    batcher.draw(key.getTexture(),previousX + OFFSET*i,y, 80, 80);
			}
		    //System.out.println();
		    previousX = x;
		    }
		update(Gdx.graphics.getDeltaTime());
	}
	public InventoryItem itemSelected =null;
	public void update(float delta){
		for(int i=0;i<keys.length;i++){
			int key=keys[i];
			if(Gdx.input.isKeyPressed(key)){
				itemSelected=inventory[i];
				}
		}
		

	}
	
	public InventoryItem getItem(int index){
		return inventory[index];
	}
	
	public void removeItem(int index){
		System.out.println(inventory[index].getItemName()+ " has been removed from inventory");
		inventory[index]=null;
		inventoryItems.remove(index);
		currentInventoryItems--;
	}
	public void clearInventory(){
		inventoryItems.clear();
		inventory=new InventoryItem[MAX_INVENTORY_ITEMS];
		//See in hashmaps how there is a method to clear the array, it would be great of we could go back to it
	}
	
}
