package me.boxcubed.main.Objects;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import me.boxcubed.main.States.GameState;
import me.boxcubed.main.TopDown;

public class Clock {
    // TODO make new class for just time handling
    public final RayHandler rayHandler;
    private final StopWatch timer;
    public boolean progressTime;
    public float amlight = 1f;
    boolean flashlightState = true;
    private boolean night = false;

    public Clock(World world) {
        timer = new StopWatch();
        rayHandler = new RayHandler(world);
        progressTime = !TopDown.debug;


    }

    public void updateLight(float delta) {

        if (Gdx.input.isKeyPressed(Keys.EQUALS))
            amlight += 0.01f;
        if (Gdx.input.isKeyPressed(Keys.MINUS))
            amlight -= 0.01f; // jkjk
        rayHandler.setAmbientLight(amlight);
        rayHandler.update();
        if (!progressTime)
            return;
        if (amlight < 0.07 || amlight > 1) {
            night = !night;
            if (amlight > 1)
                amlight = 0.99f;
            else amlight = 0.08f;
        }
        if (night && !timer.isRunning()) {
            timer.start();

        }
        if (timer.getElapsedTimeSecs() > 5) {
            timer.stop();
            amlight += 0.0005 * delta;
        }
        if (!night) {
            amlight -= 0.0005 * delta;
        }

    }

    @SuppressWarnings("deprecation")
    public void renderLIGHT(OrthographicCamera matrix) {
        rayHandler.setCombinedMatrix(matrix.combined.cpy().scl(GameState.PPM));
        rayHandler.render();

    }

    public void dispose() {
        rayHandler.dispose();
    }

    public String amToTime() {
        float hrs, mins;
        if (night) {
            hrs = (amlight * 12);
            mins = hrs * 60 - (int) hrs * 60;
            return (int) hrs + ":" + (int) mins + "am";
        } else {
            hrs = 12 - (amlight * 12);
            mins = hrs * 60 - (int) hrs * 60;
            return (int) hrs + ":" + (int) mins + "pm";
        }

        // return Float.toString(amlight);
    }

}
