package me.boxcubed.main.Objects;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import me.boxcubed.main.Objects.interfaces.LivingEntity;

public class SteeringAI implements Steerable<Vector2> {
	LivingEntity entity;
	boolean tagged;
	
	float speedMax,speedMaxAcc,
		  speedMaxAng,speedMaxAngAcc,
		  boundingRadius;
	SteeringBehavior<Vector2>behavior;
	SteeringAcceleration<Vector2>steeringOutput;
	float speedZeroLin=500;
	
	public SteeringAI(LivingEntity entity,float boundingRadius){
		this.entity=entity;
		this.boundingRadius=boundingRadius;
		speedMax=0.1f;
		speedMaxAcc=100f;
		speedMaxAng=50;
		speedMaxAngAcc=50;
		tagged=false;
		steeringOutput=new SteeringAcceleration<Vector2>(new Vector2());
	}	

	
	

	public void update(float delta){
		if(getBehavior()!=null){
			getBehavior().calculateSteering(steeringOutput);
			apply(delta);
		}
	}
	private void apply(float delta){
		if(delta<1f)delta=1f;
		boolean anyAcc=false;
		if(!steeringOutput.linear.isZero()){
			Vector2 force=steeringOutput.linear.scl(delta);
			getBody().applyForceToCenter(force, true);
			anyAcc=true;
		}
		if(steeringOutput.angular!=0){
			getBody().applyTorque(steeringOutput.angular*delta, true);
			anyAcc=true;
		}else if(!getLinearVelocity().isZero()){
			float newOr=vectorToAngle(getLinearVelocity());
			setOrientation(newOr);
		}
		if(anyAcc){
			Vector2 vel=getBody().getLinearVelocity();
			float currSpeedSquare=vel.len2();
			if(currSpeedSquare>getMaxLinearSpeed()*getMaxLinearSpeed()){
				getBody().setLinearVelocity(vel.scl(getMaxLinearSpeed()/(float)Math.sqrt(currSpeedSquare)));
			}
			if(getBody().getAngularVelocity()>speedMaxAng)
				getBody().setAngularVelocity(speedMaxAng);
			
		}
	}
	
	
	
	
	
	
	
	
	
	@Override
	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return entity.getBody().getPosition();
	}

	@Override
	public float getOrientation() {
		// TODO Auto-generated method stub
		return entity.getBody().getAngle();
	}

	@Override
	public void setOrientation(float newOr) {
		getBody().setTransform(getBody().getPosition(), newOr);
		
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		outVector.x=-(float)Math.sin(angle);
		outVector.y=(float)Math.cos(angle);
		return outVector;
	}

	@Override
	public Location<Vector2> newLocation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public float getZeroLinearSpeedThreshold() {
		return speedZeroLin;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		speedZeroLin=value;
		
	}

	@Override
	public float getMaxLinearSpeed() {
		return speedMax;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		speedMax=maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return speedMaxAcc;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		speedMaxAcc=maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return speedMaxAng;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		speedMaxAng=maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return speedMaxAngAcc;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		speedMaxAngAcc=maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return entity.getBody().getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return entity.getBody().getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}
	@Override
	public void setTagged(boolean tagged) {
		this.tagged=tagged;
	}
	public Body getBody(){
		return entity.getBody();
	}
	public void setBehavior(SteeringBehavior<Vector2> beh){
		this.behavior=beh;
	}
	public SteeringBehavior<Vector2> getBehavior(){
		return behavior;}

}
