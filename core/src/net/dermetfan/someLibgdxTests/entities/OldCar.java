package net.dermetfan.someLibgdxTests.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class OldCar {

	private Body chassis, leftWheel, rightWheel;
	private WheelJoint leftAxis, rightAxis;
	private float currentRotationForce, rotationForce = 100, motorSpeed = 20, maxMotorTorque = motorSpeed * 5000;
	private InputAdapter inputAdapter;

	public OldCar(World world, float width, float height) {
		this(world, 0, 0, width, height);
	}

	public OldCar(World world, float x, float y, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 1;
		fixtureDef.density = (width + height) / 2;
		fixtureDef.restitution = .25f;

		// create wheels
		CircleShape wheelShape = new CircleShape();
		wheelShape.setRadius(height / 3);
		fixtureDef.shape = wheelShape;

		leftWheel = world.createBody(bodyDef);
		leftWheel.createFixture(fixtureDef);
		rightWheel = world.createBody(bodyDef);
		rightWheel.createFixture(fixtureDef);

		wheelShape.dispose();

		// create chassis
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(new Vector2[] {new Vector2(-width / 2 / 1.5f, height / 2 * .8f), new Vector2(-width / 2, -height / 2), new Vector2(width / 2, -height / 2), new Vector2(width / 2 / 1.5f / 2, height / 2)});

		fixtureDef.shape = chassisShape;
		fixtureDef.density = leftWheel.getFixtureList().get(0).getDensity() / 1.5f;
		fixtureDef.friction = .6f;
		fixtureDef.restitution = .15f;

		chassis = world.createBody(bodyDef);
		chassis.createFixture(fixtureDef);

		chassisShape.dispose();

		// create axes
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = chassis;
		axisDef.localAnchorA.set(-width / 2 / 1.75f, -height / 1.5f);
		axisDef.bodyB = leftWheel;
		axisDef.localAxisA.set(Vector2.Y);
		axisDef.frequencyHz = 1.75f;
		axisDef.motorSpeed = motorSpeed;
		axisDef.maxMotorTorque = maxMotorTorque;

		leftAxis = (WheelJoint) world.createJoint(axisDef);

		axisDef.bodyB = rightWheel;
		axisDef.localAnchorA.x *= -1 * .8f;

		rightAxis = (WheelJoint) world.createJoint(axisDef);

		inputAdapter = new InputAdapter() {

			@Override
			public boolean keyDown(int keycode) {
				switch(keycode) {
				case Keys.W:
					leftAxis.setMotorSpeed(leftAxis.getMotorSpeed() > 0 ? -leftAxis.getMotorSpeed() : leftAxis.getMotorSpeed());
					rightAxis.setMotorSpeed(rightAxis.getMotorSpeed() > 0 ? -rightAxis.getMotorSpeed() : rightAxis.getMotorSpeed());
					leftAxis.enableMotor(true);
					rightAxis.enableMotor(true);
					return true;
				case Keys.S:
					leftAxis.setMotorSpeed(leftAxis.getMotorSpeed() < 0 ? -leftAxis.getMotorSpeed() : leftAxis.getMotorSpeed());
					rightAxis.setMotorSpeed(rightAxis.getMotorSpeed() < 0 ? -rightAxis.getMotorSpeed() : rightAxis.getMotorSpeed());
					leftAxis.enableMotor(true);
					rightAxis.enableMotor(true);
					return true;
				case Keys.A:
					currentRotationForce = rotationForce;
					return true;
				case Keys.D:
					currentRotationForce = -rotationForce;
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean keyUp(int keycode) {
				switch(keycode) {
				case Keys.W:
				case Keys.S:
					leftAxis.enableMotor(false);
					rightAxis.enableMotor(false);
					return true;
				case Keys.A:
				case Keys.D:
					currentRotationForce = 0;
					return true;
				default:
					return false;
				}
			}
		};
	}

	public void update(float delta) {
		chassis.applyAngularImpulse(currentRotationForce * delta, true);
	}

	/** @return the currentRotationForce */
	public float getCurrentRotationForce() {
		return currentRotationForce;
	}

	/** @param currentRotationForce the currentRotationForce to set */
	public void setCurrentRotationForce(float currentRotationForce) {
		this.currentRotationForce = currentRotationForce;
	}

	/** @return the rotationForce */
	public float getRotationForce() {
		return rotationForce;
	}

	/** @param rotationForce the rotationForce to set */
	public void setRotationForce(float rotationForce) {
		this.rotationForce = rotationForce;
	}

	/** @return the motorSpeed */
	public float getMotorSpeed() {
		return motorSpeed;
	}

	/** @param motorSpeed the motorSpeed to set */
	public void setMotorSpeed(float motorSpeed) {
		this.motorSpeed = motorSpeed;
	}

	/** @return the maxMotorTorque */
	public float getMaxMotorTorque() {
		return maxMotorTorque;
	}

	/*** @param maxMotorTorque the maxMotorTorque to set */
	public void setMaxMotorTorque(float maxMotorTorque) {
		this.maxMotorTorque = maxMotorTorque;
	}

	/** @return the {@link #inputAdapter} */
	public InputAdapter getInputAdapter() {
		return inputAdapter;
	}

	/** @param listener the {@link #inputAdapter} to set */
	public void setInputAdapter(InputAdapter listener) {
		this.inputAdapter = listener;
	}

	/** @return the chassis */
	public Body getChassis() {
		return chassis;
	}

	/** @return the leftWheel */
	public Body getLeftWheel() {
		return leftWheel;
	}

	/** @return the rightWheel */
	public Body getRightWheel() {
		return rightWheel;
	}

	/** @return the leftAxis */
	public WheelJoint getLeftAxis() {
		return leftAxis;
	}

	/** @return the rightAxis */
	public WheelJoint getRightAxis() {
		return rightAxis;
	}

}
