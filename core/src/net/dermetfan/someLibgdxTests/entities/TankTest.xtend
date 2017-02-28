package net.dermetfan.someLibgdxTests.entities

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import com.badlogic.gdx.utils.TimeUtils
import net.dermetfan.someLibgdxTests.Assets
import net.dermetfan.someLibgdxTests.SomeLibgdxTests
import net.dermetfan.utils.libgdx.graphics.Box2DSprite

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.*

class TankTest extends InputAdapter {

	Body chassis
	Body cannon
	RevoluteJoint joint
	float width
	float height
	float cannonWidth
	float cannonHeight
	float acc = 200000
	float leftAcc
	float rightAcc
	float cannonTorque
	long lastShot

	new(World world, float x, float y, float width, float height) {
		this.width = width
		this.height = height

		val bodyDef = new BodyDef
		bodyDef.type = DynamicBody
		bodyDef.position.set(x, y)

		val fixtureDef = new FixtureDef
		fixtureDef.density = (width ** height) as float
		fixtureDef.restitution = 0.2f
		fixtureDef.friction = 0.5f

		val shape = new PolygonShape
		shape.setAsBox(width / 2, height / 2)

		fixtureDef.shape = shape

		chassis = world.createBody(bodyDef)
		chassis.createFixture(fixtureDef)

		// cannon
		cannonWidth = width / 4.5f
		cannonHeight = height / 1.5f
		shape.setAsBox(cannonWidth / 2, cannonHeight / 2)
		fixtureDef.density = 0.0001f

		cannon = world.createBody(bodyDef)
		cannon.createFixture(fixtureDef)

		// connection
		val revoluteJointDef = new RevoluteJointDef
		revoluteJointDef.bodyA = chassis
		revoluteJointDef.bodyB = cannon
		revoluteJointDef.localAnchorB.y = height / 1.5f / -1.5f
		revoluteJointDef.enableLimit = true
		revoluteJointDef.enableMotor = true
		revoluteJointDef.maxMotorTorque = acc / 5000000

		joint = world.createJoint(revoluteJointDef) as RevoluteJoint
	}

	Vector2 tmp = new Vector2
	Vector2 tmp2 = new Vector2

	def update() {
		val rot = (chassis.transform.rotation + Math.PI / 2) as float
		val x = MathUtils.cos(rot)
		val y = MathUtils.sin(rot)

		chassis.applyForce(tmp.set(leftAcc * x, leftAcc * y), chassis.getWorldPoint(tmp2.set(-width / 2, 0)), true)
		chassis.applyForce(tmp.set(rightAcc * x, rightAcc * y), chassis.getWorldPoint(tmp2.set(width / 2, 0)), true)
	}

	def shoot() {
		if (TimeUtils.millis - lastShot >= 2000) {
			val bodyDef = new BodyDef
			bodyDef.type = DynamicBody
			bodyDef.position.set(cannon.getWorldPoint(tmp.set(0, cannonHeight / 2 - cannonWidth)))
			bodyDef.bullet = true

			val shape = new CircleShape
			shape.radius = cannonWidth / 2

			val fixDef = new FixtureDef
			fixDef.shape = shape
			fixDef.density = 3
			fixDef.restitution = 0.01f
			fixDef.friction = 0.75f

			val bulletForce = 500000
			val rot = (cannon.transform.rotation + Math.PI / 2) as float
			val x = MathUtils.cos(rot)
			val y = MathUtils.sin(rot)

			val bullet = cannon.world.createBody(bodyDef)
			bullet.createFixture(fixDef).body.applyLinearImpulse(bulletForce * x, bulletForce * y, 0, 0, true)
			bullet.userData = new Box2DSprite(SomeLibgdxTests.assets.<Texture>get(Assets.ball))

			lastShot = TimeUtils.millis
		}
	}

	override keyDown(int keycode) {
		switch (keycode) {
			case Keys.Q:
				leftAcc = acc
			case Keys.E:
				rightAcc = acc
			case Keys.A:
				leftAcc = -acc
			case Keys.D:
				rightAcc = -acc
			case Keys.W: {
				joint.motorSpeed = -acc
				joint.enableLimit(false)
			}
			case Keys.S: {
				joint.motorSpeed = acc
				joint.enableLimit(false)
			}
			case Keys.SPACE:
				shoot
			default:
				return false
		}
		true
	}

	override keyUp(int keycode) {
		if (keycode == Keys.Q || keycode == Keys.A)
			leftAcc = 0
		else if (keycode == Keys.E || keycode == Keys.D)
			rightAcc = 0
		else if (keycode == Keys.W || keycode == Keys.S) {
			cannonTorque = 0
			joint.setLimits(joint.jointAngle, joint.jointAngle)
			joint.enableLimit(true)
		} else
			return false
		true
	}

	def Body getChassis() {
		chassis
	}

	def Body getCannon() {
		cannon
	}

	def RevoluteJoint getJoint() {
		joint
	}

	def float getCannonHeight() {
		cannonHeight
	}

	def float getCannonWidth() {
		cannonWidth
	}

}
