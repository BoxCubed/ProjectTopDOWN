package me.boxcubed.main.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public interface Entity {
    public Vector2 pos();
    public void initPlayer();
    public void goUP();
    public void goDown();
    public void goLeft();
    public void goRight();
    public Animation animation();
    public Sprite sprite();
}
