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
	
	private final RayCastCallback callback;
	private final TextureRegion muzzleFlash;
	
	private final float rotation;
	private final float SPEED = 0.5f;
	
	private float x;
	private float y;
	private final float offX;
	private final float offY;
	private final Vector2 firePos;
	private final Player player;
	private final PointLight flash;
	private float flashDist=0.8f;
	private float elapsedTime=0;
	
	public Bullet(World world, float x, float y,float offX,float offY, float rotation,final GunType type,Player player){
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
				//System.out.println(point.sub(new Vector2(x, y)).len2());
				if (fixture.getUserData() == "WALL") {
					setDisposable(true);
					return 0;
				}
				
				else if(fixture.getUserData() == "ZOMBIE"){
					for(Entity entity:GameState.instance.entities){
						if (entity.getFixture()!=null&&entity.getFixture().equals(fixture)) {
							LivingEntity lentity=(LivingEntity)entity;
							if(type.equals(GunType.AK47)){
								lentity.setHealth(lentity.getHealth()-lentity.getMaxHealth()/10);
								lentity.playAnimation("attacked");
							}else lentity.setDisposable(true);
						}
					}

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
			 sr.setColor(toRGB());
			 sr.line(getPos(true), player.getPos(true));
			 }
	
	 }
	 private Color toRGB() {
		  float RED = 255 / 255.0f;
		  float GREEN = 200 / 255.0f;
		  float BLUE = 14 / 255.0f;
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
    private boolean disposable=false;
	@Override
	public boolean isDisposable() {
		return disposable;
	}
	@Override
	public void setDisposable(boolean disposable) {
		this.disposable=disposable;
	}
	
	

}
