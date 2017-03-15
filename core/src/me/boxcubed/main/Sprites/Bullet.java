package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
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
	private float rotation;
	
	
	public Bullet(World world, float x, float y){
		bulletDef = new BodyDef();
		bulletDef.type = BodyDef.BodyType.DynamicBody;
		// Shape
		bulletShape = new PolygonShape();
		bulletShape.setAsBox(1, 1);

		// Fixture def
		fixtureDefBullet = new FixtureDef();
		fixtureDefBullet.shape = bulletShape;

		fixtureDefBullet.friction = 0f;
		// Creates the body and assigns vars to all important values
		bulletBody = world.createBody(bulletDef);
		fixture=bulletBody.createFixture(fixtureDefBullet);
		fixture.setUserData("BULLET");
		
        bulletBody.setTransform(x + 20, y , GameState.instance.player.getRotation());
       
        
		bulletShape.dispose();
		rotation=GameState.instance.player.getRotation();
		  float mouseX = GameState.instance.getMouseCords().x;
	      float mouseY = GameState.instance.getMouseCords().y;
	      System.out.println(bulletBody.getLinearVelocity());
        vX = (mouseX - GameState.instance.player.getBody().getPosition().x)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().x) * (mouseX - GameState.instance.player.getBody().getPosition().x)) + ((mouseY - GameState.instance.player.getBody().getPosition().y) *(mouseY - GameState.instance.player.getBody().getPosition().y)));
        vY = (mouseY - GameState.instance.player.getBody().getPosition().y)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().y) * (mouseX - GameState.instance.player.getBody().getPosition().y)) + ((mouseY - GameState.instance.player.getBody().getPosition().x) *(mouseY - GameState.instance.player.getBody().getPosition().x)));
       // bulletBody.applyLinearImpulse(new Vector2(0, 0), bulletBody.getWorldCenter(), true);
        bulletBody.applyForceToCenter(10000000000.0f, 10000000000.0f, true);
    }
	 @Override
	    public void update(float delta) {
	    	 /*System.out.println(playerDirection);*/
		 if(isDisposable())return;
	    }
	 public void renderShapes(SpriteBatch sb) {

	 }
	 @Override
		public void render(SpriteBatch sb) {
			// TODO Auto-generated method stub
			
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
