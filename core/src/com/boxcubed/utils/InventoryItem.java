package com.boxcubed.utils;

import com.badlogic.gdx.graphics.Texture;

public interface InventoryItem {
    String getItemName();

    Texture getTexture();// This is like a logo for the gun to display to

    // the user
    Texture getActiveTexture();

    int getIndex();

}
