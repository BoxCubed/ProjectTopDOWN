package me.boxcubed.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.BoxoUtil;

import me.boxcubed.main.TopDown;
import me.boxcubed.main.Objects.interfaces.Renderable;

public class PauseMenu implements Renderable,Disposable {
	private Stage stage;
	private TextButton resume,settings,menu;
	private Skin skin=TopDown.assets.get(Assets.star_SKIN,Skin.class);
	public PauseMenu(Viewport port,SpriteBatch batch) {
		stage=new Stage(port,batch);
		resume=new TextButton("Resume", skin);
		settings=new TextButton("Settings", skin);
		menu=new TextButton("Return to Menu", skin);
		initButton();
		BoxoUtil.addInputProcessor(stage);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {

	}
	@Override
	public void renderShapes(ShapeRenderer sr) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.set(ShapeType.Filled);
		sr.setColor(0,0,0,.3f);
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		
		
		
		
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	private void initButton(){
		
	}
	@Override
	public void dispose() {
		BoxoUtil.remInputProcessor(stage);
		stage.dispose();
		
	}

}
