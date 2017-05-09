package com.boxcubed.utils;

public class InventorySystem {
	InventoryItem[] inventory;
	private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!
	private int currentInventoryItems = 0; 
	public InventorySystem(){
		clearInventory();
	}
	public void addItem(int index, InventoryItem item){
		if(currentInventoryItems < MAX_INVENTORY_ITEMS){
		inventory[index]=item;
		currentInventoryItems++;
		}else throw new IllegalArgumentException("Given index is larger than inventory size!");
		
	}
	//TODO Debugging ONLY
	public void listItems(){
		System.out.println("Inventory size: "+currentInventoryItems);
		System.out.println(inventory[0]);
	}
	/**
	 * May return null if no item is present in that position. 
	 * Could throw an error if item index is larger than {@value #MAX_INVENTORY_ITEMS}
	 * 
	 * @param index the index to add the item to.
	 * @return null or item given
	 * @throws IllegalArgumentException
	 */
	public InventoryItem getItem(int index){
		return inventory[index];
	}
	
	public void removeItem(int index){
		System.out.println(inventory[index].getItemName()+ " has been removed from inventory");
		inventory[index]=null;
		
		currentInventoryItems--;
	}
	public void clearInventory(){
		inventory=new InventoryItem[MAX_INVENTORY_ITEMS];
	}
	
}
