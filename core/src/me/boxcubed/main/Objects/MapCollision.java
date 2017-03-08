package me.boxcubed.main.Objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import me.boxcubed.main.States.GameState;

public class MapCollision {
	TiledMap tm;
    World world;
    FixtureDef fdef;
    PolygonShape shape;
    Body body;
    BodyDef bdef;

    public MapCollision(TiledMap tm, World world){
        this.tm=tm;
        this.world=world;
        init();
    }
    
    public void init(){
    	//learn to comment next time
        bdef = new BodyDef();
        shape = new PolygonShape();
        fdef = new FixtureDef();
           
        for(MapObject object : tm.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2) , (rect.getY()+rect.getHeight() / 2));

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2,rect.getHeight() / 2);

            fdef.shape=shape;

            body.createFixture(fdef).setUserData("WALL");;
            
        }
        shape.dispose();
        
    }
    public FixtureDef getFdef(){
        return fdef;
    }
}
