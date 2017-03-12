package me.boxcubed.main.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AnimLauncher implements ApplicationListener, InputProcessor {

   SpriteBatch batch;
   Sprite sprite;
   Vector2 maths;
   int window_size = 0;
   
   @Override public void create() {
      batch = new SpriteBatch();
      sprite = new Sprite(new Texture("assets/img/player.png"));
      sprite.setOrigin(32, 64);
      sprite.setPosition(500, 500);
      maths = new Vector2(0, 0);
      Gdx.input.setInputProcessor(this);
      
      window_size = Gdx.graphics.getHeight();
   }

   float nx, ny, angle;
   private void setRotation(int x, int y){
      nx = sprite.getX() + sprite.getOriginX();
      ny = sprite.getY() + sprite.getOriginY();
      maths.x = (x - nx);
      maths.y = (y - ny);
      sprite.setRotation(maths.angle());
   }
   
   @Override public void render() {
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | 
            (Gdx.graphics.getBufferFormat().coverageSampling? GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
      batch.begin();
      sprite.draw(batch);
      batch.end();
   }
   
   @Override public boolean mouseMoved(int x, int y) {
      setRotation(x, (window_size-y));
      return false;
   }
   
   @Override public void dispose() {}
   @Override public void pause() {}
   @Override public void resize(int arg0, int arg1) { }
   @Override public void resume() {}
   @Override public boolean keyDown(int arg0) { return false; }
   @Override public boolean keyTyped(char arg0) { return false; }
   @Override public boolean keyUp(int arg0) { return false; }
   @Override public boolean scrolled(int arg0) { return false; }
   @Override public boolean touchDown(int arg0, int arg1, int arg2, int arg3) { return false; }
   @Override public boolean touchDragged(int arg0, int arg1, int arg2) { return false; }
   @Override public boolean touchUp(int arg0, int arg1, int arg2, int arg3) { return false; }
   
   public static void main(String[] args) {
      LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
      cfg.title       = "Rotate Test";
      cfg.resizable    = false;
      cfg.width       = 1000; 
      cfg.height       = 1000;
      new LwjglApplication(new AnimLauncher(), cfg);
   }
}