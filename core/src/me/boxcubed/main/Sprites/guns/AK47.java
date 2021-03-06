package me.boxcubed.main.Sprites.guns;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;
import com.boxcubed.utils.InventoryItem;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class AK47 implements Gun, InventoryItem {
	private float elapsedBulletTime = 0;
	private final Random random = new Random();
	private final int ak47Offset = 1;
	private float randRotation;
	private Sound gunshotSound = TopDown.assets.get(Assets.gun_SOUND, Sound.class);
	// TODO get ak47 image
	Texture ak47img = TopDown.assets.get(Assets.inventoryAK47_IMAGE, Texture.class);
	Texture akActive = TopDown.assets.get(Assets.ak47Active_IMAGE,Texture.class);
	@Override
	public boolean willFire(Input input, float delta, Player player) {
		if (Gdx.input.isButtonPressed(Buttons.LEFT) || input.isKeyPressed(Keys.SPACE)) {
			elapsedBulletTime += delta;
			if (elapsedBulletTime > 10) {
				
				elapsedBulletTime = 0;
				return true;
			}
		} else
			elapsedBulletTime = 0;
		return false;
	}

	@Override
	public void fire(World world, Player player) {
		boolean togg = random.nextBoolean();
		randRotation = player.rotation;
		if (togg)
			randRotation += random.nextInt(ak47Offset) + 3;
		else
			randRotation -= random.nextInt(ak47Offset) + 3;
		
		gunshotSound.play(1.0f);

		GameState.instance.entities.add(new Bullet(world, player.getPos(false).x, player.getPos(false).y-0.4f,
				(float) (Math.cos(Math.toRadians(randRotation))), (float) (Math.sin(Math.toRadians(randRotation))),
				randRotation, GunType.AK47, player));
		BoxoUtil.shake(2, 10);

	}

	@Override
	public void netFire(NetworkManager net, World world, Player player) {
		net.onFire(player.getPos(false), player.rotation, this.getClass().getSimpleName());

	}

	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return "AK47";
	}

	@Override
	public Texture getTexture() {
		// TODO Auto-generated method stub
		return ak47img;
	}

	// TODO Remove this
	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Texture getActiveTexture() {
		// TODO Auto-generated method stub
		return akActive;
	}

}
