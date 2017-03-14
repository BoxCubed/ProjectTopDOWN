package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

public class Bullet implements Entity{
	
	BodyDef bulletDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	private double vX,vY;
	
	
	public Bullet(World world, float x, float y){
		bulletDef = new BodyDef();
		bulletDef.type = BodyDef.BodyType.DynamicBody;
		// Shape
		bulletShape = new PolygonShape();
		bulletShape.setAsBox(2, 2);

		// Fixture def
		fixtureDefBullet = new FixtureDef();
		fixtureDefBullet.shape = bulletShape;

		fixtureDefBullet.friction = 0f;
		// Creates the body and assigns vars to all important values
		bulletBody = world.createBody(bulletDef);
		fixture=bulletBody.createFixture(fixtureDefBullet);
		fixture.setUserData("BULLET");
		
        if(GameState.instance.player.getRotation() < 90 && GameState.instance.player.getRotation() >0 || GameState.instance.player.getRotation() > 275){
            bulletBody.setTransform(x + 10, y , GameState.instance.player.getRotation());
        }else{
            bulletBody.setTransform(x - 10, y , GameState.instance.player.getRotation());
        }
        
		bulletShape.dispose();
		  float mouseX = GameState.instance.getMouseCords().x;
	      float mouseY = GameState.instance.getMouseCords().y;
        vX = (mouseX - GameState.instance.player.getBody().getPosition().x)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().x) * (mouseX - GameState.instance.player.getBody().getPosition().x)) + ((mouseY - GameState.instance.player.getBody().getPosition().y) *(mouseY - GameState.instance.player.getBody().getPosition().y)));
        vY = (mouseY - GameState.instance.player.getBody().getPosition().y)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().y) * (mouseX - GameState.instance.player.getBody().getPosition().y)) + ((mouseY - GameState.instance.player.getBody().getPosition().x) *(mouseY - GameState.instance.player.getBody().getPosition().x)));
        bulletBody.setLinearVelocity((float) vX *200, (float) vY *200);


    }
	 @Override
	    public void update(float delta) {
	    	 /*System.out.println(playerDirection);*/
		 if(isDisposable())return;
          bulletBody.setLinearVelocity((float) vX *200, (float) vY *200);
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
