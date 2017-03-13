package me.boxcubed.main.Objects.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

public class CollisionDetection implements ContactListener{

public CollisionDetection(){

}
	@Override
	public void beginContact(Contact contact) {
		for(Entity entity:GameState.instance.entities){
			if((entity.getFixture().equals(contact.getFixtureA())||entity.getFixture().equals(contact.getFixtureB()))){
					if(isOneOf("PLAYER", contact)){
					  entity.playAnimation("attack");
						GameState.instance.player.setHealth(GameState.instance.player.getHealth()-1);
					
		}else if(isOneOf("BULLET", contact)&&isOneOf("ZOMBIE", contact)){
			entity.setDisposable(true);
			Gdx.app.log("[TopDown]", "Bullet HIT!");
			continue;
			
		}
			
			
			
		}}
	}
	private boolean isOneOf(String key,Contact contact){
		return contact.getFixtureA().getUserData().equals(key)||contact.getFixtureB().getUserData().equals(key);
	}
	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
