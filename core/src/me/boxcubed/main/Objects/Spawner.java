package me.boxcubed.main.Objects;

import com.badlogic.gdx.math.Vector2;
import me.boxcubed.main.Objects.collision.MapBodyBuilder;
import me.boxcubed.main.Objects.interfaces.EntityType;
import me.boxcubed.main.Objects.interfaces.LivingEntity;
import me.boxcubed.main.Sprites.Player;
import me.boxcubed.main.Sprites.Zombie;
import me.boxcubed.main.States.GameState;

import java.util.Random;

/**
 * Spawns any entity at an interval
 *
 * @author ryan9
 */
public class Spawner {
    /*
     * Map limits: y: 40-345
     * x: 22-680
     */
    private final Random random = new Random();
    private final EntityType entity;
    private final Vector2 pos;
    private final float delay;
    private final int limit;
    private final Clock clock;
    private final GameState instance;
    private float elapsedTime = 0;

    /**
     * The total amount of entities this spawner spawned
     */
    public Spawner(Vector2 pos, Clock clock, GameState instance) {
        this.pos = pos;
        this.entity = EntityType.ZOMBIE;
        this.delay = (float) 100;
        this.limit = 20;
        this.clock = clock;
        this.instance = instance;
    }

    /**
     * @param delta in same units as delay given
     */
    public void update(float delta, int currentAmount) {
        if (clock.amlight > 0.3f) return;

        if (currentAmount < limit) {
            elapsedTime += delta;
            if (elapsedTime >= delay) {

                pos.x = random.nextInt(1570);
                pos.y = random.nextInt(1570);
                while (MapBodyBuilder.checkCollision(pos)) {
                    pos.x = random.nextInt(1570);
                    pos.y = random.nextInt(1570);
                }
                LivingEntity spawnEntity = null;
                if (entity.equals(EntityType.ZOMBIE))

                    spawnEntity = new Zombie(instance.getWorld(), instance.playerAI);


                if (entity.equals(EntityType.PLAYER))
                    spawnEntity = new Player(instance.getWorld(), 0);

                if (spawnEntity != null) {
                    spawnEntity.getBody().setTransform(pos.scl(1f / GameState.PPM), spawnEntity.getBody().getAngle());
                    System.out.println("spawned entity at " + pos);
                    instance.entities.add(spawnEntity);
                }
                elapsedTime = 0;


            }
        }
    }

}
