package me.boxcubed.main.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

/**
 * 
 * @author not this guy ->ryan9
 *
 */
public class Bullet implements Entity{
	Body tej;
	BodyDef definitionofgay;
	Vector2 tejspositioninlife;
	Animation<TextureRegion> sexwithryansdad;
	public Bullet(Vector2 pos){
		tej=GameState.instance.gameWORLD.createBody(definitionofgay);
		tejspositioninlife=new Vector2(0,0);
	}

	@Override
	public Vector2 getPos() {
		return tejspositioninlife;
	}

	@Override
	public Body getBody() {
		return tej;
	}

	@Override
	public void setPos(Vector2 pos) {
		
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	@Override
	public Animation<TextureRegion> animation() {
		// TODO Auto-generated method stub
		return sexwithryansdad;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(SpriteBatch sb) {
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

}
