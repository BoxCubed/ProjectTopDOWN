package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.utils.Assets;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.States.GameState;

public class Bullet extends Sprite implements Entity{
	
	BodyDef bulletDef;
	PolygonShape bulletShape;
	FixtureDef fixtureDefBullet;
	Body bulletBody;
	Fixture fixture;
	RayCastCallback callback;
	TextureRegion muzzleFlash;
	
	float rotation;
	public float SPEED = 1;
	
	float x,y,offX,offY;
	Player player;
	
	float elapsedTime=0;
	
	public Bullet(World world, float x, float y,float offX,float offY, float rotation,GunType type,Player player){
		super(TopDown.assets.get(Assets.bulletIMAGE, Texture.class));
		this.x=x;
		this.y=y;
		this.offX=offX;
		this.offY=offY;
		this.rotation = rotation;
		this.player=player;
		bulletDef = new BodyDef();
		bulletDef.type = BodyDef.BodyType.DynamicBody;
		
		// Shape
		bulletShape = new PolygonShape();
		bulletShape.setAsBox(0.01f, 0.01f);
		
		// Fixture def
		fixtureDefBullet = new FixtureDef();
		fixtureDefBullet.shape = bulletShape;

		fixtureDefBullet.friction = 0f;
		// Creates the body and assigns vars to all important values
		bulletBody = world.createBody(bulletDef);
		fixture=bulletBody.createFixture(fixtureDefBullet);
		fixture.setUserData("BULLET");
		
		muzzleFlash = new TextureRegion(TopDown.assets.get(Assets.mflashIMAGE, Texture.class));
		//Begin init of callback for bullet
		callback=new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				//System.out.println(point.sub(new Vector2(x, y)).len2());
				if (fixture.getUserData() == "WALL") {
					setDisposable(true);
					return 0;
				}
				
				else if(fixture.getUserData() == "ZOMBIE"){
					GameState.instance.entities.forEach(entity -> {
						if (entity.getFixture().equals(fixture)) {
							LivingEntity lentity=(LivingEntity)entity;
							if(type.equals(GunType.AK47)){
							lentity.setHealth(lentity.getHealth()-lentity.getMaxHealth()/10);
							lentity.playAnimation("attacked");
							}else lentity.setDisposable(true);
						}
					});
					setDisposable(true);
					return 0;
				}
				return 1;
			}
			
		};
		
    }
	 @Override
	    public void update(float delta) {
		
		 if(!isDisposable()){
			 
			 x+=offX*delta*SPEED;
			 y+=offY*delta*SPEED;
			 
			 getBody().setTransform(x, y, rotation);
			 
		 }else{return;}
		 if(getPos(true).x<0||getPos(true).y<0||getPos(true).x>1576||getPos(true).y>1576)
			 setDisposable(true);
		 if(GameState.instance.player.isAlive())
		 GameState.instance.getWorld().rayCast(callback, player.getBody().getPosition(),
					bulletBody.getPosition());
	 }
	 public void renderShapes(ShapeRenderer sr) {
	
	 }
	 @Override
		public void render(SpriteBatch sb) {
		 if(!isDisposable()){
			 elapsedTime+=Gdx.graphics.getDeltaTime();
			/* if(elapsedTime<0.1)sb.draw(muzzleFlash,GameState.instance.player.getPos().x, 
					 GameState.instance.player.getPos().y,offX,offY,40,20,1,1,rotation);
			*/
			 
		 sb.draw(this, x*GameState.PPM, y*GameState.PPM, 5, 5, 30, 20, 1, 1, rotation,true);
		 }else{
			 this.dispose();
		 }
	 }

	
	

	@Override
	public Body getBody() {
		return bulletBody;
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
	@Override
	public Vector2 getPos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
