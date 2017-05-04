package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.net.NetworkManager;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class Pistol implements Gun {
private Sound gunshotSound=TopDown.assets.get(Assets.gunSOUND,Sound.class);
	@Override
	public boolean willFire(Input input,float delta,Player p) {
		
		
		return BoxoUtil.isButtonJustPressed(Buttons.LEFT) || input.isKeyJustPressed(Keys.SPACE);
	}

	@Override
	public void fire(World world, Player player) {
		gunshotSound.play(1.0f);
		
		GameState.instance.entities
				.add(new Bullet(world, player.getPos().x, player.getPos().y, player.crossH.offX, player.crossH.offY,player.rotation,GunType.AK47));
			

	}

	@Override
	public void netFire(NetworkManager net,World world, Player player) {
		net.onFire(player.getPos(),player.rotation,this.getClass().getName());
		
	}

}
