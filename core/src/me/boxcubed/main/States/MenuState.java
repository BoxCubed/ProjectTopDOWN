package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boxcubed.utils.Assets;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;

import me.boxcubed.main.TopDown;

/**
 * @author BoxCubed
 */
public class MenuState implements Screen {
	Stage stage;
    MenuButton clickButton;
    MenuButton clickButton1;
    TextureAtlas start=TopDown.assets.get(Assets.startATLAS, TextureAtlas.class);;
    Sprite hover=start.createSprite("hover"),click=start.createSprite("click"),normal=start.createSprite("normal");
    SpriteBatch batch=new SpriteBatch();
    //OLD  STUFF ^
    BitmapFont font;
    GlyphLayout startGlyph;
    float a = 10, b = 28, c = (8/3); //a = sigma, b = row, c = beta
    float x = 0.01f, y = 0, z = 0;
    ShapeRenderer renderer;
    public MenuState(GameState loadedInstance) {
        renderer = new ShapeRenderer();
        font = TopDown.assets.get(Assets.menuFONT, BitmapFont.class);
        font.setColor(Color.GREEN);
        startGlyph = new GlyphLayout();
        startGlyph.setText(font, "Start");
     /*this.gsm=gsm;*/
        Gdx.input.setInputProcessor(stage);
        
        clickButton=new MenuButton(startGlyph,font,  Gdx.graphics.getWidth()/2 - startGlyph.width/2,  Gdx.graphics.getHeight()/2 - startGlyph.height/2, 
        		new MenuListener() {
			@Override
			public void rightclicked(MenuButton m) {
				System.out.println("RClicked!");
				
			}
			@Override
			public void notChosen(MenuButton m) {
				
				
			}
			@Override
			public void clicked(MenuButton m) {
                TopDown.instance.setScreen(loadedInstance);
			}
			@Override
			public void chosen(MenuButton m) {
				
			}
		});

        font.setColor(Color.BLUE);
        clickButton.setGlyphNotChosen(new GlyphLayout(font, "DONT TOUCH ME"));
        clickButton.setCollisionLock(true);
        clickButton.getRect().width=clickButton.f.width;
        clickButton.getRect().height=clickButton.f.height;
        //Forgive me for this code duplication
        //I did not want to do it but i couldn't be bothered making a fucking button
        /*clickButton1.setGlyphNotChosen(new GlyphLayout(font, "DONT TOUCH ME"));
        clickButton1.setCollisionLock(true);
        clickButton1.getRect().width=clickButton.f.width;
        clickButton1.getRect().height=clickButton.f.height;*/
    }

	public void handleInput() {

    }

    public void update(float delta) {

    	

    }

    @Override
    public void show() {
    	

    }

    @Override
    public void render(float delta) {
        //Dont ask me why. It looked cool
        //Lorenz attractor
        clickButton.update(delta);


       
        batch.begin();
        //renderer.translate(20, 12, 2);
        clickButton.render(batch);

        batch.end();

    }

    


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    	
    }

    @Override
    public void dispose() {
        this.dispose();
        batch.dispose();
    }
  
  
   
}
