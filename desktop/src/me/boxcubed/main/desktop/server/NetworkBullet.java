package me.boxcubed.main.desktop.server;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.States.GameState;

public class NetworkBullet implements Entity{
	
		
		private BodyDef bulletDef;
		private PolygonShape bulletShape;
		private FixtureDef fixtureDefBullet;
		private Body bulletBody;
		private Fixture fixture;
		private World world;
		private final RayCastCallback callback;
		
		float rotation;
		public float SPEED = 20;
		
		float x,y,offX,offY;
		
		boolean lookRight, lookLeft;
		
		float elapsedTime=0;
		KyroServer server;
		
		public NetworkBullet(World world, float x, float y,float offX,float offY, float rotation,KyroServer server){
			this.x=x;
			this.y=y;
			this.offX=offX;
			this.offY=offY;
			this.rotation = rotation;
			this.world=world;
			this.server=server;
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
			
			//Begin init of callback for bullet
			callback=new RayCastCallback() {
				
				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
					if (fixture.getUserData() == "WALL") {
						setDisposable(true);
						return 0;
					}

					else if(fixture.getUserData() == "PLAYER"){
						// better disposal...DONE!
						server.players.forEach((id,player)->{
							if(player.player.getFixture().equals(fixture)){
								player.player.setHealth(player.player.getHealth()-10);
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
			 if(rotation<90||rotation>270){lookRight=true;}
			 if(rotation<=270&&rotation>=90){lookLeft=true;}
			
			 if(!isDisposable()){
				 
				 x+=offX*delta*SPEED;
				 y+=offY*delta*SPEED;
				 
				 if(lookRight){getBody().setTransform(x+10, y-5, rotation);}
				 else {getBody().setTransform(x-10, y+12, rotation);}
				 
			 }else{dispose();}
			 if(getBody().getPosition().x<0||getBody().getPosition().y<0||getBody().getPosition().x>1576||getBody().getPosition().y>1576)
				 setDisposable(true);
			world.rayCast(callback, GameState.instance.player.getBody().getPosition(),
						bulletBody.getPosition());
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
		public Sprite getSprite() {
			//TODO Add sprite
			return null;
		}

		

		


	    
		@Override
		public void dispose() {
			world.destroyBody(bulletBody);
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
		public void render(SpriteBatch sb) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void renderShapes(ShapeRenderer sr) {
			// TODO Auto-generated method stub
			
		}
		
		

	}



