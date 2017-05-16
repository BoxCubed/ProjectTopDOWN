package me.boxcubed.main.Objects.collision;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MapBodyBuilder {

    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static float ppt = 16f;
    /**
     * Stores the shapes of the most recent call to {@link MapBodyBuilder#buildShapes(Map, float, World)}
     */
    public static Shape2D[] shapes;
    public static Array<Body> buildShapes(Map map, float pixels, World world) {
        ppt = pixels;
        MapObjects objects = map.getLayers().get("Objects").getObjects();
        shapes=new Shape2D[objects.getCount()];
        
        Array<Body> bodies = new Array<Body>();

        for(int i=0;i<objects.getCount();i++) {
        	Object object=objects.get(i);
            if (object instanceof TextureMapObject) {
                continue;
            }
            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object,i);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object,i);
            }
            else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object,i);
            }
            else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object,i);
            }
            else {
                continue;
            }
            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            body.createFixture(shape, 1).setUserData("WALL");;

            bodies.add(body);

            shape.dispose();
        }
        return bodies;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject,int i) {
        Rectangle rectangle = rectangleObject.getRectangle();
        shapes[i]=rectangle;
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                                   (rectangle.y + rectangle.height * 0.5f ) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                         rectangle.height * 0.5f / ppt,
                         size,
                         0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject,int i) {
        Circle circle = circleObject.getCircle();
        shapes[i]=circle;
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject,int p) {
        PolygonShape polygon = new PolygonShape();
        Polygon poly=polygonObject.getPolygon();
        shapes[p]=poly;
        float[] vertices = poly.getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject,int p) {
    	Polyline line=polylineObject.getPolyline();
    	shapes[p]=line;
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }
        
        ChainShape chain = new ChainShape(); 
        chain.createChain(worldVertices);
        return chain;
    }
    /**
     * Checks if the point given collides with any object in the map.<br>
     * The map objects are initialized with {@link #buildShapes(Map, float, World)}
     * @return false if point does not intersect any area or if {@link #buildShapes(Map, float, World)} was never called.
     * true if the point does collide with a shape in the map
     * @param point The point to check. Must be scaled to map pixels
     */
    public static boolean checkCollision(Vector2 point){
    	if(shapes==null)return false;
    	for(Shape2D shape:shapes){
    		if(shape.contains(point))
    			//System.out.println("Found collision at: "+point);
    			return true;
    			
    		}
    	
    	return false;
    }
}