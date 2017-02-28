package me.boxcubed.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import net.dermetfan.gdx.graphics.g2d.Box2DSprite;
import net.dermetfan.gdx.physics.box2d.Box2DUtils;
//import net.dermetfan.someLibgdxTests.SomeLibgdxTests;
import net.dermetfan.someLibgdxTests.entities.Tank;

public class TopDownBox2DTankTutorial implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private float timestep = 1 / 60f;

	private Tank tank;
    Texture luigiFront=new Texture(Gdx.files.internal("assets/img/luigiFront.png"));
    
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tank.update();
		world.step(timestep, 8, 3);

		camera.position.set(tank.getChassis().getPosition().x, tank.getChassis().getPosition().y, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(luigiFront, -luigiFront.getWidth() / 2, -luigiFront.getHeight() / 2);
		Box2DSprite.draw(batch, world);
		batch.end();

		debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("resized");
		camera.viewportWidth = width / 25;
		camera.viewportHeight = height / 25;
	}

	@Override
	public void show() {
		world = new World(new Vector2(), true);
		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera();
        System.out.println("shown");
		Gdx.input.setInputProcessor(tank = new Tank(world, 0, 0, 3, 5));
		tank.getChassis().setLinearDamping(3);
		tank.getChassis().setAngularDamping(3);
		tank.getCannon().setLinearDamping(3);
		tank.getCannon().setAngularDamping(3);

		tank.getChassis().setUserData(new Box2DSprite(new Texture(Gdx.files.internal("assets/img/tank.png"))));
	}

	@Override
	public void hide() {
		System.out.println("hidden");
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		System.out.println("disposed");
		world.dispose();
		batch.dispose();
		debugRenderer.dispose();
		Box2DUtils.cache.clear(); // this is from my library. don't worry if you don't use it
	}

	

	
	

}