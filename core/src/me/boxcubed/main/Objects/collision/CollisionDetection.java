package me.boxcubed.main.Objects.collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Sprites.Pack.PackType;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.States.GameState;

public class CollisionDetection implements ContactListener{

public CollisionDetection(){

}
	@Override
	public void beginContact(Contact contact) {
		for(Entity entity:GameState.instance.entities){
			if(entity.getFixture()!=null&&(entity.getFixture().equals(contact.getFixtureA())||entity.getFixture().equals(contact.getFixtureB()))){
					if(isOneOf("PLAYER", contact)&&isOneOf("ZOMBIE", contact)){
					  entity.playAnimation("attack");
						GameState.instance.player.setHealth(GameState.instance.player.getHealth()-1);
						GameState.instance.player.playAnimation("attacked");
						continue;
					
		}/*else if(isOneOf("BULLET", contact)){
			if(isOneOf("ZOMBIE", contact))
				entity.setDisposable(true);
			if(entity.getFixture().getUserData().equals("BULLET"))
				entity.setDisposable(true);
			continue;
			
		}*/			
		if(isOneInstanceOf(PackType.class, contact)&&isOneOf("PLAYER", contact)){
		     Player p=GameState.instance.player;
		     if(p.getHealth()>=p.getMaxHealth()) continue;
		     entity.setDisposable(true);
		     
		     p.setHealth(p.getHealth()+p.getMaxHealth()/4);
		}
			
			
			
		}}
	}
	private boolean isOneOf(Object key,Contact contact){
		return contact.getFixtureA().getUserData().equals(key)||contact.getFixtureB().getUserData().equals(key);
	}
	private boolean isOneInstanceOf(Class<?> key, Contact contact){
		return (contact.getFixtureA().getUserData().getClass().isAssignableFrom(key))||
				(contact.getFixtureB().getUserData().getClass().isAssignableFrom(key));
		
		
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
