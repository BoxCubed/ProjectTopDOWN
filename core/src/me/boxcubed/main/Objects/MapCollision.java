package me.boxcubed.main.Objects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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
//        bdef = new BodyDef();
//        shape = new PolygonShape();
//        fdef = new FixtureDef();
//           
//        for(MapObject object : tm.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject)object).getRectangle();
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((rect.getX()+rect.getWidth()/2) / GameState.PPM , (rect.getY()+rect.getHeight() / 2) / GameState.PPM);
//
//            body = world.createBody(bdef);
//
//            shape.setAsBox(rect.getWidth() / 2 / GameState.PPM,rect.getHeight() / 2 / GameState.PPM);
//
//            fdef.shape=shape;
//
//            body.createFixture(fdef);
//        }
      
    }
    public FixtureDef getFdef(){
        return fdef;
    }
}
