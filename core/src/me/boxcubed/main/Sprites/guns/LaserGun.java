package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.InventoryItem;

import box2dLight.Light;
import me.boxcubed.main.Objects.interfaces.Renderable;
import me.boxcubed.main.Sprites.Player;

public class LaserGun implements Gun, InventoryItem,Renderable {
	Light light;
	boolean firing =false;
	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return "LaserGun";
	}

	@Override
	public Texture getTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Texture getActiveTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean willFire(Input input, float delta, Player player) {
		return true;
	}

	@Override
	public void fire(World world, Player player) {
		

	}

	@Override
	public void netFire(NetworkManager net, World world, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

}
