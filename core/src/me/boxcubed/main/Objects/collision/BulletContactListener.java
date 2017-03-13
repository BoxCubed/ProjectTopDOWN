package me.boxcubed.main.Objects.collision;

import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

/**
 * Created by Tej Sidhu on 13/03/2017.
 * @deprecated Thats right. How do u feel. Now go move this into a class that works
 *        _..-''--'----_.
           ,''.-''| .---/ _`-._
         ,' \ \  ;| | ,/ / `-._`-.
       ,' ,',\ \( | |// /,-._  / /
       ;.`. `,\ \`| |/ / |   )/ /
      / /`_`.\_\ \| /_.-.'-''/ /
     / /_|_:.`. \ |;'`..')  / /
     `-._`-._`.`.;`.\  ,'  / /
         `-._`.`/    ,'-._/ /
           : `-/     \`-.._/
           |  :      ;._ (
           :  |      \  ` \
            \         \   |
             :        :   ;
             |           /
             ;         ,'
            /         /
           /         /
                    /
 */
public class BulletContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if(fa == null || fb == null)
            return;
        if(fa.getUserData() == null || fb.getUserData() == null)
            return;
        for(Entity entity: GameState.instance.entities){
            if((entity.getFixture().equals(contact.getFixtureA())||entity.getFixture().equals(contact.getFixtureB()))
                    &&(contact.getFixtureA().getUserData().equals("BULLET")||contact.getFixtureA().getUserData().equals("BULLET"))){
                System.out.println("HIT");

            }
        }
        System.out.println("Collision");
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
