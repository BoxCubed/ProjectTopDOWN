package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.boxcubed.utils.Assets;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;
/**
 * Uses unit circle(kinda) to place a crosshair in front of the player<br>
 * See? Math class is useful
 * @author ryan9
 *
 */
public class Crosshair extends Sprite{
	//REMEMBER USE COS
	float dist=5;
	public float offX,offY;
	Vector2 pos;
	Entity player;

	public Crosshair(float dist,Entity player) {
		super( TopDown.assets.get(Assets.crossHairIMAGE, Texture.class));
		this.dist=dist;
		this.player=player;
		pos=new Vector2();
		//Gdx.input.setCursorCatched(true);
	}
	public void update(float delta){
		float angle=player.getSprite().getRotation();
		offX=(float) (Math.cos(Math.toRadians(angle)));
		offY=(float) (Math.sin(Math.toRadians(angle)));
		pos.x=(float) ((player.getPos(true).x)*GameState.PPM+offX*dist);
		pos.y=(float) ((player.getPos(true).y)*GameState.PPM+offY*dist);

		
		
		
	}
	public void render(SpriteBatch sb){
		sb.draw(this, pos.x, pos.y,20,20);
	}

}
