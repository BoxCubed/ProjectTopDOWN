package com.boxcubed.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import me.boxcubed.main.States.GameState;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class InventorySystem extends InputAdapter {
    private static final int MAX_INVENTORY_ITEMS = 6;//Follow Java Naming convention!no.
    private static final int OFFSET = 80;
    private final LinkedHashMap<Integer, InventoryItem> inventoryItems = new LinkedHashMap<Integer, InventoryItem>();
    private final Texture inventoryUI = new Texture("img/inventoryUI.png");
    private final int[] keys;
    public InventoryItem itemSelected = null;
    private InventoryItem[] inventory;
    private int currentInventoryItems = 0;
    private Rectangle[] itemShapes;

    public InventorySystem() {
        clearInventory();
        keys = new int[MAX_INVENTORY_ITEMS];
        for (int i = 1; i <= MAX_INVENTORY_ITEMS; i++) {
            try {
                Field f = Keys.class.getField("NUM_" + i);
                keys[i - 1] = f.getInt(null);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }


        }
        itemShapes = new Rectangle[inventory.length];
        for (int i = 0; i < inventory.length; i++) {
            itemShapes[i] = new Rectangle(-650f / 2f + OFFSET * i, -GameState.instance.hud.textCam.viewportHeight / 2, 80, 80);
        }

    }

    public void addItem(int index, InventoryItem item) {
        if (currentInventoryItems < MAX_INVENTORY_ITEMS) {
            inventory[index] = item;
            inventoryItems.put(index, item);
            currentInventoryItems++;
            //System.out.println(item.getItemName()+ " has been added to the inventory");
        } else throw new IllegalArgumentException("Given index is larger than inventory size!");

    }

    //TODO Debugging ONLY
    public void listItems() {
        int i = 0;
        for (InventoryItem item : inventory) {
            if (item == null) continue;
            System.out.println("Item " + (i++) + "= " + item.getItemName());

        }
        System.out.println("Inventory size: " + currentInventoryItems);
        //System.out.println(inventory[0]);
    }

    public void render(SpriteBatch batcher) {
        OrthographicCamera textCam = GameState.instance.hud.textCam;
        batcher.draw(inventoryUI, -650f / 2f, textCam.position.y / 4 - textCam.viewportHeight / 2, 650, 80);
        for (int i = 0; i < inventory.length; i++) {

            InventoryItem key = inventory[i];
            if (key == null) continue;


            if (key.equals(itemSelected)) {
                batcher.draw(key.getActiveTexture(), itemShapes[i].x, itemShapes[i].y, 80, 80);
            } else {
                batcher.draw(key.getTexture(), itemShapes[i].x, itemShapes[i].y, 80, 80);
            }
            //System.out.println();

        }
        update(Gdx.graphics.getDeltaTime());
    }

    private void update(float delta) {
        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            if (Gdx.input.isKeyPressed(key)) {
                itemSelected = inventory[i];
            }
        }


    }

    public InventoryItem getItem() {
        return inventory[0];
    }

    public void removeItem(int index) {
        System.out.println(inventory[index].getItemName() + " has been removed from inventory");
        inventory[index] = null;
        inventoryItems.remove(index);
        currentInventoryItems--;
    }

    private void clearInventory() {
        inventoryItems.clear();
        inventory = new InventoryItem[MAX_INVENTORY_ITEMS];
        //See in hashmaps how there is a method to clear the array, it would be great of we could go back to it
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 point = GameState.instance.hud.textCam.unproject(new Vector3(screenX, screenY, 0));
        for (int i = 0; i < inventory.length; i++) {
            Rectangle r = itemShapes[i];
            if (r.contains(point.x, point.y)) {
                itemSelected = inventory[i];
                return true;
            }
        }
        return false;
    }
}
