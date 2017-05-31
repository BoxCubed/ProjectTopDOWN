package me.boxcubed.main.Objects.interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Renderable {
    void update(float delta);

    void render(SpriteBatch batch);

    void renderShapes(ShapeRenderer sr);


}
