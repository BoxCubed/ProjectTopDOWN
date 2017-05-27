package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.InventoryItem;

import box2dLight.ConeLight;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Renderable;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class LaserGun implements Gun, InventoryItem,Renderable,Disposable {
	private final ConeLight light;
	private final Texture ak47img = TopDown.assets.get(Assets.inventoryAK47_IMAGE, Texture.class);
	private final Texture akActive = TopDown.assets.get(Assets.ak47Active_IMAGE,Texture.class);
	public LaserGun() {
		light=new ConeLight(GameState.instance.clock.rayHandler,100,Color.RED,1000f,0f,0f,0f,10f);
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
		
		light.setPosition(player.getPos(false).x, player.getPos(false).y);
		
		

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
	public void renderShapes(ShapeRenderer sr) {

	}

	@Override
	public void dispose() {
		light.remove();
		
	}

}
