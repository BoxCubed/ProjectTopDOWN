package com.boxcubed.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Boxcube's wonderful util class filled with useful applications for LibGDX <br>
 */
public class BoxoUtil implements InputProcessor {
    // for the ButtonJustPressed method
    private static final boolean[] justPressed = new boolean[5];
    private static final Random random = new Random();
    public static Runnable onAndroidBackButtonPressed = new Runnable() {
        @Override
        public void run() {
            System.out.println("Android back button was pressed");
        }
    };
    //all the input processors to process
    private static List<InputProcessor> inputProcessors = new ArrayList<>();
    //For thread safety when removing and adding input processors from multiple threads
    private static LinkedBlockingQueue<InputProcessor> qAdd = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<InputProcessor> qRemove = new LinkedBlockingQueue<>();
    private static float elapsed, duration, intensity;


    /**
     * Call this to reset the API<br>
     * This will clear the input processors and reset the Gdx input processor to this class
     */
    public static void reset() {

        inputProcessors = new ArrayList<>();
        qAdd = new LinkedBlockingQueue<>();
        qRemove = new LinkedBlockingQueue<>();
        Gdx.input.setInputProcessor(new BoxoUtil());
    }

    /**
     * Call this when updating the game <br>
     * Make sure it is on the game thread and not another thread
     */
    public static void update() {
        try {

            while (!qAdd.isEmpty()) inputProcessors.add(qAdd.take());
            while (!qRemove.isEmpty()) inputProcessors.remove(qRemove.take());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < justPressed.length; i++)
            justPressed[i] = false;

    }

    /**
     * This method allows you to lerp(ease in and out) from one coordinate to another<br>
     * Does not require for this class to be initiated or updated
     *
     * @param start  the current position, this should change every time you call this method
     * @param finish the target position
     * @return the new vector to move to
     */
    public static Vector2 universalLerpToPos(Vector2 start, Vector2 finish) {
        final float speed = 0.5f, ispeed = 1.0f - speed;
        Vector3 target = new Vector3(
                finish.x,
                finish.y,
                0);
        Vector3 cameraPosition = new Vector3(start, 0);
        cameraPosition.scl(ispeed);
        target.scl(speed);
        cameraPosition.add(target);

        return new Vector2(cameraPosition.x, cameraPosition.y);

    }

    /**
     * Start the screen shaking with a given power and duration
     */
    public static void shake() {
        elapsed = 0;
        BoxoUtil.duration = (float) 10 / 1000f;
        BoxoUtil.intensity = (float) 2;
    }

    /**
     * Updates the shake and the camera.
     * This must be called prior to camera.update()
     */
    public static void updateShake(float delta, OrthographicCamera camera) {

        // Only shake when required.
        if (elapsed < duration) {

            // Calculate the amount of shake based on how long it has been shaking already
            float currentPower = intensity * camera.zoom * ((duration - elapsed) / duration);
            float x = (random.nextFloat() - 0.5f) * 2 * currentPower;
            float y = (random.nextFloat() - 0.5f) * 2 * currentPower;
            camera.translate(-x, -y);

            // Increase the elapsed time by the delta provided.
            elapsed += delta;
        }
    }

    /**
     * Whether a button was just pressed
     *
     * @return whether the button was pressed in this update call. Returns false if button is invalid.
     */
    public static boolean isButtonJustPressed() {
        try {
            return justPressed[com.badlogic.gdx.Input.Buttons.LEFT];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Clears the screen allowing you to draw on it again. Call every render method unless you want your game<br>
     * to look like it's on drugs
     */
    public static void clearScreen() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Add an input processor. This allows multiple input processors to be used.
     * <br>
     * <br>Make sure to save it locally so you can delete it later.<br>
     * <br>
     * Make sure to remove your input processor on dispose/whenever you need with. Or not if you plan to use it to return to a state <br>
     * <br>
     * Requires for this class to be initialized and for update to be called in order to work.
     * <p>
     * {@link BoxoUtil#remInputProcessor(InputProcessor)}
     *
     * @param add the processor to add
     */
    public static void addInputProcessor(InputProcessor add) {
        qAdd.add(add);
    }

    /**
     * Remove an input processor from the list<br>
     * This might call the input processor before removing it unless the update method is called after this method.
     *
     * @param remove The processor to remove
     * @see BoxoUtil#addInputProcessor(InputProcessor)
     */
    public static void remInputProcessor(InputProcessor remove) {
        qRemove.add(remove);
    }

    /**
     * Lerp(ease in and out) the camera given to a certain position<br>
     * Currently only works with the 2D camera
     *
     * @param pos the Position to lerp to
     * @param cam the camera to perform the lerp on
     */
    public static void lerpToPos(Vector2 pos, OrthographicCamera cam) {
        //TODO add 3d support
        final float speed = 0.1f, ispeed = 1.0f - speed;
        Vector3 target = new Vector3(
                pos.x,
                pos.y,
                0);
        Vector3 cameraPosition = cam.position;
        cameraPosition.scl(ispeed);
        target.scl(speed);
        cameraPosition.add(target);

        cam.position.set(cameraPosition);
    }


    @Override
    public boolean keyDown(int keycode) {
        for (InputProcessor i : inputProcessors)
            i.keyDown(keycode);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (InputProcessor i : inputProcessors)
            i.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (InputProcessor i : inputProcessors)
            i.keyUp(character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        justPressed[button] = true;
        for (InputProcessor i : inputProcessors)
            i.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (InputProcessor i : inputProcessors)
            i.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (InputProcessor i : inputProcessors)
            i.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        for (InputProcessor i : inputProcessors)
            i.mouseMoved(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        for (InputProcessor i : inputProcessors)
            i.scrolled(amount);
        return false;
    }

}
