package me.boxcubed.main.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boxcubed.utils.MenuButton;
import com.boxcubed.utils.MenuListener;

import me.boxcubed.main.TopDown;

import java.awt.*;

/**
 * @author BoxCubed
 */
public class MenuState implements State {
	Stage stage;
    Texture button;
    MenuButton clickButton;
    TextureAtlas start=new TextureAtlas("assets/button/start.atlas");
    Sprite hover=start.createSprite("hover"),click=start.createSprite("click"),normal=start.createSprite("normal");
    SpriteBatch batch=new SpriteBatch();
    //OLD  STUFF ^
    BitmapFont font;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    GlyphLayout startGlyph;
    public MenuState(GameState loadedInstance) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/font.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        font = generator.generateFont(parameter);
        startGlyph = new GlyphLayout();
        startGlyph.setText(font, "Start BITCH");
     /*this.gsm=gsm;*/
        Gdx.input.setInputProcessor(stage);
        
        button = new Texture("assets/img/icon.png");
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
    }

    @Override
	public void handleInput() {

    }

    @Override
    public void update(float delta) {
    	clickButton.update(delta);
    	

    }

    @Override
    public void render() {
    	
        batch.begin();
        clickButton.render(batch);
        //font.draw(batch, startGlyph, Gdx.graphics.getWidth()/2 - startGlyph.width/2, Gdx.graphics.getHeight()/2 - startGlyph.height/2);
        batch.end();
    }

    @Override
    public void show() {
    	
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
        generator.dispose();
    }
  
  
   
}
