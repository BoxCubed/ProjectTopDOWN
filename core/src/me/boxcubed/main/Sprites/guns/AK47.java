package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Application;
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
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Sprites.Bullet;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.TopDown;

import java.util.Random;

public class AK47 implements Gun, InventoryItem {
    private final Random random = new Random();
    private final Sound gunshotSound = TopDown.assets.get(Assets.gun_SOUND, Sound.class);
    // TODO get ak47 image
    private final Texture ak47img = TopDown.assets.get(Assets.inventoryAK47_IMAGE, Texture.class);
    private final Texture akActive = TopDown.assets.get(Assets.ak47Active_IMAGE, Texture.class);
    private float elapsedBulletTime = 0;
    private boolean firstFire;

    @Override
    public boolean willFire(Input input, float delta, Player player) {

        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            if (GameState.instance.firePressed) {
                if (elapsedBulletTime == -1) {
                    firstFire = true;
                    elapsedBulletTime = 0;

                    return true;
                }
                firstFire = false;
                elapsedBulletTime += delta;
                if (elapsedBulletTime > 10) {
                    elapsedBulletTime = 0f;
                    return true;
                }
            } else elapsedBulletTime = -1;
            return false;
        }

        if (Gdx.input.isButtonPressed(Buttons.LEFT) || input.isKeyPressed(Keys.SPACE)) {
            if (elapsedBulletTime == -1) {
                firstFire = true;
                elapsedBulletTime = 0;

                return true;
            }
            firstFire = false;
            elapsedBulletTime += delta;
            if (elapsedBulletTime > 10) {
                elapsedBulletTime = 0f;
                return true;
            }
        } else elapsedBulletTime = -1;
        return false;
    }

    @Override
    public void fire(World world, Player player) {
        boolean togg = random.nextBoolean();
        float randRotation = player.rotation;
        int ak47Offset = 1;
        if (togg)
            randRotation += random.nextInt(ak47Offset) + 3;
        else
            randRotation -= random.nextInt(ak47Offset) + 3;

        gunshotSound.play(1.0f);

        GameState.instance.entities.add(new Bullet(world, player.getPos(false).x, player.getPos(false).y - 0.4f,
                firstFire ? (float) Math.cos(Math.toRadians(player.rotation))
                        : (float) (Math.cos(Math.toRadians(randRotation))),
                firstFire ? (float) Math.sin(Math.toRadians(player.rotation)) :
                        (float) (Math.sin(Math.toRadians(randRotation))),
                randRotation, GunType.AK47, player));
        if (!firstFire)
            BoxoUtil.shake();

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
