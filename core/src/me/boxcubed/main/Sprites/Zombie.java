package me.boxcubed.main.Sprites;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.utils.Assets;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.States.GameState;

public class Zombie extends Sprite implements LivingEntity {
	public BodyDef Def;
	FixtureDef fixtureDef;
	CircleShape Shape;
	private Body Body;
	Fixture fixture;
	double health;
	Vector2 position,vel,target;
	SteeringAI ai;
	boolean attack;
	float attackTime;
	Sound attackSound;
	private Animation<TextureRegion> zombieAnim,zombieWalk;
	private ParticleEffect bloodEffect = new ParticleEffect(TopDown.assets.get(Assets.bloodEFFECT, ParticleEffect.class));
	private boolean hurt;
	private float walkTime=0;
	private TextureRegion healthBar =new TextureRegion(TopDown.assets.get(Assets.staminaIMAGE, Texture.class));
	boolean idle;
		
	Vector2 p1,p2,collision,normal;
	
	RayCastCallback callback;
	
	@SuppressWarnings("unchecked")
	public Zombie(World world,SteeringAI playerAI) {
		super( TopDown.assets.get(Assets.zombieIMAGE, Texture.class));
		attackSound =TopDown.assets.get(Assets.ZAttackSOUND, Sound.class);
		
		
		setSize(50, 50);
		health=100;
		ai=new SteeringAI(this, 50);
		ai.setBehavior(new Seek<>(ai, playerAI));
		Def = new BodyDef();
		Def.type = BodyDef.BodyType.DynamicBody;
		Def.position.set(300 / GameState.PPM, 400 / GameState.PPM);
		
		// Shape
		Shape = new CircleShape();
		Shape.setRadius(10/GameState.PPM);
		
		// Fixture def
		fixtureDef = new FixtureDef();
		fixtureDef.shape = Shape;

		fixtureDef.friction = 0f;
		// Creates the body and assigns vars to all important values
		Body = world.createBody(Def);
		fixture = Body.createFixture(fixtureDef);
		fixture.setUserData("ZOMBIE");
		Shape.dispose();
		zombieAnim = TopDown.assets.get(Assets.zombieAttackATLAS+":anim", Animation.class);
	    zombieWalk = TopDown.assets.get(Assets.zombieWalkATLAS+":anim", Animation.class);
	        
	        
		callback = new RayCastCallback(){

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if(fixture.getUserData().equals("ZOMBIE")||fixture.getUserData().equals("PLAYER")){
					
				}else{
						getBody().setLinearVelocity(getBody().getLinearVelocity().x-=Gdx.graphics.getDeltaTime(),getBody().getLinearVelocity().y-=Gdx.graphics.getDeltaTime());
						getBody().setAngularVelocity(0);
					
				
				idle=true;
				}
				
				return 1;
			}
			
		};
		/*zombieGroan.play(0.05f,1,0);*/
	
	}

	@Override
	public void update(float delta) {
		
		
		idle=false;
		
		p1 = getPos(false);
		p2=GameState.instance.player.getPos(false);
		if(GameState.instance.player.isAlive())
		GameState.instance.getWorld().rayCast(callback, p1,p2);
		if (hurt) {
			bloodEffect.setPosition(getPos(true).x, getPos(true).y);
			bloodEffect.update(delta / 100);

			if (bloodEffect.isComplete()){
				hurt = false;
				bloodEffect.reset();
			}
		}

		
		if(GameState.instance.player.isAlive()&&isAlive()&&!idle){
			
		ai.update(delta);
		if(attack)attackTime+=delta;
			
		}
		else if(isAlive()&&GameState.instance.player.isAlive()){
			getBody().setLinearVelocity(0,0);
			getBody().setAngularVelocity(0);
		}

	
	}
	
	@Override
	public void render(SpriteBatch sb) {
		 walkTime += Gdx.graphics.getDeltaTime();
		 bloodEffect.draw(sb);

		 if(!idle){
				if(!attack){
					sb.draw(zombieWalk.getKeyFrame(walkTime, true), getPos(true).x-15, getPos(true).y-20, getWidth()/2, getHeight()/2 , 40, 40, 
							1, 1, (float)Math.toDegrees(Body.getAngle())+90);}
				else {
					sb.draw(zombieAnim.getKeyFrame(attackTime, false), getPos(true).x-25, getPos(true).y-20, getWidth()/2, getHeight()/2, 40, 40, 
							1, 1, (float)Math.toDegrees(Body.getAngle())+90);
					
					if(zombieAnim.isAnimationFinished(attackTime))
					attack=false;	 
				
				}
		 }else{
			 sb.draw(zombieWalk.getKeyFrame(walkTime, true), getPos(true).x-25, getPos(true).y-20, getWidth()/2, getHeight()/2, 40, 40, 
						1, 1, (float)Math.toDegrees(Body.getAngle())+90);
		 }
		 float size=(float)(getHealth()/getMaxHealth()*40f);
		 sb.draw(healthBar, getPos(true).x-10, getPos(true).y+20,0 , 0, size, 10, 1, 1,0);
		 
		 
	}
	boolean rayEnabled;
	
	@Override
	public void renderShapes(ShapeRenderer sr) {
		
		if(Gdx.input.isKeyJustPressed(Keys.P)){
			rayEnabled=!rayEnabled;
		}
		if(rayEnabled){
			Gdx.gl.glLineWidth(1);
			sr.setColor(Color.RED);
			sr.line(p1.scl(GameState.PPM), p2.scl(GameState.PPM));
		}
		/*sr.set(ShapeType.Filled);
		sr.setColor(Color.GREEN);
		float size=(float)(getHealth()/getMaxHealth()*40f);
		sr.rect(getPos().x-10, getPos().y+20, size, 10);*/
	}
	
	
	

	
	@Override
	public Sprite getSprite() {
		return this;
	}

	@Override
	public Body getBody() {
		return Body;
	}

	@Override
	public void dispose() {
		attackSound.dispose();
		GameState.instance.getWorld().destroyBody(Body);
		bloodEffect.dispose();
		
		
		
	}

	@Override
	public Fixture getFixture() {
		return fixture;
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth(double health) {
		this.health=health;
	}

	@Override
	public double getMaxHealth() {
		return 100;
	}
	Random rand=new Random();
	@Override
	public void playAnimation(String key) {
		if(key.toUpperCase().equals("ATTACK")){
			
			attack=true;attackTime=0;
			if(rand.nextFloat()<0.25f)
			attackSound.play();
		}
		if(key.toUpperCase().equals("ATTACKED")){
			hurt = true;
			bloodEffect.start();
		}
		
	}

	@Override
	public EntityType getID() {
		// TODO Auto-generated method stub
		return EntityType.ZOMBIE;
	}

}