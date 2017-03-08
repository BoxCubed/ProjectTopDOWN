package me.boxcubed.main.Objects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.boxcubed.main.States.GameState;

public class CollisionDetection implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		for(Entity entity:GameState.instance.entities){
			/*if((entity.getBody().equals(contact.getFixtureA().getBody())
					||entity.getBody().equals(contact.getFixtureA().getBody()))
					&&(GameState.instance.player.getBody().equals(contact.getFixtureA().getBody())
							||GameState.instance.player.getBody().equals(contact.getFixtureA().getBody()))){
				GameState.instance.healthNum-=1;
			}*/
			
			if((entity.getFixture().getUserData().equals(contact.getFixtureA().getUserData())||entity.getFixture().getUserData().equals(contact.getFixtureB().getUserData()))
					&&(contact.getFixtureA().getUserData().equals("PLAYER")||contact.getFixtureA().getUserData().equals("PLAYER"))){
				//System.out.println("Player touched zombie");
				GameState.instance.player.setHealth(GameState.instance.player.getHealth()-1);}
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
