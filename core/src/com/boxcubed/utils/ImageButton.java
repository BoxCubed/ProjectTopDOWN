package com.boxcubed.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import me.boxcubed.main.TopDown;

/**
 * Created by ryan9 on 29/05/2017.
 */
public class ImageButton extends Image{
    public float width,height;

    Rectangle collision;
    public ImageButton(float width,float height,Vector2 pos){
        super(new TextureRegion(TopDown.assets.get(Assets.bulletFire_IMAGE, Texture.class)));
        this.height=height;
        this.width=width;
        setSize(width,height);
        collision=new Rectangle(pos.x,pos.y,width,height);


    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        collision.x=x;
        collision.y=y;
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        collision.setSize(width,height);
    }

}
