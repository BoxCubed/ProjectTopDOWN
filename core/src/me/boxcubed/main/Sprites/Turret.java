package me.boxcubed.main.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.boxcubed.utils.InventoryItem;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;

/**
 * Created by SimplyBallistic on 23/07/2017.
 *
 * @author SimplyBallistic
 */
public class Turret implements LivingEntity, InventoryItem {
    final World world;
    Body turretBody;
    Body barrelBody;
    Fixture bodyFix, barrelFix;
    RevoluteJoint joint;

    public Turret(World world, Vector2 pos) {
        this.world = world;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        turretBody = world.createBody(bodyDef);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        barrelBody = world.createBody(bodyDef);

        FixtureDef def = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);
        def.shape = shape;
        def.friction = 0f;
        bodyFix = turretBody.createFixture(def);

        shape.setAsBox(1.5f, 0.4f);
        def = new FixtureDef();
        def.shape = shape;
        barrelFix = barrelBody.createFixture(def);

        shape.dispose();

        RevoluteJointDef jointDef = new RevoluteJointDef();
        // jointDef.enableMotor=true;
        jointDef.initialize(turretBody, barrelBody, new Vector2());
        //jointDef.maxMotorTorque=30;
        //jointDef.motorSpeed=3;
        jointDef.collideConnected = false;


        turretBody.setTransform(pos.x, pos.y, 0);
        barrelBody.setTransform(pos.x + 0.75f, pos.y, 0);

        // joint= (RevoluteJoint) world.createJoint(jointDef);


    }

    @Override
    public void update(float delta) {


    }

    @Override
    public void render(SpriteBatch batch) {

    }


    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public String getItemName() {
        return null;
    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public Texture getActiveTexture() {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }


    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public EntityType getID() {
        return null;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public void playAnimation(String key) {

    }

    @Override
    public Fixture getFixture() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
