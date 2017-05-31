package me.boxcubed.main.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.lang.reflect.Field;

/**
 * @author ryan9
 * @deprecated
 */
class FileAtlas {


    private static final TextureAtlas zombieAtlas = new TextureAtlas(Gdx.files.internal("assets/spritesheets/zombieanim.atlas"));
    private static final TextureAtlas zombieWalkAtlas = new TextureAtlas(Gdx.files.internal("assets/spritesheets/zombie_walk.atlas"));
    public static TiledMap map = new TmxMapLoader().load("assets/maps/map2.tmx");
    public static Texture zombieTex = new Texture(Gdx.files.internal("assets/img/skeleton-idle_0.png"));
    public static Texture healthTexture = new Texture(Gdx.files.internal("assets/img/health.png"));
    public static Animation<TextureRegion> zombieAnim = new Animation<TextureRegion>(1f / 30f * 150f, zombieAtlas.getRegions());
    public static Animation<TextureRegion> zombieWalkAnim = new Animation<TextureRegion>(1 / 30f, zombieWalkAtlas.getRegions());
    static Class<? extends FileAtlas> fileAt;

    private FileAtlas() {
        fileAt = this.getClass();

    }


    @SuppressWarnings("unchecked")
    public static <T> T getFile(String path) throws ClassCastException {
        try {
            Field field = FileAtlas.class.getField(path);
            field.setAccessible(true);
            Object ret = field.get(null);

            return (T) ret;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            // TODO Auto-generated catch block
            Gdx.app.log("[TopDown]", "Error accessing file: " + e.getMessage(), e);
        }
        return null;

    }
}
