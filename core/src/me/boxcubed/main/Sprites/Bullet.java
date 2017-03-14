package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

public class Bullet implements Entity{
	Animation<TextureRegion> sexwithryansdad;
	BodyDef bulletDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	public Boolean remove;
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

		bulletBody.setTransform(x+10, y+10, GameState.instance.player.getRotation());
		bulletShape.dispose();
        float mouseX = Gdx.input.getX()-300;
        float mouseY = Gdx.graphics.getHeight()-Gdx.input.getY();
        double velY = (Math.sqrt(Math.pow((GameState.instance.player.getBody().getPosition().x - mouseX), 2) + Math.pow((GameState.instance.player.getBody().getPosition().y - mouseY ),2)))* Math.sin(GameState.instance.player.getRotation());
        double velX = (Math.sqrt(Math.pow((GameState.instance.player.getBody().getPosition().x - mouseX), 2) + Math.pow((GameState.instance.player.getBody().getPosition().y - mouseY ),2)))* Math.cos(GameState.instance.player.getRotation());;

        double vX = (mouseX - GameState.instance.player.getBody().getPosition().x)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().x) * (mouseX - GameState.instance.player.getBody().getPosition().x)) + ((mouseY - GameState.instance.player.getBody().getPosition().y) *(mouseY - GameState.instance.player.getBody().getPosition().y)));
        double vY = (mouseY - GameState.instance.player.getBody().getPosition().y)/Math.sqrt(((mouseX - GameState.instance.player.getBody().getPosition().y) * (mouseX - GameState.instance.player.getBody().getPosition().y)) + ((mouseY - GameState.instance.player.getBody().getPosition().x) *(mouseY - GameState.instance.player.getBody().getPosition().x)));
        System.out.println(vX*100 + "   " + vY*100);
        bulletBody.setLinearVelocity((float) vX *100, (float) vY *100);


    }
	 @Override
	    public void update(float delta) {
	    	 /*System.out.println(playerDirection);*/
		 if(isDisposable())return;
          remove = true;
	       /* switch ((int) GameState.instance.player.getRotation()){
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
	        }*/

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
