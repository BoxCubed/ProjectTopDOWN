package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import me.boxcubed.main.Objects.interfaces.Entity;
/**
 * Uses unit circle(kinda) to place a crosshair in front of the player<br>
 * See? Math class is useful
 * @author ryan9
 *
 */
public class Crosshair extends Sprite{
	//REMEMBER USE COS
	float dist=5;
	Vector2 pos;
	Entity player;
	
	public Crosshair(float dist,Entity player) {
		super( new Texture(Gdx.files.internal("assets/img/crosshair.png")));
		this.dist=dist;
		this.player=player;
		pos=new Vector2();
	}
	public void update(float delta){
		float angle=player.getSprite().getRotation();
		pos.x=(float) (player.getBody().getPosition().x+Math.cos(Math.toRadians(angle))*dist);
		pos.y=(float) (player.getBody().getPosition().y+Math.sin(Math.toRadians(angle))*dist);
		
		
		Gdx.input.setCursorCatched(true);
		
	}
	public void render(SpriteBatch sb){
		//sr.rect(pos.x, pos.y, 5, 5);
		sb.draw(this, pos.x, pos.y,20,20);
	}

}
