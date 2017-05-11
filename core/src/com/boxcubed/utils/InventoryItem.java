package com.boxcubed.utils;

import com.badlogic.gdx.graphics.Texture;

public interface InventoryItem{
	public String getItemName();
	public Texture getTexture();//This is like a logo for the gun to display to the user
	public int getIndex();
	public int getOffset();
}
