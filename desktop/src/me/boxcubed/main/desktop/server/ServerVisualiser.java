package me.boxcubed.main.desktop.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.boxcubed.utils.BoxoUtil;

public class ServerVisualiser implements ApplicationListener {
	Box2DDebugRenderer renderer;
	OrthographicCamera cam;
	World world;
	private Runnable onDispose;
	public ServerVisualiser(World world,Runnable onDispose) {
		this.world=world;
		this.onDispose=onDispose;
	}

	@Override
	public void create() {
		renderer=new Box2DDebugRenderer();
		cam=new OrthographicCamera();
		Gdx.graphics.setWindowedMode(4*480/3, 480);

	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false,width,height);

	}

	@Override
	public void render() {
		float delta=Gdx.graphics.getDeltaTime();
		BoxoUtil.clearScreen();
		cam.update();
		renderer.render(world, cam.combined);
		
		if(Gdx.input.isKeyPressed(Keys.UP))
			cam.translate(0, 1000*delta);
		
		if(Gdx.input.isKeyPressed(Keys.DOWN))
			cam.translate(0, -1000*delta);
		if(Gdx.input.isKeyPressed(Keys.LEFT))
			cam.translate(-1000*delta,0);
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
			cam.translate(1000*delta, 0);
		

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		renderer.dispose();
		onDispose.run();
	}

}
