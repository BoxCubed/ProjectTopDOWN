package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.States.GameState;

public class Pack extends Sprite implements Entity {
	BodyDef def;
	Fixture fixture;
	FixtureDef fixtureDef;
	Body body;
	PolygonShape shape;
	Animation<TextureRegion> anim;
	float x,y;
	
	public Pack(PackType type, float x, float y,World world){
		this.x=x;
		this.y=y;
		def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		// Shape
		shape= new PolygonShape();
		shape.setAsBox(10, 10);
		
		// Fixture def
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		fixtureDef.friction = Float.MAX_VALUE;
		// Creates the body and assigns vars to all important values
		body = world.createBody(def);
		fixture = body.createFixture(fixtureDef);
		fixture.setUserData(type);
		
		body.setTransform(x, y, 0);

        shape.dispose();
        setSize(20, 20);
     
/*
		anim=GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/img/health.gif").read());
*/

		
	}
	
	
	
	
	
	public PackType getPackType(){
		
		return (PackType)getFixture().getUserData();
	}
	
	
	@Override
	public void update(float delta) {
		body.setTransform(x,y,0);
		
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.draw(GameState.instance.anim.getKeyFrame(Gdx.graphics.getDeltaTime()*10), x-10f, y-10f,0, 0, 20, 20, 1, 1, 0);
		
	}
	

	@Override
	public Vector2 getPos() {
		return body.getPosition();
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public EntityType getID() {
		return EntityType.PACK;
	}

	@Override
	public void setPos(Vector2 pos) {
		body.setTransform(pos, pos.angle());
		
	}

	@Override
	public Sprite getSprite() {
		return this;
	}

	@Override
	public void playAnimation(String key) {
		
	}

	

	@Override
	public void dispose() {
		GameState.instance.getWorld().destroyBody(getBody());
	}

	@Override
	public Fixture getFixture() {
		// TODO Auto-generated method stub
		return fixture;
	}

	

	@Override
	public void setDisposable(boolean disposable) {

		this.disposable=disposable;
	}

	@Override
	public void renderShapes(ShapeRenderer sr) {
		// TODO Auto-generated method stub
		
	}
	public enum PackType{
		HEALTH,AMMO,POWER,WEAPON
	}
	boolean disposable=false;
	@Override
	public boolean isDisposable() {
		// TODO Auto-generated method stub
		return disposable;
	}
}
