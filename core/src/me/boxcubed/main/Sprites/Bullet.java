package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.utils.Assets;

import box2dLight.PointLight;
import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.GunType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.States.GameState;

public class Bullet extends Sprite implements Entity{
	
	RayCastCallback callback;
	TextureRegion muzzleFlash;
	
	float rotation;
	public float SPEED = 0.5f;
	
	float x,y,offX,offY;
	Vector2 firePos;
	Player player;
	PointLight flash;
	float flashDist=0.4f;
	float elapsedTime=0;
	
	Vector2 normalvector;
	Vector2 collision;
	
	public Bullet(World world, float x, float y,float offX,float offY, float rotation,GunType type,Player player){
		super(TopDown.assets.get(Assets.bullet_IMAGE, Texture.class));
		this.x=x;
		this.y=y;
		this.offX=offX;
		this.offY=offY;
		this.rotation = rotation;
		this.player=player;
		firePos=new Vector2(x, y);
		
		flash=new PointLight(GameState.instance.clock.rayHandler,50);
		flash.setDistance(flashDist);
		flash.setXray(true);
		flash.setColor(Color.YELLOW);
		flash.setSoft(false);
		
		
		muzzleFlash = new TextureRegion(TopDown.assets.get(Assets.mflash_IMAGE, Texture.class));
		callback=new RayCastCallback() {
			
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
       		if (fixture.getUserData() == "WALL") {
					setDisposable(true);
					return 0;
				}
				
				else if(fixture.getUserData() == "ZOMBIE"){
					GameState.instance.entities.forEach(entity -> {
						if (entity.getFixture()!=null&&entity.getFixture().equals(fixture)) {
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
			 
			 
		 }else{return;}
		 flash.setPosition(x,y);
		 flashDist-=0.03f*delta;
		 flash.setDistance(flashDist);
		 
		 if(getPos(true).x<0||getPos(true).y<0||getPos(true).x>1576||getPos(true).y>1576)
			 setDisposable(true);
		 if(GameState.instance.player.isAlive())
		 GameState.instance.getWorld().rayCast(callback, player.getBody().getPosition(),
					getPos(false));
	 }
	 public void renderShapes(ShapeRenderer sr) {
		 if(TopDown.debug){
			 sr.setColor(toRGB(255,200,14));
			 sr.line(getPos(true), player.getPos(true));
			 
				sr.setColor(Color.BLUE);
			 }
	
	 }
	 public Color toRGB(int r, int g, int b) {
		  float RED = r / 255.0f;
		  float GREEN = g / 255.0f;
		  float BLUE = b / 255.0f;
		  return new Color(RED, GREEN, BLUE, 1);
		 }
	 @Override
		public void render(SpriteBatch sb) {
		 if(!isDisposable()){
			 elapsedTime+=Gdx.graphics.getDeltaTime();
			/* if(elapsedTime<0.1)sb.draw(muzzleFlash,GameState.instance.player.getPos().x, 
					 GameState.instance.player.getPos().y,offX,offY,40,20,1,1,rotation);
			*/
			 
		 sb.draw(this, x*GameState.PPM-5, y*GameState.PPM,0, 0, 20, 5, 1, 1, rotation,true);
		 }else{
			 this.dispose();
		 }
	 }

	
	

	@Override
	public Body getBody() {
		return null;
	}

	@Override 
	public Vector2 getPos(boolean asPix){
		if(asPix)
			return new Vector2(x, y).scl(GameState.PPM);
		return new Vector2(x,y);
		
		
	}
	

	@Override
	public Sprite getSprite() {
		//TODO Add sprite
		return null;
	}

	

	


    
	@Override
	public void dispose() {
		flash.remove();
	}

	@Override
	public Fixture getFixture() {
		return null;
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
