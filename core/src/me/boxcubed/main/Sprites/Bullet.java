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
	Player player;
	public Bullet(World world, float x, float y){
		System.out.println(x + "	" + y );
		playerDef = new BodyDef();
		playerDef.type = BodyDef.BodyType.DynamicBody;
		// Shape
		bulletShape = new PolygonShape();
		bulletShape.setAsBox(2, 2);

		// Fixture def
		fixtureDefBullet = new FixtureDef();
		fixtureDefBullet.shape = bulletShape;

		fixtureDefBullet.friction = 0f;
		// Creates the body and assigns vars to all important values
		bulletBody = world.createBody(playerDef);
		fixture = bulletBody.createFixture(fixtureDefBullet);

		System.out.println("Bullet created  " + world.getBodyCount());
		/*bulletBody.setTransform(player.playerBody.getPosition().x, player.playerBody.getPosition().y, 0);*/
        bulletBody.setTransform(x+3, y+3, 0);
		bulletShape.dispose();
		bulletBody.setLinearVelocity(200, 0);
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

	

	public void update(float delta, float playerDirection) {//Player direction is need to see which way to fire the bullet
        /*System.out.println(playerDirection);*/
        int velX = 0, velY = 0;
        remove = true;
        switch ((int) playerDirection){
            case 0:  //RIGHT
                velX = 200;
            case -180: //LEFT
                velX = -200;
            case 90: //UP
                velY = 200;
            case -90:   //DOWN
                velY = -200;
            default:
            	velX = 0;
            	velY = 0;
        }
        System.out.println(velX + " " + velY);
        //This method is not needed yet
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
    public void update(float delta) {

    }

    @Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
