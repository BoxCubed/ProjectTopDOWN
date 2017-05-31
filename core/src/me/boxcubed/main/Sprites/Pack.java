package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import me.boxcubed.main.Objects.interfaces.Entity;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.States.GameState;

public class Pack extends Sprite implements Entity {
    private final BodyDef def;
    private final Fixture fixture;
    private final FixtureDef fixtureDef;
    private final Body body;
    private final PolygonShape shape;
    private final float x;
    private final float y;
    private boolean disposable = false;


    public Pack(float x, float y, World world) {
        this.x = x;
        this.y = y;
        def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        // Shape
        shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        // Fixture def
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        fixtureDef.friction = Float.MAX_VALUE;
        // Creates the body and assigns vars to all important values
        body = world.createBody(def);
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(PackType.HEALTH);

        body.setTransform(x, y, 0);

        shape.dispose();
        setSize(20, 20);

/*
        anim=GIFDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/img/health.gif").read());
*/


    }

    public PackType getPackType() {

        return (PackType) getFixture().getUserData();
    }

    @Override
    public void update(float delta) {
        body.setTransform(x, y, 0);

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(GameState.instance.anim.getKeyFrame(Gdx.graphics.getDeltaTime() * 10), x * GameState.PPM - 10f, y * GameState.PPM - 10f, 0, 0, 20, 20, 1, 1, 0);

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
    public void renderShapes(ShapeRenderer sr) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDisposable() {
        // TODO Auto-generated method stub
        return disposable;
    }

    @Override
    public void setDisposable(boolean disposable) {

        this.disposable = disposable;
    }

    @Override
    public Vector2 getPos(boolean asPixels) {
        if (asPixels)
            return body.getPosition().cpy().scl(GameState.PPM);
        return body.getPosition();
    }

    public enum PackType {
        HEALTH, AMMO, POWER, WEAPON
    }
}
