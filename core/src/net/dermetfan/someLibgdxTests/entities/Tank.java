package net.dermetfan.someLibgdxTests.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Tank extends InputAdapter {

	private Body chassis, cannon;
	private RevoluteJoint joint;
	private float width, height;
	private float acc = 200000, leftAcc, rightAcc;
	private BodyDef bulletBodyDef;
	private FixtureDef bulletFixtureDef;

	public Tank(World world, float x, float y, float width, float height) {
		this.width = width;
		this.height = height;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.density = (float) Math.pow(width, height);
		fixDef.restitution = .1f;
		fixDef.friction = .5f;

		chassis = world.createBody(bodyDef);
		chassis.createFixture(fixDef);

		// cannon
		shape.setAsBox(width / 2 / 5, height / 3);

		fixDef.density /= 500;

		cannon = world.createBody(bodyDef);
		cannon.createFixture(fixDef);

		// joint
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = chassis;
		jointDef.bodyB = cannon;
		jointDef.localAnchorB.y = -height / 3;
		jointDef.enableLimit = true;
		jointDef.upperAngle = 10 * MathUtils.degreesToRadians;
		jointDef.lowerAngle = -10 * MathUtils.degreesToRadians;
		jointDef.maxMotorTorque = 100;

		joint = (RevoluteJoint) world.createJoint(jointDef);
		
		// shoot stuff
		bulletBodyDef = bodyDef;
		
		CircleShape bulletShape = new CircleShape();
		bulletShape.setRadius(width / 2 / 5);
		
		fixDef.shape = bulletShape;
		fixDef.density = (float) Math.pow(bulletShape.getRadius(), 25);
		fixDef.restitution = 0;
		fixDef.friction = 1;
		
		bulletFixtureDef = fixDef;
	}

	Vector2 tmp = new Vector2(), tmp2 = new Vector2();

	public void update() {
		float rot = (float) (chassis.getTransform().getRotation() + Math.PI / 2);
		float x = MathUtils.cos(rot);
		float y = MathUtils.sin(rot);

		chassis.applyForce(tmp.set(leftAcc * x, leftAcc * y), chassis.getWorldPoint(tmp2.set(-width / 2, 0)), true);
		chassis.applyForce(tmp.set(rightAcc * x, rightAcc * y), chassis.getWorldPoint(tmp2.set(width / 2, 0)), true);
	}
	
	public void shoot() {
		bulletBodyDef.position.set(cannon.getWorldPoint(tmp.set(0, height / 3)));
		
		Body bullet = chassis.getWorld().createBody(bulletBodyDef);
		bullet.createFixture(bulletFixtureDef);
		
		float rot = (float) (cannon.getTransform().getRotation() + Math.PI / 2);
		float x = MathUtils.cos(rot);
		float y = MathUtils.sin(rot);
		
		bullet.setLinearVelocity(50000 * x, 50000 * y);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.Q:
			leftAcc = acc;
			break;
		case Keys.A:
			leftAcc = -acc;
			break;
		case Keys.E:
			rightAcc = acc;
			break;
		case Keys.D:
			rightAcc = -acc;
			break;
		case Keys.W:
			joint.enableLimit(false);
			joint.enableMotor(true);
			joint.setMotorSpeed(-50);
			break;
		case Keys.S:
			joint.enableLimit(false);
			joint.enableMotor(true);
			joint.setMotorSpeed(50);
			break;
		case Keys.SPACE:
			shoot();
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.Q || keycode == Keys.A)
			leftAcc = 0;
		else if(keycode == Keys.E || keycode == Keys.D)
			rightAcc = 0;
		else if(keycode == Keys.W || keycode == Keys.S) {
			joint.enableMotor(false);
			joint.setLimits(joint.getJointAngle() - 20 * MathUtils.degRad, joint.getJointAngle() + 20 * MathUtils.degRad);
			joint.enableLimit(true);
		} else
			return false;
		return true;
	}

	/** @return the acc */
	public float getAcc() {
		return acc;
	}

	/** @param acc the acc to set */
	public void setAcc(float acc) {
		this.acc = acc;
	}

	/** @return the leftAcc */
	public float getLeftAcc() {
		return leftAcc;
	}

	/** @param leftAcc the leftAcc to set */
	public void setLeftAcc(float leftAcc) {
		this.leftAcc = leftAcc;
	}

	/** @return the rightAcc */
	public float getRightAcc() {
		return rightAcc;
	}

	/** @param rightAcc the rightAcc to set */
	public void setRightAcc(float rightAcc) {
		this.rightAcc = rightAcc;
	}

	/** @return the chassis */
	public Body getChassis() {
		return chassis;
	}

	/** @return the cannon */
	public Body getCannon() {
		return cannon;
	}

	/** @return the joint */
	public RevoluteJoint getJoint() {
		return joint;
	}

	/** @return the width */
	public float getWidth() {
		return width;
	}

	/** @return the height */
	public float getHeight() {
		return height;
	}

}
