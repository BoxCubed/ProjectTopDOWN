package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.Entity;

public class Bullet implements Entity{
	Animation<TextureRegion> sexwithryansdad;
	BodyDef playerDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	public Boolean remove;
	public Bullet(World world){
	    remove = new Boolean(false);
		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;
		// Shape
		bulletShape = new PolygonShape();
		bulletShape.setAsBox(5, 5);

		// Fixture def
		fixtureDefBullet = new FixtureDef();
		fixtureDefBullet.shape = bulletShape;

		fixtureDefBullet.friction = 0f;
		// Creates the body and assigns vars to all important values
		bulletBody = world.createBody(playerDef);
		fixture = bulletBody.createFixture(fixtureDefBullet);

		System.out.println("Bullet created  " + world.getBodyCount());
		bulletBody.setTransform(100, 100, 0);

		bulletShape.dispose();
	}

	@Override
	public Vector2 getPos() {
		return null;
	}

	@Override
	public Body getBody() {
		return null;
	}

	@Override
	public void setPos(Vector2 pos) {
		
	}

	@Override
	public Sprite getSprite() {
		return null;
	}

	
	@Override
	public void update(float delta) {
        remove = true;
        bulletBody.setLinearVelocity(20, 0);
        System.out.println(bulletBody.getPosition().x + "" + bulletBody.getPosition().y);
    }


    public void render(SpriteBatch sb) {
     //Render the bullet

    }

	@Override
	public void dispose() {

	}

	@Override
	public Fixture getFixture() {
		return null;
	}

	@Override
	public void playAnimation(String key) {

	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
