package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.InventoryItem;

import box2dLight.DirectionalLight;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Renderable;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class LaserGun implements Gun, InventoryItem,Renderable,Disposable {
	DirectionalLight light;
	Texture ak47img = TopDown.assets.get(Assets.inventoryAK47, Texture.class);
	Texture akActive = TopDown.assets.get(Assets.ak47ActiveIMAGE,Texture.class);
	public LaserGun() {
		light=new DirectionalLight(GameState.instance.clock.rayHandler, 100, Color.RED, -90);
		light.setActive(false);
		
	}
	@Override
	public String getItemName() {
		return "LaserGun";
	}

	@Override
	public Texture getTexture() {
		return ak47img;
	}

	@Override
	public Texture getActiveTexture() {
		return akActive;
	}

	@Override
	public int getIndex() {
		return 2;
	}

	@Override
	public boolean willFire(Input input, float delta, Player player) {
		light.setActive(input.isKeyPressed(Keys.NUM_0));
		return light.isActive();
		
	}

	@Override
	public void fire(World world, Player player) {
		
		light.setPosition(1, 2);
		
		

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

	@Override
	public void dispose() {
		light.remove();
		
	}

}
