package me.boxcubed.main.Objects.collision;

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
			if((entity.getFixture().equals(contact.getFixtureA())||entity.getFixture().equals(contact.getFixtureB()))
					&&(contact.getFixtureA().getUserData().equals("PLAYER")||contact.getFixtureA().getUserData().equals("PLAYER"))){
			    entity.playAnimation("attack");
				GameState.instance.player.setHealth(GameState.instance.player.getHealth()-1);
				}
		
		}
	}
	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

	
}
