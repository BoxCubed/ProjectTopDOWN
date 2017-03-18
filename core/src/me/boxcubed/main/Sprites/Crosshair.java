package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;
/**
 * Uses unit circle(kinda) to place a crosshair in front of the player<br>
 * See? Math class is useful
 * @author ryan9
 *
 */
public class Crosshair extends Sprite implements Entity{
	//REMEMBER USE COS
	float dist=5;
	public float offX,offY;
	Vector2 pos;
	Entity player;

	public Crosshair(float dist,Entity player) {
		super( new Texture(Gdx.files.internal("assets/img/crosshair.png")));
		this.dist=dist;
		this.player=player;
		pos=new Vector2();
		Gdx.input.setCursorCatched(true);
	}
	public void update(float delta){		
		float angle=player.getSprite().getRotation();
		offX=(float) (Math.cos(Math.toRadians(angle)));
		offY=(float) (Math.sin(Math.toRadians(angle)));
		pos.x=(float) (player.getBody().getPosition().x+offX*dist);
		pos.y=(float) (player.getBody().getPosition().y+offY*dist);

		
		//Gdx.input.setCursorCatched(true);
		
		
	}
	public void render(SpriteBatch sb){
		sb.draw(this, GameState.instance.getMouse().x,GameState.instance.getMouse().y,20,20);
	}
	@Override
	public Vector2 getPos() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setPos(Vector2 pos) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Sprite getSprite() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void playAnimation(String key) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Fixture getFixture() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isDisposable() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setDisposable(boolean disposable) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void renderShapes(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}	

}
