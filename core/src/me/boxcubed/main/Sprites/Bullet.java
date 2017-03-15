package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.States.GameState;

public class Bullet extends Sprite implements Entity{
	
	BodyDef bulletDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	float rotation;
	public int SPEED = 3;
	
	float x,y;
	
	public Bullet(World world, float x, float y,float rotation){
		super(new Texture(Gdx.files.internal("assets/img/bullet.png")));
		this.x=x;
		this.y=y;
		this.rotation=rotation;
				
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
		
		bulletBody.setTransform(new Vector2(x+15,y+10),0);
    }
	 @Override
	    public void update(float delta) {
		 if(!isDisposable()){
		    if(rotation<35||rotation>340){
				 x+=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<339&&rotation>297){
				 x+=SPEED*delta;
				 y-=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<296&&rotation>243){
				 y-=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<242&&rotation>205){
				 y-=SPEED*delta;
				 x-=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<204&&rotation>166){
				 x-=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<165&&rotation>128){
				 x-=SPEED*delta;
				 y+=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<127&&rotation>75){
				 y+=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    if(rotation<74&&rotation>36){
				 x+=SPEED*delta;
				 y+=SPEED*delta;
				 bulletBody.setTransform(new Vector2(x,y),0);
			}
		    
		 }else{return;}
	 }
	 public void renderShapes(ShapeRenderer sr) {
	
	 }
	 @Override
		public void render(SpriteBatch sb) {
		 if(!isDisposable()){
		 sb.draw(this, x-8, y-4, (float)13, 7);}
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
