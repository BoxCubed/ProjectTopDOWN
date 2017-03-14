package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

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
		fixture=bulletBody.createFixture(fixtureDefBullet);
		fixture.setUserData("BULLET");

		bulletBody.setTransform(x+3, y+3, 0);
		bulletShape.dispose();
	}
	 @Override
	    public void update(float delta) {
	    	 /*System.out.println(playerDirection);*/
		 if(isDisposable())return;
	        int velX = 0, velY = 0;
	        remove = true;
	        switch ((int) GameState.instance.player.getRotation()){
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
	        getBody().setLinearVelocity(new Vector2(velX, velY));
	        /*System.out.println(velX + " " + velY);
	        //This method is not needed yet
	        System.out.println(bulletBody.getPosition().x + "" + bulletBody.getPosition().y);*/
	        //No, just no
	    }
	 public void render(SpriteBatch sb) {

	 	
	 }

	
	@Override
	public Vector2 getPos() {
        return bulletBody.getPosition();
	}

	@Override
	public Body getBody() {
		return bulletBody;
	}

	@Override
	public void setPos(Vector2 pos) {
		
	}

	@Override
	public Sprite getSprite() {
		//TODO Add sprite
		return null;
	}

	

	


    
	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(bulletBody);
	}

	@Override
	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public void playAnimation(String key) {

	}

   

    @Override
	public String getID() {
		return "Bullet";
	}
    boolean disposable=false;
	@Override
	public boolean isDisposable() {
		return disposable;
	}
	@Override
	public void setDisposable(boolean disposable) {
		this.disposable=disposable;
	}

}
