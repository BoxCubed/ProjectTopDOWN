package me.boxcubed.main.Sprites.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
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

public class LaserGun implements Gun, InventoryItem,Disposable,Renderable {
	ConeLight light;
	Texture ak47img = TopDown.assets.get(Assets.inventoryAK47_IMAGE, Texture.class);
	Texture akActive = TopDown.assets.get(Assets.ak47Active_IMAGE,Texture.class);
	TextureRegion laser=new TextureRegion(TopDown.assets.get(Assets.laser_IMAGE,Texture.class));
	boolean firing=false,collide=false;
	float elapsedTime=0;
	final float SPEED=1;
	Player p;
	Vector2 pos,target;
	public LaserGun(Player p) {
		this.p=p;
		light=new ConeLight(GameState.instance.clock.rayHandler,100,Color.RED,1000f,0f,0f,0,1f);
		light.setActive(false);
		pos=new Vector2();
		
		
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
		//light.setActive(input.isKeyPressed(Keys.NUM_0));
		if(firing!=Gdx.input.isKeyPressed(Keys.SPACE)){
			elapsedTime=0;
			firing=Gdx.input.isKeyPressed(Keys.SPACE);
			pos.set(player.getPos(false));
			target=new Vector2();
			collide=false;
		
		}
		return Gdx.input.isKeyPressed(Keys.SPACE);
		
	}

	@Override
	public void fire(World world, Player player) {
		
		//light.setPosition(player.getPos(false).x, player.getPos(false).y);
		//light.setDirection(player.rotation);
		Vector2 off=new Vector2(player.crossH.offX, player.crossH.offY);
		
		pos.x+=off.x;
		pos.y+=off.y;
		world.rayCast(new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(!fixture.getUserData().equals("PLAYER")){
					if(collide)
						return 0;
					collide=true;
					target.set(point);
					
				return 0;}
				else {collide=false;return 1;}
			}
		}, player.getPos(false).x, player.getPos(false).y, pos.x, pos.y);
		

	}

	@Override
	public void netFire(NetworkManager net, World world, Player player) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void dispose() {
		light.remove();
		
	}
	@Override
	public void update(float delta) {
		
		
	}
	@Override
	public void render(SpriteBatch batch) {
		if(firing)
		batch.draw(laser, p.getPos(true).x, p.getPos(true).y, 0, 0, target.dst(p.getPos(false))*GameState.PPM, 20, 1, 1, p.rotation);
		
	}
	

}
