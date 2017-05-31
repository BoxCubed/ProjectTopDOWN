package me.boxcubed.main.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.boxcubed.utils.Assets;
import me.boxcubed.main.Objects.SteeringAI;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.TopDown;

import java.util.Random;

public class Zombie extends Sprite implements LivingEntity {
    private final BodyDef Def;
    private final FixtureDef fixtureDef;
    private final CircleShape Shape;
    private final Body Body;
    private final Fixture fixture;
    private final SteeringAI ai;
    private final Sound attackSound;
    private final Animation<TextureRegion> zombieAnim;
    private final Animation<TextureRegion> zombieWalk;
    private final ParticleEffect bloodEffect = new ParticleEffect(TopDown.assets.get(Assets.blood_EFFECT, ParticleEffect.class));
    private final TextureRegion healthBar = new TextureRegion(TopDown.assets.get(Assets.stamina_IMAGE, Texture.class));
    private final RayCastCallback callback;
    private final Random rand = new Random();
    Vector2 position, vel, target;
    Vector2 collision;
    Vector2 normal;
    private double health;
    private boolean attack;
    private float attackTime;
    private boolean hurt;
    private float walkTime = 0;
    private boolean idle;
    private Vector2 p1;
    private Vector2 p2;
    private boolean rayEnabled;

    @SuppressWarnings("unchecked")
    public Zombie(World world, SteeringAI playerAI) {
        super(TopDown.assets.get(Assets.zombie_IMAGE, Texture.class));
        attackSound = TopDown.assets.get(Assets.ZAttack_SOUND, Sound.class);


        setSize(50, 50);
        health = 100;
        ai = new SteeringAI(this, 50);
        ai.setBehavior(new Seek<>(ai, playerAI));
        Def = new BodyDef();
        Def.type = BodyDef.BodyType.DynamicBody;
        Def.position.set(300 / GameState.PPM, 400 / GameState.PPM);

        // Shape
        Shape = new CircleShape();
        Shape.setRadius(0.5f);

        // Fixture def
        fixtureDef = new FixtureDef();
        fixtureDef.shape = Shape;

        fixtureDef.friction = 0f;
        // Creates the body and assigns vars to all important values
        Body = world.createBody(Def);
        fixture = Body.createFixture(fixtureDef);
        fixture.setUserData("ZOMBIE");
        Shape.dispose();
        zombieAnim = TopDown.assets.get(Assets.zombieAttack_ATLAS + ":anim", Animation.class);
        zombieWalk = TopDown.assets.get(Assets.zombieWalk_ATLAS_30 + ":anim", Animation.class);


        callback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getUserData().equals("ZOMBIE") || fixture.getUserData().equals("PLAYER")) {

                } else {
                    getBody().setLinearVelocity(getBody().getLinearVelocity().x -= Gdx.graphics.getDeltaTime(), getBody().getLinearVelocity().y -= Gdx.graphics.getDeltaTime());
                    getBody().setAngularVelocity(0);


                    idle = true;
                }

                return 1;
            }

        };
        /*zombieGroan.play(0.05f,1,0);*/

    }

    @Override
    public void update(float delta) {


        idle = false;

        p1 = getPos(false);
        p2 = GameState.instance.player.getPos(false);
        if (GameState.instance.player.isAlive())
            GameState.instance.getWorld().rayCast(callback, p1, p2);
        if (hurt) {
            bloodEffect.setPosition(getPos(true).x, getPos(true).y);
            bloodEffect.update(delta / 100);

            if (bloodEffect.isComplete()) {
                hurt = false;
                bloodEffect.reset();
            }
        }


        if (GameState.instance.player.isAlive() && isAlive() && !idle) {

            ai.update(delta);
            if (attack) attackTime += delta;

        }
        /*else if(isAlive()&&GameState.instance.player.isAlive()){
			getBody().setLinearVelocity(0,0);
			getBody().setAngularVelocity(0);
		}*/


    }

    @Override
    public void render(SpriteBatch sb) {
        walkTime += Gdx.graphics.getDeltaTime();
        bloodEffect.draw(sb);

        if (!idle) {
            if (!attack) {
                sb.draw(zombieWalk.getKeyFrame(walkTime, true), getPos(true).x - 15, getPos(true).y - 20, getWidth() / 2, getHeight() / 2, 40, 40,
                        1, 1, (float) Math.toDegrees(Body.getAngle()) + 90);
            } else {
                sb.draw(zombieAnim.getKeyFrame(attackTime, false), getPos(true).x - 25, getPos(true).y - 20, getWidth() / 2, getHeight() / 2, 40, 40,
                        1, 1, (float) Math.toDegrees(Body.getAngle()) + 90);

                if (zombieAnim.isAnimationFinished(attackTime))
                    attack = false;

            }
        } else {
            sb.draw(zombieWalk.getKeyFrame(walkTime, true), getPos(true).x - 25, getPos(true).y - 20, getWidth() / 2, getHeight() / 2, 40, 40,
                    1, 1, (float) Math.toDegrees(Body.getAngle()) + 90);
        }
        float size = (float) (getHealth() / getMaxHealth() * 40f);
        sb.draw(healthBar, getPos(true).x - 10, getPos(true).y + 18, 0, 0, size, 5, 1, 1, 0);


    }

    @Override
    public void renderShapes(ShapeRenderer sr) {

        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            rayEnabled = !rayEnabled;
        }
        if (TopDown.debug) {
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
        this.health = health;
    }

    @Override
    public double getMaxHealth() {
        return 100;
    }

    @Override
    public void playAnimation(String key) {
        if (key.toUpperCase().equals("ATTACK")) {

            attack = true;
            attackTime = 0;
            if (rand.nextFloat() < 0.25f)
                attackSound.play();
        }
        if (key.toUpperCase().equals("ATTACKED")) {
            hurt = true;
            bloodEffect.start();
        }

    }

    @Override
    public EntityType getID() {
        // TODO Auto-generated method stub
        return EntityType.ZOMBIE;
    }

    @Override
    public Vector2 getPos(boolean asPixels) {
        if (asPixels)

            return Body.getPosition().cpy().scl(GameState.PPM);

        else
            return Body.getPosition();
    }

    @Override
    public boolean isAlive() {
        return getHealth() > 0;
    }

    @Override
    public boolean isDisposable() {
        return !isAlive();
    }

    @Override
    public void setDisposable(boolean disposable) {
        if (disposable)
            setHealth(0);
        else setHealth(getMaxHealth());
    }
}