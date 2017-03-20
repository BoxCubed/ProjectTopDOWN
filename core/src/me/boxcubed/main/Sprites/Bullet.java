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
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.States.GameState;

public class Bullet extends Sprite implements Entity{
	
	BodyDef bulletDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	float rotation;
	public float SPEED = 5;
	
	float x,y,offX,offY;
	
	boolean lookRight, lookLeft;
	
	public Bullet(World world, float x, float y,float offX,float offY){
		super(new Texture(Gdx.files.internal("assets/img/bullet.png")));
		this.x=x;
		this.y=y;
		this.offX=offX;
		this.offY=offY;
		rotation = GameState.instance.player.getRotation();
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
		
		System.out.println(rotation);
		
    }
	 @Override
	    public void update(float delta) {
		 if(rotation<90||rotation>270){lookRight=true;}
		 if(rotation<=270&&rotation>=90){lookLeft=true;}
		 
		 if(!isDisposable()){
			 
			 x+=offX*delta*SPEED;
			 y+=offY*delta*SPEED;
			 
			 if(lookRight){getBody().setTransform(x+10, y-5, rotation);}
			 else {getBody().setTransform(x-10, y+12, rotation);}
			 
		 }else{return;}
	 }
	 public void renderShapes(ShapeRenderer sr) {
	
	 }
	 @Override
		public void render(SpriteBatch sb) {
		 if(!isDisposable()){
		 if(lookRight)sb.draw(this, x+5, y-8, 5, 5, 7, 7, 1, 1, rotation,true);
		 if(lookLeft)sb.draw(this, x-17, y+5, 5, 5, 7, 7, 1, 1, rotation,true);
		 }else{
			 this.dispose();
		 }
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
	public EntityType getID() {
		return EntityType.BULLET;
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
