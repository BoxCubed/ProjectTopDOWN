
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class Player extends Sprite{
	public BodyDef playerDef;
	FixtureDef fixtureDefPlayer;
	CircleShape playerShape;
	public Body playerBody;
	public Player(World w) {
		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;
		playerDef.position.set(300, 400);
		//Shape
		playerShape = new CircleShape();
		playerShape.setRadius(1.5f);
		//Fixture def
		fixtureDefPlayer = new FixtureDef();
		fixtureDefPlayer.shape = playerShape;
		fixtureDefPlayer.density = 1f;
		fixtureDefPlayer.restitution = 0f;
		fixtureDefPlayer.friction = 2.5f;
		//Creates the body
		playerBody = w.createBody(playerDef);
		playerBody.createFixture(fixtureDefPlayer);
		//world.createBody(playerDef).createFixture(fixtureDefPlayer);
	
	}
	public void render(SpriteBatch sb){
		
		
	}
	public void handleInput(Input i){
		
		
		
	}
    public void goUP(){

    }
    public void goDOWN(){

    }
    public void goLEFT(){

    }
    public void goRIGHT(){

    }
}
