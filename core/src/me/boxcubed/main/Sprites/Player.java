
package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 23/02/2017.
 */
public class Player extends Sprite{
	Body body;
	public Player(World w) {
		BodyDef bd=new BodyDef();
		w.createBody(bd);
	
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
