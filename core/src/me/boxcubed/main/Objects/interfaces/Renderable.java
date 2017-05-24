package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable {
	public void update(float delta);
	public void render(SpriteBatch batch);
	public default void renderShapes(ShapeRenderer sr){
		
	}

}
